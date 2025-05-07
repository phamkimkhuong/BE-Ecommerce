/*
 * @(#) $(NAME).java    1.0     4/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.paymentservice.services;

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

     /**
      * Xử lý thanh toán cho đơn hàng
      * 
      * @param orderId       ID của đơn hàng
      * @param customerId    ID của khách hàng
      * @param amount        Số tiền thanh toán
      * @param paymentMethod Phương thức thanh toán
      * @return true nếu thanh toán thành công, false nếu thất bại
      */
     boolean processPayment(Long orderId, Long customerId, Double amount, String paymentMethod);

     /**
      * Lấy ID của thanh toán từ ID đơn hàng
      * 
      * @param orderId ID của đơn hàng
      * @return ID của thanh toán
      */
     Long getPaymentIdByOrderId(Long orderId);

     /**
      * Lấy ID giao dịch từ ID đơn hàng
      * 
      * @param orderId ID của đơn hàng
      * @return ID giao dịch
      */
     String getTransactionIdByOrderId(Long orderId);
}
