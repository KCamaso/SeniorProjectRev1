package edu.wit.karen.seniorprojectrev1;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "timerxv-mobilehub-1645529831-medication")

public class MedicationDO {
    private String _userId;
    private String _currentNum;
    private Boolean _infinite;
    private String _info;
    private String _maxNum;
    private Double _medId;
    private String _name;
    private String _notify;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "currentNum")
    public String getCurrentNum() {
        return _currentNum;
    }

    public void setCurrentNum(final String _currentNum) {
        this._currentNum = _currentNum;
    }
    @DynamoDBAttribute(attributeName = "infinite")
    public Boolean getInfinite() {
        return _infinite;
    }

    public void setInfinite(final Boolean _infinite) {
        this._infinite = _infinite;
    }
    @DynamoDBAttribute(attributeName = "info")
    public String getInfo() {
        return _info;
    }

    public void setInfo(final String _info) {
        this._info = _info;
    }
    @DynamoDBAttribute(attributeName = "maxNum")
    public String getMaxNum() {
        return _maxNum;
    }

    public void setMaxNum(final String _maxNum) {
        this._maxNum = _maxNum;
    }
    @DynamoDBAttribute(attributeName = "medId")
    public Double getMedId() {
        return _medId;
    }

    public void setMedId(final Double _medId) {
        this._medId = _medId;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "notify")
    public String getNotify() {
        return _notify;
    }

    public void setNotify(final String _notify) {
        this._notify = _notify;
    }

}
