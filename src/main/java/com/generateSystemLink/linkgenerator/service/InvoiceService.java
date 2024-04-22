package com.generateSystemLink.linkgenerator.service;

import com.generateSystemLink.linkgenerator.exception.InvoiceNotFoundException;
import com.generateSystemLink.linkgenerator.model.Invoice;
import com.generateSystemLink.linkgenerator.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class InvoiceService {

    private static final String VIEW_INVOICE_URL_TEMPLATE = "https://felixpay.com/view-invoice/%d";
    private static final String PAY_INVOICE_URL_TEMPLATE = "https://invoice.com/pay-invoice/%d";

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EmailService emailService;

    public Invoice saveInvoice(Invoice invoice) {
        String viewLink = generateViewInvoiceLink(invoice.getId());
        String paymentLink = generatePaymentLink(invoice.getId());

        invoice.setViewLink(viewLink);
        invoice.setPaymentLink(paymentLink);
        invoice.setExpiryDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS)); 

        Invoice savedInvoice = invoiceRepository.save(invoice);

        sendLinksViaEmail(savedInvoice);

        return savedInvoice;
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }

    private String generateViewInvoiceLink(Long id) {
        Invoice invoice = getInvoiceById(id);

        if (invoice.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invoice link has expired");
        }

        return String.format(VIEW_INVOICE_URL_TEMPLATE, id);
    }

    private String generatePaymentLink(Long id) {
        Invoice invoice = getInvoiceById(id);

        if (invoice.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invoice link has expired");
        }

        return String.format(PAY_INVOICE_URL_TEMPLATE, id);
    }

    private void sendLinksViaEmail(Invoice invoice) {
        String subject = "Invoice Details";
        String body = "View Invoice: " + invoice.getViewLink() + "\n" +
                      "Pay Invoice: " + invoice.getPaymentLink();

        emailService.sendEmail(invoice.getEmail(), subject, body);
    }
    
}
