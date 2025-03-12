package fit.se.promotionservice.controllers;

import fit.se.promotionservice.entities.Promotion;
import fit.se.promotionservice.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    // API để áp dụng khuyến mãi
    @PostMapping("/apply")
    public double applyPromo(@RequestParam String promoCode, @RequestParam double totalAmount) {
        return promotionService.applyDiscount(promoCode, totalAmount);
    }

    // API để thêm khuyến mãi
    @PostMapping("/add")
    public ResponseEntity<Promotion> addPromotion(@RequestBody Promotion promotion) {
        Promotion savedPromotion = promotionService.addPromotion(promotion);
        return ResponseEntity.ok(savedPromotion);
    }

    // API để sửa thông tin khuyến mãi
    @PutMapping("/update/{code}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable String code, @RequestBody Promotion updatedPromotion) {
        Promotion promotion = promotionService.updatePromotion(code, updatedPromotion);
        if (promotion != null) {
            return ResponseEntity.ok(promotion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // API để xóa khuyến mãi
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<Void> deletePromotion(@PathVariable String code) {
        promotionService.deletePromotion(code);
        return ResponseEntity.noContent().build();
    }

    // API để tìm khuyến mãi theo mã
    @GetMapping("/find/{code}")
    public ResponseEntity<Promotion> getPromotionByCode(@PathVariable String code) {
        Promotion promotion = promotionService.getPromotionByCode(code);
        return promotion != null ? ResponseEntity.ok(promotion) : ResponseEntity.notFound().build();
    }

    // API để tìm khuyến mãi theo loại
    @GetMapping("/find/type/{type}")
    public List<Promotion> getPromotionsByType(@PathVariable String type) {
        return promotionService.getPromotionsByType(type);
    }

    // API để tìm khuyến mãi theo giá trị
    @GetMapping("/find/value/{value}")
    public List<Promotion> getPromotionsByValue(@PathVariable Double value) {
        return promotionService.getPromotionsByValue(value);
    }

    // API để tìm khuyến mãi theo loại và giá trị
    @GetMapping("/find/type-and-value")
    public List<Promotion> getPromotionsByTypeAndValue(@RequestParam String type, @RequestParam Double value) {
        return promotionService.getPromotionsByTypeAndValue(type, value);
    }
    // API để kiểm tra và vô hiệu hóa khuyến mãi hết hạn
    @PutMapping("/check-expired")
    public ResponseEntity<String> checkExpiredPromotions() {
        promotionService.checkExpiredPromotions();
        return ResponseEntity.ok("Checked and deactivated expired promotions.");
    }

}
