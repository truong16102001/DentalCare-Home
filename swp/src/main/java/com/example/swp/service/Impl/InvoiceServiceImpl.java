package com.example.swp.service.Impl;

import com.example.swp.entity.Invoice;
import com.example.swp.entity.Session;
import com.example.swp.repository.InvoiceRepository;
import com.example.swp.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceServiceImpl implements InvoiceService {
    InvoiceRepository invoiceRepository;
    @Override
    public Optional<Invoice> findBySession(Session session) {
        return invoiceRepository.findBySession(session);
    }

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getByCreatedDate(LocalDate date) {
        return invoiceRepository.findByCreatedDate(date);
    }

    @Override
    public Optional<Invoice> getById(Integer id) {
        return  invoiceRepository.findById(id);
    }
}
