package com.mservices.review.controller;

import com.mservices.review.entity.StoreReview;
import com.mservices.review.service.StoreReviewService;
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
@RequestMapping(value = "/stores")
public class StoreReviewController {

    @Autowired
    private StoreReviewService storeReviewService;

    @GetMapping
    public ResponseEntity<List<StoreReview>> listStoreReviews() {
        List<StoreReview> list = storeReviewService.listReviews();
        return list != null ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{storeCode}")
    public ResponseEntity<StoreReview> getStoreReview(@PathVariable(name = "storeCode") String storeCode) {
        StoreReview storeReview = storeReviewService.getReview(storeCode);
        return storeReview != null ? ResponseEntity.ok(storeReview) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<StoreReview> createStoreReview(@Valid @RequestBody StoreReview storeReview, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatErrors(validation));
        }
        storeReview = storeReviewService.saveReview(storeReview);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeReview);
    }

    @PutMapping(value = "/{storeCode}")
    public ResponseEntity<StoreReview> updateStoreReview(@PathVariable(name = "storeCode") String storeCode, @Valid @RequestBody StoreReview storeReview, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatErrors(validation));
        }
        storeReview.setStoreCode(storeCode);
        storeReview = storeReviewService.updateReview(storeReview);
        return storeReview != null? ResponseEntity.ok(storeReview) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{storeCode}")
    public ResponseEntity<StoreReview> deleteStoreReview(@PathVariable(name = "storeCode") String storeCode) {
        StoreReview storeReview = new StoreReview();
        storeReview.setStoreCode(storeCode);
        storeReview = storeReviewService.deleteReview(storeReview);
        return storeReview != null? ResponseEntity.ok(storeReview) : ResponseEntity.notFound().build();
    }
}
