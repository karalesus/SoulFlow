package ru.rutmiit.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.models.SessionRegistration;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.SessionRegistrationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRegistrationRepositoryImpl extends BaseRepository<SessionRegistration, MemberSessionKeys> implements SessionRegistrationRepository {

    @PersistenceContext
    EntityManager entityManager;

    public SessionRegistrationRepositoryImpl() {
        super(SessionRegistration.class);
    }

    @Override
    public List<SessionRegistration> getSessionRegistrationsByMember(UUID memberId) {
        return entityManager.createQuery("SELECT sr FROM SessionRegistration sr JOIN sr.id.session s WHERE sr.id.member.id = :memberId", SessionRegistration.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public Long countBySessionIdAndStatus(UUID sessionId, String status) {
        return (Long) entityManager.createQuery("select count(sr.id.session) from SessionRegistration sr where sr.id.session.id = :sessionId and sr.status.name = :status")
                .setParameter("sessionId", sessionId)
                .setParameter("status", status)
                .getSingleResult();
    }

    @Override
    public Optional<String> getStatusBySessionIdAndUserId(UUID sessionId, UUID memberId) {
        List<String> results = entityManager.createQuery(
                        "select sr.status.name from SessionRegistration sr where sr.id.session.id = :sessionId and sr.id.member.id = :memberId",
                        String.class)
                .setParameter("sessionId", sessionId)
                .setParameter("memberId", memberId)
                .getResultList();

        if (results.isEmpty()) return Optional.empty();
        else return Optional.of(results.getFirst());
    }
}
