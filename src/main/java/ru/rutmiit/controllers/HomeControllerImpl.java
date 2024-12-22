package ru.rutmiit.controllers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.HomeController;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.home.HomeViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeControllerImpl implements HomeController {

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Override
    @GetMapping("/")
    public String index(Model model) {
        LOG.log(Level.INFO, "Show home page");
        var viewModel = new HomeViewModel(createBaseViewModel("Главная страница"));
        model.addAttribute("model", viewModel);

        return "index";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }
}
