package com.BasicProject.service;

import com.BasicProject.model.Contact;
import com.BasicProject.repository.ContactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository)
    {
        this.contactRepository = contactRepository;
    }

    /**
     * Get contacts with pagination
     */
    @Transactional(readOnly = true)
    public Page<Contact> getAllContacts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return contactRepository.findAll(pageable);
    }


    /**
     * Search contacts with pagination
     */
    @Transactional(readOnly = true)
    public Page<Contact> searchContacts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (keyword == null || keyword.trim().isEmpty()) {
            return contactRepository.findAll(pageable);
        }

        return contactRepository.searchByKeyword(keyword.trim(), pageable);
    }


}