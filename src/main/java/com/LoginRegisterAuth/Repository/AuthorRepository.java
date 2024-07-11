package com.LoginRegisterAuth.Repository;


import com.LoginRegisterAuth.Model.Author;
import com.LoginRegisterAuth.Model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByAuthorNameAndPublisher(String authorName, Publisher publisher);
}
