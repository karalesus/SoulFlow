package ru.rutmiit.utils.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.rutmiit.domain.*;
import ru.rutmiit.dto.*;
import ru.rutmiit.repository.implementations.UserRepositoryImpl;

@Component
public class Mapper {
    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertUserDtoToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public Session convertSessionDtoToSession(SessionDTO sessionDTO) {
        return modelMapper.map(sessionDTO, Session.class);
    }
    public SessionDTO convertSessionToSessionDto(Session session) {
        return modelMapper.map(session, SessionDTO.class);
    }
    public InstructorDTO convertInstructorToInstructorDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorDTO.class);
    }

    public Instructor convertInstructorDtoToInstructor(InstructorDTO instructorDTO) {
        return modelMapper.map(instructorDTO, Instructor.class);
    }
}
