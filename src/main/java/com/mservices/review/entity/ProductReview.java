package com.mservices.review.entity;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class ProductReview {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String productCode;
    private String productName;
    private Float score;
    private Integer sales;
    private Integer inCart;

}
