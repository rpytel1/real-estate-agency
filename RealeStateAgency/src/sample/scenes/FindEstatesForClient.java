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
import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Created by Pawel on 1/17/2017.
 */
public class FindEstatesForClient extends Scene {

    protected TableView<EstateToChoose> table = new TableView<EstateToChoose>();
    BorderPane border = new BorderPane();
    final HBox hboxMain = new HBox();
    protected Stage stage;
    protected final VBox vbox = new VBox();

    protected Connection con;

    protected ObservableList<EstateToChoose> data = FXCollections.observableArrayList();

    protected ObservableList<EstateToChoose> userData;

    protected List<Client> clientList = new ArrayList<Client>();
    protected ObservableList<String> options = FXCollections.observableArrayList();

    final HBox hb = new HBox();

    public FindEstatesForClient(Group group) {
        super(group);
    }

    public void init() {

        try {
            con = getConnection();
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
                "jdbc:oracle:thin:@localhost:1521:pzgodaf", "pzgodaf", "pzgodaf");
        return connection;
    }

    public void getClientList(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * from klient");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
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
        List<Integer> indexes = new ArrayList();

        PreparedStatement st1 = con.prepareStatement("SELECT * from nieruchomosc INNER JOIN nieruch_cechy on nieruchomosc.id_nieruchomosci = nieruch_cechy.id_nieruchomosci INNER JOIN preferencja_cechy on nieruch_cechy.id_cechy = preferencja_cechy.id_cechy " +
                "INNER JOIN preferencja on preferencja.id_preferencji = preferencja_cechy.id_preferencji INNER JOIN cecha on cecha.id_cechy = preferencja_cechy.id_cechy " +
                "WHERE id_klienta = ? AND ((nazwa_cechy = 'powierzchnia' AND wartosc <= cecha_max_wart AND wartosc >= cecha_min_wart) OR " +
                "(nazwa_cechy = 'ilosc_pokoi' AND wartosc <= cecha_max_wart AND wartosc >= cecha_min_wart) OR (nazwa_cechy = 'pietro' AND wartosc <= cecha_max_wart AND wartosc >= cecha_min_wart))");
        st1.setString(1,pesel);
        ResultSet rs1 = st1.executeQuery();
        while(rs1.next()){
            Integer tmpIndex = rs1.getInt("id_nieruchomosci");
            Boolean flag = false;
            if(indexes.size()==0){
                indexes.add(tmpIndex);
            }else {
                for (Integer i : indexes) {
                    if (tmpIndex == i) {
                        flag = true;
                    }
                }
                if(!flag){
                    System.out.print(tmpIndex);
                    indexes.add(tmpIndex);
                }
            }

        }

        PreparedStatement st4 = con.prepareStatement("SELECT * FROM nieruchomosc INNER JOIN bud_nieruchomosc on bud_nieruchomosc.ID_NIERUCHOMOSCI = nieruchomosc.ID_NIERUCHOMOSCI INNER JOIN PROJEKT_BUDYNEK on PROJEKT_BUDYNEK.ID_BUDYNKU = bud_nieruchomosc.ID_BUDYNKU INNER JOIN osiedle_projekt on osiedle_projekt.ID_PROJEKTU = PROJEKT_BUDYNEK.ID_PROJEKTU INNER JOIN dzielnica_osiedle on dzielnica_osiedle.ID_OSIEDLA = osiedle_projekt.ID_OSIEDLA INNER JOIN DZIELNICA on DZIELNICA.ID_DZIELNICY=dzielnica_osiedle.ID_DZIELNICY INNER JOIN PREFERENCJA_CECHY on PREFERENCJA_CECHY.CECHA_MIN_WART = DZIELNICA.ID_DZIELNICY INNER JOIN preferencja on preferencja.id_preferencji = preferencja_cechy.id_preferencji WHERE id_cechy = '4' AND id_klienta = ?");
        st4.setString(1,pesel);
        ResultSet rs4 = st4.executeQuery();

        while(rs4.next()){
            Integer tmpIndex = rs4.getInt("id_nieruchomosci");
            Boolean flag = false;
            if(indexes.size()==0){
                indexes.add(tmpIndex);
            }else {
                for (Integer i : indexes) {
                    if (tmpIndex == i) {
                        flag = true;
                    }
                }
                if(!flag){
                    System.out.print(tmpIndex);
                    indexes.add(tmpIndex);
                }
            }

        }

        for (Integer i : indexes) {
            String area = new String();
            String roomNum = new String();
            String level = new String();
            String elevator = new String();
            String add = new String();
            PreparedStatement st2 = con.prepareStatement("SELECT * from nieruch_cechy NATURAL JOIN nieruchomosc " +
                    "Natural JOIN cecha WHERE id_nieruchomosci=?");
            st2.setInt(1, i);
            ResultSet rs2 = st2.executeQuery();
            while (rs2.next()) {
                String charName = rs2.getString("nazwa_cechy");
                switch (charName) {
                    case "powierzchnia":
                        area = rs2.getString("wartosc");
                        break;
                    case "ilosc_pokoi":
                        roomNum = rs2.getString("wartosc");
                        break;
                    case "pietro":
                        level = rs2.getString("wartosc");
                        break;
                }
            }

            PreparedStatement st3 = con.prepareStatement("SELECT ulica,winda from budynek Natural JOin bud_nieruchomosc NATURAL JOIN nieruchomosc WHERE id_nieruchomosci=?");
            st3.setInt(1, i);
            ResultSet rs3 = st3.executeQuery();
            while (rs3.next()) {
                add = rs3.getString("ulica");
                elevator = rs3.getString("winda");
            }

            EstateToChoose estate = new EstateToChoose(add, area, roomNum, level, elevator, i.toString());
            data.add(estate);
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
                for (Client client : clientList){
                    if(comboBox.getValue().toString().equals(client.getFirstName()+" "+client.getLastName())){
                        pesel = client.getPesel();
                        try{
                            data=getEstateTable(con,pesel);
                            table.setItems(data);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                table.setEditable(false);
            }
        });
        actionVbox.setPadding(new Insets(10, 10, 0, 0));
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
        table.getColumns().addAll(adressCol, areaCol, roomNumCol, floorCol, elevatorNum, actionCol);


    }


}
