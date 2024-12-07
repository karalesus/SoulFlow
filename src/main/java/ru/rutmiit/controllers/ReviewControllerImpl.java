package ru.rutmiit.controllers;

import jakarta.validation.Valid;
import org.example.controllers.ReviewController;
import org.example.input.review.AddReviewForm;
import org.example.input.review.ReviewSearchForm;
import org.example.viewModel.BaseViewModel;
import org.example.viewModel.create.ReviewCreateViewModel;
import org.example.viewModel.create.SessionCreateViewModel;
import org.example.viewModel.home.ReviewViewModel;
import org.example.viewModel.lists.ReviewListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rutmiit.dto.Review.ReviewInputDTO;
import ru.rutmiit.dto.Review.ReviewOutputDTO;
import ru.rutmiit.service.implementations.ReviewServiceImpl;

@Controller
@RequestMapping("/reviews")
public class ReviewControllerImpl implements ReviewController {

    private ReviewServiceImpl reviewService;

    @Autowired
    public void setReviewService(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping()
    public String listReviews(ReviewSearchForm form, Model model) {
        var page = form.page() != null ? form.page() : 1;
        var size = form.size() != null ? form.size() : 3;
        form = new ReviewSearchForm(page, size);

        Page<ReviewOutputDTO> reviewPage = reviewService.getAllReviewsWithPagination(page, size);
        var reviewViewModels = reviewPage.stream()
                .map(r -> new ReviewViewModel(r.getSessionId(), r.getRate().toString(), r.getComment(), r.getDate()))
                .toList();

        var viewModel = new ReviewListViewModel(
                createBaseViewModel("Список отзывов"),
                reviewViewModels,
                reviewPage.getTotalPages()
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        return "review-list";
    }
}
