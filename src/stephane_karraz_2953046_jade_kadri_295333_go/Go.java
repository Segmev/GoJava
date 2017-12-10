package stephane_karraz_2953046_jade_kadri_295333_go;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stephane_karraz_2953046_jade_kadri_295333_go.UI.CustomControl;

public class Go extends Application {
    public void init() {
        sp_mainlayout = new StackPane();
        go_scene = new Scene(sp_mainlayout, 800, 600);
        go_scene.getStylesheets().add("resources/go.css");
        go_controller = new CustomControl();
        sp_mainlayout.getChildren().add(go_controller);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GO");
        primaryStage.setScene(go_scene);
        primaryStage.show();
    }

    public void stop() {}

    public static void main(String[] args) {
        launch(args);
    }

    private StackPane sp_mainlayout;
    private Scene go_scene;
    private CustomControl go_controller;
}
