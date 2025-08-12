package com.BasicProject.controller;

import com.BasicProject.exception.ContactNotFoundException;
import com.BasicProject.model.Contact;
import com.BasicProject.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
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
    public String listContacts(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size , Model model) {

        Page<Contact> contactPage;

        if (search != null && !search.trim().isEmpty()) {
            contactPage = contactService.searchContacts(search, page, size);
        } else {
            contactPage = contactService.getAllContacts(page, size);
        }

        model.addAttribute("contacts", contactPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactPage.getTotalPages());
        model.addAttribute("totalContacts", contactPage.getTotalElements());
        model.addAttribute("hasNext", contactPage.hasNext());
        model.addAttribute("hasPrevious", contactPage.hasPrevious());
        model.addAttribute("search", search);

        return "contacts/list";
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


    /**
     * Delete contact (REST API style)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.ok("Contact deleted successfully!");

        } catch (ContactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while deleting the contact.");
        }
    }

    /**
     * Update existing contact
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        try {
            Contact contact = contactService.updateContact(id, updatedContact);
            return ResponseEntity.ok(contact); // Returns updated contact as JSON

        } catch (ContactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while updating the contact.");
        }
    }




}
