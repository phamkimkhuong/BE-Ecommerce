package com.backend.promotionservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Promotion {

    @Id
    private String code;    // Mã khuyến mãi
    private String type;    // Loại khuyến mãi (giảm giá, quà tặng, v.v.)
    private double value;   // Giá trị khuyến mãi (giảm giá % hoặc số tiền)

    @Temporal(TemporalType.DATE)  // Hoặc TemporalType.TIMESTAMP nếu muốn lưu cả giờ và phút
    private Date startDate;

    @Temporal(TemporalType.DATE)  // Hoặc TemporalType.TIMESTAMP nếu muốn lưu cả giờ và phút
    private Date endDate;

    private boolean isActive;  // Trạng thái hoạt động của khuyến mãi
    private int usageLimit;    // Giới hạn số lần sử dụng khuyến mãi
    private int usageCount;    // Số lần đã sử dụng khuyến mãi
    private double minSpendAmount;  // Mức chi tiêu tối thiểu để áp dụng khuyến mãi

    // Kiểm tra xem khuyến mãi có còn hợp lệ không và có còn giới hạn sử dụng không
    public boolean isValid() {
        if (startDate == null || endDate == null) {
            return false;
        }
        Date currentDate = new Date();
        return currentDate.after(startDate) && currentDate.before(endDate) && isActive && usageCount < usageLimit;
    }

    // Kiểm tra xem khuyến mãi đã hết hạn chưa
    public boolean isExpired() {
        return new Date().after(endDate);
    }

    // Tăng số lần sử dụng khuyến mãi
    public void incrementUsageCount() {
        if (usageCount < usageLimit) {
            usageCount++;
        }
    }

    // Constructor mặc định để JPA sử dụng
    public Promotion() {
    }

    // Constructor với các tham số
    public Promotion(String code, String type, double value, Date startDate, Date endDate, boolean isActive, int usageLimit, double minSpendAmount) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.usageLimit = usageLimit;
        this.usageCount = 0; // Mặc định ban đầu số lần sử dụng là 0
        this.minSpendAmount = minSpendAmount;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isActive=" + isActive +
                ", usageLimit=" + usageLimit +
                ", usageCount=" + usageCount +
                ", minSpendAmount=" + minSpendAmount +
                '}';
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }


}
