package ru.rutmiit.service;

import org.springframework.data.domain.Page;
import ru.rutmiit.dto.session.ScheduleSessionsOutputDTO;
import ru.rutmiit.dto.session.SessionInputDTO;
import ru.rutmiit.dto.session.SessionOutputDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionService {

    String addSession(SessionInputDTO sessionInputDTO);

    Page<SessionOutputDTO> getAllSessionsWithPagination(String searchTerm, int page, int size);

    SessionOutputDTO editSession(String uuid, SessionInputDTO sessionInputDTO);

    SessionOutputDTO getSessionById(UUID uuid);
    List<ScheduleSessionsOutputDTO> getUpcomingSessions(LocalDateTime now);
    List<ScheduleSessionsOutputDTO> getDiscountSessions(LocalDateTime now);
}
