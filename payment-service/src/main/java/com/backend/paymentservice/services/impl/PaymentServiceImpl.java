/*
 * @(#) $(NAME).java    1.0     4/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.paymentservice.services.impl;

/*
 * @description
 * @author: Tran Tan Dat, updated by Pham Kim Khuong
 * @version: 1.1
 * @created: 26-April-2025 1:43 AM
 */

import com.backend.commonservice.enums.PaymentEventType;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.commonservice.model.TokenContext;
import com.backend.paymentservice.configs.VNPayConfig;
import com.backend.paymentservice.entity.PaymentInfo;
import com.backend.paymentservice.event.PaymentProducer;
import com.backend.paymentservice.repository.PaymentInfoRepository;
import com.backend.paymentservice.repository.feignClient.GatewayClient;
import com.backend.paymentservice.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    PaymentInfoRepository paymentInfoRep;
    PaymentProducer paymentProducer;
    GatewayClient gatewayClient;

    /**
     * Tạo đường dẫn thanh toán VNPay cho đơn hàng
     *
     * @param request Tham số từ request
     * @param orderId ID đơn hàng cần thanh toán
     * @return Đường dẫn thanh toán VNPay
     */
    @Override
    public String createVNPPayment(HttpServletRequest request, long orderId) {
        try {
            PaymentInfo paymentInfo = paymentInfoRep.findByOrderId(orderId);
            if (paymentInfo == null) {
                throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND,
                        "Không tìm thấy thông tin thanh toán cho đơn hàng: " + orderId);
            }
//        if (paymentInfo.getPaymentUrl() != null) {
//            log.info("Đường dẫn thanh toán đã tồn tại: {}", paymentInfo.getPaymentUrl());
//            return paymentInfo.getPaymentUrl();
//        }
            log.info("Tạo VNPay payment cho đơn hàng:");
            String orderType = "other";
            long amount = (long) (paymentInfo.getAmount() * 100L);
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
            String returnUrl = VNPayConfig.vnp_ReturnUrl;
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // With this code
//            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            TimeZone vnpTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            Calendar cld = Calendar.getInstance(vnpTimeZone);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(vnpTimeZone);
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
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query;
            paymentInfo.setPaymentUrl(paymentUrl);
            paymentInfo.setVnpTxnRef(vnp_TxnRef);
            paymentInfoRep.save(paymentInfo);
            // Lấy JWT token từ request header
            String token = request.getHeader("Authorization");
            // Lưu token vào cache với key là orderId
            gatewayClient.storeToken(orderId, token);
            return paymentUrl;
        } catch (Exception e) {
            log.error("Lỗi khi tạo VNPay payment cho đơn hàng {}: {}", orderId, e.getMessage(), e);
            throw new AppException(ErrorMessage.INTERNAL_SERVER_ERROR,
                    "Lỗi khi tạo VNPay payment cho đơn hàng: " + orderId);
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
            PaymentInfo paymentInfo = paymentInfoRep.findByOrderId((orderId));
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
     * Lưu thông tin thanh toán
     *
     * @param paymentInfo Thông tin thanh toán cần lưu
     */
    @Override
    public void save(PaymentInfo paymentInfo) {
        log.info("Lưu thông tin thanh toán: {}", paymentInfo);
        paymentInfoRep.save(paymentInfo);
    }

    /**
     * Cập nhật thông tin thanh toán từ VNPay callback
     *
     * @param request Tham số từ VNPay
     */
    @Override
    public void update(HttpServletRequest request) {
        log.info("Cập nhật thông tin thanh toán từ VNPay callback");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String date = request.getParameter("vnp_PayDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime paymentDate = LocalDateTime.parse(date, formatter);
        String cardType = request.getParameter("vnp_CardType");
        String bankCode = request.getParameter("vnp_BankCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String token = request.getParameter("token");
        PaymentInfo paymentInfo = paymentInfoRep.findByVnpTxnRef((vnp_TxnRef));
        if (paymentInfo == null) {
            throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND,
                    "Không tìm thấy thông tin thanh toán cho giao dịch: " + transactionId);
        }
        paymentInfo.setPaymentDate(paymentDate);
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setCardType(cardType);
        paymentInfo.setBankCode(bankCode);
        paymentInfo.setSuccess(true);
        paymentInfo.setPaymentEventType(PaymentEventType.PAYMENT_SUCCESS);
        paymentInfoRep.save(paymentInfo);
        try {
            log.info("lấy token ra xem {}", token);
            TokenContext.setToken(token);
            paymentProducer.sendPaymentEvent(paymentInfo);
        } catch (Exception e) {
            throw new AppException(ErrorMessage.KAFKA_ERROR);
        }
    }

    /**
     * Xử lý callback từ VNPay
     *
     * @param request Tham số từ VNPay
     * @return true nếu xác thực thành công, false nếu thất bại
     */
    public boolean processVNPayCallback(HttpServletRequest request) {
        try {
            // Lấy các tham số từ VNPay callback
            String transactionId = request.getParameter("vnp_TransactionNo");
            LocalDateTime paymentDate = LocalDateTime.parse(request.getParameter("vnp_PayDate"));
            String cardType = request.getParameter("vnp_CardType");
            String bankCode = request.getParameter("vnp_BankCode");
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            // Xác thực chữ ký
            StringBuilder hashData = new StringBuilder();
            List<String> fieldNames = new ArrayList<>(request.getParameterMap().keySet());
            Collections.sort(fieldNames);
            for (String fieldName : fieldNames) {
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && !fieldName.equals("vnp_SecureHash") && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    hashData.append('&');
                }
            }
            String calculatedHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            // Kiểm tra chữ ký và mã trạng thái
            if (calculatedHash.equals(vnp_SecureHash) &&
                    "00".equals(vnp_ResponseCode) &&
                    "00".equals(vnp_TransactionStatus)) {

                // Trích xuất ID đơn hàng từ mã giao dịch VNPay (format: orderId_timestamp)
                String[] txnRefParts = vnp_TxnRef.split("_");
                if (txnRefParts.length > 0) {
                    Long orderId = Long.parseLong(txnRefParts[0]);

                    // Cập nhật trạng thái thanh toán
                    PaymentInfo paymentInfo = paymentInfoRep.findByTransactionId(transactionId);
                    if (paymentInfo != null) {
                        paymentInfo.setSuccess(true);
                        paymentInfo.setTransactionId(vnp_TxnRef);
//                        paymentsMap.put(orderId, paymentInfo);

                        log.info("Thanh toán VNPay thành công cho đơn hàng: {}", orderId);
                        return true;
                    } else {
                        log.error("Không tìm thấy thông tin thanh toán cho đơn hàng: {}", orderId);
                    }
                }
            } else {
                log.error("Xác thực callback VNPay thất bại: Mã phản hồi={}, Trạng thái={}",
                        vnp_ResponseCode, vnp_TransactionStatus);
            }
            return false;
        } catch (Exception e) {
            log.error("Lỗi khi xử lý callback từ VNPay: {}", e.getMessage(), e);
            return false;
        }
    }
}
