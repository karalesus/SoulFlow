package ru.rutmiit.repository.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Review;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.ReviewRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class ReviewRepositoryImpl extends BaseRepository<Review, UUID> implements ReviewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    @Override
    public BigDecimal getRatingByInstructor(UUID instructorId) {
        return entityManager.createQuery("SELECT ROUND(AVG(r.rate), 2) FROM Review r WHERE r.id.session.instructor.id = :instructorId", BigDecimal.class)
                .setParameter("instructorId", instructorId)
                .getSingleResult();
    }
}
