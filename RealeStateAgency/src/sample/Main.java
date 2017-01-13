package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import sample.scenes.HomeScene;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Real Estate App");
        stage.setWidth(800);
        stage.setHeight(600);
        HomeScene homeScene=new HomeScene(new Group(),stage);
        homeScene.init();
        stage.setScene(homeScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
