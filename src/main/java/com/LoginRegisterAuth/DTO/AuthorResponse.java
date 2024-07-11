package com.LoginRegisterAuth.DTO;

import java.util.List;

public class AuthorResponse {
    private Long authorId;
    private String authorName;
    private List<BookResponse> books;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<BookResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponse> books) {
        this.books = books;
    }
}
