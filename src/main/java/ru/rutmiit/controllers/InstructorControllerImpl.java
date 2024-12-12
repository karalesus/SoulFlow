package ru.rutmiit.controllers;

import jakarta.validation.Valid;
import org.example.controllers.InstructorController;
import org.example.input.instructor.AddInstructorForm;
import org.example.input.instructor.EditInstructorForm;
import org.example.input.instructor.InstructorSearchForm;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.create.InstructorCreateViewModel;
import org.example.viewModel.details.InstructorDetailsViewModel;
import org.example.viewModel.edit.InstructorEditViewModel;
import org.example.viewModel.home.InstructorViewModel;
import org.example.viewModel.home.InstructorWithPhotoViewModel;
import org.example.viewModel.lists.InstructorListViewModel;
import org.example.viewModel.lists.InstructorsWithPhotoListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rutmiit.dto.instructor.InstructorInputDTO;
import ru.rutmiit.dto.instructor.InstructorOutputDTO;
import ru.rutmiit.service.implementations.InstructorServiceImpl;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/instructors")
public class InstructorControllerImpl implements InstructorController {

    private InstructorServiceImpl instructorService;

    @Autowired
    public void setInstructorService(InstructorServiceImpl instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/active")
    public String getInstructors(Model model) {
        List<InstructorOutputDTO> instructors = instructorService.getActiveInstructors();
        var instructorViewModels = instructors.stream()
                .map(i -> new InstructorWithPhotoViewModel(i.getId(), i.getName(), i.getCertificate(), i.getPhotoUrl()))
                .toList();
        var viewModel = new InstructorsWithPhotoListViewModel(
                createBaseViewModel("Наши инструкторы"),
                instructorViewModels
        );
        model.addAttribute("model", viewModel);
        return "instructors-view";
    }

    @Override
    @GetMapping()
    public String listInstructors(InstructorSearchForm form, Model model) {
        var searchTerm = form.searchTerm() != null ? form.searchTerm() : "";
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 5;
        form = new InstructorSearchForm(searchTerm, page, size);

        Page<InstructorOutputDTO> instructorPage = instructorService.getInstructors(searchTerm, page, size);
        var instructorViewModels = instructorPage.stream()
                .map(b -> new InstructorViewModel(b.getId(), b.getName(), b.getCertificate(), b.getPhotoUrl(), b.getIsDeleted()))
                .toList();

        var viewModel = new InstructorListViewModel(
                createBaseViewModel("Список инструкторов"),
                instructorViewModels,
                instructorPage.getTotalPages()
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "instructors-list";
    }

    @Override
    @GetMapping("/{id}")
    public String details(@PathVariable("id") String id, Model model) {
        var instructor = instructorService.getInstructorById(UUID.fromString(id));
        var viewModel = new InstructorDetailsViewModel(
                createBaseViewModel("Детали инструктора"),
                new InstructorViewModel(instructor.getId(), instructor.getName(), instructor.getCertificate(), instructor.getPhotoUrl(), instructor.getIsDeleted())
        );
        model.addAttribute("model", viewModel);
        return "instructor-details";
    }


    @Override
    @GetMapping("/add")
    public String addForm(Model model) {
        var viewModel = new InstructorCreateViewModel(
                createBaseViewModel("Добавление инструктора")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddInstructorForm("", "", ""));
        return "instructor-add";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("form") AddInstructorForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new InstructorCreateViewModel(
                    createBaseViewModel("Добавление инструктора")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "instructor-add";
        }

        var id = instructorService.addInstructor(new InstructorInputDTO(form.name(), form.certificate(), form.photoUrl()));
        return "redirect:/instructors/" + id;
    }

    @Override
    @GetMapping("{id}/edit")
    public String editForm(@PathVariable("id") String id, Model model) {
        var instructor = instructorService.getInstructorById(UUID.fromString(id));
        var viewModel = new InstructorEditViewModel(
                createBaseViewModel("Редактирование инструктора")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new EditInstructorForm(instructor.getId(), instructor.getName(), instructor.getCertificate(), instructor.getPhotoUrl()));
        return "instructor-edit";
    }

    @Override
    @PostMapping("{id}/edit")
    public String edit(@PathVariable("id") String id, @Valid @ModelAttribute("form") EditInstructorForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new InstructorEditViewModel(
                    createBaseViewModel("Редактирование инструктора")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "instructor-edit";
        }
        var instructorDTO = new InstructorInputDTO(form.name(), form.certificate(), form.photoUrl());
        instructorService.editInstructor(id, instructorDTO);
        return "redirect:/instructors/" + id;
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") String id) {
        instructorService.deleteInstructor(id);
        return "redirect:/instructors/" + id;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(
                title
        );
    }
}
