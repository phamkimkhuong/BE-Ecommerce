package com.backend.promotionservice.services;

import com.backend.promotionservice.entities.Promotion;
import com.backend.promotionservice.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    // Thêm khuyến mãi
    public Promotion addPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    // Áp dụng khuyến mãi
    public double applyDiscount(String promoCode, double totalAmount) {
        Promotion promo = promotionRepository.findByCode(promoCode);
        if (promo != null && promo.isValid()) {
            // Kiểm tra mức chi tiêu tối thiểu
            if (totalAmount >= promo.getMinSpendAmount()) {
                if ("discount".equals(promo.getType())) {
                    return totalAmount * (1 - promo.getValue() / 100); // Giảm giá theo tỷ lệ phần trăm
                } else if ("flat_discount".equals(promo.getType())) {
                    return totalAmount - promo.getValue(); // Giảm giá cố định (số tiền)
                }
            } else {
                // Trả lại toàn bộ nếu không đủ điều kiện chi tiêu tối thiểu
                return totalAmount;
            }
        }
        return totalAmount; // Trả về giá trị gốc nếu không có khuyến mãi
    }

    // Cập nhật khuyến mãi
    public Promotion updatePromotion(String code, Promotion promotion) {
        // Tìm khuyến mãi theo mã
        Promotion existingPromotion = promotionRepository.findByCode(code);

        if (existingPromotion != null) {
            // Cập nhật thông tin khuyến mãi
            existingPromotion.setType(promotion.getType());
            existingPromotion.setValue(promotion.getValue());
            existingPromotion.setStartDate(promotion.getStartDate());
            existingPromotion.setEndDate(promotion.getEndDate());

            // Lưu lại khuyến mãi đã cập nhật
            return promotionRepository.save(existingPromotion);
        }
        // Nếu không tìm thấy khuyến mãi
        return null;
    }

    // Xóa khuyến mãi
    public void deletePromotion(String code) {
        Promotion promotion = promotionRepository.findByCode(code);
        if (promotion != null) {
            promotionRepository.delete(promotion);
        }
    }

    // Tìm khuyến mãi theo mã
    public Promotion getPromotionByCode(String code) {
        return promotionRepository.findByCode(code);
    }

    // Tìm khuyến mãi theo loại
    public List<Promotion> getPromotionsByType(String type) {
        return promotionRepository.findByType(type);
    }

    // Tìm khuyến mãi theo giá trị
    public List<Promotion> getPromotionsByValue(Double value) {
        return promotionRepository.findByValue(value);
    }

    // Tìm khuyến mãi theo loại và giá trị
    public List<Promotion> getPromotionsByTypeAndValue(String type, Double value) {
        return promotionRepository.findByTypeAndValue(type, value);
    }

    // Cập nhật trạng thái khuyến mãi (kích hoạt/vô hiệu hóa)
    public Promotion updateStatus(String code, boolean isActive) {
        Promotion promotion = promotionRepository.findByCode(code);
        if (promotion != null) {
            promotion.setIsActive(isActive);
            return promotionRepository.save(promotion);
        }
        return null;
    }

    // Kiểm tra và tự động vô hiệu hóa khuyến mãi hết hạn
    public void checkExpiredPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        Date currentDate = new Date();
        for (Promotion promo : promotions) {
            if (promo.isExpired()) {
                promo.setIsActive(false);
                promotionRepository.save(promo);
            }
        }
    }

    // Cập nhật số lần sử dụng và kiểm tra số lần sử dụng
    public double applyDiscountAndUpdateUsage(String promoCode, double totalAmount) {
        Promotion promo = promotionRepository.findByCode(promoCode);
        if (promo != null && promo.isValid()) {
            if (promo.getUsageCount() < promo.getUsageLimit()) {
                promo.setUsageCount(promo.getUsageCount() + 1);
                promotionRepository.save(promo); // Cập nhật lại số lần sử dụng

                if ("discount".equals(promo.getType())) {
                    return totalAmount * (1 - promo.getValue() / 100);
                } else if ("flat_discount".equals(promo.getType())) {
                    return totalAmount - promo.getValue();
                }
            }
        }
        return totalAmount; // Trả về giá trị gốc nếu không có khuyến mãi
    }
}
