package com.mservices.review.controller;

import com.mservices.review.entity.ProductReview;
import com.mservices.review.exception.ServiceException;
import com.mservices.review.service.ProductReviewService;
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
@RequestMapping(value = "/products")
public class ProductReviewController extends BaseController {
    
    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping(value = "/overview")
    public ResponseEntity<List<ProductReview>> listProductReviews() {
        List<ProductReview> list = productReviewService.listReviews();
        return list != null ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{productCode}/overview")
    public ResponseEntity<ProductReview> getProductReview(@PathVariable(name = "productCode") String productCode) {
        ProductReview productReview = productReviewService.getReview(productCode);
        return productReview != null ? ResponseEntity.ok(productReview) : ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/overview")
    public ResponseEntity<ProductReview> createProductReview(@Valid @RequestBody ProductReview productReview, BindingResult validation)
            throws ServiceException {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatErrors(validation));
        }
        productReview = productReviewService.saveReview(productReview);
        return ResponseEntity.status(HttpStatus.CREATED).body(productReview);
    }

    @PutMapping(value = "/{productCode}/overview")
    public ResponseEntity<ProductReview> updateProductReview(@PathVariable(name = "productCode") String productCode, @Valid @RequestBody ProductReview productReview, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatErrors(validation));
        }
        productReview.setProductCode(productCode);
        productReview = productReviewService.updateReview(productReview);
        return productReview != null? ResponseEntity.ok(productReview) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{productCode}/overview")
    public ResponseEntity<ProductReview> deleteProductReview(@PathVariable(name = "productCode") String productCode) {
        ProductReview productReview = new ProductReview();
        productReview.setProductCode(productCode);
        productReview = productReviewService.deleteReview(productReview);
        return productReview != null? ResponseEntity.ok(productReview) : ResponseEntity.notFound().build();
    }
}
