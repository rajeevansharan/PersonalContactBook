package com.BasicProject.controller;

import com.BasicProject.exception.ContactNotFoundException;
import com.BasicProject.model.Contact;
import com.BasicProject.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    /**
     * Home page - List all contacts
     */
    @GetMapping("/")
    public String listContacts(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        try {
            Page<Contact> contactPage;

            if (search != null && !search.trim().isEmpty()) {
                contactPage = contactService.searchContacts(search, page, size);
                model.addAttribute("search", search);
            } else {
                contactPage = contactService.getAllContacts(page, size);
            }

            model.addAttribute("contacts", contactPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", contactPage.getTotalPages());
            model.addAttribute("totalContacts", contactPage.getTotalElements());
            model.addAttribute("hasNext", contactPage.hasNext());
            model.addAttribute("hasPrevious", contactPage.hasPrevious());

            return "contacts/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading contacts: " + e.getMessage());
            return "contacts/list";
        }
    }


}