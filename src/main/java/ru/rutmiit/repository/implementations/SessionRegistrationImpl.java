package ru.rutmiit.repository.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.SessionRegistration;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.SessionRegistrationRepository;

import java.util.UUID;

@Repository
public class SessionRegistrationImpl extends BaseRepository<SessionRegistration, UUID> implements SessionRegistrationRepository {

    public SessionRegistrationImpl() {
        super(SessionRegistration.class);
    }
}
