package com.mservices.review.service;

import com.mservices.review.entity.StoreReview;
import com.mservices.review.repository.StoreReviewRepo;
import com.mservices.review.repository.util.CriteriaSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mservices.review.repository.util.CriteriaSearch.CriteriaArg.Operator.EQUALS;

@Service
public class StoreReviewService implements ReviewService<StoreReview> {

    @Autowired
    private StoreReviewRepo storeReviewRepo;

    @Override
    public List<StoreReview> listReviews() {
        CriteriaSearch search = CriteriaSearch.builder()
                .addCriteria("deleted", Boolean.FALSE, EQUALS)
                .build();
        return Optional.ofNullable(storeReviewRepo.findByCriteria(search)).orElse(new ArrayList<>());
    }

    @Override
    public StoreReview getReview(String key) {
        return storeReviewRepo.findRecord(key);
    }

    @Override
    public StoreReview saveReview(StoreReview review) {
        if (review != null) {
            review.setDeleted(Boolean.FALSE);
        }
        return storeReviewRepo.saveRecord(review);
    }

    @Override
    public StoreReview updateReview(StoreReview review) {
        StoreReview storeReview = getReview(review.getStoreCode());
        if (storeReview != null && !storeReview.getDeleted()) {
            storeReview.copy(review);
            return storeReviewRepo.updateRecord(storeReview);
        }
        return null;
    }

    @Override
    public StoreReview deleteReview(StoreReview review) {
        StoreReview storeReview = getReview(review.getStoreCode());

        if (storeReview != null) {
            storeReview.setDeleted(Boolean.TRUE);
            return storeReviewRepo.updateRecord(storeReview);
        }
        return null;
    }
}
