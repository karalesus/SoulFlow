package ru.rutmiit.service;

import org.springframework.data.domain.Page;
import ru.rutmiit.dto.Session.DiscountSessionOutputDTO;
import ru.rutmiit.dto.Session.SessionInputDTO;
import ru.rutmiit.dto.Session.SessionOutputDTO;
import ru.rutmiit.dto.Session.UpcomingSessionOutputDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionService {

    String addSession(SessionInputDTO sessionInputDTO);

    List<SessionInputDTO> getAllSessions();

    Page<SessionOutputDTO> getAllSessionsWithPagination(String searchTerm, int page, int size);

    SessionOutputDTO editSession(String uuid, SessionInputDTO sessionInputDTO);

    SessionOutputDTO getSessionById(UUID uuid);
    List<UpcomingSessionOutputDTO> getUpcomingSessions(LocalDateTime now);
    List<DiscountSessionOutputDTO> getDiscountSessions(LocalDateTime now);
}
