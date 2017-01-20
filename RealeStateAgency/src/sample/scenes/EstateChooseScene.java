package sample.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.entities.Client;
import sample.entities.EstateToChoose;
import sample.entities.Person;
import sample.entities.Project;

import java.sql.*;
import java.util.EmptyStackException;
import java.util.Properties;

/**
 * Created by Rafal on 2017-01-13.
 */
public class EstateChooseScene extends HomeScene {

    protected TableView<EstateToChoose> tableV = new TableView();
    protected String projectId;
    protected final ObservableList<EstateToChoose> data4 = FXCollections.observableArrayList();

    public EstateChooseScene(Group group, Stage stage, String projectID) {
        super(group, stage);
        projectId = projectID;
    }

    @Override
    public void init() {
        makeEstateTable();
        initScene(tableV);
    }

    public void getEstates() {
        try {
            Connection con = getConnection();
            data4.clear();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM nieruchomosc\n" +
                    "  INNER JOIN bud_nieruchomosc ON nieruchomosc.id_nieruchomosci = bud_nieruchomosc.id_nieruchomosci\n" +
                    "INNER JOIN budynek ON bud_nieruchomosc.id_budynku=budynek.id_budynku\n" +
                    "INNER JOIN projekt_budynek ON projekt_budynek.id_budynku=budynek.id_budynku \n" +
                    "WHERE projekt_budynek.id_projektu=? AND nieruchomosc.w_ofercie=0 ");
            ps.setString(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String add = rs.getString("ulica");
                String elevator = rs.getString("winda");
                String roomNum = new String();
                String floor = new String();
                String area = new String();
                Integer k = rs.getInt("id_nieruchomosci");
                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM nieruchomosc\n" +
                        "  INNER JOIN nieruch_cechy ON nieruchomosc.id_nieruchomosci=nieruch_cechy.id_nieruchomosci\n" +
                        "  INNER JOIN cecha on nieruch_cechy.id_cechy=cecha.id_cechy\n" +
                        "WHERE nieruchomosc.id_nieruchomosci=? AND nieruchomosc.w_ofercie=0");
                ps2.setInt(1, k);
                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    String type = rs2.getString("nazwa_cechy");
                    switch (type) {
                        case "powierzchnia":
                            area = rs2.getString("wartosc");
                            break;
                        case "ilosc_pokoi":
                            roomNum = rs2.getString("wartosc");
                            break;
                        case "pietro":
                            floor = rs2.getString("wartosc");
                            break;
                    }
                }

                EstateToChoose estate = new EstateToChoose(add, area, roomNum, floor, elevator, k.toString());
                this.data4.add(estate);

            }

            // ps =con.prepareStatement()
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToOffer(int id) {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps=con.prepareStatement("UPDATE nieruchomosc SET w_ofercie='1' " +
                    "where id_nieruchomosci=?");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void makeEstateTable() {
        tableV.setEditable(true);
        getEstates();
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
                                        alert.setTitle("Adding new offer");
                                        alert.setHeaderText("Added Estate to our offer");
                                        alert.showAndWait();
                                        addToOffer(new Integer(estateToChoose.getId()));
                                        getEstates();
                                        tableV.setItems(data4);
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

        tableV.setItems(data4);
        tableV.getColumns().addAll(adressCol, areaCol, roomNumCol, floorCol, elevatorNum, actionCol);


    }

}
