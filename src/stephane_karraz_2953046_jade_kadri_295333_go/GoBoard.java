package stephane_karraz_2953046_jade_kadri_295333_go;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class GoBoard extends Pane {
    public GoBoard() {
        initialiseBoardUI();
    }

    private void initialiseBoardUI()
    {
        bg_image = new Image("textures/board.jpg");
        background = new Rectangle();
        ImagePattern ip_background = new ImagePattern(bg_image);
        background.setFill(ip_background);
        getChildren().add(background);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background.setWidth(width);
        background.setHeight(height);
    }

    //private Rectangle background;
    private Rectangle background;
    private Image bg_image;
}
