package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rutmiit.domain.Instructor;
import ru.rutmiit.dto.InstructorDTO;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.instructor.InvalidInstructorDataException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repository.implementations.InstructorRepositoryImpl;
import ru.rutmiit.service.InstructorService;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final Mapper mapper;
    private final ValidationUtil validationUtil;
    private InstructorRepositoryImpl instructorRepository;

    public InstructorServiceImpl(Mapper mapper, InstructorRepositoryImpl instructorRepository, ValidationUtil validationUtil) {
        this.mapper = mapper;
        this.instructorRepository = instructorRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addInstructor(InstructorDTO instructorDTO) {
        if (!this.validationUtil.isValid(instructorDTO)) {

            this.validationUtil
                    .violations(instructorDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidInstructorDataException("Данные инструктора введены неверно!");
        }
        Instructor instructor = mapper.convertInstructorDtoToInstructor(instructorDTO);
        this.instructorRepository.update(instructor);
    }

    @Override
    public List<InstructorDTO> getAllInstructors() {
        List<InstructorDTO> instructorsList = instructorRepository.findAll().stream().map(mapper::convertInstructorToInstructorDto).collect(Collectors.toList());
        if (instructorsList.isEmpty()) {
            throw new UserNotFoundException("Инструкторов не найдено");
        }
        return instructorsList;

    }

    @Override
    @Transactional
    public void editInstructor(InstructorDTO instructorDTO) {
        if (!this.validationUtil.isValid(instructorDTO)) {
            this.validationUtil
                    .violations(instructorDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidInstructorDataException("Данные инструктора введены неверно!");
        }
        Instructor instructor = mapper.convertInstructorDtoToInstructor(instructorDTO);
        this.instructorRepository.save(instructor);
    }

    @Override
    public InstructorDTO getInstructorById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidUserDataException("Ошибка ID инструктора");
        }

        return instructorRepository
                .findById(uuid)
                .map(mapper::convertInstructorToInstructorDto)
                .orElseThrow(() -> new UserNotFoundException("Инструктор не найден"));
    }

    @Override
    public InstructorDTO findByName(String name) {
        return mapper.convertInstructorToInstructorDto(this.instructorRepository.findByName(name).orElseThrow(() -> new InstructorNotFoundException("Такого инструктора не существует")));
    }
}
