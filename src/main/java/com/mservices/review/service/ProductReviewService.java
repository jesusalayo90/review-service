package com.mservices.review.service;

import com.mservices.review.entity.ProductReview;
import com.mservices.review.exception.ServiceException;
import com.mservices.review.repository.ProductReviewRepo;
import com.mservices.review.repository.util.CriteriaSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mservices.review.repository.util.CriteriaSearch.CriteriaArg.Operator.EQUALS;

@Service
public class ProductReviewService implements ReviewSvc<ProductReview> {

    @Autowired
    private ProductReviewRepo productReviewRepo;

    @Override
    public List<ProductReview> listReviews() {
        CriteriaSearch search = CriteriaSearch.builder()
                .addCriteria("deleted", Boolean.FALSE, EQUALS)
                .build();
        return Optional.ofNullable(productReviewRepo.findByCriteria(search)).orElse(new ArrayList<>());
    }

    @Override
    public ProductReview getReview(String key) {
        return productReviewRepo.findRecord(key);
    }

    @Override
    public ProductReview saveReview(ProductReview review) throws ServiceException {
        if (review != null) {
            review.setDeleted(Boolean.FALSE);
        }
        return productReviewRepo.saveRecord(review);
    }

    @Override
    public ProductReview updateReview(ProductReview review) {
        ProductReview productReview = getReview(review.getProductCode());
        if (productReview != null && !productReview.getDeleted()) {
            productReview.copy(review);
            return productReviewRepo.updateRecord(productReview);
        }
        return null;
    }

    @Override
    public ProductReview deleteReview(ProductReview review) {
        ProductReview productReview = getReview(review.getProductCode());

        if (productReview != null && !productReview.getDeleted()) {
            productReview.setDeleted(Boolean.TRUE);
            return productReviewRepo.updateRecord(productReview);
        }
        return null;
    }
}
