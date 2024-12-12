package ru.rutmiit.service;

import org.springframework.data.domain.Page;
import ru.rutmiit.dto.review.ReviewInputDTO;
import ru.rutmiit.dto.review.ReviewOutputDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

    void addReview(ReviewInputDTO reviewInputDTO);

    List<ReviewOutputDTO> getAllReviews();
    boolean hasUserReviewedSession(UUID userId, UUID sessionId);

//    ReviewOutputDTO editReview(UUID uuid, ReviewInputDTO reviewInputDTO);

    Page<ReviewOutputDTO> getAllReviewsWithPagination(int page, int size);

    ReviewOutputDTO getReviewById(UUID uuid);
    BigDecimal getRatingByInstructor(UUID instructorId);
}
