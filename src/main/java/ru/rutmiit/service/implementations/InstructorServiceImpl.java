package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.instructor.InstructorInputDTO;
import ru.rutmiit.models.Instructor;
import ru.rutmiit.dto.instructor.InstructorOutputDTO;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.instructor.InvalidInstructorDataException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repositories.implementations.InstructorRepositoryImpl;
import ru.rutmiit.service.InstructorService;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private InstructorRepositoryImpl instructorRepository;

    public InstructorServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setInstructorRepository(InstructorRepositoryImpl instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public String addInstructor(InstructorInputDTO instructorDTO) {
        validateInstructor(instructorDTO);
        Instructor instructor = convertInstructorDtoToInstructor(instructorDTO);
        return instructorRepository.save(instructor).getId().toString();
    }

    @Override
    public List<String> getAllInstructorsByName() {
        return instructorRepository.findAll().stream()
                .map(Instructor::getName).collect(Collectors.toList());
    }

    @Override
    public void editInstructor(String id, InstructorInputDTO instructorDTO) {
        Instructor existingInstructor = instructorRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Инструктор с ID " + id + " не найден"));

        validateInstructor(instructorDTO);

        existingInstructor.setName(instructorDTO.getName());
        existingInstructor.setCertificate(instructorDTO.getCertificate());
        existingInstructor.setPhotoUrl(instructorDTO.getPhotoUrl());

        instructorRepository.save(existingInstructor);
    }

    @Override
    public void deleteInstructor(String id) {
        Instructor existingInstructor = instructorRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Инструктор с ID " + id + " не найден"));
        existingInstructor.setDeleted(true);
        instructorRepository.save(existingInstructor);
    }

    @Override
    public List<InstructorOutputDTO> getActiveInstructors() {
        return instructorRepository.findInstructorsNotDeleted()
                .stream()
                .map(this::convertInstructorToInstructorDto)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorOutputDTO getInstructorById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidInstructorDataException("Ошибка ID инструктора");
        }

        return instructorRepository
                .findById(uuid)
                .map(this::convertInstructorToInstructorDto)
                .orElseThrow(() -> new UserNotFoundException("Инструктор не найден"));
    }

    @Override
    public InstructorOutputDTO findByName(String name) {
        return convertInstructorToInstructorDto(this.instructorRepository.findByName(name).orElseThrow(() -> new InstructorNotFoundException("Такого инструктора не существует")));
    }

    @Override
    public Page<InstructorOutputDTO> getInstructors(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        String term = searchTerm != null ? searchTerm : "";
        List<Instructor> instructorPage = instructorRepository.findAllWithPagination(term, pageable);

        long totalInstructors = instructorRepository.countInstructors(searchTerm);
        List<InstructorOutputDTO> instructorOutputDTOS = instructorPage.stream()
                .map(instructor -> new InstructorOutputDTO(instructor.getId().toString(), instructor.getName(), instructor.getCertificate(), instructor.getPhotoUrl(), instructor.isDeleted()))
                .toList();

        return new PageImpl<>(instructorOutputDTOS, pageable, totalInstructors);
    }

    private void validateInstructor(InstructorInputDTO instructorDTO) {
        if (!this.validationUtil.isValid(instructorDTO)) {
            this.validationUtil
                    .violations(instructorDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidInstructorDataException("Данные инструктора введены неверно!");
        }
    }

    public Instructor convertInstructorDtoToInstructor(InstructorInputDTO instructorDTO) {
        return modelMapper.map(instructorDTO, Instructor.class);
    }

    public InstructorOutputDTO convertInstructorToInstructorDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorOutputDTO.class);
    }

}
