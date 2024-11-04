package ru.rutmiit.repository;

import ru.rutmiit.domain.SessionRegistration;

import java.util.List;
import java.util.UUID;


public interface UserRepository {

    List<SessionRegistration> getSessionRegistrationsByMember(UUID memberId);
}
