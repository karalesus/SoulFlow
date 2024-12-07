package ru.rutmiit.repositories;

import ru.rutmiit.models.Review;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface ReviewRepository {

    BigDecimal getRatingByInstructor(UUID instructorId);
    boolean existsById(MemberSessionKeys memberSessionKeys);

    List<Review> findAllWithPagination(int offset, int limit);
    long countReviews();
}
