package sample.entities;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafal on 2017-01-15.
 */
public class Project {
    private final SimpleStringProperty name;
    private final SimpleStringProperty dateStart;
    private final SimpleStringProperty dateEnd;
    private final SimpleStringProperty cost;
    private final SimpleStringProperty id;

    public Project(String fname, String dateSt, String dateEn, String cost, String ID) {
        name = new SimpleStringProperty(fname);
        dateStart = new SimpleStringProperty(dateSt);
        dateEnd = new SimpleStringProperty(dateEn);
        this.cost = new SimpleStringProperty(cost);
        id = new SimpleStringProperty(ID);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String names) {
        name.set(names);
    }

    public String getDateStart() {
        return dateStart.get();
    }

    public void setDateStart(String dateStart1) {
        name.set(dateStart1);
    }

    public String getDateEnd() {
        return dateEnd.get();
    }

    public void setDateEnd(String dateEnda) {
        name.set(dateEnda);
    }

    public String getCost() {
        return cost.get();
    }

    public void setCost(String cost1) {
        cost.set(cost1);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String ID) {
        id.set(ID);
    }

}
