package com.example.books.config;

import com.example.books.domain.book.BookDataAccessRepository;
import com.example.books.domain.book.BookRepository;
import com.example.books.domain.book.BookService;
import com.example.books.domain.bookfile.BookFileService;
import com.example.books.domain.bookpage.BookPageTextService;
import com.example.books.domain.ingestevent.IngestEventService;
import com.example.books.domain.ingestrun.IngestRunService;
import com.example.books.domain.user.UserService;
import com.example.books.infrastructure.database.jpa.mapper.BookFileMapper;
import com.example.books.infrastructure.database.jpa.mapper.BookPageTextMapper;
import com.example.books.infrastructure.database.jpa.mapper.IngestEventMapper;
import com.example.books.infrastructure.database.jpa.mapper.IngestRunMapper;
import com.example.books.infrastructure.database.jpa.repository.AuthorityJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.BookFileJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.IngestEventJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.IngestRunJpaRepository;
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
    public BookService bookService(BookRepository bookRepository, BookDataAccessRepository bookDataAccessRepository) {
        return new BookService(bookRepository, bookDataAccessRepository);
    }

    @Bean
    public BookFileService bookFileService(BookFileJpaRepository bookFileJpaRepository, BookFileMapper bookFileMapper) {
        return new BookFileService(bookFileJpaRepository, bookFileMapper);
    }

    @Bean
    public BookPageTextService bookPageTextService(
        BookPageTextJpaRepository bookPageTextJpaRepository,
        BookPageTextMapper bookPageTextMapper
    ) {
        return new BookPageTextService(bookPageTextJpaRepository, bookPageTextMapper);
    }

    @Bean
    public IngestRunService ingestRunService(IngestRunJpaRepository ingestRunJpaRepository, IngestRunMapper ingestRunMapper) {
        return new IngestRunService(ingestRunJpaRepository, ingestRunMapper);
    }

    @Bean
    public IngestEventService ingestEventService(IngestEventJpaRepository ingestEventJpaRepository, IngestEventMapper ingestEventMapper) {
        return new IngestEventService(ingestEventJpaRepository, ingestEventMapper);
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
