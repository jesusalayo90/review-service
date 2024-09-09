package com.mservices.review.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import static com.mservices.review.util.ValidationConstants.VLD_NOT_EMPTY;

@Data
@DynamoDbBean
public class StoreReview {

    @NotEmpty(message = VLD_NOT_EMPTY)
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String storeCode;
    @NotEmpty(message = VLD_NOT_EMPTY)
    private String storeName;
    private Float score;
    private Integer sales;
    private Integer shoppers;
    @JsonIgnore
    private Boolean deleted;

    public void copy(StoreReview r, boolean clone) {
        copy(r);
        if (clone) {
            this.setStoreCode(r.getStoreCode());
            this.setDeleted(r.getDeleted());
        }
    }

    public void copy(StoreReview r) {
        this.setStoreName(r.getStoreName());
        this.setScore(r.getScore());
        this.setSales(r.getSales());
        this.setShoppers(r.getShoppers());
    }
}
