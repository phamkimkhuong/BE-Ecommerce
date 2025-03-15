package com.backend.danhgia_service.services;

import com.backend.danhgia_service.entities.Review;
import com.backend.danhgia_service.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Thêm một đánh giá
    public Review addReview(Review review) {
        return reviewRepository.save(review);  // Lưu đánh giá vào cơ sở dữ liệu
    }

    // Cập nhật một đánh giá
    public Review updateReview(Long id, Review reviewDetails) {
        Review existingReview = reviewRepository.findById(id).orElse(null);
        if (existingReview != null) {
            existingReview.setSoSao(reviewDetails.getSoSao());
            existingReview.setNoiDung(reviewDetails.getNoiDung());
            existingReview.setNgayDanhGia(reviewDetails.getNgayDanhGia());
            existingReview.setProduct(reviewDetails.getProduct());
            existingReview.setCustomer(reviewDetails.getCustomer());
            return reviewRepository.save(existingReview);
        }
        return null;  // Nếu không tìm thấy đánh giá
    }

    // Xóa một đánh giá
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            reviewRepository.delete(review);  // Xóa đánh giá từ cơ sở dữ liệu
        }
    }

    // Lấy tất cả các đánh giá của một sản phẩm
    public List<Review> getReviewsByProduct(int productId) {
        return reviewRepository.findByProduct(productId);  // Lấy các đánh giá của sản phẩm từ repository
    }

    // Lấy tất cả các đánh giá của một khách hàng
    public List<Review> getReviewsByCustomer(int customerId) {
        return reviewRepository.findByCustomer(customerId);  // Lấy các đánh giá của khách hàng từ repository
    }

    // Lấy tất cả đánh giá có số sao >= một giá trị nhất định
    public List<Review> getReviewsByRatingGreaterThanEqual(int minRating) {
        return reviewRepository.findBySoSaoGreaterThanEqual(minRating);  // Lấy các đánh giá có số sao >= minRating
    }

    // Xóa tất cả đánh giá của một sản phẩm
    public void deleteReviewsByProduct(int productId) {
        reviewRepository.deleteByProduct(productId);  // Xóa các đánh giá của sản phẩm từ repository
    }

    // Xóa tất cả đánh giá của một khách hàng
    public void deleteReviewsByCustomer(int customerId) {
        reviewRepository.deleteByCustomer(customerId);  // Xóa các đánh giá của khách hàng từ repository
    }
}
