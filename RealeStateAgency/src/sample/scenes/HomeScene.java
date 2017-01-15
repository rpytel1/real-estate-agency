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
import sample.components.CustomLongLabel;
import sample.entities.Person;

/**
 * Created by Rafal on 2017-01-13.
 */
public class HomeScene extends Scene {

    protected TableView<Person> table = new TableView<Person>();
    BorderPane border = new BorderPane();
    final HBox hboxMain = new HBox();
    protected Stage stage;

    protected final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person("Jacob", "Smith", "jacob.smith@example.com"),
                    new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
                    new Person("Ethan", "Williams", "ethan.williams@example.com"),
                    new Person("Emma", "Jones", "emma.jones@example.com"),
                    new Person("Michael", "Brown", "michael.brown@example.com"));

    final HBox hb = new HBox();


    public HomeScene(Group group, Stage stage) {
        super(group);
        this.stage = stage;
    }

    public void init() {
        tableHandle();
        initScene(table);


    }

    public void initScene(TableView<?> tV) {
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tV, hb);
        final VBox vboxOptions = new VBox();
        vboxOptions.setSpacing(5);
        vboxOptions.setPadding(new Insets(10, 0, 0, 10));
        VBox actionVbox = new VBox();
        Label label1 = new Label("Possible actions");
        Button button = new Button("Find best estates for clients");
        Button addTotallyNewEstate = new Button("Add totally new EstateToChoose");
        addTotallyNewEstate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage addStage = new Stage();
                TotalNewEstateScene tnE = new TotalNewEstateScene(new Group());
                tnE.init();
                addStage.setScene(tnE);
                addStage.show();
            }
        });
        Button findNewDevOffers = new Button("Find new offers from Developer");
        findNewDevOffers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                actionVbox.getChildren().clear();
                setupDev(actionVbox);
            }
        });
        vboxOptions.getChildren().addAll(label1, button, addTotallyNewEstate, findNewDevOffers, actionVbox);
        hboxMain.setSpacing(5);
        hboxMain.setPadding(new Insets(10, 0, 0, 10));
        hboxMain.getChildren().addAll(vbox, vboxOptions);

        ((Group) this.getRoot()).getChildren().addAll(hboxMain);


    }

    public void tableHandle() {
        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);


        hb.setSpacing(3);
    }

    public void setupDev(VBox actionVbox) {
        HBox hboxDev = new HBox();
        CustomLongLabel lab = new CustomLongLabel("Developer");
        ObservableList<String> options = FXCollections.observableArrayList("dev1", "dev2", "dev3");
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setVisibleRowCount(5);
        hboxDev.getChildren().addAll(lab, comboBox);
        HBox hboxDistrict = new HBox();
        CustomLongLabel labDistrict = new CustomLongLabel("Developer");
        ObservableList<String> optionsDistrict = FXCollections.observableArrayList("district1", "district2", "district3");
        final ComboBox comboBoxDist = new ComboBox(optionsDistrict);
        comboBox.setVisibleRowCount(5);
        hboxDistrict.getChildren().addAll(labDistrict, comboBoxDist);
        Button send = new Button("Send");


        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("EstateScene!");
                ProjectChooseScene projectChooseScene = new ProjectChooseScene(new Group(), stage,comboBox.getSelectionModel().getSelectedItem().toString(),comboBoxDist.getSelectionModel().getSelectedItem().toString());
                projectChooseScene.init();
                stage.setScene(projectChooseScene);
                stage.show();


            }
        });

        actionVbox.getChildren().addAll(hboxDev, hboxDistrict, send);

    }
}
