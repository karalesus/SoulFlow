package ru.rutmiit.service.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.session.ScheduleSessionsOutputDTO;
import ru.rutmiit.dto.session.SessionOutputDTO;
import ru.rutmiit.models.*;
import ru.rutmiit.dto.session.SessionInputDTO;
import ru.rutmiit.exceptions.difficulty.DifficultyNotFoundException;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.exceptions.type.TypeNotFoundException;
import ru.rutmiit.repositories.implementations.*;
import ru.rutmiit.service.SessionService;
import ru.rutmiit.utils.validation.ValidationUtil;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class SessionServiceImpl implements SessionService {
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private SessionRepositoryImpl sessionRepository;
    private SessionRegistrationServiceImpl sessionRegistrationService;
    private DifficultyRepositoryImpl difficultyRepository;
    private TypeRepositoryImpl typeRepository;
    private InstructorRepositoryImpl instructorRepository;
    private DiscountServiceImpl discountService;

    @Autowired
    public SessionServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setSessionRepository(SessionRepositoryImpl sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Autowired
    public void setDifficultyRepository(DifficultyRepositoryImpl difficultyRepository) {
        this.difficultyRepository = difficultyRepository;
    }

    @Autowired
    public void setTypeRepository(TypeRepositoryImpl typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Autowired
    public void setInstructorRepository(InstructorRepositoryImpl instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Autowired
    public void setSessionRegistrationService(SessionRegistrationServiceImpl sessionRegistrationService) {
        this.sessionRegistrationService = sessionRegistrationService;
    }

    @Autowired
    public void setDiscountService(DiscountServiceImpl discountService) {
        this.discountService = discountService;
    }


    @Override
    @Transactional
    @CacheEvict(cacheNames = "sessions", allEntries = true)
    public String addSession(SessionInputDTO sessionInputDTO) {
        validateSession(sessionInputDTO);
        Session session = convertSessionDtoToSession(sessionInputDTO);
        Difficulty difficulty = difficultyRepository.findByName(sessionInputDTO.getDifficulty())
                .orElseThrow(() -> new DifficultyNotFoundException("Сложность не найдена"));
        Type type = typeRepository.findByName(sessionInputDTO.getType())
                .orElseThrow(() -> new TypeNotFoundException("Тип не найден"));
        Instructor instructor = instructorRepository.findById(UUID.fromString(sessionInputDTO.getInstructor()))
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден"));

        session.setDifficulty(difficulty);
        session.setType(type);
        session.setInstructor(instructor);
        return sessionRepository.save(session).getId().toString();
    }

    @Override
    @Cacheable("sessions")
    public Page<SessionOutputDTO> getAllSessionsWithPagination(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        String term = searchTerm != null ? searchTerm : "";
        List<Session> sessionPage = sessionRepository.findAllSessionsWithPagination(term, pageable);

        long totalSessions = sessionRepository.countSessions(searchTerm);
        List<SessionOutputDTO> sessionDTOs = sessionPage.stream()
                .map(session ->
                        new SessionOutputDTO(session.getId().toString(), session.getName(), session.getDuration(), session.getDescription(), session.getDateTime(), session.getMaxCapacity(), session.getPrice(), session.getDifficulty().getName(), session.getType().getName(), session.getInstructor().getName()))
                .toList();

        return new PageImpl<>(sessionDTOs, pageable, totalSessions);
    }


    @Override
    @CacheEvict(cacheNames = "sessions", allEntries = true)
    public SessionOutputDTO editSession(String id, SessionInputDTO sessionInputDTO) {
        validateSession(sessionInputDTO);

        Session existingSession = sessionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Такого занятия не найдено"));

        existingSession.setName(sessionInputDTO.getName());
        existingSession.setDuration(sessionInputDTO.getDuration());
        existingSession.setDateTime(sessionInputDTO.getDateTime());
        existingSession.setMaxCapacity(sessionInputDTO.getMaxCapacity());
        existingSession.setPrice(sessionInputDTO.getPrice());
        existingSession.setDescription(sessionInputDTO.getDescription());

        Difficulty difficulty = difficultyRepository.findByName(sessionInputDTO.getDifficulty())
                .orElseThrow(() -> new DifficultyNotFoundException("Сложность не найдена"));
        Type type = typeRepository.findByName(sessionInputDTO.getType())
                .orElseThrow(() -> new TypeNotFoundException("Тип не найден"));
        Instructor instructor = instructorRepository.findByName(sessionInputDTO.getInstructor())
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден"));

        existingSession.setDifficulty(difficulty);
        existingSession.setType(type);
        existingSession.setInstructor(instructor);
        sessionRepository.save(existingSession);
        return convertSessionToSessionOutputDto(existingSession);
    }

    @Override
    public SessionOutputDTO getSessionById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidSessionDataException("Ошибка ID занятия");
        }

        return sessionRepository
                .findById(uuid)
                .map(this::convertSessionToSessionOutputDto)
                .orElseThrow(() -> new SessionNotFoundException("Занятие не найдено"));
    }

    @Override
    public List<ScheduleSessionsOutputDTO> getUpcomingSessions(LocalDateTime now) {
        List<Session> upcomingSessions = sessionRepository.findAllUpcomingSessions(now);

        return upcomingSessions.stream().map(session -> {
                    ScheduleSessionsOutputDTO upcomingSessionOutputDTO
                            = convertSessionToUpcomingSessionOutputDto(session);
                    upcomingSessionOutputDTO
                            .setAvailableSpots(sessionRegistrationService.getAvailableSpots(session.getId().toString(), session.getMaxCapacity()));
                    upcomingSessionOutputDTO.setPriceBeforeDiscount(session.getPrice());

                    BigDecimal discountedPrice = calculateDiscountedPrice(session.getId());
                    if (discountedPrice.compareTo(session.getPrice()) < 0) {
                        upcomingSessionOutputDTO.setPriceAfterDiscount(discountedPrice);
                    } else {
                        upcomingSessionOutputDTO.setPriceAfterDiscount(BigDecimal.ZERO);
                    }
                    return upcomingSessionOutputDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleSessionsOutputDTO> getDiscountSessions(LocalDateTime now) {
        List<Session> upcomingSessions = sessionRepository.findAllUpcomingSessions(now);

        return upcomingSessions.stream()
                .filter(session -> {
                    BigDecimal discountedPrice = calculateDiscountedPrice(session.getId());
                    return discountedPrice.compareTo(session.getPrice()) < 0;
                })
                .map(session -> {
                    BigDecimal discountedPrice = calculateDiscountedPrice(session.getId());
                    ScheduleSessionsOutputDTO sessionDTO = convertSessionToDiscountSessionOutputDto(session);
                    sessionDTO.setPriceBeforeDiscount(session.getPrice());
                    sessionDTO.setPriceAfterDiscount(discountedPrice);
                    sessionDTO.setAvailableSpots(sessionRegistrationService.getAvailableSpots(session.getId().toString(), session.getMaxCapacity()));
                    return sessionDTO;
                })
                .collect(Collectors.toList());
    }

    public BigDecimal calculateDiscountedPrice(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException("Занятие не найдено"));

        BigDecimal discount = discountService.calculateDiscount(session.getInstructor().getId());
        return discountService.calculateDiscountedPrice(session.getPrice(), discount);
    }

    private void validateSession(SessionInputDTO sessionInputDTO) {
        if (!this.validationUtil.isValid(sessionInputDTO)) {
            String constraintViolations = this.validationUtil
                    .violations(sessionInputDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new InvalidSessionDataException(constraintViolations);
        }
    }

    public ScheduleSessionsOutputDTO convertSessionToDiscountSessionOutputDto(Session session) {
        return modelMapper.map(session, ScheduleSessionsOutputDTO.class);
    }

    public SessionOutputDTO convertSessionToSessionOutputDto(Session session) {
        return modelMapper.map(session, SessionOutputDTO.class);
    }

    public ScheduleSessionsOutputDTO convertSessionToUpcomingSessionOutputDto(Session session) {
        return modelMapper.map(session, ScheduleSessionsOutputDTO.class);
    }

    public Session convertSessionDtoToSession(SessionInputDTO sessionInputDTO) {
        return modelMapper.map(sessionInputDTO, Session.class);
    }

    public SessionInputDTO convertSessionToSessionInputDto(Session session) {
        return modelMapper.map(session, SessionInputDTO.class);
    }
}
