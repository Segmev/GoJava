package stephane_karraz_2953046_jade_kadri_295333_go;

import javafx.scene.control.Control;

public class CustomControl extends Control {
    public CustomControl() {
        setSkin(new CustomControlSkin(this));
        go_board = new GoBoard();
        getChildren().add(go_board);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        go_board.resize(width, height);
    }

    private GoBoard go_board;
}
