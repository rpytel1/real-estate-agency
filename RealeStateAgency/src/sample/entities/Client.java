package sample.entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal on 2017-01-13.
 */
public class Client {
    private final SimpleStringProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty pesel;
    private final SimpleStringProperty companyName;


    public Client(String firstName, String lastName, String pesel){
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.pesel = new SimpleStringProperty(pesel);
        this.id = new SimpleStringProperty();
        this.companyName = new SimpleStringProperty();
    }

    public Client(String firstName, String lastName, String pesel, String companyName,String id) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName =new SimpleStringProperty(lastName);
        this.pesel =new SimpleStringProperty(pesel);
        this.companyName = new SimpleStringProperty(companyName);
        this.id=new SimpleStringProperty(id);

    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPesel() {
        return pesel.get();
    }

    public SimpleStringProperty peselProperty() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel.set(pesel);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    public String getId(){
        return id.get();
    }
    public void setId(String ID){
        this.id.set(ID);
    }

}
