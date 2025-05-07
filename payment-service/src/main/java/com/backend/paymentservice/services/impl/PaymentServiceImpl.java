/*
 * @(#) $(NAME).java    1.0     4/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.paymentservice.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-April-2025 1:43 AM
 */

import com.backend.paymentservice.configs.VNPayConfig;
import com.backend.paymentservice.entity.PaymentInfo;
import com.backend.paymentservice.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final Map<Long, PaymentInfo> paymentsMap = new ConcurrentHashMap<>();

    @Override
    public String createVNPPayment(HttpServletRequest request, long amountRequest) throws UnsupportedEncodingException {
        String orderType = "other";
        long amount = amountRequest * 100;
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return VNPayConfig.vnp_PayUrl + "?" + query;
    }

    /**
     * Xử lý thanh toán cho đơn hàng
     * Trong môi trường microservice, phương thức này giả lập quá trình thanh toán
     * không cần HttpServletRequest
     *
     * @param orderId       ID của đơn hàng
     * @param customerId    ID của khách hàng
     * @param amount        Số tiền thanh toán
     * @param paymentMethod Phương thức thanh toán
     * @return true nếu thanh toán thành công, false nếu thất bại
     */
    @Override
    public boolean processPayment(Long orderId, Long customerId, Double amount, String paymentMethod) {
        try {
            log.info("Xử lý thanh toán cho đơn hàng: {}, khách hàng: {}, số tiền: {}, phương thức: {}",
                    orderId, customerId, amount, paymentMethod);

            // Giả lập quá trình thanh toán với tỷ lệ thành công 90%
            boolean paymentSuccess = Math.random() < 0.9;

            // Tạo ID thanh toán và ID giao dịch
            Long paymentId = System.currentTimeMillis();
            String transactionId = paymentSuccess ? UUID.randomUUID().toString() : null;

            // Lưu thông tin thanh toán
            paymentsMap.put(orderId, new PaymentInfo(
                    paymentId, orderId, amount, transactionId, paymentMethod, paymentSuccess));

            log.info("Kết quả thanh toán cho đơn hàng {}: {}", orderId,
                    paymentSuccess ? "Thành công" : "Thất bại");

            return paymentSuccess;
        } catch (Exception e) {
            log.error("Lỗi khi xử lý thanh toán cho đơn hàng {}: {}", orderId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Xử lý hoàn tiền cho đơn hàng
     *
     * @param orderId ID đơn hàng cần hoàn tiền
     */
    @Override
    public boolean processRefund(Long orderId) {
        try {
            PaymentInfo paymentInfo = paymentsMap.get(orderId);
            if (paymentInfo == null) {
                log.warn("Không tìm thấy thông tin thanh toán cho đơn hàng: {}", orderId);
                return false;
            }

            log.info("Xử lý hoàn tiền cho đơn hàng: {}, số tiền: {}", orderId, paymentInfo.getAmount());
            // Giả lập quá trình hoàn tiền thành công
            log.info("Đã hoàn tiền cho đơn hàng: {}", orderId);
            return true;
        } catch (Exception e) {
            log.error("Lỗi khi hoàn tiền cho đơn hàng {}: {}", orderId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Lấy ID của thanh toán từ ID đơn hàng
     *
     * @param orderId ID của đơn hàng
     * @return ID của thanh toán
     */
    @Override
    public Long getPaymentIdByOrderId(Long orderId) {
        PaymentInfo paymentInfo = paymentsMap.get(orderId);
        return paymentInfo != null ? paymentInfo.getPaymentId() : null;
    }

    /**
     * Lấy ID giao dịch từ ID đơn hàng
     *
     * @param orderId ID của đơn hàng
     * @return ID giao dịch
     */
    @Override
    public String getTransactionIdByOrderId(Long orderId) {
        PaymentInfo paymentInfo = paymentsMap.get(orderId);
        return paymentInfo != null ? paymentInfo.getTransactionId() : null;
    }
}
