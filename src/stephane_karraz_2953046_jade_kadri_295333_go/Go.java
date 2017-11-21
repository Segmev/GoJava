package stephane_karraz_2953046_jade_kadri_295333_go;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Go extends Application {
    public void init() {
        sp_mainlayout = new StackPane();
        go_controller = new GoController();
        sp_mainlayout.getChildren().add(go_controller);
    }

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("GO");
        primaryStage.setScene(new Scene(sp_mainlayout   , 900, 900));
        primaryStage.show();
    }

    public void stop() {}

    public static void main(String[] args) {
        launch(args);
    }

    private StackPane sp_mainlayout;
    private GoController go_controller;
}
