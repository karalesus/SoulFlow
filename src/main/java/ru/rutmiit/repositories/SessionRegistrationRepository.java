package ru.rutmiit.repositories;

import ru.rutmiit.models.SessionRegistration;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRegistrationRepository {
    List<SessionRegistration> getSessionRegistrationsByMember(UUID memberId);
    Long countBySessionIdAndStatus(UUID sessionId, String status);
    Optional<String> getStatusBySessionIdAndUserId(UUID sessionId, UUID memberId);
}
