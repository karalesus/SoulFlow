package ru.rutmiit.service;

import ru.rutmiit.dto.InstructorDTO;
import ru.rutmiit.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface InstructorService {

    void addInstructor(InstructorDTO instructorDTO);
    List<InstructorDTO> getAllInstructors();
    // TODO: рейтинг инструктора
    void editInstructor(InstructorDTO instructorDTO);
    InstructorDTO getInstructorById(UUID uuid);
    InstructorDTO findByName(String name);

}
