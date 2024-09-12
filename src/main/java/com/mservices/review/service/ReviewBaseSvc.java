package com.mservices.review.service;

import com.mservices.review.exception.ServiceException;

public interface ReviewBaseSvc<T> {

    T saveReview(T review) throws ServiceException;
    T updateReview(T review);
    T deleteReview(T review);

}
