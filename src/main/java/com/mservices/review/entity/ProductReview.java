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
public class ProductReview {

    @NotEmpty(message = VLD_NOT_EMPTY)
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String productCode;
    @NotEmpty(message = VLD_NOT_EMPTY)
    private String productName;
    private Float score;
    private Integer sales;
    private Integer inCart;
    @JsonIgnore
    private Boolean deleted;

    public void copy(ProductReview r, boolean clone) {
        copy(r);
        if (clone) {
            this.setProductCode(r.getProductCode());
            this.setDeleted(r.getDeleted());
        }
    }

    public void copy(ProductReview r) {
        this.setProductName(r.getProductName());
        this.setScore(r.getScore());
        this.setSales(r.getSales());
        this.setInCart(r.getInCart());
    }
}
