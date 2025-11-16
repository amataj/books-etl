package com.example.books.config;

import com.example.books.domain.book.BookCommandRepository;
import com.example.books.domain.book.BookQueryRepository;
import com.example.books.domain.book.BookService;
import com.example.books.domain.bookfile.BookFileCommandRepository;
import com.example.books.domain.bookfile.BookFileQueryRepository;
import com.example.books.domain.bookfile.BookFileService;
import com.example.books.domain.bookpage.BookPageTextCommandRepository;
import com.example.books.domain.bookpage.BookPageTextQueryRepository;
import com.example.books.domain.bookpage.BookPageTextService;
import com.example.books.domain.ingestevent.IngestEventCommandRepository;
import com.example.books.domain.ingestevent.IngestEventQueryRepository;
import com.example.books.domain.ingestevent.IngestEventService;
import com.example.books.domain.ingestrun.IngestRunCommandRepository;
import com.example.books.domain.ingestrun.IngestRunQueryRepository;
import com.example.books.domain.ingestrun.IngestRunService;
import com.example.books.domain.user.UserService;
import com.example.books.infrastructure.database.jpa.repository.AuthorityJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.UserJpaRepository;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Central place to wire Books domain services and infrastructure adapters together.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public BookService bookService(BookCommandRepository bookCommandRepository, BookQueryRepository bookQueryRepository) {
        return new BookService(bookCommandRepository, bookQueryRepository);
    }

    @Bean
    public BookFileService bookFileService(
        BookFileCommandRepository bookFileCommandRepository,
        BookFileQueryRepository bookFileQueryRepository
    ) {
        return new BookFileService(bookFileCommandRepository, bookFileQueryRepository);
    }

    @Bean
    public BookPageTextService bookPageTextService(
        BookPageTextCommandRepository bookPageTextCommandRepository,
        BookPageTextQueryRepository bookPageTextQueryRepository
    ) {
        return new BookPageTextService(bookPageTextCommandRepository, bookPageTextQueryRepository);
    }

    @Bean
    public IngestRunService ingestRunService(
        IngestRunCommandRepository ingestRunCommandRepository,
        IngestRunQueryRepository ingestRunQueryRepository
    ) {
        return new IngestRunService(ingestRunCommandRepository, ingestRunQueryRepository);
    }

    @Bean
    public IngestEventService ingestEventService(
        IngestEventCommandRepository ingestEventCommandRepository,
        IngestEventQueryRepository ingestEventQueryRepository
    ) {
        return new IngestEventService(ingestEventCommandRepository, ingestEventQueryRepository);
    }

    @Bean
    public UserService userService(
        UserJpaRepository userJpaRepository,
        PasswordEncoder passwordEncoder,
        AuthorityJpaRepository authorityJpaRepository,
        CacheManager cacheManager
    ) {
        return new UserService(userJpaRepository, passwordEncoder, authorityJpaRepository, cacheManager);
    }
}
