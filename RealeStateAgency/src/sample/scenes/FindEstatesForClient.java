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
import sample.entities.*;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

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

    protected ObservableList<EstateToChoose> userData;

    protected List<Client> clientList = new ArrayList<Client>();
    protected ObservableList<String> options = FXCollections.observableArrayList("Pawel Zgoda");

    final HBox hb = new HBox();

    public FindEstatesForClient(Group group) {
        super(group);
    }

    public void init(){

        try {
            Connection con=getConnection();
            getClientList(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        makeEstateTable(data);
        initScene(table);
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:rpytel", "rpytel", "rpytel");
        return connection;
    }

    public void getClientList(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * from klient");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String imie = rs.getString("imie");
            String nazwisko = rs.getString("nazwisko");
            String pesel = rs.getString("PESEL");
            Client client = new Client(imie, nazwisko, pesel);
            clientList.add(client);
            options.add(client.getFirstName() + " " + client.getLastName());
        }
    }

    public ObservableList<EstateToChoose> getEstateTable(Connection con, String pesel) throws SQLException {

        ObservableList<EstateToChoose> data = FXCollections.observableArrayList();

        PreparedStatement st1 = con.prepareStatement("SELECT * from Preferencja_cechy NATURAL JOIN Preferencja NATURAL JOIN Cecha NATURAL JOIN Klient Where pesel='" + pesel + "'");
        ResultSet rs1 = st1.executeQuery();

        Integer powierzchnia_min = 0;
        Integer powierzchnia_max = 0;
        Integer ilosc_pokoi = 0;
        Integer pietro_min = 0;
        Integer pietro_max = 0;

        while (rs1.next()) {


            switch (rs1.getString("nazwa_cechy")) {
                case "powierzchnia_min":
                    powierzchnia_min = rs1.getInt("wartosc");
                    break;
                case "powierzchnia_max":
                    powierzchnia_max = rs1.getInt("wartosc");
                    break;
                case "ilosc_pokoi":
                    ilosc_pokoi = rs1.getInt("wartosc");
                    break;
                case "pietro_min":
                    pietro_min = rs1.getInt("wartosc");
                    break;
                case "pietro_max":
                    pietro_max = rs1.getInt("wartosc");
                    break;
            }
        }

        Preferences preferences = new Preferences(powierzchnia_min, powierzchnia_max, pietro_min, pietro_max, ilosc_pokoi);

        PreparedStatement st2 = con.prepareStatement("SELECT id_nieruchomosci FROM nieruchomosc");
        ResultSet rs2 = st2.executeQuery();
        List<Integer> indexes = new ArrayList();
        while (rs2.next()) {
            indexes.add(rs2.getInt("id_nieruchomosci"));
        }

        for (Integer i : indexes) {

            Integer area = 0;
            Integer numberOfRooms = 0;
            Integer level = 0;

            PreparedStatement st3 = con.prepareStatement("SELECT * from nieruch_cechy NATURAL JOIN nieruchomosc Natural JOIN cecha WHERE id_nieruchomosci='" + i + "'");
            ResultSet rs3 = st3.executeQuery();
            while (rs3.next()) {
                switch (rs3.getString("nazwa_cechy")) {
                    case "powierzchnia":
                        area = rs3.getInt("wartosc");
                        break;
                    case "ilosc_pokoi":
                        numberOfRooms = rs3.getInt("wartosc");
                        break;
                    case "pietro":
                        level = rs3.getInt("wartosc");
                        break;
                }

                EstateToChoose nieruchomosc = new EstateToChoose(area.toString(), numberOfRooms.toString(), level.toString());
                //Tutaj warunek dodawania nieruchomosci do tabeli
                if (true) {
                    data.add(nieruchomosc);
                }
            }

        }
        return data;
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
                String pesel = new String();
                for(Client client : clientList){
                    if(comboBox.getValue().toString() == (client.getFirstName().toString()+" "+client.getLastName().toString())){
                        pesel = client.getPesel().toString();
                    }
                }


                //table.setItems(data); to jak juz stworzysz data
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
