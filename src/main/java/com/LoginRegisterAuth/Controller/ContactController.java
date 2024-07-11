package com.LoginRegisterAuth.Controller;
import com.LoginRegisterAuth.DTO.contactDTO;
import com.LoginRegisterAuth.Model.contact;
import com.LoginRegisterAuth.Service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/contact")
    public ResponseEntity<String> submitContactForm(@RequestBody contactDTO contactDTO, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
        }
        contactService.saveContact(contactDTO);
        return new ResponseEntity<>("Contact submitted successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/contact")
    public ResponseEntity<List<contact>> getAllContacts() {
        List<contact> contacts = contactService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<contact> getContactById(@PathVariable int contactId) {
        contact contact = contactService.getContactById(contactId);
        if (contact == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<contact> updateContact(@PathVariable int contactId,@RequestBody contactDTO contactDTO, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        contact updatedContact = contactService.updateContact(contactId, contactDTO);
        if (updatedContact == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<String> deleteContact(@PathVariable int contactId) {
        boolean isDeleted = contactService.deleteContact(contactId);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }
}
