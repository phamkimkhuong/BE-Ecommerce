package com.backend.danhgia_service.repositories;

import com.backend.danhgia_service.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Tìm tất cả các đánh giá của một sản phẩm
    List<Review> findByProduct(int productId);

    // Tìm tất cả các đánh giá của một khách hàng
    List<Review> findByCustomer(int customerId);

    // Tìm tất cả đánh giá có số sao >= một giá trị nhất định
    List<Review> findBySoSaoGreaterThanEqual(int minRating);

    // Xóa tất cả đánh giá của một sản phẩm
    void deleteByProduct(int productId);

    // Xóa tất cả đánh giá của một khách hàng
    void deleteByCustomer(int customerId);
}
