package ru.rutmiit.repositories;

import ru.rutmiit.models.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository {

    List<Session> getUpcomingSessions(LocalDateTime now);

    List<Session> findAllWithPagination(String searchTerm, int offset, int limit);

    long countSessions(String searchTerm);
}
