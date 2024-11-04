package ru.rutmiit.repository.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.SessionRegistration;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.SessionRegistrationRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class SessionRegistrationImpl extends BaseRepository<SessionRegistration, UUID> implements SessionRegistrationRepository {


    @PersistenceContext
    EntityManager entityManager;
    public SessionRegistrationImpl() {
        super(SessionRegistration.class);
    }

    @Override
    public List<SessionRegistration> getSessionRegistrationsByMember(UUID memberId) {
        return entityManager.createQuery("SELECT sr FROM SessionRegistration sr JOIN FETCH sr.id.session s WHERE sr.id.member = :memberId", SessionRegistration.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
