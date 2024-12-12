package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import ru.rutmiit.dto.review.ReviewInputDTO;
import ru.rutmiit.exceptions.review.AlreadyPostedReviewException;
import ru.rutmiit.exceptions.review.NotAttendedToPostReviewException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.models.*;
import ru.rutmiit.dto.review.ReviewOutputDTO;
import ru.rutmiit.exceptions.review.InvalidReviewDataException;
import ru.rutmiit.exceptions.review.ReviewNotFoundException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;
import ru.rutmiit.repositories.implementations.*;
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

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private ReviewRepositoryImpl reviewRepository;
    private UserRepositoryImpl userRepository;
    private SessionRepositoryImpl sessionRepository;
    private SessionRegistrationRepositoryImpl sessionRegistrationRepository;

    @Autowired
    public ReviewServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
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
    public void setSessionRegistrationRepository(SessionRegistrationRepositoryImpl sessionRegistrationRepository) {
        this.sessionRegistrationRepository = sessionRegistrationRepository;
    }

    @Override
    public void addReview(ReviewInputDTO reviewInputDTO) {
        validateReview(reviewInputDTO);

        UUID sessionId = UUID.fromString(reviewInputDTO.getSessionId());
        UUID memberId = UUID.fromString(reviewInputDTO.getMemberId());

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

        Review review = convertReviewInputDtoToReview(reviewInputDTO);
        review.setId(memberSessionKeys);
        review.setDate(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    public List<ReviewOutputDTO> getAllReviews() {
        List<ReviewOutputDTO> reviewsList = reviewRepository.findAll().stream()
                .map(this::convertReviewToReviewDto)
                .collect(Collectors.toList());

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
    public Page<ReviewOutputDTO> getAllReviewsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        List<Review> reviewPage = reviewRepository.findAllWithPagination(pageable);

        long totalReviews = reviewRepository.countReviews();
        List<ReviewOutputDTO> reviewOutputDTOS = reviewPage.stream()
                .map(r -> new ReviewOutputDTO(r.getId().getMember().getId().toString(), r.getId().getSession().getId().toString(), r.getRate(), r.getComment(), r.getDate()))
                .toList();

        return new PageImpl<>(reviewOutputDTOS, pageable, totalReviews);
    }


    @Override
    public ReviewOutputDTO getReviewById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidUserDataException("Ошибка ID отзыва");
        }

        return reviewRepository
                .findById(uuid)
                .map(this::convertReviewToReviewDto)
                .orElseThrow(() -> new ReviewNotFoundException("Отзыв не найден"));
    }

    @Override
    public BigDecimal getRatingByInstructor(UUID instructorId) {
        return reviewRepository.getRatingByInstructor(instructorId);
    }

    private void validateReview(ReviewInputDTO reviewInputDTO) {
        if (!validationUtil.isValid(reviewInputDTO)) {
            validationUtil.violations(reviewInputDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidReviewDataException("Данные для добавления отзыва некорректны");
        }
    }

//    @Override
//    public ReviewOutputDTO editReview(UUID uuid, ReviewInputDTO reviewInputDTO) {
//        Review existingReview = reviewRepository.findById(uuid)
//                .orElseThrow(() -> new ReviewNotFoundException("Отзыв с указанным ID не найден"));
//
//        validateReview(reviewInputDTO, "Данные для обнолвения отзыва некорректны");
//
//        existingReview.setRate(reviewInputDTO.getRate());
//        existingReview.setComment(reviewInputDTO.getComment());
//        reviewRepository.save(existingReview);
//        return convertReviewToReviewDto(existingReview);
//    }

    public Review convertReviewInputDtoToReview(ReviewInputDTO reviewInputDTO){
        return modelMapper.map(reviewInputDTO, Review.class);
    }

    public ReviewOutputDTO convertReviewToReviewDto(Review review){
        return modelMapper.map(review, ReviewOutputDTO.class);
    }
}
