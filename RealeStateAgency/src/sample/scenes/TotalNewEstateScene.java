package sample.scenes;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.components.NewEstateLabel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafal on 2017-01-12.
 */
public class TotalNewEstateScene extends Scene {
    String[] districtLabels = {"City", "District name", "how long travel from city center", "how far from city center"};
    String[] estateLabels = {"Estate name", "Playground", "Parking"};
    String[] buildingLabels={"Level number","Post Code","Adress"};
    String[] propertyLabels={"Room number","Square meters","Level","Terrace"};
    Map<String,String> answers=new HashMap<>();


    public TotalNewEstateScene(Group group) {
        super(group);
    }

    public void init() {
        VBox mainVBox = new VBox();
        NewEstateLabel title = new NewEstateLabel("Specify your new estate");
        NewEstateLabel districtTitle = new NewEstateLabel("District");
        mainVBox.getChildren().addAll(title, districtTitle);

        for (String label : districtLabels) {
            HBox hbox = new HBox();
            NewEstateLabel lab = new NewEstateLabel(label);
            TextField textField = new TextField();

            hbox.getChildren().addAll(lab, textField);
            mainVBox.getChildren().add(hbox);
        }
        NewEstateLabel estateTitle = new NewEstateLabel("Estate");
        NewEstateLabel estateName = new NewEstateLabel(estateLabels[0]);
        mainVBox.getChildren().addAll(estateTitle, estateName);

        for (int i = 1; i < estateLabels.length; i++) {
            HBox hbox = new HBox();
            NewEstateLabel lab=new NewEstateLabel(estateLabels[i]);
            final ToggleGroup toggleGroup=new ToggleGroup();
            RadioButton yes=new RadioButton("Yes");
            yes.setToggleGroup(toggleGroup);
            RadioButton no=new RadioButton("No");
            no.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });
            no.setToggleGroup(toggleGroup);
            hbox.getChildren().addAll(lab,yes,no);
            mainVBox.getChildren().add(hbox);
        }
        NewEstateLabel propertyTitle = new NewEstateLabel("Property");

        for(int i=0;i<propertyLabels.length;i++){
            HBox hbox=new HBox();

                NewEstateLabel lab = new NewEstateLabel(propertyLabels[i]);
            if(i<(propertyLabels.length-1)){
                TextField textField = new TextField();
                hbox.getChildren().addAll(lab, textField);
            }
            else{
                final ToggleGroup toggleGroup=new ToggleGroup();
                RadioButton yes=new RadioButton("Yes");
                yes.setToggleGroup(toggleGroup);
                RadioButton no=new RadioButton("No");
                no.setToggleGroup(toggleGroup);
                hbox.getChildren().addAll(lab,yes,no);
            }
            mainVBox.getChildren().add(hbox);
        }
        Button send = new Button("Send");
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send(mainVBox.getChildren());
            }
        });
        mainVBox.getChildren().add(send);

        ((Group) this.getRoot()).getChildren().addAll(mainVBox);
    }
    public void send(ObservableList<Node> list){
        int i=1;
        for(Node node:list){

        }
    }
}