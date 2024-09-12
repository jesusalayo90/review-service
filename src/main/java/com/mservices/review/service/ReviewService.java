package com.mservices.review.service;

import com.mservices.review.entity.Review;
import com.mservices.review.exception.ServiceException;
import com.mservices.review.repository.ReviewRepo;
import com.mservices.review.repository.util.CriteriaSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mservices.review.repository.util.CriteriaSearch.CriteriaArg.Operator.EQUALS;

@Service
public class ReviewService implements ReviewBaseSvc<Review> {

    public enum Entity { stores, products }

    @Autowired
    private ReviewRepo reviewRepo;

    public List<Review> listReviewsByCode(String code) {
        CriteriaSearch search = CriteriaSearch.builder()
                .addCriteria("entityCode", code, EQUALS)
                .addCriteria("deleted", Boolean.FALSE, EQUALS)
                .build();
        List<Review> reviews = reviewRepo.findByCriteria(search);

        return Optional.ofNullable(reviews).orElse(new ArrayList<>());
    }

    @Override
    public Review saveReview(Review review) throws ServiceException {
        if (review != null) {
            review.setCreatedAt(new Date());
            review.setDeleted(Boolean.FALSE);
        }
        return reviewRepo.saveRecord(review);
    }

    @Override
    public Review updateReview(Review review) {
        Review record = reviewRepo.findRecord(review.getEntityCode(), review.getId());
        if (record != null && !record.getDeleted()) {
            record.copy(review);
            record.setUpdatedAt(new Date());
            return reviewRepo.updateRecord(record);
        }
        return null;
    }

    @Override
    public Review deleteReview(Review review) {
        Review record = reviewRepo.findRecord(review.getEntityCode(), review.getId());
        if (record != null && !record.getDeleted()) {
            record.setUpdatedAt(new Date());
            record.setDeleted(Boolean.TRUE);
            return reviewRepo.updateRecord(record);
        }
        return null;
    }
}
