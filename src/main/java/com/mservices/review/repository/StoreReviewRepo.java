package com.mservices.review.repository;

import com.mservices.review.entity.StoreReview;
import com.mservices.review.repository.util.CriteriaSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StoreReviewRepo extends DynamoRepo implements DynamoRecord<StoreReview> {

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Override
    public List<StoreReview> findByCriteria(CriteriaSearch criteriaSearch) {
        List<StoreReview> records = null;

        String expression = buildExpressionQuery(criteriaSearch);
        Map<String, AttributeValue> expValues = buildExpressionValues(criteriaSearch);

        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .consistentRead(Boolean.TRUE)
                .filterExpression(Expression.builder()
                        .expression(expression)
                        .expressionValues(expValues)
                        .build())
                .build();
        PageIterable<StoreReview> pages = getTable(dynamoDbEnhancedClient, StoreReview.class).scan(req);

        if (pages.stream().count() > 0) {
            records = pages.stream().flatMap(p -> p.items().stream()).collect(Collectors.toList());
        }

        return records;
    }

    @Override
    public List<StoreReview> findAll() {
        List<StoreReview> records = null;

        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .consistentRead(Boolean.TRUE)
                .build();
        PageIterable<StoreReview> pages = getTable(dynamoDbEnhancedClient, StoreReview.class).scan(req);

        if (pages.stream().count() > 0) {
            records = pages.stream().flatMap(p -> p.items().stream()).collect(Collectors.toList());
        }

        return records;
    }

    @Override
    public StoreReview findRecord(String... keys) {
        StoreReview record = null;
        if (keys != null && keys.length > 0) {
            String key = keys[0];
            record = getTable(dynamoDbEnhancedClient, StoreReview.class).getItem(Key.builder().partitionValue(key).build());
        }
        return record;
    }

    @Override
    public StoreReview saveRecord(StoreReview record) {
        getTable(dynamoDbEnhancedClient, StoreReview.class).putItem(record);
        return record;
    }

    @Override
    public StoreReview updateRecord(StoreReview record) {
        getTable(dynamoDbEnhancedClient, StoreReview.class).updateItem(record);
        return record;
    }
}
