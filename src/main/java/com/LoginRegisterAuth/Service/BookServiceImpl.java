package com.LoginRegisterAuth.Service;


import com.LoginRegisterAuth.DTO.AuthorResponse;
import com.LoginRegisterAuth.DTO.BookRequest;
import com.LoginRegisterAuth.DTO.BookResponse;
import com.LoginRegisterAuth.DTO.PublisherResponse;
import com.LoginRegisterAuth.Model.*;
import com.LoginRegisterAuth.Repository.AuthorRepository;
import com.LoginRegisterAuth.Repository.BookRepository;
import com.LoginRegisterAuth.Repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherService publisherService;
    @Override
    public Book createBook(BookRequest bookRequest) {
        logger.info("Creating book with title: " + bookRequest.getBookTitle());
        Author author = authorService.findOrCreateAuthor(bookRequest.getAuthorName(), bookRequest.getPublisherName());
        Book book = new Book();
        book.setBookTitle(bookRequest.getBookTitle());
        book.setTotalCount(bookRequest.getTotalCount());
        book.setPublishingDate(bookRequest.getPublishingDate());
        book.setPrice(bookRequest.getPrice());
        book.setImgUrl(bookRequest.getImgUrl());
        book.setBookContent(bookRequest.getBookContent());
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<PublisherResponse> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();
        return publishers.stream().map(this::convertToPublisherResponse).collect(Collectors.toList());
    }

    @Override
    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId).orElse(null);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    @Override
    public PublisherResponse getPublisherById(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId).orElse(null);
        if (publisher == null) {
            return null;
        }
        return convertToPublisherResponse(publisher);
    }

    private PublisherResponse convertToPublisherResponse(Publisher publisher) {
        PublisherResponse publisherResponse = new PublisherResponse();
        publisherResponse.setPublisherId(publisher.getPublisherId());
        publisherResponse.setPublisherName(publisher.getPublisherName());
        publisherResponse.setAuthors(publisher.getAuthors().stream()
                .map(this::convertToAuthorResponse)
                .collect(Collectors.toList()));
        return publisherResponse;
    }

    private AuthorResponse convertToAuthorResponse(Author author) {
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setAuthorId(author.getAuthorId());
        authorResponse.setAuthorName(author.getAuthorName());
        authorResponse.setBooks(author.getBooks().stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList()));
        return authorResponse;
    }

    private BookResponse convertToBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setBookId(book.getBookId());
        bookResponse.setBookTitle(book.getBookTitle());
        bookResponse.setTotalCount(book.getTotalCount());
        bookResponse.setPublishingDate(book.getPublishingDate());
        bookResponse.setPrice(book.getPrice());
        bookResponse.setImgUrl(book.getImgUrl());
        bookResponse.setBookContent(book.getBookContent());
        return bookResponse;
    }
    // Additional methods for business logic as needed
    // Existing methods...

    @Override
    public boolean deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return false;
        }
        bookRepository.deleteById(bookId);
        return true;
    }

    @Override
    public Book updateBook(Long bookId, BookRequest bookRequest) {
        return bookRepository.findById(bookId).map(book -> {
            book.setBookTitle(bookRequest.getBookTitle());
            book.setTotalCount(bookRequest.getTotalCount());
            book.setPublishingDate(bookRequest.getPublishingDate());
            book.setPrice(bookRequest.getPrice());
            book.setImgUrl(bookRequest.getImgUrl());
            book.setBookContent((bookRequest.getBookContent()));
            Author author = authorService.findOrCreateAuthor(bookRequest.getAuthorName(), bookRequest.getPublisherName());
            book.setAuthor(author);
            return bookRepository.save(book);
        }).orElse(null);
    }

    @Override
    public boolean deleteAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            return false;
        }
        authorRepository.deleteById(authorId);
        return true;
    }

    @Override
    public Author updateAuthor(Long authorId, AuthorRequest authorRequest) {
        return authorRepository.findById(authorId).map(author -> {
            author.setAuthorName(authorRequest.getAuthorName());
            Publisher publisher = publisherService.findOrCreatePublisher(authorRequest.getPublisherName());
            author.setPublisher(publisher);
            return authorRepository.save(author);
        }).orElse(null);
    }

    @Override
    public boolean deletePublisher(Long publisherId) {
        if (!publisherRepository.existsById(publisherId)) {
            return false;
        }
        publisherRepository.deleteById(publisherId);
        return true;
    }

    @Override
    public PublisherResponse updatePublisher(Long publisherId, PublisherRequest publisherRequest) {
        return publisherRepository.findById(publisherId).map(publisher -> {
            publisher.setPublisherName(publisherRequest.getPublisherName());
            publisher = publisherRepository.save(publisher);
            return convertToPublisherResponse(publisher);
        }).orElse(null);
    }

    @Override
    public Book patchBook(Long bookId, Map<String, Object> updates) {
        return bookRepository.findById(bookId).map(book -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "bookTitle":
                        book.setBookTitle((String) value);
                        break;
                    case "totalCount":
                        book.setTotalCount((Integer) value);
                        break;
                    case "publishingDate":
                        book.setPublishingDate(LocalDate.parse((String) value));
                        break;
                    case "price":
                        book.setPrice((Double) value);
                        break;
                    case "imgUrl":
                        book.setImgUrl((String) value);
                        break;
                    case "authorName":
                        String authorName = (String) value;
                        Author author = authorService.findOrCreateAuthor(authorName, book.getAuthor().getPublisher().getPublisherName());
                        book.setAuthor(author);
                        break;
                    case "publisherName":
                        String publisherName = (String) value;
                        Publisher publisher = publisherService.findOrCreatePublisher(publisherName);
                        Author authorWithNewPublisher = authorService.findOrCreateAuthor(book.getAuthor().getAuthorName(), publisherName);
                        book.setAuthor(authorWithNewPublisher);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown property: " + key);
                }
            });
            return bookRepository.save(book);
        }).orElse(null);
    }
}

