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
import sample.entities.EstateToChoose;
import sample.entities.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal on 2017-01-13.
 */
public class HomeScene extends Scene {

    protected TableView<EstateToChoose> table = new TableView<EstateToChoose>();
    BorderPane border = new BorderPane();
    final HBox hboxMain = new HBox();
    protected Stage stage;

    protected final ObservableList<EstateToChoose> data =
            FXCollections.observableArrayList();


    final HBox hb = new HBox();


    public HomeScene(Group group, Stage stage) {
        super(group);
        this.stage = stage;
    }

    public void init() {
        tableHandle();
        initScene(table);

        try {
            Connection con = getConnection();
            System.out.println("Got Connection");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:rpytel", "rpytel", "rpytel");
        return connection;
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
        Button findEstatesForClient = new Button("Find best estates for clients");
        findEstatesForClient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage addStage = new Stage();
                FindEstatesForClient fEfC = new FindEstatesForClient(new Group());
                fEfC.init();
                addStage.setScene(fEfC);
                addStage.show();
            }
        });
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
        vboxOptions.getChildren().addAll(label1, findEstatesForClient, addTotallyNewEstate, findNewDevOffers, actionVbox);
        hboxMain.setSpacing(5);
        hboxMain.setPadding(new Insets(10, 0, 0, 10));
        hboxMain.getChildren().addAll(vbox, vboxOptions);

        ((Group) this.getRoot()).getChildren().addAll(hboxMain);


    }
    public void getEstateTable(Connection con) throws SQLException {
        PreparedStatement ps =con.prepareStatement("SELECT id_nieruchomosci FROM nieruchomosc");
        ResultSet rs=ps.executeQuery();
        List<Integer> indexes=new ArrayList();
        while(rs.next()){
            indexes.add(rs.getInt("id_nieruchomosci"));
        }
        String area=new String();
        String roomNum=new String();
        String level=new String();
         String elevator=new String();
        String add=new String();
       for(Integer i : indexes) {
           PreparedStatement st = con.prepareStatement("SELECT * from nieruch_cechy NATURAL JOIN nieruchomosc Natural JOIN cecha");
           rs = st.executeQuery();
           while (rs.next()) {
               switch (rs.getString("nazwa_cechy")) {
                   case "powierzchnia":
                       area=rs.getString("wartosc");
                       break;
                   case "ilosc_pokoi":
                       roomNum=rs.getString("wartosc");
                       break;
                   case "pietro":
                        level=rs.getString("wartosc");
                       break;
               }
               PreparedStatement st2 = con.prepareStatement("SELECT ulica,winda from budynek Natural JOin bud_nieruchomosc NATURAL JOIN nieruchomosc");
               rs = st2.executeQuery();
               while(rs.next())
               {
                  add=rs.getString("ulica");
                   elevator=rs.getString("winda");
               }

               EstateToChoose estate=new EstateToChoose(add,area,roomNum,level,elevator,i.toString());
               data.add(estate);
           }
       }
    }

    public void tableHandle() {
        try {
            Connection con=getConnection();
            getEstateTable(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        table.setItems(data);
        table.getColumns().addAll(adressCol, areaCol, roomNumCol, floorCol,elevatorNum);


        hb.setSpacing(3);
    }

    public void chooseClient(VBox actionVbox) {

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
                ProjectChooseScene projectChooseScene = new ProjectChooseScene(new Group(), stage, comboBox.getSelectionModel().getSelectedItem().toString(), comboBoxDist.getSelectionModel().getSelectedItem().toString());
                projectChooseScene.init();
                stage.setScene(projectChooseScene);
                stage.show();


            }
        });

        actionVbox.getChildren().addAll(hboxDev, hboxDistrict, send);

    }
}
