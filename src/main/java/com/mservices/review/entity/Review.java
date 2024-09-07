package com.mservices.review.entity;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@DynamoDbBean
public class Review {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private UUID id;
    @Getter(onMethod_ = @DynamoDbSortKey)
    private String entityCode;
    private String title;
    private String comment;
    private Float rating;
    private List<String> images;
    private Date createdAt;

}
