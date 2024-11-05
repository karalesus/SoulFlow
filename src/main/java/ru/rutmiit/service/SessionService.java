package ru.rutmiit.service;

import ru.rutmiit.dto.SessionDTO;
import ru.rutmiit.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface SessionService {

    void addSession(SessionDTO sessionDTO);
    List<SessionDTO> getAllSessions();
    SessionDTO editSession(SessionDTO sessionDTO);
    SessionDTO getSessionById(UUID uuid);
}
