package com.example.books.adapter.fs;

import com.example.books.config.BooksProperties;
import com.example.books.infrastructure.broker.KafkaProducer;
import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.FileChangeType;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

@Component
public class FileSystemWatcher implements SmartLifecycle {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemWatcher.class);

    private final Path root;
    private final List<PathMatcher> excludeMatchers = new ArrayList<>();
    private final FileChecksumCalculator checksumCalculator;
    private final KafkaProducer kafkaProducer;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService executor;
    private final WatchService watchService;
    private final Set<Path> registeredDirectories = ConcurrentHashMap.newKeySet();

    public FileSystemWatcher(BooksProperties properties, FileChecksumCalculator checksumCalculator, KafkaProducer kafkaProducer)
        throws IOException {
        this.root = properties.getRoot();
        this.checksumCalculator = checksumCalculator;
        this.kafkaProducer = kafkaProducer;
        ThreadFactory threadFactory = new CustomizableThreadFactory("books-fs-watcher-");
        this.executor = Executors.newSingleThreadExecutor(threadFactory);
        this.watchService = FileSystems.getDefault().newWatchService();
        properties.getExclude().stream().filter(Objects::nonNull).forEach(pattern -> excludeMatchers.add(glob(pattern)));
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            if (!Files.isDirectory(root)) {
                LOG.warn("Skipping filesystem watcher startup; root {} does not exist", root);
                running.set(false);
                return;
            }
            try {
                registerRecursively(root);
            } catch (IOException e) {
                LOG.error("Failed to register directories for watcher", e);
                running.set(false);
                return;
            }
            executor.submit(this::run);
            LOG.info("Started filesystem watcher for {}", root);
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            try {
                watchService.close();
            } catch (IOException e) {
                LOG.warn("Error closing watch service", e);
            }
            executor.shutdownNow();
            registeredDirectories.clear();
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    private void run() {
        try {
            while (running.get()) {
                WatchKey key = watchService.take();
                Path dir = (Path) key.watchable();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    Path relativePath = (Path) event.context();
                    Path child = dir.resolve(relativePath);
                    if (Files.isDirectory(child) && kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        registerRecursively(child);
                    }
                    if (Files.isDirectory(child) || isExcluded(child)) {
                        continue;
                    }
                    emitEvent(kind, child);
                }
                boolean valid = key.reset();
                if (!valid) {
                    registeredDirectories.remove(dir);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.debug("Filesystem watcher interrupted");
        } catch (IOException e) {
            LOG.error("Watcher error", e);
        } finally {
            stop();
        }
    }

    private void emitEvent(WatchEvent.Kind<?> kind, Path path) throws IOException {
        FileChangeType changeType = mapKind(kind);
        long size = Files.exists(path) ? Files.size(path) : 0L;
        String checksum = changeType.requiresContent() && Files.isRegularFile(path) ? checksumCalculator.sha256(path) : null;
        FileChangeNotification notification = new FileChangeNotification(
            path.toAbsolutePath().toString(),
            changeType,
            checksum,
            size,
            Instant.now()
        );
        kafkaProducer.publishFileChange(notification);
    }

    private void registerRecursively(Path start) throws IOException {
        if (isExcluded(start)) {
            return;
        }
        Files.walkFileTree(
            start,
            new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws IOException {
                    if (isExcluded(dir)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    registerDirectory(dir);
                    return FileVisitResult.CONTINUE;
                }
            }
        );
    }

    private void registerDirectory(Path dir) throws IOException {
        if (registeredDirectories.add(dir)) {
            dir.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
            );
            LOG.debug("Watching directory {}", dir);
        }
    }

    private boolean isExcluded(Path path) {
        if (path == null) {
            return false;
        }
        Path absolute = path.toAbsolutePath().normalize();
        if (!absolute.startsWith(root)) {
            return false;
        }
        Path relative = root.relativize(absolute);
        for (PathMatcher matcher : excludeMatchers) {
            if (matcher.matches(relative)) {
                return true;
            }
        }
        return false;
    }

    private static FileChangeType mapKind(WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            return FileChangeType.CREATED;
        }
        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            return FileChangeType.MODIFIED;
        }
        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            return FileChangeType.DELETED;
        }
        throw new IllegalArgumentException("Unsupported watch kind: " + kind);
    }

    private PathMatcher glob(String pattern) {
        String globPattern = pattern.startsWith("glob:") ? pattern : "glob:" + pattern;
        return FileSystems.getDefault().getPathMatcher(globPattern);
    }
}
