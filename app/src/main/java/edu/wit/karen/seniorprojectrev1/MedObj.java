package edu.wit.karen.seniorprojectrev1;

import static java.sql.Types.NULL;

public class MedObj {

    private String name;
    private int ammnt;
    private boolean infinite;
    private boolean notify;
    private String info;

    /*
    * Potential attributes:
    * Color
    * Shape
    * Dosage*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmmnt() {
        return ammnt;
    }

    public void setAmmnt(int ammnt) {
        this.ammnt = ammnt;
    }

    public boolean getInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void MedObj(String name, int ammnt, boolean notify, String info)
    {
        this.name = name;
        this.ammnt = ammnt;
        this.notify = notify;
        this.info = info;
        infinite = false;




    }
    public void MedObj(String name, boolean infinite, boolean notify, String info)
    {
        this.name = name;
        this.infinite = infinite;
        this.notify = notify;
        this.info = info;
        ammnt = NULL;
    }
}
