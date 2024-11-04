package ru.rutmiit.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.rutmiit.domain.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository {

    List<Session> getUpcomingSessions(LocalDateTime now);
}
