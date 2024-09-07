package com.mservices.review.repository;

import com.mservices.review.repository.util.CriteriaSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DynamoRepo {

    private final static Logger logger = LoggerFactory.getLogger(DynamoRepo.class);
    private final static String COLON = ":";
    private final static String AND = " AND ";

    protected <T>DynamoDbTable<T> getTable(DynamoDbEnhancedClient dynamoDbEnhancedClient, Class<T> type) {
        return dynamoDbEnhancedClient.table(type.getSimpleName(), TableSchema.fromBean(type));
    }

    protected String buildExpressionQuery(CriteriaSearch criteriaSearch) {
        String query = "";

        if (criteriaSearch != null && !CollectionUtils.isEmpty(criteriaSearch.getCriteriaArgs())) {
            query = criteriaSearch.getCriteriaArgs().stream().map(c -> createCondition(c)).collect(Collectors.joining(AND));
        } else {
            logger.warn("Cannot build expression query from an empty criteria search object.");
        }

        return query;
    }

    private String createCondition(CriteriaSearch.CriteriaArg criteriaArg) {
        if (CriteriaSearch.CriteriaArg.Operator.EQUALS.equals(criteriaArg.getOperator())) {
            return String.format("%s %s :%s", criteriaArg.getField(), criteriaArg.getOperator().getValue(), criteriaArg.getField());
        } else {
            throw new IllegalArgumentException("Unrecognized operator type");
        }
    }

    protected Map<String, AttributeValue> buildExpressionValues(CriteriaSearch criteriaSearch) {
        Map<String, AttributeValue> map = new HashMap<>();

        if (criteriaSearch != null && !CollectionUtils.isEmpty(criteriaSearch.getCriteriaArgs())) {
            map = criteriaSearch.getCriteriaArgs().stream().collect(Collectors.toMap(c -> COLON + c.getField(), c -> createAttribute(c)));
        } else {
            logger.warn("Cannot build expression values from an empty criteria search object.");
        }

        return map;
    }

    private AttributeValue createAttribute(CriteriaSearch.CriteriaArg criteriaArg) {
        switch (criteriaArg.getValue()) {
            case Boolean b: return AttributeValue.fromBool(b);
            case String s: return AttributeValue.fromS(s);
            default: throw new IllegalArgumentException("Unrecognized value type");
        }
    }
}
