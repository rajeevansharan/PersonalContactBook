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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * List all contacts (with optional search and pagination)
     */
    @GetMapping("/")
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
    public String saveContact(
            @ModelAttribute Contact contact,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Contact savedContact = contactService.saveContact(contact);
            String message = contact.getId() == null ?
                    "Contact created successfully!" :
                    "Contact updated successfully!";

            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", contact.getId() != null);
            return "contacts/form";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred while saving the contact.");
            model.addAttribute("isEdit", contact.getId() != null);
            return "contacts/form";
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


    /**
     * Update existing contact (Simple)
     */
    @PostMapping("/update") // Use POST since it's a form submission
    public String updateContact(
            @ModelAttribute("contact") Contact updatedContact,
            RedirectAttributes redirectAttributes) {
        try {
            contactService.updateContact(updatedContact.getId(), updatedContact);
            redirectAttributes.addFlashAttribute("successMessage", "Contact updated successfully!");

            return "redirect:/";

        } catch (ContactNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "An unexpected error occurred while updating the contact.");
            return "redirect:/";
        }
    }


    /**
     * Show add contact form
     */
    @GetMapping("/new")
    public String showAddContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("isEdit", false);
        return "contacts/form";
    }




}
