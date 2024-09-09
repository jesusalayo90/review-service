package com.mservices.review.service;

import com.mservices.review.exception.ServiceException;

import java.util.List;

public interface ReviewService<T> {

    List<T> listReviews();
    T getReview(String key);
    T saveReview(T review) throws ServiceException;
    T updateReview(T review);
    T deleteReview(T review);

}
