package com.mservices.review.repository;

import com.mservices.review.entity.ProductReview;
import com.mservices.review.exception.ServiceException;
import com.mservices.review.repository.util.CriteriaSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mservices.review.util.ErrorConstants.RVW0100;

@Repository
public class ProductReviewRepo extends DynamoRepo implements DynamoRecord<ProductReview> {

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<ProductReview> findByCriteria(CriteriaSearch criteriaSearch) {
        List<ProductReview> records = null;

        String expression = buildExpressionQuery(criteriaSearch);
        Map<String, AttributeValue> expValues = buildExpressionValues(criteriaSearch);

        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .consistentRead(Boolean.TRUE)
                .filterExpression(Expression.builder()
                        .expression(expression)
                        .expressionValues(expValues)
                        .build())
                .build();
        PageIterable<ProductReview> pages = getTable(dynamoDbEnhancedClient, ProductReview.class).scan(req);

        if (pages.stream().count() > 0) {
            records = pages.stream().flatMap(p -> p.items().stream()).collect(Collectors.toList());
        }

        return records;
    }

    @Override
    public List<ProductReview> findAll() {
        List<ProductReview> records = null;

        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .consistentRead(Boolean.TRUE)
                .build();
        PageIterable<ProductReview> pages = getTable(dynamoDbEnhancedClient, ProductReview.class).scan(req);

        if (pages.stream().count() > 0) {
            records = pages.stream().flatMap(p -> p.items().stream()).collect(Collectors.toList());
        }

        return records;
    }

    @Override
    public ProductReview findRecord(String... keys) {
        ProductReview record = null;
        if (keys != null && keys.length > 0) {
            String key = keys[0];
            record = getTable(dynamoDbEnhancedClient, ProductReview.class).getItem(Key.builder().partitionValue(key).build());
        }
        return record;
    }

    @Override
    public ProductReview saveRecord(ProductReview record) throws ServiceException {
        try {
            PutItemEnhancedRequest<ProductReview> request = PutItemEnhancedRequest.builder(ProductReview.class)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(productCode)")
                            .build())
                    .item(record)
                    .build();

            getTable(dynamoDbEnhancedClient, ProductReview.class).putItem(request);
        } catch (DynamoDbException dbException) {
            String msg = messageSource.getMessage(RVW0100, new String[] {"ProductReview"}, LocaleContextHolder.getLocale());
            throw new ServiceException(RVW0100, msg);
        }
        return record;
    }

    @Override
    public ProductReview updateRecord(ProductReview record) {
        getTable(dynamoDbEnhancedClient, ProductReview.class).updateItem(record);
        return record;
    }
}
