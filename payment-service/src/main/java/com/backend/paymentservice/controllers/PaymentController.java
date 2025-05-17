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
import com.backend.paymentservice.repository.feignClient.GatewayClient;
import com.backend.paymentservice.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponseDTO<?> paymentSuccess(HttpServletRequest request, @RequestParam("vnp_ResponseCode") String status) {
        log.info("Xem trạng thái thanh toán: {}", status);
        ApiResponseDTO<Boolean> response = new ApiResponseDTO<>();
        if ("00".equals(status)) {
            response.setCode(200);
            response.setMessage("Thanh toán thành công");
            response.setData(true);
            paymentService.update(request);
        } else {
            response.setCode(400);
            response.setMessage("Thanh toán thất bại");
            response.setData(false);
        }
        return response;
    }

}
