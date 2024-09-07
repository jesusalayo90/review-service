package com.mservices.review.service;

import java.util.List;

public interface ReviewService<T> {

    List<T> listReviews();
    T getReview(String key);
    T saveReview(T review);
    T updateReview(T review);
    T deleteReview(T review);

}
