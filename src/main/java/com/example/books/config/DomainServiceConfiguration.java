package com.example.books.config;

import com.example.books.domain.repository.book.BookDataAccessRepository;
import com.example.books.domain.repository.book.BookRepository;
import com.example.books.domain.repository.bookfile.BookFileDataAccessRepository;
import com.example.books.domain.repository.bookfile.BookFileRepository;
import com.example.books.domain.repository.bookpagetext.BookPageTextDataAccessRepository;
import com.example.books.domain.repository.bookpagetext.BookPageTextRepository;
import com.example.books.domain.repository.ingestevent.IngestEventDataAccessRepository;
import com.example.books.domain.repository.ingestevent.IngestEventRepository;
import com.example.books.domain.repository.ingestrun.IngestRunDataAccessRepository;
import com.example.books.domain.repository.ingestrun.IngestRunRepository;
import com.example.books.domain.service.book.BookService;
import com.example.books.domain.service.book.impl.BookServiceImpl;
import com.example.books.domain.service.bookfile.BookFileService;
import com.example.books.domain.service.bookfile.impl.BookFileServiceImpl;
import com.example.books.domain.service.bookpagetext.BookPageTextService;
import com.example.books.domain.service.bookpagetext.impl.BookPageTextServiceImpl;
import com.example.books.domain.service.ingestevent.IngestEventService;
import com.example.books.domain.service.ingestevent.impl.IngestEventServiceImpl;
import com.example.books.domain.service.ingestrun.IngestRunService;
import com.example.books.domain.service.ingestrun.impl.IngestRunServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires framework-free domain services with their repository ports.
 */
@Configuration
public class DomainServiceConfiguration {

    @Bean
    BookService bookService(BookRepository bookRepository, BookDataAccessRepository bookDataAccessRepository) {
        return new BookServiceImpl(bookRepository, bookDataAccessRepository);
    }

    @Bean
    BookFileService bookFileService(BookFileRepository bookFileRepository, BookFileDataAccessRepository bookFileDataAccessRepository) {
        return new BookFileServiceImpl(bookFileRepository, bookFileDataAccessRepository);
    }

    @Bean
    BookPageTextService bookPageTextService(
        BookPageTextRepository bookPageTextRepository,
        BookPageTextDataAccessRepository bookPageTextDataAccessRepository
    ) {
        return new BookPageTextServiceImpl(bookPageTextRepository, bookPageTextDataAccessRepository);
    }

    @Bean
    IngestRunService ingestRunService(
        IngestRunRepository ingestRunRepository,
        IngestRunDataAccessRepository ingestRunDataAccessRepository
    ) {
        return new IngestRunServiceImpl(ingestRunRepository, ingestRunDataAccessRepository);
    }

    @Bean
    IngestEventService ingestEventService(
        IngestEventRepository ingestEventRepository,
        IngestEventDataAccessRepository ingestEventDataAccessRepository
    ) {
        return new IngestEventServiceImpl(ingestEventRepository, ingestEventDataAccessRepository);
    }
}
