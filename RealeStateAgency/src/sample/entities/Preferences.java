package sample.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Pawel on 1/17/2017.
 */
public class Preferences {

    private List<String> neighborhood;
    private Integer minArea;
    private Integer maxArea;
    private Integer minFloor;
    private Integer maxFloor;
    private Boolean elevator;
    private SimpleStringProperty id;

    public Preferences(List<String> neighborhood, Integer minArea, Integer maxArea, Integer minFloor, Integer maxFloor, boolean elevator, String id) {
        this.neighborhood = new ArrayList<String>();
        for(int i=0;i<neighborhood.size();i++){
            this.neighborhood.add(neighborhood.get(i));
        }
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.elevator = elevator;
        this.id = new SimpleStringProperty(id);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String ID) {
        id.set(ID);
    }

    public Integer getMinArea(){return minArea;}

    public Integer getMaxArea(){return maxArea;}

    public Integer getMinFloor() {return minFloor;}

    public Integer getMaxFloor() {return maxFloor;}

    public Boolean getElevator() {return elevator;}

    }
