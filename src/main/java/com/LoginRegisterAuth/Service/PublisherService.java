package com.LoginRegisterAuth.Service;

import com.LoginRegisterAuth.Model.Publisher;
import com.LoginRegisterAuth.Repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;

    public Publisher findOrCreatePublisher(String publisherName) {
        return publisherRepository.findByPublisherName(publisherName)
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher();
                    newPublisher.setPublisherName(publisherName);
                    return publisherRepository.save(newPublisher);
                });
    }
}
