package com.mservices.review.service;

import com.mservices.review.exception.ServiceException;

import java.util.List;

public interface ReviewSvc<T> extends ReviewBaseSvc<T> {

    List<T> listReviews();
    T getReview(String key);

}
