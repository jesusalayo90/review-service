package com.mservices.review.controller;

import com.mservices.review.entity.Review;
import com.mservices.review.exception.ServiceException;
import com.mservices.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.mservices.review.controller.util.BindingResultUtil.formatErrors;

@Controller
@RequestMapping
public class ReviewController extends BaseController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/{entity}/{entityCode}")
    public ResponseEntity<List<Review>> listReviewsByCode(@PathVariable(name = "entity") ReviewService.Entity entity,
                                                          @PathVariable(name = "entityCode") String entityCode) {
        List<Review> list = reviewService.listReviewsByCode(entityCode);
        return list != null ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{entity}")
    public ResponseEntity<Review> createReview(@PathVariable(name = "entity") ReviewService.Entity entity,
                                               @Valid @RequestBody Review review, BindingResult validation) throws ServiceException {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatErrors(validation));
        }
        review = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping(value = "/{entity}/{entityCode}/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable(name = "entity") ReviewService.Entity entity,
                                               @PathVariable(name = "entityCode") String entityCode,
                                               @PathVariable(name = "reviewId") String reviewId,
                                               @Valid @RequestBody Review review, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatErrors(validation));
        }
        review.setId(reviewId);
        review.setEntityCode(entityCode);
        review = reviewService.updateReview(review);
        return review != null? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{entity}/{entityCode}/{reviewId}")
    public ResponseEntity<Review> deleteReview(@PathVariable(name = "entity") ReviewService.Entity entity,
                                               @PathVariable(name = "entityCode") String entityCode,
                                               @PathVariable(name = "reviewId") String reviewId) {
        Review review = new Review();
        review.setId(reviewId);
        review.setEntityCode(entityCode);
        review = reviewService.deleteReview(review);
        return review != null? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }
}
