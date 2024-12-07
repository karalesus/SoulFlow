package ru.rutmiit.controllers;

import org.example.controllers.ScheduleController;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.home.SessionsWithDiscountsViewModel;
import org.example.viewModel.lists.UpcomingSessionsListViewModel;
import org.example.viewModel.home.UpcomingSessionsViewModel;
import org.example.viewModel.lists.SessionWithDiscountsListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rutmiit.dto.SessionRegistrationDTO;
import ru.rutmiit.service.implementations.SessionRegistrationServiceImpl;
import ru.rutmiit.service.implementations.SessionServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@RequestMapping("/schedule")
public class ScheduleControllerImpl implements ScheduleController {

    private SessionServiceImpl sessionService;
    private SessionRegistrationServiceImpl sessionRegistrationService;

    @Autowired
    public void setSessionService(SessionServiceImpl sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setSessionRegistrationService(SessionRegistrationServiceImpl sessionRegistrationService) {
        this.sessionRegistrationService = sessionRegistrationService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping("/upcoming")
    public String listUpcomingSessions(Model model) {
        var upcomingSessions = sessionService.getUpcomingSessions(LocalDateTime.now());
        var upcomingSessionsViewModels = upcomingSessions.stream()
                .map(s -> new UpcomingSessionsViewModel(s.getId(), s.getName(), s.getDuration(), s.getDescription(), s.getDateTime(), s.getAvailableSpots(), s.getPrice(), s.getDifficulty(), s.getType(), s.getInstructorName()))
                .toList();
        var viewModel = new UpcomingSessionsListViewModel(
                createBaseViewModel("Ближайшие занятия"), upcomingSessionsViewModels
        );
        model.addAttribute("model", viewModel);
        return "upcoming-sessions";
    }

    @Override
    @GetMapping("/discounts")
    public String listSessionsWithDiscounts(Model model) {
        var discountSessions = sessionService.getDiscountSessions(LocalDateTime.now());
        var discountSessionsViewModels = discountSessions.stream()
                .map(s -> new SessionsWithDiscountsViewModel(s.getId(), s.getName(), s.getDuration(), s.getDescription(), s.getDateTime(), s.getInstructorName(), s.getType(), s.getDifficulty(), s.getAvailableSpots(), s.getPriceBeforeDiscount(), s.getPriceAfterDiscount()))
                .toList();

        var viewModel = new SessionWithDiscountsListViewModel(
                createBaseViewModel("Акции"), discountSessionsViewModels
        );

        model.addAttribute("model", viewModel);
        return "discount-sessions";
    }

    @Override
    @PostMapping("/register/{sessionId}")
    public String registerToSession(@PathVariable("sessionId") String sessionId, Model model) {
        // TODO: действие для авторизованного пользователя!
//        User currentUser = getCurrentUser();
        String currentUser = "fcf0cf65-b8ec-4a50-8150-16aa96c0aef1";

        SessionRegistrationDTO sessionRegistrationDTO = new SessionRegistrationDTO(UUID.fromString(currentUser), UUID.fromString(sessionId));
        sessionRegistrationService.addSessionRegistration(sessionRegistrationDTO);

        return "redirect:/schedule/upcoming";
    }
}
