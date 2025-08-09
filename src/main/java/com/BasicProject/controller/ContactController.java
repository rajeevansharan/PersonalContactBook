package com.BasicProject.controller;

import com.BasicProject.model.Contact;
import com.BasicProject.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * List all contacts (with optional search and pagination)
     */
    @GetMapping
    public Map<String, Object> listContacts(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Contact> contactPage;

        if (search != null && !search.trim().isEmpty()) {
            contactPage = contactService.searchContacts(search, page, size);
        } else {
            contactPage = contactService.getAllContacts(page, size);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("contacts", contactPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", contactPage.getTotalPages());
        response.put("totalContacts", contactPage.getTotalElements());
        response.put("hasNext", contactPage.hasNext());
        response.put("hasPrevious", contactPage.hasPrevious());

        return response;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveContact(@RequestBody Contact contact) {
        try {
            Contact savedContact = contactService.saveContact(contact);
            String message = contact.getId() == null ?
                    "Contact created successfully!" :
                    "Contact updated successfully!";

            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("contact", savedContact);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("errorMessage", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errorMessage", "An unexpected error occurred while saving the contact."));
        }
    }

}
