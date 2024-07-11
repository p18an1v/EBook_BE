package com.LoginRegisterAuth.Controller;

import com.LoginRegisterAuth.DTO.BookRequest;
import com.LoginRegisterAuth.DTO.PublisherResponse;
import com.LoginRegisterAuth.Model.Author;
import com.LoginRegisterAuth.Model.AuthorRequest;
import com.LoginRegisterAuth.Model.Book;
import com.LoginRegisterAuth.Model.PublisherRequest;
import com.LoginRegisterAuth.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody BookRequest bookRequest) {
        Book savedBook = bookService.createBook(bookRequest);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = bookService.getAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/authors/{authorId}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long authorId) {
        Author author = bookService.getAuthorById(authorId);
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping("/publishers")
    public ResponseEntity<List<PublisherResponse>> getAllPublishers() {
        List<PublisherResponse> publishers = bookService.getAllPublishers();
        return new ResponseEntity<>(publishers, HttpStatus.OK);
    }

    @GetMapping("/publishers/{publisherId}")
    public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable Long publisherId) {
        PublisherResponse publisher = bookService.getPublisherById(publisherId);
        if (publisher == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(publisher, HttpStatus.OK);
    }

    //--------------------------------updating code--------------------------------------------//

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        boolean isDeleted = bookService.deleteBook(bookId);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody BookRequest bookRequest) {
        Book updatedBook = bookService.updateBook(bookId, bookRequest);
        if (updatedBook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long authorId) {
        boolean isDeleted = bookService.deleteAuthor(authorId);
        if (!isDeleted) {
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/authors/{authorId}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long authorId, @RequestBody AuthorRequest authorRequest) {
        Author updatedAuthor = bookService.updateAuthor(authorId, authorRequest);
        if (updatedAuthor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/publishers/{publisherId}")
    public ResponseEntity<String> deletePublisher(@PathVariable Long publisherId) {
        boolean isDeleted = bookService.deletePublisher(publisherId);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }

    @PutMapping("/publishers/{publisherId}")
    public ResponseEntity<PublisherResponse> updatePublisher(@PathVariable Long publisherId, @RequestBody PublisherRequest publisherRequest) {
        PublisherResponse updatedPublisher = bookService.updatePublisher(publisherId, publisherRequest);
        if (updatedPublisher == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedPublisher, HttpStatus.OK);
    }

    @PatchMapping("/books/{bookId}")
    public ResponseEntity<Book> patchBook(@PathVariable Long bookId, @RequestBody Map<String, Object> updates) {
        Book updatedBook = bookService.patchBook(bookId, updates);
        if (updatedBook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }


}
