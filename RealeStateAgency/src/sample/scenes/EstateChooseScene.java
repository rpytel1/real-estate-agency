package sample.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.entities.Client;
import sample.entities.Person;

/**
 * Created by Rafal on 2017-01-13.
 */
public class EstateChooseScene extends HomeScene {

 protected TableView<Client> tableView=new TableView<>();
    protected final ObservableList<Client> data2 =
            FXCollections.observableArrayList(
                    new Client("Jacob", "Smith", "1234",""),
                    new Client("Isabella", "Johnson", "2345","Isabella Sp.Zoo"),
                    new Client("Ethan", "Williams", "3456","EthanGBH"),
                    new Client("Emma", "Jones", "4567","Emmma Inc"));

    public EstateChooseScene(Group group,Stage stage){
        super(group,stage);
    }
    public void init(){
        makeEstateTable();
        initScene(tableView);
    }
    public void makeEstateTable() {
        tableView = new TableView<Client>();
        tableView.setEditable(true);
        TableColumn firstName = new TableColumn("First Name");
        firstName.setMinWidth(100);
        firstName.setCellValueFactory(new PropertyValueFactory<Client, String>("firstName"));
        TableColumn secondName = new TableColumn("Second Name");
        secondName.setCellValueFactory(new PropertyValueFactory<Client, String>("lastName"));
        TableColumn pesel = new TableColumn("PESEL");
        pesel.setCellValueFactory(new PropertyValueFactory<Client, String>("pesel"));
        TableColumn companyName = new TableColumn("Company Name");
        companyName.setCellValueFactory(new PropertyValueFactory<Client, String>("companyName"));

        TableColumn actionCol = new TableColumn("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Client, String>, TableCell<Client, String>> cellFactory =
                new Callback<TableColumn<Client, String>, TableCell<Client, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Client, String> param) {
                        final TableCell<Client, String> cell = new TableCell<Client, String>() {

                            final Button btn = new Button("Just Do It");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        Client client = getTableView().getItems().get(getIndex());
                                        System.out.println(client.getFirstName() + "   " + client.getLastName());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        actionCol.setCellFactory(cellFactory);

        tableView.setItems(data2);
        tableView.getColumns().addAll(firstName, secondName, pesel, companyName,actionCol);


    }

}
