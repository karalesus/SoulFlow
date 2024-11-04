package ru.rutmiit.repository.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Session;
import ru.rutmiit.domain.SessionRegistration;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.SessionRepository;

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
    public List<Session> getUpcomingSessions(LocalDateTime now) {
        return entityManager.createQuery("SELECT s FROM Session s JOIN FETCH s.instructor i WHERE s.dateTime > :now ORDER BY s.dateTime ASC", Session.class)
                .setParameter("now", now)
                .getResultList();
    }
}
