package com.backend.danhgia_service.controllers;

import com.backend.danhgia_service.entities.Review;
import com.backend.danhgia_service.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // API để tạo mới một đánh giá
    @PostMapping
    public ResponseEntity<Review> addReview(@Valid @RequestBody Review review) {
        Review savedReview = reviewService.addReview(review);  // Lưu đánh giá vào cơ sở dữ liệu
        return ResponseEntity.ok(savedReview);  // Trả về đánh giá đã được tạo
    }

    // API để cập nhật đánh giá
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        Review updatedReview = reviewService.updateReview(id, reviewDetails); // Cập nhật thông tin đánh giá
        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
    }

    // API để xóa đánh giá
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id); // Xóa đánh giá từ cơ sở dữ liệu
        return ResponseEntity.noContent().build(); // Trả về status code 204 (No Content)
    }

    // API để lấy tất cả đánh giá của một sản phẩm
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable int productId) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews); // Trả về danh sách đánh giá
    }

    // API để lấy tất cả đánh giá của một khách hàng
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Review>> getReviewsByCustomer(@PathVariable int customerId) {
        List<Review> reviews = reviewService.getReviewsByCustomer(customerId);
        return ResponseEntity.ok(reviews); // Trả về danh sách đánh giá
    }

    // API để lấy tất cả đánh giá có số sao >= một giá trị nhất định
    @GetMapping("/rating/{minRating}")
    public ResponseEntity<List<Review>> getReviewsByRatingGreaterThanEqual(@PathVariable int minRating) {
        List<Review> reviews = reviewService.getReviewsByRatingGreaterThanEqual(minRating);
        return ResponseEntity.ok(reviews); // Trả về danh sách đánh giá
    }

    // API để xóa tất cả đánh giá của một sản phẩm
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteReviewsByProduct(@PathVariable int productId) {
        reviewService.deleteReviewsByProduct(productId);
        return ResponseEntity.ok("Đánh giá của sản phẩm với ID " + productId + " đã được xóa.");
    }

    // API để xóa tất cả đánh giá của một khách hàng
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<String> deleteReviewsByCustomer(@PathVariable int customerId) {
        reviewService.deleteReviewsByCustomer(customerId);
        return ResponseEntity.ok("Đánh giá của khách hàng với ID " + customerId + " đã được xóa.");
    }
}
