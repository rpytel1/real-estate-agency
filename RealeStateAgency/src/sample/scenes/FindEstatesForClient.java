package sample.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.components.CustomLongLabel;
import sample.entities.Client;
import sample.entities.EstateToChoose;
import sample.entities.Person;
import sample.entities.Project;

import java.util.EmptyStackException;

/**
 * Created by Pawel on 1/17/2017.
 */
public class FindEstatesForClient extends Scene{

    protected TableView<EstateToChoose> table = new TableView<EstateToChoose>();
    BorderPane border = new BorderPane();
    final HBox hboxMain = new HBox();
    protected Stage stage;
    protected final VBox vbox = new VBox();

    protected final ObservableList<EstateToChoose> data =
            FXCollections.observableArrayList(
                    new EstateToChoose("Wiazowa 11 a", "56", "3", "1", "1","1"),
                    new EstateToChoose("Kleszczowa 1 b", "45", "4", "0", "0","2"),
                    new EstateToChoose("BlaStreet 10b", "102", "6", "12", "2","3"),
                    new EstateToChoose("Warszawska 11b", "123", "2", "4", "3","4"));

    protected final ObservableList<EstateToChoose> data2 =
            FXCollections.observableArrayList(
                    new EstateToChoose("Kleszczowa 1 b", "45", "4", "0", "0","2"),
                    new EstateToChoose("Warszawska 11b", "123", "2", "4", "3","4"));


    final HBox hb = new HBox();

    public FindEstatesForClient(Group group) {
        super(group);
    }

    public void init(){
        makeEstateTable(data);
        initScene(table);
    }

    public void initScene(TableView<?> tV) {
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tV, hb);
        final VBox vboxOptions = new VBox();
        vboxOptions.setSpacing(5);
        vboxOptions.setPadding(new Insets(10, 0, 0, 10));
        VBox actionVbox = new VBox();
        actionVbox.getChildren().clear();
        setupDev(actionVbox);
        vboxOptions.getChildren().addAll(actionVbox);
        hboxMain.setSpacing(5);
        hboxMain.setPadding(new Insets(10, 0, 15, 10));
        hboxMain.getChildren().addAll(vbox, vboxOptions);

        ((Group) this.getRoot()).getChildren().addAll(hboxMain);


    }

    public void setupDev(VBox actionVbox) {
        HBox hboxDev = new HBox();
        CustomLongLabel lab = new CustomLongLabel("Select a Client: ");
        ObservableList<String> options = FXCollections.observableArrayList("Rafal Pytel", "Piotr Kucharski", "Pawel Zgoda");
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setValue(options.get(0));
        comboBox.setVisibleRowCount(5);
        hboxDev.getChildren().addAll(lab, comboBox);
        HBox hboxDistrict = new HBox();
        Button show = new Button("Show real estates");
        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                table.setEditable(true);
                switch(comboBox.getValue().toString()){
                    case "Rafal Pytel":
                        table.setItems(data2);
                        break;
                    default:
                        table.setItems(data);
                }
                table.setEditable(false);
            }
        });
        actionVbox.setPadding(new Insets(10,10,0,0));
        actionVbox.getChildren().addAll(hboxDev, hboxDistrict, show);

    }

    public void makeEstateTable(ObservableList<EstateToChoose> data) {
        table.setEditable(true);
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
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("New interested Client");
                                        alert.setHeaderText("Added interested Client");
                                        alert.showAndWait();
                                        //TODO: powinno też znikać

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
        table.setItems(data);
        table.getColumns().addAll(adressCol, areaCol, roomNumCol, floorCol,elevatorNum, actionCol);


    }


}
