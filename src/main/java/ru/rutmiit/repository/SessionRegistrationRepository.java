package ru.rutmiit.repository;

import ru.rutmiit.domain.SessionRegistration;

import java.util.List;
import java.util.UUID;

public interface SessionRegistrationRepository {
    List<SessionRegistration> getSessionRegistrationsByMember(UUID memberId);
}
