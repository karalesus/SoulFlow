package ru.rutmiit.service.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.Session.AttendedSessionsDTO;
import ru.rutmiit.dto.Session.RegisteredSessionsDTO;
import ru.rutmiit.exceptions.sessionRegistration.AlreadyRegisteredException;
import ru.rutmiit.exceptions.sessionRegistration.NoAvailableSpotsException;
import ru.rutmiit.exceptions.sessionRegistration.SessionRegistrationsNotFound;
import ru.rutmiit.models.*;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;
import ru.rutmiit.dto.SessionRegistrationDTO;
import ru.rutmiit.exceptions.status.StatusNotFoundException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repositories.implementations.SessionRegistrationRepositoryImpl;
import ru.rutmiit.repositories.implementations.SessionRepositoryImpl;
import ru.rutmiit.repositories.implementations.StatusRepositoryImpl;
import ru.rutmiit.repositories.implementations.UserRepositoryImpl;
import ru.rutmiit.service.SessionRegistrationService;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionRegistrationServiceImpl implements SessionRegistrationService {

    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private SessionRegistrationRepositoryImpl sessionRegistrationRepository;
    private StatusRepositoryImpl statusRepository;
    private SessionRepositoryImpl sessionRepository;
    private UserRepositoryImpl userRepository;

    @Autowired
    public SessionRegistrationServiceImpl(ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setSessionRegistrationRepository(SessionRegistrationRepositoryImpl sessionRegistrationRepository) {
        this.sessionRegistrationRepository = sessionRegistrationRepository;
    }

    @Autowired
    public void setStatusRepository(StatusRepositoryImpl statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Autowired
    public void setSessionRepository(SessionRepositoryImpl sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addSessionRegistration(SessionRegistrationDTO sessionRegistrationDTO) {
        // TODO: это действие возможно только со стороны авторизованного пользователя
        UUID sessionId = sessionRegistrationDTO.getSessionId();
        UUID memberId = sessionRegistrationDTO.getMemberId();
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException("Занятие не найдено"));
        User member = userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException("Участник не найден"));
        Status status = statusRepository.findByName("Записан").orElseThrow(() -> new StatusNotFoundException("Статуса не существует"));

        LocalDateTime now = LocalDateTime.now();

        if (session.getDateTime().isBefore(now)) {
            throw new SessionNotFoundException("Вы можете зарегистрироваться только на ближайшее мероприятие!");
        }
        if (getAvailableSpots(sessionRegistrationDTO.getSessionId().toString(), session.getMaxCapacity()) == 0) {
            throw new NoAvailableSpotsException("Все места на занятие заняты!");
        }

        Optional<String> existingRegistrationOpt = sessionRegistrationRepository.getStatusBySessionIdAndUserId(sessionId, memberId);
        if (existingRegistrationOpt.isPresent()) {
            String existingRegistration = existingRegistrationOpt.get();
            if (existingRegistration.equals(status.getName())) {
                throw new AlreadyRegisteredException("Вы уже зарегистрированы на это занятие!");
            }
        }

        MemberSessionKeys memberSessionKeys = new MemberSessionKeys(member, session);
        SessionRegistration sessionRegistration = convertRegistrationDtoToRegistration(sessionRegistrationDTO);
        sessionRegistration.setId(memberSessionKeys);
        sessionRegistration.setRegistrationDate(LocalDateTime.now());
        sessionRegistration.setStatus(status);
        sessionRegistrationRepository.save(sessionRegistration);
    }

    @Override
    public List<SessionRegistrationDTO> getAllSessionRegistrations() {
        List<SessionRegistrationDTO> sessionRegistrationList = sessionRegistrationRepository.findAll().stream().map(this::convertRegistrationToRegistrationDto).collect(Collectors.toList());
        if (sessionRegistrationList.isEmpty()) {
            throw new SessionRegistrationsNotFound("Записей не найдено");
        } else {
            return sessionRegistrationList;
        }
    }

    @Override
    public void cancelSessionRegistration(SessionRegistrationDTO sessionRegistrationDTO) {
        // TODO: это действие возможно только со стороны авторизованного пользователя
        UUID sessionId = sessionRegistrationDTO.getSessionId();
        UUID memberId = sessionRegistrationDTO.getMemberId();
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException("Занятие не найдено"));
        User member = userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException("Участник не найдено"));
        Status canceledStatus = statusRepository.findByName("Отменено").orElseThrow(() -> new StatusNotFoundException("Статуса не существует"));

        MemberSessionKeys memberSessionKeys = new MemberSessionKeys(member, session);

        SessionRegistration registration = sessionRegistrationRepository.findById(memberSessionKeys)
                .orElseThrow(() -> new SessionRegistrationsNotFound("Запись на занятие не найдена"));

        if (registration.getStatus().getName().equals("Записан")) {
            registration.setStatus(canceledStatus);
            sessionRegistrationRepository.save(registration);
        } else {
            throw new SessionRegistrationsNotFound("Вы не были записаны на это занятие");
        }
    }

    @Override
    public void updateStatusToAttended(UUID memberId) {
        Status attendedStatus = statusRepository.findByName("Посещено").orElseThrow(() -> new StatusNotFoundException("Статуса не существует"));
        List<SessionRegistration> sessionRegistrations = sessionRegistrationRepository.getSessionRegistrationsByMember(memberId);
        for (SessionRegistration registration : sessionRegistrations) {
            Session session = registration.getId().getSession();
            LocalDateTime dateTime = session.getDateTime();

            if (!registration.getStatus().equals(attendedStatus) && dateTime.isBefore(LocalDateTime.now())) {
                registration.setStatus(attendedStatus);
                sessionRegistrationRepository.save(registration);
            }
        }
    }


    @Override
    public List<AttendedSessionsDTO> getAttendedSessionsByUserId(UUID memberId) {
        List<SessionRegistration> sessionRegistrations = sessionRegistrationRepository.getSessionRegistrationsByMember(memberId);

        Status isAttendedStatus = statusRepository.findByName("Посещено").orElseThrow(() -> new StatusNotFoundException("Статуса не существует"));

        List<AttendedSessionsDTO> sessionRegistrationDTOS = sessionRegistrations.stream()
                .filter(sessionRegistration ->
                        sessionRegistration.getStatus().getName().equals(isAttendedStatus.getName()))
                .map(sessionRegistration -> {
                    Session session = sessionRegistration.getId().getSession();
                    AttendedSessionsDTO attendedSessionsDTO = convertRegistrationToAttendedSessionsDto(sessionRegistration);
                    attendedSessionsDTO.setName(session.getName());
                    attendedSessionsDTO.setDateTime(session.getDateTime());
                    attendedSessionsDTO.setPrice(session.getPrice());
                    attendedSessionsDTO.setSessionId(session.getId().toString());
                    attendedSessionsDTO.setInstructorName(session.getInstructor().getName());
                    return attendedSessionsDTO;
                })
                .collect(Collectors.toList());
        return sessionRegistrationDTOS;
    }

    @Override
    public List<RegisteredSessionsDTO> getRegisteredSessionsByUserId(UUID memberId) {
        List<SessionRegistration> sessionRegistrations = sessionRegistrationRepository.getSessionRegistrationsByMember(memberId);

        Status isRegisteredStatus = statusRepository.findByName("Записан").orElseThrow(() -> new StatusNotFoundException("Статуса не существует"));

        List<RegisteredSessionsDTO> sessionRegistrationDTOS = sessionRegistrations.stream()
                .filter(sessionRegistration ->
                        sessionRegistration.getStatus().getName().equals(isRegisteredStatus.getName()))
                .map(sessionRegistration -> {
                            Session session = sessionRegistration.getId().getSession();
                            RegisteredSessionsDTO registeredSessionsDTO = convertRegistrationToRegisteredSessionsDto(sessionRegistration);
                            registeredSessionsDTO.setSessionId(session.getId().toString());
                            registeredSessionsDTO.setDateTime(session.getDateTime());
                            registeredSessionsDTO.setName(session.getName());
                            registeredSessionsDTO.setInstructorName(session.getInstructor().getName());
                            registeredSessionsDTO.setPrice(session.getPrice());
                            return registeredSessionsDTO;
                        }
                )
                .collect(Collectors.toList());

        return sessionRegistrationDTOS;
    }

    @Override
    public Long getAvailableSpots(String sessionId, int maxCapacity) {
        Session existingSession = sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new RuntimeException("Такого занятия не найдено"));

        Long registeredCount = sessionRegistrationRepository.countBySessionIdAndStatus(UUID.fromString(sessionId), "Записан");
        if (existingSession.getMaxCapacity() <= registeredCount) {
            return 0L;
        } else {
            return existingSession.getMaxCapacity() - registeredCount;
        }
    }

    public SessionRegistration convertRegistrationDtoToRegistration(SessionRegistrationDTO sessionRegistrationDTO) {
        return modelMapper.map(sessionRegistrationDTO, SessionRegistration.class);
    }


    public SessionRegistrationDTO convertRegistrationToRegistrationDto(SessionRegistration sessionRegistration) {
        return modelMapper.map(sessionRegistration, SessionRegistrationDTO.class);
    }

    public RegisteredSessionsDTO convertRegistrationToRegisteredSessionsDto(SessionRegistration sessionRegistration) {
        return modelMapper.map(sessionRegistration, RegisteredSessionsDTO.class);
    }

    public AttendedSessionsDTO convertRegistrationToAttendedSessionsDto(SessionRegistration sessionRegistration) {
        return modelMapper.map(sessionRegistration, AttendedSessionsDTO.class);
    }

}
