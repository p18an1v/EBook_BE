package com.LoginRegisterAuth.Service;

import com.LoginRegisterAuth.DTO.AuthorResponse;
import com.LoginRegisterAuth.DTO.BookRequest;
import com.LoginRegisterAuth.DTO.BookResponse;
import com.LoginRegisterAuth.DTO.PublisherResponse;
import com.LoginRegisterAuth.Model.*;
import com.LoginRegisterAuth.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface BookService {
    Book createBook(BookRequest bookRequest);
    List<Book> getAllBooks();
    List<Author> getAllAuthors();
    List<PublisherResponse> getAllPublishers();
    Author getAuthorById(Long authorId);
    Book getBookById(Long bookId);
    PublisherResponse getPublisherById(Long publisherId);
    boolean deleteBook(Long bookId);
    Book updateBook(Long bookId, BookRequest bookRequest);
    boolean deleteAuthor(Long authorId);
    Author updateAuthor(Long authorId, AuthorRequest authorRequest);
    boolean deletePublisher(Long publisherId);
    PublisherResponse updatePublisher(Long publisherId, PublisherRequest publisherRequest);

    Book patchBook(Long bookId, Map<String, Object> updates);
}
