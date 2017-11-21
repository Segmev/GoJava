package stephane_karraz_2953046_jade_kadri_295333_go;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class GoBoard extends Pane {
    public GoBoard() {
        font = Font.loadFont(GoBoard.class.getResource("SuperMario256.ttf").toExternalForm(), 100);
        initialiseBoardUI();
    }

    private void initialiseBoardUI()
    {
        //test = new Label("TEST");
        //test.setFont(font);
        //test.setTextFill(Color.BLACK);
        bg_image = new Image("textures/board.jpg");
        background = new Rectangle();
        ImagePattern ip_background = new ImagePattern(bg_image);
        background.setFill(ip_background);
        getChildren().addAll(background, test);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background.setWidth(width);
        background.setHeight(height);
    }

    //private Rectangle background;
    private Label test;
    private Rectangle background;
    private Image bg_image;
    private Font font;
}
