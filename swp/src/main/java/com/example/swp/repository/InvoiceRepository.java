package com.example.swp.repository;

import com.example.swp.entity.Invoice;
import com.example.swp.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findBySession(Session session);
    List<Invoice> findByCreatedDate(LocalDate date);
}
