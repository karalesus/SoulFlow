package ru.rutmiit.repositories;

import org.springframework.data.domain.Pageable;
import ru.rutmiit.models.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository {

    List<Session> findAllUpcomingSessions(LocalDateTime now);

    List<Session> findAllSessionsWithPagination(String searchTerm, Pageable pageable);

    long countSessions(String searchTerm);
}
