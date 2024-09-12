package com.mservices.review.repository;

import com.mservices.review.entity.Review;
import com.mservices.review.exception.ServiceException;
import com.mservices.review.repository.util.CriteriaSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mservices.review.util.ErrorConstants.RVW0100;

@Repository
public class ReviewRepo extends DynamoRepo implements DynamoRecord<Review> {

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Review> findByCriteria(CriteriaSearch criteriaSearch) {
        List<Review> records = null;

        String expression = buildExpressionQuery(criteriaSearch);
        Map<String, AttributeValue> expValues = buildExpressionValues(criteriaSearch);

        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .consistentRead(Boolean.TRUE)
                .filterExpression(Expression.builder()
                        .expression(expression)
                        .expressionValues(expValues)
                        .build())
                .build();
        PageIterable<Review> pages = getTable(dynamoDbEnhancedClient, Review.class).scan(req);

        if (pages.stream().count() > 0) {
            records = pages.stream().flatMap(p -> p.items().stream()).collect(Collectors.toList());
        }

        return records;
    }

    @Override
    public List<Review> findAll() {
        List<Review> records = null;

        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .consistentRead(Boolean.TRUE)
                .build();
        PageIterable<Review> pages = getTable(dynamoDbEnhancedClient, Review.class).scan(req);

        if (pages.stream().count() > 0) {
            records = pages.stream().flatMap(p -> p.items().stream()).collect(Collectors.toList());
        }

        return records;
    }

    @Override
    public Review findRecord(String... keys) {
        Review record = null;
        if (keys != null && keys.length > 0) {
            String entityCode = keys[0];
            String reviewId = keys[1];
            Key keyReq = Key.builder().partitionValue(reviewId).sortValue(entityCode).build();
            record = getTable(dynamoDbEnhancedClient, Review.class).getItem(keyReq);
        }
        return record;
    }

    @Override
    public Review saveRecord(Review record) throws ServiceException {
        try {
            UpdateItemEnhancedRequest<Review> request = UpdateItemEnhancedRequest.builder(Review.class)
                    .item(record)
                    .returnValues(ReturnValue.ALL_NEW)
                    .build();

            UpdateItemEnhancedResponse<Review> resp = getTable(dynamoDbEnhancedClient, Review.class).updateItemWithResponse(request);
            record = resp.attributes();
        } catch (DynamoDbException dbException) {
            String msg = messageSource.getMessage(RVW0100, new String[] {"Review"}, LocaleContextHolder.getLocale());
            throw new ServiceException(RVW0100, msg);
        }
        return record;
    }

    @Override
    public Review updateRecord(Review record) {
        UpdateItemEnhancedRequest<Review> request = UpdateItemEnhancedRequest.builder(Review.class)
                .item(record)
                .build();

        getTable(dynamoDbEnhancedClient, Review.class).updateItem(request);
        return record;
    }
}
