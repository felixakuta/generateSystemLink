package com.generateSystemLink.linkgenerator.service;

import com.generateSystemLink.linkgenerator.exception.InvoiceNotFoundException;
import com.generateSystemLink.linkgenerator.model.Invoice;
import com.generateSystemLink.linkgenerator.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(100.0);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

        assertNotNull(savedInvoice);
        assertEquals(1L, savedInvoice.getId());
        assertEquals(100.0, savedInvoice.getAmount());
    }

    @Test
    public void testGetInvoiceById() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(100.0);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice retrievedInvoice = invoiceService.getInvoiceById(1L);

        assertNotNull(retrievedInvoice);
        assertEquals(1L, retrievedInvoice.getId());
        assertEquals(100.0, retrievedInvoice.getAmount());
    }

    @Test
    public void testGetInvoiceByIdNotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceService.getInvoiceById(1L);
        });
    }
}
