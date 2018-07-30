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

@DynamoDBTable(tableName = "timerxv-mobilehub-1645529831-timer")

public class TimerDO {
    private String _userId;
    private Double _fromHour;
    private Boolean _active;
    private Set<Double> _dayOfWeek;
    private Double _fromMinute;
    private Boolean _isWindow;
    private Set<String> _medName;
    private Double _timerId;
    private Double _toHour;
    private Double _toMinute;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "fromHour")
    @DynamoDBAttribute(attributeName = "fromHour")
    public Double getFromHour() {
        return _fromHour;
    }

    public void setFromHour(final Double _fromHour) {
        this._fromHour = _fromHour;
    }
    @DynamoDBAttribute(attributeName = "active")
    public Boolean getActive() {
        return _active;
    }

    public void setActive(final Boolean _active) {
        this._active = _active;
    }
    @DynamoDBAttribute(attributeName = "dayOfWeek")
    public Set<Double> getDayOfWeek() {
        return _dayOfWeek;
    }

    public void setDayOfWeek(final Set<Double> _dayOfWeek) {
        this._dayOfWeek = _dayOfWeek;
    }
    @DynamoDBAttribute(attributeName = "fromMinute")
    public Double getFromMinute() {
        return _fromMinute;
    }

    public void setFromMinute(final Double _fromMinute) {
        this._fromMinute = _fromMinute;
    }
    @DynamoDBAttribute(attributeName = "isWindow")
    public Boolean getIsWindow() {
        return _isWindow;
    }

    public void setIsWindow(final Boolean _isWindow) {
        this._isWindow = _isWindow;
    }
    @DynamoDBAttribute(attributeName = "medName")
    public Set<String> getMedName() {
        return _medName;
    }

    public void setMedName(final Set<String> _medName) {
        this._medName = _medName;
    }
    @DynamoDBAttribute(attributeName = "timerId")
    public Double getTimerId() {
        return _timerId;
    }

    public void setTimerId(final Double _timerId) {
        this._timerId = _timerId;
    }
    @DynamoDBAttribute(attributeName = "toHour")
    public Double getToHour() {
        return _toHour;
    }

    public void setToHour(final Double _toHour) {
        this._toHour = _toHour;
    }
    @DynamoDBAttribute(attributeName = "toMinute")
    public Double getToMinute() {
        return _toMinute;
    }

    public void setToMinute(final Double _toMinute) {
        this._toMinute = _toMinute;
    }

}
