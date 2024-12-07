package ru.rutmiit.service;

import org.springframework.data.domain.Page;
import ru.rutmiit.dto.instructor.InstructorInputDTO;
import ru.rutmiit.dto.instructor.InstructorOutputDTO;
import ru.rutmiit.dto.instructor.InstructorsViewOutputDTO;
import ru.rutmiit.models.Instructor;

import java.util.List;
import java.util.UUID;

public interface InstructorService {

    String addInstructor(InstructorInputDTO instructorDTO);

    List<String> getAllInstructors();

    void editInstructor(String id, InstructorInputDTO instructorDTO);
    void deleteInstructor(String id);
    public List<InstructorsViewOutputDTO> getActiveInstructors();
    InstructorOutputDTO getInstructorById(UUID uuid);

    InstructorOutputDTO findByName(String name);

    Page<InstructorOutputDTO> getInstructors(String searchTerm, int page, int size);
}