package ru.rutmiit.repository.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.User;
import ru.rutmiit.domain.SessionRegistration;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, UUID> implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<SessionRegistration> getSessionRegistrationsByMember(UUID memberId) {
        return entityManager.createQuery("SELECT sr FROM SessionRegistration sr JOIN FETCH sr.id.session s WHERE sr.id.member = :memberId", SessionRegistration.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
