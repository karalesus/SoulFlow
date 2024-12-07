package ru.rutmiit.service;

import ru.rutmiit.dto.Session.AttendedSessionsDTO;
import ru.rutmiit.dto.Session.RegisteredSessionsDTO;
import ru.rutmiit.dto.SessionRegistrationDTO;

import java.util.List;
import java.util.UUID;

public interface SessionRegistrationService {

    void addSessionRegistration(SessionRegistrationDTO sessionRegistrationDTO);

    Long getAvailableSpots(String sessionId, int maxCapacity);
    List<SessionRegistrationDTO> getAllSessionRegistrations();
    void cancelSessionRegistration(SessionRegistrationDTO sessionRegistrationDTO);

    void updateStatusToAttended(UUID memberId);

    List<AttendedSessionsDTO> getAttendedSessionsByUserId(UUID userId);
    List<RegisteredSessionsDTO> getRegisteredSessionsByUserId(UUID userId);

}
