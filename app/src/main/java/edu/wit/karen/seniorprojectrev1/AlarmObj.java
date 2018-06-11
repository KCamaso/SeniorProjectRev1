package edu.wit.karen.seniorprojectrev1;

import java.util.List;

public class AlarmObj {

    private Time from;
    private Time to;
    private boolean isWindow;
    private boolean active;
    private boolean[] dayOfWeek;
    private List<MedObj> medications;



/* GETTERS, SETTERS, AND  CONSTRUCTORS BELOW */

    public Time getFrom() {
        return from;
    }

    public void setFrom(Time from) {
        this.from = from;
    }

    public Time getTo() {
        return to;
    }

    public void setTo(Time to) {
        this.to = to;
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

    public List<MedObj> getMedications() {
        return medications;
    }

    public void setMedications(List<MedObj> medications) {
        this.medications = medications;
    }

    public void AlarmObj(Time from, Time to,boolean isWindow,boolean active, boolean[] dayOfWeek, List<MedObj> medications)
    {
        this.from = from;
        this.to = to;
        this.isWindow = isWindow;
        this.active = active;
        this.dayOfWeek = dayOfWeek;
        this.medications = medications;
    }

    public class Time {
        private int hour;
        private int minute;

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }
    }
}
