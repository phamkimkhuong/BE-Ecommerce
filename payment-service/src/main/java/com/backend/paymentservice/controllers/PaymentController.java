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

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.paymentservice.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/payment")
@Slf4j
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;

    @PostMapping("/vn-pay/create-payment")
    public ApiResponseDTO<?> createPayment(HttpServletRequest request, @RequestParam("orderId") long orderId)
            throws UnsupportedEncodingException {
        String paymentUrl = paymentService.createVNPPayment(request, orderId);
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        if (paymentUrl == null) {
            response.setCode(400);
            response.setMessage("Lỗi tạo đường dẫn thanh toán");
            response.setData(null);
            return response;
        }
        response.setCode(201);
        response.setMessage("Tạo đường dẫn thanh toán thành công");
        response.setData(paymentUrl);
        return response;
    }

    @GetMapping("/vn-pay/payment-info")
    public ModelAndView paymentSuccess(HttpServletRequest request, @RequestParam("vnp_ResponseCode") String status){
        log.info("Xem trạng thái thanh toán: {}", status);
        ModelAndView modelAndView = new ModelAndView("payment-result");
        if ("00".equals(status)) {
            paymentService.update(request);
            modelAndView.addObject("success", true);
            modelAndView.addObject("message", "Thanh toán thành công");
            modelAndView.addObject("orderCode", request.getParameter("vnp_TxnRef"));
            modelAndView.addObject("amount", request.getParameter("vnp_Amount"));
            modelAndView.addObject("bankCode", request.getParameter("vnp_BankCode"));
        } else {
            modelAndView.addObject("success", false);
            modelAndView.addObject("message", "Thanh toán thất bại");
        }
        return modelAndView;
    }

}
