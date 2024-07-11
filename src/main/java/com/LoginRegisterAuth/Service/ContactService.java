package com.LoginRegisterAuth.Service;

import com.LoginRegisterAuth.DTO.contactDTO;
import com.LoginRegisterAuth.Model.contact;
import com.LoginRegisterAuth.Repository.ContactRepository;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public void saveContact(contactDTO contactDTO) {
        contact contact = new contact();
        contact.setContactName(contactDTO.getContactName());
        contact.setContactMob(contactDTO.getContactMob());
        contact.setContactEmail(contactDTO.getContactEmail());
        contact.setContactDesc(contactDTO.getContactDesc());
        contactRepository.save(contact);
    }

    public List<contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public contact getContactById(int contactId) {
        return contactRepository.findById(contactId).orElse(null);
    }

    public contact updateContact(int contactId, contactDTO contactDTO) {
        contact contact = contactRepository.findById(contactId).orElse(null);
        if (contact != null) {
            contact.setContactName(contactDTO.getContactName());
            contact.setContactMob(contactDTO.getContactMob());
            contact.setContactEmail(contactDTO.getContactEmail());
            contact.setContactDesc(contactDTO.getContactDesc());
            return contactRepository.save(contact);
        }
        return null;
    }

    public boolean deleteContact(int contactId) {
        if (contactRepository.existsById(contactId)) {
            contactRepository.deleteById(contactId);
            return true;
        }
        return false;
    }
}
