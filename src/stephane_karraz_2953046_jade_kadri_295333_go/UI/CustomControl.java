package stephane_karraz_2953046_jade_kadri_295333_go.UI;

import javafx.scene.control.Control;

public class CustomControl extends Control {
    public CustomControl() {
        setSkin(new CustomControlSkin(this));
        go_board = new GoBoard();
        getChildren().add(go_board);
        setOnMouseClicked(event -> {
            if (!go_board.gl_go.isTerritoryPhase() && !go_board.gl_go.isGameEnded())
               go_board.placePiece(event.getX(), event.getY());
            else
                go_board.removePiece(event.getX(), event.getY());
        });
        go_board.btn1.setOnAction(event -> {
            if (go_board.btn1.getText().equals("Pass") && !go_board.gl_go.isGameEnded())
                go_board.passTurn();
            else if (go_board.btn1.getText().equals("Continue") && !go_board.gl_go.isGameEnded()) {
                go_board.continueGame();
                go_board.btn1.setText("Pass");
                go_board.btn2.setText("Roll Back");
            }
            if (go_board.gl_go.isTerritoryPhase() && !go_board.gl_go.isGameEnded()) {
                go_board.btn1.setText("Continue");
                go_board.btn2.setText("End Game");
                go_board.getBoard();
            }
        });
        go_board.btn2.setOnAction(event -> {
            if (go_board.btn2.getText().equals("Roll Back") && !go_board.gl_go.isGameEnded())
                go_board.rollBack();
            else if (go_board.btn2.getText().equals("End Game") && !go_board.gl_go.isGameEnded())
                go_board.endGame();
        });
        go_board.btn3.setOnAction(event -> {
            go_board.resetGame();
        });
        go_board.btn4.setOnAction(event -> {
            go_board.helpGame();
        });
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        go_board.resize(width, height);
    }

    private GoBoard go_board;
}
