package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.rutmiit.dto.Review.ReviewInputDTO;
import ru.rutmiit.dto.instructor.InstructorOutputDTO;
import ru.rutmiit.exceptions.review.AlreadyPostedReviewException;
import ru.rutmiit.exceptions.review.NotAttendedToPostReviewException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.models.*;
import ru.rutmiit.dto.Review.ReviewOutputDTO;
import ru.rutmiit.exceptions.review.InvalidReviewDataException;
import ru.rutmiit.exceptions.review.ReviewNotFoundException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;
import ru.rutmiit.repositories.implementations.*;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.rutmiit.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final Mapper mapper;
    private final ValidationUtil validationUtil;
    private ReviewRepositoryImpl reviewRepository;
    private UserRepositoryImpl userRepository;
    private SessionRepositoryImpl sessionRepository;
    private StatusRepositoryImpl statusRepository;
    private SessionRegistrationRepositoryImpl sessionRegistrationRepository;

    @Autowired
    public ReviewServiceImpl(Mapper mapper, ValidationUtil validationUtil) {
        this.mapper = mapper;
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setReviewRepository(ReviewRepositoryImpl reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Autowired
    public void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSessionRepository(SessionRepositoryImpl sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Autowired
    public void setStatusRepository(StatusRepositoryImpl statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Autowired
    public void setSessionRegistrationRepository(SessionRegistrationRepositoryImpl sessionRegistrationRepository) {
        this.sessionRegistrationRepository = sessionRegistrationRepository;
    }

    @Override
    public ReviewOutputDTO addReview(ReviewInputDTO reviewInputDTO) {
        validateReview(reviewInputDTO, "Данные для добавления отзыва некорректны");

        UUID sessionId = UUID.fromString(reviewInputDTO.getSessionId());
        UUID memberId = UUID.fromString(reviewInputDTO.getMemberId());
        // TODO: получение юзера с помощью security
//        User currentUser = getCurrentUser();

        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException("Занятие не найдено"));
        User member = userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException("Участник не найдено"));

        MemberSessionKeys memberSessionKeys = new MemberSessionKeys(member, session);
        if (reviewRepository.existsById(memberSessionKeys)) {
            throw new AlreadyPostedReviewException("Вы уже оставляли отзыв на это занятие!");
        }
        Optional<String> statusOfSessionRegistrationOpt = sessionRegistrationRepository.getStatusBySessionIdAndUserId(sessionId, memberId);
        if (statusOfSessionRegistrationOpt.isPresent()) {
            String statusOfSessionRegistration = statusOfSessionRegistrationOpt.get();
            if (!statusOfSessionRegistration.equals("Посещено"))
                throw new NotAttendedToPostReviewException("Вы не посещали это занятие!");
        }
        Review review = mapper.convertReviewInputDtoToReview(reviewInputDTO);
        review.setId(memberSessionKeys);
        review.setDate(LocalDateTime.now());
//        review.setRate(review.getRate());
//        review.setComment(review.getComment());
        this.reviewRepository.save(review);
        return mapper.convertReviewToReviewDto(review);
    }

    @Override
    public List<ReviewOutputDTO> getAllReviews() {
        List<ReviewOutputDTO> reviewsList = reviewRepository.findAll().stream().map(mapper::convertReviewToReviewDto).collect(Collectors.toList());
        if (reviewsList.isEmpty()) {
            throw new UserNotFoundException("Отзывов не найдено");
        } else {
            return reviewsList;
        }
    }

    @Override
    public boolean hasUserReviewedSession(UUID userId, UUID sessionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Участника не найдено"));
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException("Занятие не найдено"));
        MemberSessionKeys memberSessionKeys = new MemberSessionKeys(user, session);
        return reviewRepository.existsById(memberSessionKeys);
    }

    @Override
    public ReviewOutputDTO editReview(UUID uuid, ReviewInputDTO reviewInputDTO) {
        Review existingReview = reviewRepository.findById(uuid)
                .orElseThrow(() -> new ReviewNotFoundException("Отзыв с указанным ID не найден"));

        validateReview(reviewInputDTO, "Данные для обнолвения отзыва некорректны");

        existingReview.setRate(reviewInputDTO.getRate());
        existingReview.setComment(reviewInputDTO.getComment());
        reviewRepository.save(existingReview);
        return mapper.convertReviewToReviewDto(existingReview);
    }

    @Override
    public Page<ReviewOutputDTO> getAllReviewsWithPagination(int page, int size) {
        int offset = (page - 1) * size;

        List<Review> reviewPage = reviewRepository.findAllWithPagination(offset, size);

        long totalReviews = reviewRepository.countReviews();
        List<ReviewOutputDTO> reviewOutputDTOS = reviewPage.stream()
                .map(r -> new ReviewOutputDTO(r.getId().getMember().getId().toString(), r.getId().getSession().getId().toString(), r.getRate(), r.getComment(), r.getDate()))
                .toList();

        return new PageImpl<>(reviewOutputDTOS, PageRequest.of(page - 1, size), totalReviews);
    }


    @Override
    public ReviewOutputDTO getReviewById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidUserDataException("Ошибка ID отзыва");
        }

        return reviewRepository
                .findById(uuid)
                .map(mapper::convertReviewToReviewDto)
                .orElseThrow(() -> new ReviewNotFoundException("Отзыв не найден"));
    }

    @Override
    public BigDecimal getRatingByInstructor(UUID instructorId) {
        return reviewRepository.getRatingByInstructor(instructorId);
    }

    private void validateReview(ReviewInputDTO reviewInputDTO, String exceptionMessage) {
        if (!validationUtil.isValid(reviewInputDTO)) {
            validationUtil.violations(reviewInputDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidReviewDataException(exceptionMessage);
        }
    }

//    private User getCurrentUser() {
////        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        String email = "k";
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
//    }
}
