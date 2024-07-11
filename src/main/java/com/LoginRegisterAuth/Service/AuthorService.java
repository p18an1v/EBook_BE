package com.LoginRegisterAuth.Service;

import com.LoginRegisterAuth.Model.Author;
import com.LoginRegisterAuth.Model.Publisher;
import com.LoginRegisterAuth.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherService publisherService;

    public Author findOrCreateAuthor(String authorName, String publisherName) {
        Publisher publisher = publisherService.findOrCreatePublisher(publisherName);
        return authorRepository.findByAuthorNameAndPublisher(authorName, publisher)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setAuthorName(authorName);
                    newAuthor.setPublisher(publisher);
                    return authorRepository.save(newAuthor);
                });
    }
}
