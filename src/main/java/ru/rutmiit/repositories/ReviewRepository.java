package ru.rutmiit.repositories;

import org.springframework.data.domain.Pageable;
import ru.rutmiit.models.Review;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface ReviewRepository {

    BigDecimal getRatingByInstructor(UUID instructorId);
    boolean existsById(MemberSessionKeys memberSessionKeys);

    List<Review> findAllReviewsWithPagination(Pageable pageable);

    long countReviews();
}
