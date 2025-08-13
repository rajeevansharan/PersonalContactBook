package com.BasicProject.repository;

import com.BasicProject.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Paginated search
    @Query("SELECT c FROM Contact c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "c.phone LIKE CONCAT('%', :keyword, '%')")
    Page<Contact> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Check if email exists (excluding current contact)
    @Query("SELECT COUNT(c) > 0 FROM Contact c WHERE c.email = :email AND c.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);


    // Find by email (exact match)
    Optional<Contact> findByEmail(String email);

}