package ru.rutmiit.controllers;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.SessionController;
import org.example.input.session.AddSessionForm;
import org.example.input.session.EditSessionForm;
import org.example.input.session.SessionSearchForm;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.create.SessionCreateViewModel;
import org.example.viewModel.details.SessionDetailsViewModel;
import org.example.viewModel.edit.SessionEditViewModel;
import org.example.viewModel.home.SessionViewModel;
import org.example.viewModel.lists.SessionListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rutmiit.dto.session.SessionInputDTO;
import ru.rutmiit.dto.session.SessionOutputDTO;
import ru.rutmiit.service.implementations.DifficultyServiceImpl;
import ru.rutmiit.service.implementations.InstructorServiceImpl;
import ru.rutmiit.service.implementations.SessionServiceImpl;
import ru.rutmiit.service.implementations.TypeServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/sessions")
public class SessionControllerImpl implements SessionController {

    private SessionServiceImpl sessionService;
    private DifficultyServiceImpl difficultyService;
    private TypeServiceImpl typeService;
    private InstructorServiceImpl instructorService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setSessionService(SessionServiceImpl sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setDifficultyService(DifficultyServiceImpl difficultyService) {
        this.difficultyService = difficultyService;
    }

    @Autowired
    public void setTypeService(TypeServiceImpl typeService) {
        this.typeService = typeService;
    }

    @Autowired
    public void setInstructorService(InstructorServiceImpl instructorService) {
        this.instructorService = instructorService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping()
    public String listSessions(SessionSearchForm form, Model model, Principal principal) {
        LOG.log(Level.INFO, "Show all sessions for " + principal.getName());
        var searchTerm = form.searchTerm() != null ? form.searchTerm() : "";
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 5;
        form = new SessionSearchForm(searchTerm, page, size);

        Page<SessionOutputDTO> sessionPage = sessionService.getAllSessionsWithPagination(searchTerm, page, size);
        var sessionViewModels = sessionPage.stream()
                .map(s -> new SessionViewModel(s.getId(), s.getName(), s.getDuration(), s.getDescription(), s.getDateTime(), s.getMaxCapacity(), s.getPrice(), s.getDifficultyName(), s.getTypeName(), s.getInstructor()))
                .toList();

        var viewModel = new SessionListViewModel(
                createBaseViewModel("Список занятий"),
                sessionViewModels,
                sessionPage.getTotalPages()
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "session-list";
    }

    @Override
    @GetMapping("/{id}")
    public String details(@PathVariable("id") String id, Model model) {
        LOG.log(Level.INFO, "Show details for session " + id);
        var session = sessionService.getSessionById(UUID.fromString(id));
        var viewModel = new SessionDetailsViewModel(
                createBaseViewModel("Детали занятия"),
                new SessionViewModel(session.getId(), session.getName(), session.getDuration(), session.getDescription(), session.getDateTime(), session.getMaxCapacity(), session.getPrice(), session.getDifficultyName(), session.getTypeName(), session.getInstructor())
        );
        model.addAttribute("model", viewModel);
        return "session-details";
    }


    @Override
    @GetMapping("/add")
    public String addForm(Model model) {
        var viewModel = new SessionCreateViewModel(
                createBaseViewModel("Добавление занятия")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddSessionForm("", 20, "", LocalDateTime.now(), 15, new BigDecimal("500.00"), "", "", ""));
        model.addAttribute("difficulties", difficultyService.getAllDifficultiesByName());
        model.addAttribute("types", typeService.getAllTypesByName());
        model.addAttribute("instructors", instructorService.getActiveInstructors());
        return "session-add";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("form") AddSessionForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Add session");
        if (bindingResult.hasErrors()) {

            var viewModel = new SessionCreateViewModel(
                    createBaseViewModel("Добавление занятия")
            );

            model.addAttribute("form", form);
            model.addAttribute("model", viewModel);
            model.addAttribute("difficulties", difficultyService.getAllDifficultiesByName());
            model.addAttribute("types", typeService.getAllTypesByName());
            model.addAttribute("instructors", instructorService.getActiveInstructors());
            return "session-add";
        }

        var id = sessionService.addSession(new SessionInputDTO(form.name(), form.duration(), form.description(), form.dateTime(), form.maxCapacity(), form.price(), form.difficultyName(), form.typeName(), form.instructorId()));
        LOG.log(Level.INFO, "Session successfully added with id: " + id);
        return "redirect:/sessions/" + id;
    }

    @Override
    @GetMapping("{id}/edit")
    public String editForm(@PathVariable("id") String id, Model model) {
        var session = sessionService.getSessionById(UUID.fromString(id));
        var viewModel = new SessionEditViewModel(
                createBaseViewModel("Редактирование занятия")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new EditSessionForm(session.getId(), session.getName(), session.getDuration(), session.getDescription(), session.getDateTime(), session.getMaxCapacity(), session.getPrice(), session.getDifficultyName(), session.getTypeName(), session.getInstructor()));
        model.addAttribute("difficulties", difficultyService.getAllDifficultiesByName());
        model.addAttribute("types", typeService.getAllTypesByName());
        model.addAttribute("instructors", instructorService.getActiveInstructors());
        return "session-edit";
    }

    @Override
    @PostMapping("{id}/edit")
    public String edit(@PathVariable("id") String id, @Valid @ModelAttribute("form") EditSessionForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Edit session with id " + id);
        if (bindingResult.hasErrors()) {
            var viewModel = new SessionEditViewModel(
                    createBaseViewModel("Редактирование занятия")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("difficulties", difficultyService.getAllDifficultiesByName());
            model.addAttribute("types", typeService.getAllTypesByName());
            model.addAttribute("instructors", instructorService.getActiveInstructors());
            return "session-edit";
        }
        var sessionDTO = new SessionInputDTO(form.name(), form.duration(), form.description(), form.dateTime(), form.maxCapacity(), form.price(), form.difficultyName(), form.typeName(), form.instructorName());
        sessionService.editSession(id, sessionDTO);
        return "redirect:/sessions/" + id;
    }
}
