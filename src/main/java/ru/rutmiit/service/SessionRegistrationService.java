package ru.rutmiit.service;

import ru.rutmiit.dto.session.AttendedSessionsDTO;
import ru.rutmiit.dto.session.ScheduleSessionsOutputDTO;
import ru.rutmiit.dto.sessionRegistration.SessionRegistrationDTO;

import java.util.List;
import java.util.UUID;

public interface SessionRegistrationService {

    String addSessionRegistration(SessionRegistrationDTO sessionRegistrationDTO);

    Long getAvailableSpots(String sessionId, int maxCapacity);
//    List<SessionRegistrationDTO> getAllSessionRegistrations();
    void cancelSessionRegistration(SessionRegistrationDTO sessionRegistrationDTO);

    void updateStatusToAttended(UUID memberId);

    List<AttendedSessionsDTO> getAttendedSessionsByUserId(UUID userId);
    List<ScheduleSessionsOutputDTO> getRegisteredSessionsByUserId(UUID userId);

}
