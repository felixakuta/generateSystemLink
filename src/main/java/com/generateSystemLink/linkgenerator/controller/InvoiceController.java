package com.generateSystemLink.linkgenerator.controller;

import com.generateSystemLink.linkgenerator.exception.InvoiceNotFoundException;
import com.generateSystemLink.linkgenerator.model.Invoice;
import com.generateSystemLink.linkgenerator.service.InvoiceService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> generateInvoice(@RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @SneakyThrows
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("/{id}/viewLink")
    @SneakyThrows
    public ResponseEntity<String> generateViewInvoiceLink(@PathVariable Long id) {
        String viewLink = invoiceService.generateViewInvoiceLink(id);
        return new ResponseEntity<>(viewLink, HttpStatus.OK);
    }

    @GetMapping("/{id}/paymentLink")
    @SneakyThrows
    public ResponseEntity<String> generatePaymentLink(@PathVariable Long id) {
        String paymentLink = invoiceService.generatePaymentLink(id);
        return new ResponseEntity<>(paymentLink, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
