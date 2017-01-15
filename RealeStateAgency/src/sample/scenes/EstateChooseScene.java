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
import sample.entities.EstateToChoose;
import sample.entities.Person;

import java.util.EmptyStackException;

/**
 * Created by Rafal on 2017-01-13.
 */
public class EstateChooseScene extends HomeScene {

    protected TableView<EstateToChoose> tableV = new TableView();
    protected String projectId;
    protected final ObservableList<EstateToChoose> data4 =
            FXCollections.observableArrayList(
                    new EstateToChoose("Wiazowa 11 a", "56", "3", "1", "1","1"),
                    new EstateToChoose("Kleszczowa 1 b", "45", "4", "0", "0","2"),
                    new EstateToChoose("BlaStreet 10b", "102", "6", "12", "2","3"),
                    new EstateToChoose("Warszawska 11b", "123", "2", "4", "3","4"));

    public EstateChooseScene(Group group, Stage stage, String projectID) {
        super(group, stage);
        projectId = projectID;
    }

    @Override
    public void init() {
        makeEstateTable();
        initScene(tableV);
    }

    public void makeEstateTable() {
        tableV.setEditable(true);
        TableColumn adressCol = new TableColumn("Adress");
        adressCol.setMinWidth(100);
        adressCol.setCellValueFactory(new PropertyValueFactory<EstateToChoose, String>("adress"));
        TableColumn areaCol = new TableColumn("Area");
        areaCol.setCellValueFactory(new PropertyValueFactory<EstateToChoose, String>("area"));
        TableColumn roomNumCol = new TableColumn("Room Number");
        roomNumCol.setCellValueFactory(new PropertyValueFactory<EstateToChoose, String>("roomNumber"));
        TableColumn floorCol = new TableColumn("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<EstateToChoose, String>("floor"));
        TableColumn elevatorNum = new TableColumn("Elevator Number");
        elevatorNum.setCellValueFactory(new PropertyValueFactory<EstateToChoose, String>("elevator"));


        TableColumn actionCol = new TableColumn("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<EstateToChoose, String>, TableCell<EstateToChoose, String>> cellFactory =
                new Callback<TableColumn<EstateToChoose, String>, TableCell<EstateToChoose, String>>() {
                    @Override
                    public TableCell call(final TableColumn<EstateToChoose, String> param) {
                        final TableCell<EstateToChoose, String> cell = new TableCell<EstateToChoose, String>() {

                            final Button btn = new Button("Add");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        EstateToChoose estateToChoose = getTableView().getItems().get(getIndex());
                                        System.out.println(estateToChoose.getId());
                                        HomeScene homeScene = new HomeScene(new Group(), stage);
                                        homeScene.init();
                                        stage.setScene(homeScene);
                                        stage.show();
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

        tableV.setItems(data4);
        tableV.getColumns().addAll(adressCol, areaCol, roomNumCol, floorCol,elevatorNum, actionCol);


    }

}
