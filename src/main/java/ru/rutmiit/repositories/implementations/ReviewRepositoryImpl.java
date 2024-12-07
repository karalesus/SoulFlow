package ru.rutmiit.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Instructor;
import ru.rutmiit.models.Review;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.ReviewRepository;

import java.math.BigDecimal;
import java.util.List;
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
        Double result = entityManager.createQuery("SELECT ROUND(COALESCE(AVG(r.rate), 0), 2) FROM Review r WHERE r.id.session.instructor.id = :instructorId", Double.class)
                .setParameter("instructorId", instructorId)
                .getSingleResult();
        return BigDecimal.valueOf(result);
    }

    @Override
    public boolean existsById(MemberSessionKeys memberSessionKeys) {
        String query = "SELECT COUNT(r) FROM Review r WHERE r.id = :memberSessionKeys";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("memberSessionKeys", memberSessionKeys)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<Review> findAllWithPagination(int offset, int limit) {
        var query = entityManager.createQuery(
                "SELECT r FROM Review r", Review.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long countReviews() {
        var query = entityManager.createQuery(
                "SELECT COUNT(r) FROM Review r", Long.class);
        return query.getSingleResult();
    }

}
