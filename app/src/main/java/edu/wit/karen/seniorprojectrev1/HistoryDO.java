package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "timerxv-mobilehub-1645529831-history")

public class HistoryDO {
    private String _userId;
    private Set<String> _medList;
    private Boolean _taken;
    private String _timeStampRespond;
    private String _timeStampSent;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "medList")
    public Set<String> getMedList() {
        return _medList;
    }

    public void setMedList(final Set<String> _medList) {
        this._medList = _medList;
    }
    @DynamoDBAttribute(attributeName = "taken")
    public Boolean getTaken() {
        return _taken;
    }

    public void setTaken(final Boolean _taken) {
        this._taken = _taken;
    }
    @DynamoDBAttribute(attributeName = "timeStampRespond")
    public String getTimeStampRespond() {
        return _timeStampRespond;
    }

    public void setTimeStampRespond(final String _timeStampRespond) {
        this._timeStampRespond = _timeStampRespond;
    }
    @DynamoDBAttribute(attributeName = "timeStampSent")
    public String getTimeStampSent() {
        return _timeStampSent;
    }

    public void setTimeStampSent(final String _timeStampSent) {
        this._timeStampSent = _timeStampSent;
    }

}
