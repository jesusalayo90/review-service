package com.mservices.review.repository;

import com.mservices.review.repository.util.CriteriaSearch;

import java.util.List;

public interface DynamoRecord<T> {

    List<T> findByCriteria(CriteriaSearch criteriaSearch);
    List<T> findAll();
    T findRecord(String... keys);
    T saveRecord(T record);
    T updateRecord(T record);

}
