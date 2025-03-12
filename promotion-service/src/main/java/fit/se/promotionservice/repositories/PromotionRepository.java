package fit.se.promotionservice.repositories;

import fit.se.promotionservice.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, String> {
    // Tìm khuyến mãi theo mã
    Promotion findByCode(String code);

    // Tìm tất cả khuyến mãi theo loại
    List<Promotion> findByType(String type);

    // Tìm tất cả khuyến mãi theo giá trị
    List<Promotion> findByValue(Double value);

    // Tìm khuyến mãi theo loại và giá trị
    List<Promotion> findByTypeAndValue(String type, Double value);

    // Tìm khuyến mãi theo trạng thái (active/inactive)
    List<Promotion> findByIsActive(boolean isActive);

    // Tìm khuyến mãi đã hết hạn
    List<Promotion> findByEndDateBeforeAndIsActiveTrue(java.util.Date currentDate);

    // Tìm khuyến mãi theo số lần sử dụng và giới hạn số lần sử dụng
    List<Promotion> findByUsageCountLessThanAndUsageLimitGreaterThan(int usageCount, int usageLimit);
}