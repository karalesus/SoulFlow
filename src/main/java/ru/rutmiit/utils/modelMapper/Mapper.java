package ru.rutmiit.utils.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.rutmiit.dto.Review.ReviewInputDTO;
import ru.rutmiit.dto.Review.ReviewOutputDTO;
import ru.rutmiit.dto.Session.*;
import ru.rutmiit.dto.instructor.InstructorOutputDTO;
import ru.rutmiit.dto.instructor.InstructorInputDTO;
import ru.rutmiit.dto.instructor.InstructorsViewOutputDTO;
import ru.rutmiit.models.*;
import ru.rutmiit.dto.*;

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

    public EditUserDTO convertEditUserToUserDto(User user) {
        return modelMapper.map(user, EditUserDTO.class);
    }


    public Session convertSessionDtoToSession(SessionInputDTO sessionInputDTO) {
        return modelMapper.map(sessionInputDTO, Session.class);
    }

    public SessionInputDTO convertSessionToSessionInputDto(Session session) {
        return modelMapper.map(session, SessionInputDTO.class);
    }

    public UpcomingSessionOutputDTO convertSessionToUpcomingSessionOutputDto(Session session) {
        return modelMapper.map(session, UpcomingSessionOutputDTO.class);
    }

    public SessionOutputDTO convertSessionToSessionOutputDto(Session session) {
        return modelMapper.map(session, SessionOutputDTO.class);
    }
    public DiscountSessionOutputDTO convertSessionToDiscountSessionOutputDto(Session session) {
        return modelMapper.map(session, DiscountSessionOutputDTO.class);
    }
    public InstructorOutputDTO convertInstructorToInstructorDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorOutputDTO.class);
    }

    public InstructorsViewOutputDTO convertInstructorToInstructorViewDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorsViewOutputDTO.class);
    }


    public Instructor convertInstructorDtoToInstructor(InstructorOutputDTO instructorOutputDTO) {
        return modelMapper.map(instructorOutputDTO, Instructor.class);
    }

    public Instructor convertInstructorDtoToInstructor(InstructorInputDTO instructorDTO) {
        return modelMapper.map(instructorDTO, Instructor.class);
    }

    public Review convertReviewOutputDtoToReview(ReviewOutputDTO reviewOutputDTO){
        return modelMapper.map(reviewOutputDTO, Review.class);
    }

    public Review convertReviewInputDtoToReview(ReviewInputDTO reviewInputDTO){
        return modelMapper.map(reviewInputDTO, Review.class);
    }

    public ReviewOutputDTO convertReviewToReviewDto(Review review){
        return modelMapper.map(review, ReviewOutputDTO.class);
    }




}
