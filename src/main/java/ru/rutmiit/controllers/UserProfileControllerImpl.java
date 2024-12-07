package ru.rutmiit.controllers;

import jakarta.validation.Valid;
import org.example.controllers.UserProfileController;
import org.example.input.review.AddReviewForm;
import org.example.input.user.EditUserForm;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.create.ReviewCreateViewModel;
import org.example.viewModel.create.SessionCreateViewModel;
import org.example.viewModel.edit.UserEditViewModel;
import org.example.viewModel.userProfile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rutmiit.dto.EditUserDTO;
import ru.rutmiit.dto.Review.ReviewInputDTO;
import ru.rutmiit.dto.SessionRegistrationDTO;
import ru.rutmiit.service.implementations.ReviewServiceImpl;
import ru.rutmiit.service.implementations.SessionRegistrationServiceImpl;
import ru.rutmiit.service.implementations.UserServiceImpl;

import java.util.UUID;

@Controller
@RequestMapping("/user/profile")
public class UserProfileControllerImpl implements UserProfileController {

    private ReviewServiceImpl reviewService;
    private SessionRegistrationServiceImpl sessionRegistrationService;
    private UserServiceImpl userService;

    @Autowired
    public void setReviewService(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setSessionRegistrationService(SessionRegistrationServiceImpl sessionRegistrationService) {
        this.sessionRegistrationService = sessionRegistrationService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") String id, Model model) {
        var user = userService.getUserById(UUID.fromString("776afc9b-fae8-4bb9-81d7-6a67197ff623"));

        var form = new EditUserForm(
                id, user.getName(), user.getEmail(), "", "", "");

        var viewModel = new UserEditViewModel(createBaseViewModel("Редактирование профиля"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);

        return "user-edit";
    }

    @Override
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") String id, @Valid @ModelAttribute("form") EditUserForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new UserEditViewModel(createBaseViewModel("Редактирование профиля"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "user-edit";
        }

        if (!form.newPassword().isBlank()) {
            if (!form.newPassword().equals(form.confirmNewPassword())) {
               // todo: ошибка если пароли не совпадают
                var viewModel = new UserEditViewModel(createBaseViewModel("Редактирование профиля"));
                model.addAttribute("model", viewModel);
                model.addAttribute("form", form);
                return "user-edit";
            }
        }

        userService.editUser(new EditUserDTO(form.id(), form.name(), form.email(), form.currentPassword(), form.newPassword(), form.confirmNewPassword())
        );

        return "redirect:/user/profile/" + id;

    }

    @Override
    @GetMapping("/{userId}")
    public String getUserProfile(@PathVariable("userId") String userId, Model model) {
        var user = userService.getUserById(UUID.fromString(userId));
        sessionRegistrationService.updateStatusToAttended(UUID.fromString(userId));

        var attendedSessions = sessionRegistrationService.getAttendedSessionsByUserId(UUID.fromString(userId));
        var registeredSessions = sessionRegistrationService.getRegisteredSessionsByUserId(UUID.fromString(userId));

        var attendedSessionsViewModels = attendedSessions.stream()
                .map(s -> {
                    boolean hasReview = reviewService.hasUserReviewedSession(UUID.fromString(userId), UUID.fromString(s.getSessionId()));
                    return new AttendedSessionsViewModel(s.getSessionId(), s.getName(), s.getDateTime(), s.getInstructorName(), s.getPrice(), hasReview);
                })
                .toList();

        var registeredSessionsViewModels = registeredSessions.stream()
                .map(s -> new RegisteredSessionsViewModel(s.getSessionId(), s.getName(), s.getDateTime(), s.getInstructorName(), s.getPrice()))
                .toList();

        var viewModel = new UserProfileViewModel(createBaseViewModel("Профиль"),
                new UserViewModel(userId, user.getName(), user.getEmail()),
                new RegisteredSessionsListViewModel(registeredSessionsViewModels),
                new AttendedSessionsListViewModel(attendedSessionsViewModels));
        model.addAttribute("model", viewModel);

        return "user-profile";
    }


    @Override
    @PostMapping("/{userId}/cancel/{sessionId}")
    public String cancelRegistration(@PathVariable("userId") String userId, @PathVariable("sessionId") String sessionId, Model model) {
        // TODO: действие для авторизованного пользователя!
//        User currentUser = getCurrentUser();
        String currentUser = "7951742d-8304-4ead-8af2-4823b6621f36"; // для проверки!!!!!

        SessionRegistrationDTO sessionRegistrationDTO = new SessionRegistrationDTO(UUID.fromString(userId), UUID.fromString(sessionId));

        sessionRegistrationService.cancelSessionRegistration(sessionRegistrationDTO);

        return "redirect:/user/profile/" + userId;
    }

    @Override
    @GetMapping("/{userId}/add/{sessionId}")
    public String addReviewForm(@PathVariable("userId") String userId, @PathVariable("sessionId") String sessionId, Model model) {
        var viewModel = new SessionCreateViewModel(
                createBaseViewModel("Добавление занятия")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddReviewForm(5, ""));
        return "review-add";
    }

    @Override
    @PostMapping("/{userId}/add/{sessionId}")
    public String leaveReview(@PathVariable("userId") String userId, @Valid @ModelAttribute("form") AddReviewForm form, @PathVariable("sessionId") String sessionId, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new ReviewCreateViewModel(
                    createBaseViewModel("Добавление отзыва")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "review-add";
        }

        reviewService.addReview(new ReviewInputDTO(userId, sessionId, form.rate(), form.comment()));
        return "redirect:/user/profile/" + userId;
    }
}
