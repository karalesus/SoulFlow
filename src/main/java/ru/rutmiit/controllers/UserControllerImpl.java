package ru.rutmiit.controllers;

import jakarta.validation.Valid;
import org.example.controllers.UserController;
import org.example.input.instructor.AddInstructorForm;
import org.example.input.user.UserLoginForm;
import org.example.input.user.UserRegistrationForm;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.auth.RegisterUserViewModel;
import org.example.viewModel.auth.UserLoginViewModel;
import org.example.viewModel.create.InstructorCreateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.dto.instructor.InstructorInputDTO;
import ru.rutmiit.models.User;
import ru.rutmiit.service.implementations.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    private UserServiceImpl userService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping("/register")
    public String registerForm(Model model) {
        var viewModel = new RegisterUserViewModel(
                createBaseViewModel("Регистрация")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new UserRegistrationForm("", "", ""));
        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") UserRegistrationForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var viewModel = new RegisterUserViewModel(
                    createBaseViewModel("Регистрация")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "register";
        }
        var id = userService.register(new UserDTO(form.name(), form.email(), form.password()));
        return "redirect:/user/profile/" + id;
    }

    @Override
    @GetMapping("/login")
    public String loginForm(Model model) {
        var viewModel = new UserLoginViewModel(
                createBaseViewModel("Вход")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new UserLoginForm("", ""));
        return "login";
    }

    @Override
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("form") UserLoginForm userLoginForm, BindingResult bindingResult, Model model) {
        // todo: когда подключится секьюрити!
        return null;
    }
}
