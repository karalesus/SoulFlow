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
import ru.rutmiit.dto.user.EditUserDTO;
import ru.rutmiit.dto.review.ReviewInputDTO;
import ru.rutmiit.dto.sessionRegistration.SessionRegistrationDTO;
import ru.rutmiit.models.User;
import ru.rutmiit.service.implementations.ReviewServiceImpl;
import ru.rutmiit.service.implementations.SessionRegistrationServiceImpl;
import ru.rutmiit.service.implementations.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
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
    @GetMapping("/edit")
    public String editForm(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUser(email);

        var form = new EditUserForm(
                user.getId().toString(), user.getName(), user.getEmail(), "", "", "");

        var viewModel = new UserEditViewModel(createBaseViewModel("Редактирование профиля"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);

        return "user-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(Principal principal, @Valid @ModelAttribute("form") EditUserForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new UserEditViewModel(createBaseViewModel("Редактирование профиля"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "user-edit";
        }

        userService.editUser(new EditUserDTO(form.id(), form.name(), form.email(), form.currentPassword(), form.newPassword(), form.confirmNewPassword()));

        return "redirect:/user/profile";

    }

    @Override
    @GetMapping()
    public String getUserProfile(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUser(email);
        sessionRegistrationService.updateStatusToAttended(user.getId());

        var attendedSessions = sessionRegistrationService.getAttendedSessionsByUserId(user.getId());
        var registeredSessions = sessionRegistrationService.getRegisteredSessionsByUserId(user.getId());

        var attendedSessionsViewModels = attendedSessions.stream()
                .map(s -> {
                    boolean hasReview = reviewService.hasUserReviewedSession(user.getId(), UUID.fromString(s.getSessionId()));
                    return new AttendedSessionsViewModel(s.getSessionId(), s.getName(), s.getDateTime(), s.getInstructorName(), s.getPrice(), hasReview);
                })
                .toList();

        var registeredSessionsViewModels = registeredSessions.stream()
                .map(s -> new RegisteredSessionsViewModel(s.getId(), s.getName(), s.getDateTime(), s.getInstructorName(), s.getPriceBeforeDiscount(), s.getPriceAfterDiscount()))
                .toList();

        var viewModel = new UserProfileViewModel(createBaseViewModel("Профиль"),
                new UserViewModel(user.getId().toString(), user.getName(), user.getEmail()),
                new RegisteredSessionsListViewModel(registeredSessionsViewModels),
                new AttendedSessionsListViewModel(attendedSessionsViewModels));
        model.addAttribute("model", viewModel);

        return "user-profile";
    }


    @Override
    @PostMapping("/cancel/{sessionId}")
    public String cancelRegistration(Principal principal, @PathVariable("sessionId") String sessionId, Model model) {
        String email = principal.getName();
        User user = userService.getUser(email);

        SessionRegistrationDTO sessionRegistrationDTO = new SessionRegistrationDTO(user.getId(), UUID.fromString(sessionId), BigDecimal.ZERO);

        sessionRegistrationService.cancelSessionRegistration(sessionRegistrationDTO);

        return "redirect:/user/profile";
    }

    @Override
    @GetMapping("add/{sessionId}")
    public String addReviewForm(Principal principal, @PathVariable("sessionId") String sessionId, Model model) {
        var viewModel = new SessionCreateViewModel(
                createBaseViewModel("Добавление занятия")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddReviewForm(5, ""));
        return "review-add";
    }

    @Override
    @PostMapping("add/{sessionId}")
    public String leaveReview(Principal principal, @Valid @ModelAttribute("form") AddReviewForm form, @PathVariable("sessionId") String sessionId, BindingResult bindingResult, Model model) {
        String email = principal.getName();
        User user = userService.getUser(email);
        if (bindingResult.hasErrors()) {
            var viewModel = new ReviewCreateViewModel(
                    createBaseViewModel("Добавление отзыва")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "review-add";
        }

        reviewService.addReview(new ReviewInputDTO(user.getId().toString(), sessionId, form.rate(), form.comment()));
        return "redirect:/user/profile";
    }
}
