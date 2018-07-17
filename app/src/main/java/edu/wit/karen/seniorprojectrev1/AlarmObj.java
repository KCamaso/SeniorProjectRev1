package edu.wit.karen.seniorprojectrev1;

import java.util.List;

public class AlarmObj {

    private int fromHour;
    private int toHour;
    private int fromMinute;
    private int toMinute;
    private boolean isWindow;
    private boolean active;
    private boolean[] dayOfWeek;
    private List<String> medications;


    /* GETTERS, SETTERS, AND  CONSTRUCTORS BELOW */




    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getFromMinute() {
        return fromMinute;
    }

    public void setFromMinute(int fromMinute) {
        this.fromMinute = fromMinute;
    }

    public int getToMinute() {
        return toMinute;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
    }


    public boolean isWindow() {
        return isWindow;
    }

    public void setWindow(boolean window) {
        this.isWindow = window;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean[] getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(boolean[] dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public AlarmObj(int fromHour, int fromMinute, int toHour, int toMinute,boolean isWindow,boolean active, boolean[] dayOfWeek, List<String> medications)
    {
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.fromMinute = fromMinute;
        this.toMinute = toMinute;
        this.isWindow = isWindow;
        this.active = active;
        this.dayOfWeek = dayOfWeek;
        this.medications = medications;
    }


}
