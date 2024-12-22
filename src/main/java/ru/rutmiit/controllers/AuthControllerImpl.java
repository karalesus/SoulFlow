package ru.rutmiit.controllers;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.AuthController;
import org.example.input.user.UserLoginForm;
import org.example.input.user.UserRegistrationForm;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.auth.RegisterUserViewModel;
import org.example.viewModel.auth.UserLoginViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rutmiit.dto.user.UserDTO;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.PasswordsNotMatchException;
import ru.rutmiit.service.implementations.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class AuthControllerImpl implements AuthController {

    private UserServiceImpl userService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    private static final Logger LOG = LogManager.getLogger(Controller.class);

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
        model.addAttribute("form", new UserRegistrationForm("", "", "", ""));
        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") UserRegistrationForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Register user");
        if (bindingResult.hasErrors()) {
            var viewModel = new RegisterUserViewModel(
                    createBaseViewModel("Регистрация")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "register";
        }
        try {
            var id = userService.register(new UserDTO(form.name(), form.email(), form.password(), form.confirmPassword()));
            LOG.log(Level.INFO, "User successfully registered " + id);
            return "redirect:/user/profile/" + id;
        } catch (PasswordsNotMatchException | InvalidUserDataException message) {
            var viewModel = new RegisterUserViewModel(
                    createBaseViewModel("Регистрация")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("error", message.getMessage());
            return "register";
        }
    }

    @Override
    @GetMapping("/login")
    public String loginForm(Model model) {
        LOG.log(Level.INFO, "Login user");
        var viewModel = new UserLoginViewModel(
                createBaseViewModel("Вход")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new UserLoginForm("", ""));
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String email,
            RedirectAttributes redirectAttributes) {

        LOG.log(Level.INFO, "Failed user login");
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, email);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/user/login";
    }
}
