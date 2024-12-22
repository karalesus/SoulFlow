package ru.rutmiit.controllers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rutmiit.dto.sessionRegistration.SessionRegistrationDTO;
import ru.rutmiit.models.User;
import ru.rutmiit.service.implementations.SessionRegistrationServiceImpl;
import ru.rutmiit.service.implementations.SessionServiceImpl;
import ru.rutmiit.service.implementations.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/schedule")
public class ScheduleControllerImpl implements ScheduleController {
    private SessionServiceImpl sessionService;
    private SessionRegistrationServiceImpl sessionRegistrationService;
    private UserServiceImpl userService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setSessionService(SessionServiceImpl sessionService) {
        this.sessionService = sessionService;
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
    @GetMapping("/upcoming")
    public String listUpcomingSessions(Model model) {
        LOG.log(Level.INFO, "Show upcoming sessions");
        var upcomingSessions = sessionService.getUpcomingSessions(LocalDateTime.now());
        var upcomingSessionsViewModels = upcomingSessions.stream()
                .map(s -> new UpcomingSessionsViewModel(s.getId(), s.getName(), s.getDuration(), s.getDescription(), s.getDateTime(), s.getAvailableSpots(), s.getPriceBeforeDiscount(), s.getPriceAfterDiscount(), s.getDifficulty(), s.getType(), s.getInstructorName()))
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
        LOG.log(Level.INFO, "Show discount sessions");
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
    public String registerToSession(Principal principal, @PathVariable("sessionId") String sessionId, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        User user = userService.getUser(email);

        SessionRegistrationDTO sessionRegistrationDTO = new SessionRegistrationDTO(user.getId(), UUID.fromString(sessionId), BigDecimal.ZERO);
        sessionRegistrationService.addSessionRegistration(sessionRegistrationDTO);
        LOG.log(Level.INFO, "Make registration to session for " + principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Вы успешно зарегистрировались на занятие!");
        return "redirect:/schedule/upcoming";
    }
}
