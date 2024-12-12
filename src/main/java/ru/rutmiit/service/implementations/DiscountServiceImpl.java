package ru.rutmiit.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rutmiit.repositories.implementations.ReviewRepositoryImpl;
import ru.rutmiit.service.DiscountService;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DiscountServiceImpl implements DiscountService {

    ReviewRepositoryImpl reviewRepository;

    @Autowired
    public void setReviewRepository(ReviewRepositoryImpl reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public BigDecimal calculateDiscount(UUID instructorId) {
        BigDecimal instructorRating =
                reviewRepository.getRatingByInstructor(instructorId);
        if (instructorRating.compareTo(BigDecimal.valueOf(4.0)) <= 0) {
            return BigDecimal.valueOf(0.30);
        } else if (instructorRating.compareTo(BigDecimal.valueOf(4.3)) < 0) {
            return BigDecimal.valueOf(0.10);
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            return originalPrice.subtract(originalPrice.multiply(discount));
        } else {
            return originalPrice;
        }
    }
}
