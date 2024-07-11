package com.LoginRegisterAuth.Service;


import com.LoginRegisterAuth.Model.Book;
import com.LoginRegisterAuth.Model.Purchase;
import com.LoginRegisterAuth.DTO.PurchaseRequest;
import com.LoginRegisterAuth.Repository.BookRepository;
import com.LoginRegisterAuth.Repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private BookRepository bookRepository;

    public String purchaseBook(PurchaseRequest purchaseRequest) {
        Book book = bookRepository.findById(purchaseRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getTotalCount() <= 0) {
            return "Book out of stock";
        }

        // Decrease available stock
        book.setTotalCount(book.getTotalCount() - 1);
        bookRepository.save(book);

        // Create purchase record
        Purchase purchase = new Purchase();
        purchase.setBookId(book.getBookId());
        purchase.setBookTitle(book.getBookTitle());
        purchase.setAuthorName(book.getAuthor().getAuthorName()); // Assuming you have author information in book entity
        purchase.setPublisherName(book.getAuthor().getPublisher().getPublisherName()); // Assuming publisher info is nested
        purchase.setPrice(book.getPrice());
        purchase.setUserId(purchaseRequest.getUserId());
        purchase.setUserEmail(purchaseRequest.getUserEmail());

        purchaseRepository.save(purchase);

        return "Book purchased successfully";
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }
}