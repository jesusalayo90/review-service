package com.mservices.review.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mservices.review.repository.conversion.DateConverter;
import com.mservices.review.repository.extension.GeneratorBehavior;
import com.mservices.review.repository.extension.UuidGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.UpdateBehavior;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.Date;
import java.util.List;

import static com.mservices.review.util.ValidationConstants.VLD_NOT_EMPTY;
import static com.mservices.review.util.ValidationConstants.VLD_POSITIVE;

@Data
@DynamoDbBean
public class Review {

    @Getter(onMethod_ = {@DynamoDbUpdateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS) ,@UuidGenerator(behavior = GeneratorBehavior.ONLY_IF_EMPTY), @DynamoDbPartitionKey})
    private String id;
    @Getter(onMethod_ = @DynamoDbSortKey)
    private String entityCode;
    @NotEmpty(message = VLD_NOT_EMPTY)
    private String title;
    @NotEmpty(message = VLD_NOT_EMPTY)
    private String comment;
    @Positive(message = VLD_POSITIVE)
    private Float rating;
    private List<String> images;
    @Getter(onMethod_ = @DynamoDbConvertedBy(DateConverter.class))
    private Date createdAt;
    @Getter(onMethod_ = @DynamoDbConvertedBy(DateConverter.class))
    private Date updatedAt;
    @JsonIgnore
    private Boolean deleted;

    public void copy(Review r, boolean clone) {
        copy(r);
        if (clone) {
            this.setId(r.getId());
            this.setDeleted(r.getDeleted());
        }
    }

    public void copy(Review r) {
        this.setEntityCode(r.getEntityCode());
        this.setTitle(r.getTitle());
        this.setComment(r.getComment());
        this.setRating(r.getRating());
        this.setImages(r.getImages());
        this.setCreatedAt(r.getCreatedAt());
    }
}
