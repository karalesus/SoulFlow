package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;
import ru.rutmiit.domain.*;
import ru.rutmiit.dto.SessionDTO;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.difficulty.DifficultyNotFoundException;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.exceptions.type.TypeNotFoundException;
import ru.rutmiit.exceptions.user.InvalidEmailException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repository.implementations.DifficultyRepositoryImpl;
import ru.rutmiit.repository.implementations.InstructorRepositoryImpl;
import ru.rutmiit.repository.implementations.SessionRepositoryImpl;
import ru.rutmiit.repository.implementations.TypeRepositoryImpl;
import ru.rutmiit.service.SessionService;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private final Mapper mapper;
    private final ValidationUtil validationUtil;
    private SessionRepositoryImpl sessionRepository;
    private DifficultyRepositoryImpl difficultyRepository;
    private TypeRepositoryImpl typeRepository;
    private InstructorRepositoryImpl instructorRepository;

    public SessionServiceImpl(Mapper mapper, ValidationUtil validationUtil, SessionRepositoryImpl sessionRepository, DifficultyRepositoryImpl difficultyRepository, TypeRepositoryImpl typeRepository, InstructorRepositoryImpl instructorRepository) {
        this.mapper = mapper;
        this.validationUtil = validationUtil;
        this.sessionRepository = sessionRepository;
        this.difficultyRepository = difficultyRepository;
        this.typeRepository = typeRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public void addSession(SessionDTO sessionDTO) {
        if (!this.validationUtil.isValid(sessionDTO)) {

            this.validationUtil
                    .violations(sessionDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidSessionDataException("Данные занятия введены неверно!");
        }

        // TODO: добавление занятия возможно только для админа
        Difficulty difficulty = difficultyRepository.findByName(sessionDTO.getDifficulty())
                .orElseThrow(() -> new DifficultyNotFoundException("Сложность не найдена"));
        Type type = typeRepository.findByName(sessionDTO.getType())
                .orElseThrow(() -> new TypeNotFoundException("Тип не найден"));
        Instructor instructor = instructorRepository.findByName(sessionDTO.getInstructor())
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден"));

        Session session = this.mapper.convertSessionDtoToSession(sessionDTO);
        session.setDifficulty(difficulty);
        session.setType(type);
        session.setInstructor(instructor);
        this.sessionRepository.save(session);
    }

    @Override
    public List<SessionDTO> getAllSessions() {
        List<SessionDTO> sessionsList = sessionRepository.findAll().stream().map(mapper::convertSessionToSessionDto).collect(Collectors.toList());
        if (sessionsList.isEmpty()) {
            throw new SessionNotFoundException("Занятий не найдено");
        } else {
            return sessionsList;
        }
    }

    @Override
    public SessionDTO editSession(SessionDTO sessionDTO) {
        if (!this.validationUtil.isValid(sessionDTO)) {

            this.validationUtil
                    .violations(sessionDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidSessionDataException("Данные занятия введены неверно!");
        }
        return
                null;
    }

    @Override
    public SessionDTO getSessionById(UUID uuid) {
        return null;
    }
}
