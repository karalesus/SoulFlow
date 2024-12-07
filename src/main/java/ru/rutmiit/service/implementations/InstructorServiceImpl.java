package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rutmiit.dto.instructor.InstructorInputDTO;
import ru.rutmiit.dto.instructor.InstructorsViewOutputDTO;
import ru.rutmiit.models.Instructor;
import ru.rutmiit.dto.instructor.InstructorOutputDTO;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.instructor.InvalidInstructorDataException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repositories.implementations.InstructorRepositoryImpl;
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

    public InstructorServiceImpl(Mapper mapper, ValidationUtil validationUtil) {
        this.mapper = mapper;
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setInstructorRepository(InstructorRepositoryImpl instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public String addInstructor(InstructorInputDTO instructorDTO) {
        validateInstructor(instructorDTO);
        // TODO: добавление инструктора возможно только для админа
        Instructor instructor = mapper.convertInstructorDtoToInstructor(instructorDTO);
        return instructorRepository.save(instructor).getId().toString();
    }

    @Override
    public List<String> getAllInstructors() {
        return instructorRepository.findAll().stream().map(Instructor::getName).collect(Collectors.toList());
    }

    @Override
    @Transactional
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
    public List<InstructorsViewOutputDTO> getActiveInstructors() {
        return instructorRepository.findInstructorsNotDeleted()
                .stream()
                .map(instructor ->
                        mapper.convertInstructorToInstructorViewDto(instructor))
                .collect(Collectors.toList());

    }

    @Override
    public InstructorOutputDTO getInstructorById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidUserDataException("Ошибка ID инструктора");
        }

        return instructorRepository
                .findById(uuid)
                .map(mapper::convertInstructorToInstructorDto)
                .orElseThrow(() -> new UserNotFoundException("Инструктор не найден"));
    }

    @Override
    public InstructorOutputDTO findByName(String name) {
        return mapper.convertInstructorToInstructorDto(this.instructorRepository.findByName(name).orElseThrow(() -> new InstructorNotFoundException("Такого инструктора не существует")));
    }

    @Override
    public Page<InstructorOutputDTO> getInstructors(String searchTerm, int page, int size) {
        int offset = (page - 1) * size;
        String term = searchTerm != null ? searchTerm : "";
        List<Instructor> instructorPage = instructorRepository.findAllWithPagination(term, offset, size);

        long totalInstructors = instructorRepository.countInstructors(searchTerm);
        List<InstructorOutputDTO> instructorOutputDTOS = instructorPage.stream()
                .map(instructor -> new InstructorOutputDTO(instructor.getId().toString(), instructor.getName(), instructor.getCertificate(), instructor.getPhotoUrl(), instructor.isDeleted()))
                .toList();

        return new PageImpl<>(instructorOutputDTOS, PageRequest.of(page - 1, size), totalInstructors);
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
}
