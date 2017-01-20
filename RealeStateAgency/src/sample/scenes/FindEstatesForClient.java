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

    protected Connection con;

    protected ObservableList<EstateToChoose> data =  FXCollections.observableArrayList();

    protected final ObservableList<EstateToChoose> data2 =
            FXCollections.observableArrayList(
                    new EstateToChoose("Kleszczowa 1 b", "45", "4", "0", "0","2"),
                    new EstateToChoose("Warszawska 11b", "123", "2", "4", "3","4"));

    protected ObservableList<EstateToChoose> userData;

    protected List<Client> clientList = new ArrayList<Client>();
    protected ObservableList<String> options = FXCollections.observableArrayList();

    final HBox hb = new HBox();

    public FindEstatesForClient(Group group) {
        super(group);
    }

    public void init(){

        try {
            con=getConnection();
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

        PreparedStatement st2 = con.prepareStatement("SELECT id_nieruchomosci FROM nieruchomosc");
        ResultSet rs2 = st2.executeQuery();
        List<Integer> indexes = new ArrayList();
        while (rs2.next()) {
            indexes.add(rs2.getInt("id_nieruchomosci"));
        }

        PreparedStatement st1 = con.prepareStatement("SELECT * from nieruchomosc INNER JOIN nieruch_cechy on nieruchomosc.id_nieruchomosci = nieruch_cechy.id_nieruchomosci INNER JOIN preferencja_cechy on nieruch_cechy.id_cechy = preferencja_cechy.id_cechy" +
                "INNER JOIN preferencja on preferencja.id_preferencji = preferencja_cechy.id_preferencji INNER JOIN cecha on cecha.id_cechy = preferencja_cechy.id_cechy" +
                "WHERE id_klienta = 95031110214 AND ((nazwa_cechy = 'powierzchnia' AND wartosc <= cecha_max_wart AND wartosc >= cecha_min_wart) OR" +
                "(nazwa_cechy = 'ilosc_pokoi' AND wartosc <= cecha_max_wart AND wartosc >= cecha_min_wart) OR (nazwa_cechy = 'pietro' AND wartosc <= cecha_max_wart AND wartosc >= cecha_min_wart))");
        //st1.setInt(1,new Integer(pesel));
        ResultSet rs1 = st1.executeQuery();

        for (Integer i : indexes){

            String powierzchnia = new String();
            String ilosc_pokoi = new String();
            String pietro = new String();

            while(rs1.next()){
                if(rs1.getInt("id_nieruchomosci") == i){
                    switch (rs1.getString("nazwa_cechy")){
                        case "powierzchnia":
                            powierzchnia = rs1.getString("wartosc");
                            break;
                        case "ilosc_pokoi":
                            ilosc_pokoi = rs1.getString("wartosc");
                            break;
                        case "pietro":
                            pietro = rs1.getString("wartosc");
                            break;
                    }
                }
            }
            EstateToChoose nieruchomosc = new EstateToChoose(powierzchnia,ilosc_pokoi,pietro);
            data.add(nieruchomosc);
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
                try{
                    data = getEstateTable(con,"95031110214");
                    table.setItems(data);
                }catch(Exception e){
                    e.printStackTrace();
                }
                table.setEditable(false);
//                for(Client client : clientList){
//                    System.out.print(client.getFirstName() + " " +client.getLastName());
//                    System.out.print(comboBox.getValue().toString());
//                    if(comboBox.getValue().toString().equals(client.getFirstName().toString()+" "+client.getLastName().toString())){
//                        pesel = client.getPesel().toString();
//                        try {
//                            data=getEstateTable(con,pesel);
//                            table.setItems(data);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
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
