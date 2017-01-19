package sample.entities;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafal on 2017-01-15.
 */
public class EstateToChoose {
    private SimpleStringProperty adress;//Budynek
    private SimpleStringProperty area;//n
    private SimpleStringProperty roomNumber;//n
    private SimpleStringProperty floor;//n
    private SimpleStringProperty elevator;//b
    private SimpleStringProperty id;//n

    public EstateToChoose(String add, String ar, String rNum, String fl, String elev, String ID) {
        adress = new SimpleStringProperty(add);
        area = new SimpleStringProperty(ar);
        roomNumber = new SimpleStringProperty(rNum);
        floor = new SimpleStringProperty(fl);
        elevator = new SimpleStringProperty(elev);
        id = new SimpleStringProperty(ID);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String ID) {
        id.set(ID);
    }

    public String getAdress() {
        return adress.get();
    }

    public void setAdress(String add) {
        adress.set(add);
    }

    public String getArea() {
        return area.get();
    }

    public void setArea(String ar) {
        area.set(ar);
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public void setRoomNumber(String rNum) {
        roomNumber.set(rNum);
    }

    public String getFloor() {
        return floor.get();
    }

    public void setFloor(String fl) {
        floor.set(fl);
    }

    public String getElevator() {
        return elevator.get();
    }

    public void setElevator(String el) {
        elevator.set(el);
    }

}
