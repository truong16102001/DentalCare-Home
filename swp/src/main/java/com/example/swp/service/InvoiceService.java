package com.example.swp.service;

import com.example.swp.entity.Invoice;
import com.example.swp.entity.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    Optional<Invoice> findBySession(Session session);
    Invoice save(Invoice invoice);
    List<Invoice> getByCreatedDate(LocalDate date);
    Optional<Invoice> getById(Integer id);

}
