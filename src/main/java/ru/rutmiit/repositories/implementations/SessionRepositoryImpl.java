package ru.rutmiit.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Session;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class SessionRepositoryImpl extends BaseRepository<Session, UUID> implements SessionRepository {
    public SessionRepositoryImpl() {
        super(Session.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Session> findAllUpcomingSessions(LocalDateTime now) {
        return entityManager.createQuery("SELECT s FROM Session s JOIN FETCH s.instructor i WHERE s.dateTime > :now ORDER BY s.dateTime ASC", Session.class)
                .setParameter("now", now)
                .getResultList();
    }

    @Override
    public List<Session> findAllSessionsWithPagination(String searchTerm, Pageable pageable) {
        var query = entityManager.createQuery(
                "SELECT s FROM Session s WHERE upper(s.name) LIKE upper(:searchTerm) ORDER BY s.name ASC", Session.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long countSessions(String searchTerm) {
        var query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Session s WHERE upper(s.name) LIKE upper(:searchTerm)", Long.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        return query.getSingleResult();
    }
}
