/*
 * @(#) $(NAME).java    1.0     4/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.paymentservice.controllers;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 25-April-2025 11:30 PM
 */
import com.backend.paymentservice.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/payment/vn-pay/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestParam("amount") long amount)
            throws UnsupportedEncodingException {
        String paymentUrl = paymentService.createVNPPayment(request, amount);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/api/payment/vn-pay/payment-info")
    public ResponseEntity<?> paymentSuccess(@RequestParam("vnp_ResponseCode") String status) {
        if ("00".equals(status)) {
            return ResponseEntity.ok("Payment successful");
        } else {
            return ResponseEntity.badRequest().body("Payment failed");
        }
    }
}
