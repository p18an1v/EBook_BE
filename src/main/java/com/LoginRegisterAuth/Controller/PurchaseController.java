package com.LoginRegisterAuth.Controller;
import com.LoginRegisterAuth.Model.Book;
import com.LoginRegisterAuth.Model.Purchase;
import com.LoginRegisterAuth.DTO.PurchaseRequest;
import com.LoginRegisterAuth.Repository.BookRepository;
import com.LoginRegisterAuth.Repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/purchases")
    public ResponseEntity<String> buyBook(@RequestBody PurchaseRequest purchaseRequest) {
        Book book = bookRepository.findById(purchaseRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getTotalCount() <= 0) {
            return ResponseEntity.badRequest().body("Book out of stock");
        }

        book.setTotalCount(book.getTotalCount() - 1);
        bookRepository.save(book);

        Purchase purchase = new Purchase();
        purchase.setBookId(book.getBookId());
        purchase.setBookTitle(book.getBookTitle());
        purchase.setAuthorName(book.getAuthor().getAuthorName());
        purchase.setPublisherName(book.getAuthor().getPublisher().getPublisherName());
        purchase.setPrice(book.getPrice());
        purchase.setUserId(purchaseRequest.getUserId());
        purchase.setUserEmail(purchaseRequest.getUserEmail());
        purchase.setPurchaseDate(LocalDateTime.now());

        purchaseRepository.save(purchase);

        return ResponseEntity.ok("Book purchased successfully");
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll();
        return ResponseEntity.ok(purchases);
    }
}
