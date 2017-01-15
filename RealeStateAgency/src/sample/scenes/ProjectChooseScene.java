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

/**
 * Created by Rafal on 2017-01-15.
 */
public class ProjectChooseScene extends HomeScene {

    private String developerId;
    private String district;
    Stage stage;
    TableView tableView=new TableView();
    protected final ObservableList<Project> data2 =
            FXCollections.observableArrayList(
                    new Project("proj1", "12-12-2012", "12-04-2017","2000000$","1"),
                    new Project("proj2", "11-11-2014", "12-04-2017","2000003$","2"),
                    new Project("proj3", "12-04-2018", "12-04-2017","2000003$","3"),
                    new Project("proj4", "01-12-2006", "12-04-2017","2000003$","4"),
                    new Project("proj5", "01-01-2012", "12-04-2017","2000003$","5"));

    public ProjectChooseScene(Group group, Stage stage, String dev, String district){
        super(group,stage);
        this.stage=stage;
        developerId=dev;
        this.district=district;
    }

    @Override
    public void init(){
        makeProjectTable();
        initScene(tableView);
    }
    public void makeProjectTable(){

        tableView.setEditable(true);
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
                                        EstateChooseScene estateChooseScene = new EstateChooseScene(new Group(), stage,project.getId());
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
