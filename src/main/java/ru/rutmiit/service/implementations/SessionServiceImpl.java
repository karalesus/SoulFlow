package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.Session.DiscountSessionOutputDTO;
import ru.rutmiit.dto.Session.SessionOutputDTO;
import ru.rutmiit.dto.Session.UpcomingSessionOutputDTO;
import ru.rutmiit.models.*;
import ru.rutmiit.dto.Session.SessionInputDTO;
import ru.rutmiit.exceptions.difficulty.DifficultyNotFoundException;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.exceptions.type.TypeNotFoundException;
import ru.rutmiit.repositories.implementations.*;
import ru.rutmiit.service.SessionService;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private final Mapper mapper;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private SessionRepositoryImpl sessionRepository;
    private SessionRegistrationServiceImpl sessionRegistrationService;
    private DifficultyRepositoryImpl difficultyRepository;
    private TypeRepositoryImpl typeRepository;
    private InstructorRepositoryImpl instructorRepository;
    private ReviewRepositoryImpl reviewRepository;

    @Autowired
    public SessionServiceImpl(Mapper mapper, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.mapper = mapper;
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
    public void setReviewRepository(ReviewRepositoryImpl reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public String addSession(SessionInputDTO sessionInputDTO) {
        // TODO: добавление занятия возможно только для админа
        validateSession(sessionInputDTO, "Некорректные данные для добавления занятия");

        Difficulty difficulty = difficultyRepository.findByName(sessionInputDTO.getDifficulty())
                .orElseThrow(() -> new DifficultyNotFoundException("Сложность не найдена"));
        Type type = typeRepository.findByName(sessionInputDTO.getType())
                .orElseThrow(() -> new TypeNotFoundException("Тип не найден"));
        Instructor instructor = instructorRepository.findByName(sessionInputDTO.getInstructor())
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден"));

        if (instructor.isDeleted()) {
            throw new InstructorNotFoundException("Инструктор уволен и не может быть прикреплён к занятию");
        }

        Session session = convertSessionDtoToSession(sessionInputDTO);
        session.setDifficulty(difficulty);
        session.setType(type);
        session.setInstructor(instructor);
        return sessionRepository.save(session).getId().toString();
    }

    @Override
    public List<SessionInputDTO> getAllSessions() {
        List<SessionInputDTO> sessionsList = sessionRepository.findAll().stream()
                .map(this::convertSessionToSessionInputDto)
                .collect(Collectors.toList());

        if (sessionsList.isEmpty()) {
            throw new SessionNotFoundException("Занятий не найдено");
        } else {
            return sessionsList;
        }
    }

    @Override
    public Page<SessionOutputDTO> getAllSessionsWithPagination(String searchTerm, int page, int size) {
        int offset = (page - 1) * size;
        String term = searchTerm != null ? searchTerm : "";
        List<Session> sessionPage = sessionRepository.findAllWithPagination(term, offset, size);

        long totalSessions = sessionRepository.countSessions(searchTerm);
        List<SessionOutputDTO> sessionDTOs = sessionPage.stream()
                .map(session -> new SessionOutputDTO(session.getId().toString(), session.getName(), session.getDuration(), session.getDescription(), session.getDateTime(), session.getMaxCapacity(), session.getPrice(), session.getDifficulty().getName(), session.getType().getName(), session.getInstructor().getName()))
                .toList();

        return new PageImpl<>(sessionDTOs, PageRequest.of(page - 1, size), totalSessions);
    }


    @Override
    public SessionOutputDTO editSession(String id, SessionInputDTO sessionInputDTO) {
        validateSession(sessionInputDTO, "Некорректные данные для изменения занятия");

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
    public List<UpcomingSessionOutputDTO> getUpcomingSessions(LocalDateTime now) {
        List<UpcomingSessionOutputDTO> upcomingSessions = sessionRepository
                .getUpcomingSessions(now).stream().map(session -> {
                    UpcomingSessionOutputDTO upcomingSessionOutputDTO
                            = convertSessionToUpcomingSessionOutputDto(session);
                    upcomingSessionOutputDTO
                            .setAvailableSpots(sessionRegistrationService.getAvailableSpots(session.getId().toString(), session.getMaxCapacity()));
                    return upcomingSessionOutputDTO;
                }).collect(Collectors.toList());

        return upcomingSessions;
    }

    @Override
    public List<DiscountSessionOutputDTO> getDiscountSessions(LocalDateTime now) {
        List<Session> upcomingSessions = sessionRepository.getUpcomingSessions(now);

        List<DiscountSessionOutputDTO> discountSessionsDTO = upcomingSessions.stream()
                .filter(session -> {
                    BigDecimal instructorRating =
                            reviewRepository.getRatingByInstructor(session.getInstructor().getId());
                    BigDecimal discount = calculateDiscount(instructorRating);
                    return discount.compareTo(BigDecimal.ZERO) > 0;
                })
                .map(session -> {
                    BigDecimal instructorRating = reviewRepository.getRatingByInstructor(session.getInstructor().getId());
                    BigDecimal discount = calculateDiscount(instructorRating);
                    BigDecimal discountedPrice = calculateDiscountedPrice(session.getPrice(), discount);

                    DiscountSessionOutputDTO sessionDTO = convertSessionToDiscountSessionOutputDto(session);
                    sessionDTO.setAvailableSpots(sessionRegistrationService.getAvailableSpots(session.getId().toString(), session.getMaxCapacity()));
                    sessionDTO.setPriceBeforeDiscount(session.getPrice());
                    sessionDTO.setPriceAfterDiscount(discountedPrice);

                    return sessionDTO;
                })
                .collect(Collectors.toList());

        return discountSessionsDTO;
    }

    private BigDecimal calculateDiscount(BigDecimal instructorRating) {
        if (instructorRating.compareTo(BigDecimal.valueOf(4.0)) <= 0) {
            // Если рейтинг меньше или равнo 4, скидка 30%
            return BigDecimal.valueOf(0.30);
        } else if (instructorRating.compareTo(BigDecimal.valueOf(4.3)) < 0) {
            // Если рейтинг меньше 4.3, скидка 10%
            return BigDecimal.valueOf(0.10);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            return originalPrice.subtract(originalPrice.multiply(discount));
        } else {
            return originalPrice;
        }
    }

    private void validateSession(SessionInputDTO sessionInputDTO, String exceptionMessage) {
        if (!this.validationUtil.isValid(sessionInputDTO)) {

            this.validationUtil
                    .violations(sessionInputDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidSessionDataException(exceptionMessage);
        }
    }

    public DiscountSessionOutputDTO convertSessionToDiscountSessionOutputDto(Session session) {
        return modelMapper.map(session, DiscountSessionOutputDTO.class);
    }

    public SessionOutputDTO convertSessionToSessionOutputDto(Session session) {
        return modelMapper.map(session, SessionOutputDTO.class);
    }

    public UpcomingSessionOutputDTO convertSessionToUpcomingSessionOutputDto(Session session) {
        return modelMapper.map(session, UpcomingSessionOutputDTO.class);
    }

    public Session convertSessionDtoToSession(SessionInputDTO sessionInputDTO) {
        return modelMapper.map(sessionInputDTO, Session.class);
    }

    public SessionInputDTO convertSessionToSessionInputDto(Session session) {
        return modelMapper.map(session, SessionInputDTO.class);
    }
}
