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
import sample.entities.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Rafal on 2017-01-15.
 */
public class ProjectChooseScene extends HomeScene {

    private String developerId;
    private String district;
    Stage stage;
    TableView tableView = new TableView();
    protected final ObservableList<Project> data2 = FXCollections.observableArrayList();

    public ProjectChooseScene(Group group, Stage stage, String dev, String district) {
        super(group, stage);
        this.stage = stage;
        developerId = dev;
        this.district = district;
    }

    @Override
    public void init() {
        makeProjectTable();
        initScene(tableView);
    }

    public void getProjects() {
        try {
            Connection con = getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT projekt.nazwa,projekt.data_rozpoczecia_budowy," +
                    "projekt.data_ukonczenia,projekt.koszt,projekt.id_projektu FROM projekt\n" +
                    "  INNER JOIN osiedle_projekt ON projekt.id_projektu = osiedle_projekt.id_projektu\n" +
                    "  INNER JOIN osiedle ON osiedle.id_osiedla=osiedle_projekt.id_osiedla\n" +
                    "  INNER JOIN dzielnica_osiedle ON osiedle.id_osiedla = dzielnica_osiedle.id_osiedla\n" +
                    "INNER JOIN dzielnica ON dzielnica_osiedle.id_dzielnicy=dzielnica.id_dzielnicy\n" +
                    "  INNER JOIN deweloper ON projekt.nip_developera=deweloper.NIP WHERE deweloper.nazwa=? ");
            ps.setString(1, developerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("nazwa");
                String dateStart = rs.getString("data_rozpoczecia_budowy");
                String dateEnd = rs.getString("data_ukonczenia");
                String cost = rs.getString("koszt");
                String id = rs.getString("id_projektu");
                Project proj = new Project(name, dateStart, dateEnd, cost, id);
                data2.add(proj);

            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeProjectTable() {

        tableView.setEditable(true);
        getProjects();
        TableColumn projectNameCol = new TableColumn("Project Name");
        projectNameCol.setMinWidth(100);
        projectNameCol.setCellValueFactory(new PropertyValueFactory<Project, String>("name"));
        TableColumn startDateCol = new TableColumn("Start date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<Project, String>("dateStart"));
        TableColumn endDateCol = new TableColumn("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<Project, String>("dateEnd"));
        TableColumn costCol = new TableColumn("Cost");
        costCol.setCellValueFactory(new PropertyValueFactory<Project, String>("cost"));

        TableColumn actionCol = new TableColumn("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Project, String>, TableCell<Project, String>> cellFactory =
                new Callback<TableColumn<Project, String>, TableCell<Project, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Project, String> param) {
                        final TableCell<Project, String> cell = new TableCell<Project, String>() {

                            final Button btn = new Button("choose");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        Project project
                                                = getTableView().getItems().get(getIndex());
                                        System.out.println(project.getId());
                                        EstateChooseScene estateChooseScene = new EstateChooseScene(new Group(), stage, project.getId());
                                        estateChooseScene.init();
                                        stage.setScene(estateChooseScene);
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

        tableView.setItems(data2);
        tableView.getColumns().addAll(projectNameCol, startDateCol, endDateCol, costCol, actionCol);

    }
}
