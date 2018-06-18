package edu.wit.karen.seniorprojectrev1;

import java.util.ArrayList;

public class HistoryObj {

    private String timeStamp;
    private ArrayList<String> missedMeds;
    private Boolean dismissed;

    public HistoryObj(String timeStamp, ArrayList<String> missedMeds, Boolean dismissed) {
        this.timeStamp = timeStamp;
        this.missedMeds = missedMeds;
        this.dismissed = dismissed;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<String> getMissedMeds() {
        return missedMeds;
    }

    public void setMissedMeds(ArrayList<String> missedMeds) {
        this.missedMeds = missedMeds;
    }

    public Boolean getDismissed() {
        return dismissed;
    }

    public void setDismissed(Boolean dismissed) {
        this.dismissed = dismissed;
    }


}
