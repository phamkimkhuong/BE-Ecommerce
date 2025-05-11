/*
 * @(#) $(NAME).java    1.0     4/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.paymentservice.services;

import com.backend.paymentservice.entity.PaymentInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 25-April-2025 11:00 PM
 */
public interface PaymentService {
    String createVNPPayment(HttpServletRequest request, long amountRequest) throws UnsupportedEncodingException;

    boolean processRefund(Long orderId);

    void save(PaymentInfo paymentInfo);

    void update(HttpServletRequest request);
}
