package stephane_karraz_2953046_jade_kadri_295333_go.UI;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;
import stephane_karraz_2953046_jade_kadri_295333_go.Logic.GameLogic;

public class GoBoard extends Pane {
    GoBoard() {
        score_font = Font.loadFont(GoBoard.class.getResource("../../resources/SuperMario256.ttf").toExternalForm(), 35);
        turn_font = Font.loadFont(GoBoard.class.getResource("../../resources/SuperMario256.ttf").toExternalForm(), 23);
        pos_font = Font.loadFont(GoBoard.class.getResource("../../resources/SuperMario256.ttf").toExternalForm(), 25);
        horizontal = new Line[7];
        vertical = new Line[7];
        horizontal_t = new Translate[7];
        vertical_t = new Translate[7];
        lb_horizontal_top = new Label[7];
        lb_horizontal_bottom = new Label[7];
        lb_vertical_left = new Label[7];
        lb_vertical_right = new Label[7];
        gl_go = new GameLogic();
        stone = new Stone[7][7];
        territories = new Stone[7][7];
        gl_go.resetGame(7, 7);
        initialiseBoardUI();
        initialiseButtons();
        initialisePosLabel();
        initialiseStroke();
        initialiseStonesTerritories();
        initialiseScore();
        resetGame();
    }

    private void initialiseBoardUI()
    {
        Image bg_image_board = new Image("resources/board.jpg");
        Image bg_image_menu = new Image("resources/menu.jpg");
        ImagePattern ip_background_board = new ImagePattern(bg_image_board);
        ImagePattern ip_background_menu = new ImagePattern(bg_image_menu);
        background_board = new Rectangle();
        background_menu = new Rectangle();
        background_board.setFill(ip_background_board);
        background_menu.setFill(ip_background_menu);
        getChildren().addAll(background_board, background_menu);
    }

    private void initialiseButtons()
    {
        btn1 = new Button("Pass");
        btn1.setId("buttonPass");
        btn2 = new Button("Roll Back");
        btn2.setId("buttonRollBack");
        btn3 = new Button("Reset Game");
        btn3.setId("buttonReset");
        btn4 = new Button("Help");
        btn4.setId("buttonHelp");
        getChildren().addAll(btn1, btn2, btn3, btn4);
    }

    private void initialiseStroke()
    {
        separation = createLine(0, 0, 0, 0, "-fx-stroke-width: 6;");
        separation_t = new Translate(0, 0);
        separation.getTransforms().add(separation_t);
        for (int i = 0; i < 7; i++)
        {
            horizontal[i] = createLine(100, 100, 0, 100, "-fx-stroke-width: 3;");
            horizontal_t[i] = new Translate(0, 0);
            horizontal[i].getTransforms().add(horizontal_t[i]);
            vertical[i] = createLine(100, 100, 100, 0, "-fx-stroke-width: 3;");
            vertical_t[i] = new Translate(0, 0);
            vertical[i].getTransforms().add(vertical_t[i]);
            getChildren().addAll(horizontal[i], vertical[i]);
        }
        getChildren().add(separation);
    }

    private void initialiseScore()
    {
        p1s_label = createLabel(" : " + gl_go.getTeamScore(1), Color.BLACK, score_font);
        p2s_label = createLabel(" : " + gl_go.getTeamScore(2), Color.BLACK, score_font);
        turn_label = createLabel("Player Turn", Color.BLACK, turn_font);
        p1s_ellipse = createEllipse(30, "black_stone");
        p1s_ellipse_t = new Translate();
        p1s_ellipse.getTransforms().add(p1s_ellipse_t);
        p2s_ellipse = createEllipse(30, "white_stone");
        p2s_ellipse_t = new Translate();
        p2s_ellipse.getTransforms().add(p2s_ellipse_t);
        turn_ellipse = createEllipse(50, "black_stone");
        turn_ellipse_t = new Translate();
        turn_ellipse.getTransforms().add(turn_ellipse_t);
        getChildren().addAll(p1s_ellipse, p2s_ellipse, turn_ellipse, p1s_label, p2s_label, turn_label);
    }

    private void initialiseStonesTerritories()
    {
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
            {
                territories[i][j] = new Stone(1, 30);
                stone[i][j] = new Stone(0, 30);
                getChildren().addAll(stone[i][j], territories[i][j]);
            }
    }

    private void initialisePosLabel()
    {
        char pos_letter = 'A';
        int pos_number = 7;
        for (char i = 0; i < 7; i++)
        {
            char letter = (char) (pos_letter + i);
            lb_horizontal_top[i] = createLabel("" + letter, Color.BLACK, pos_font);
            lb_horizontal_bottom[i] = createLabel("" + letter, Color.BLACK, pos_font);
            lb_vertical_left[i] = createLabel("" + (pos_number - i), Color.BLACK, pos_font);
            lb_vertical_right[i] = createLabel("" + (pos_number - i), Color.BLACK, pos_font);
            getChildren().addAll(lb_horizontal_top[i], lb_horizontal_bottom[i], lb_vertical_left[i], lb_vertical_right[i]);
        }
    }

    void placePiece(double x, double y)
    {
        int cell_x = (int) ((x - 100) / cell_width);
        int cell_y = (int) ((y - 100) / cell_height);
        back_x = cell_x;
        back_y = cell_y;
        if (!inGame)
            return;
        if ((cell_x > 6 || cell_x < 0) || (cell_y > 6 || cell_y < 0))
            return;
        canRoll = true;
        System.out.println("X : " + x + " | Y : " + y + " | cellx : " + cell_x + " | celly : " + cell_y);
        if (gl_go.getCurrentPlayer() == 1)
            turn_ellipse.setId("white_stone");
        else
            turn_ellipse.setId("black_stone");
        gl_go.playPos(cell_x , cell_y);
        stone[cell_x][cell_y].setStone(gl_go.getCurrentPlayer());
        p1s_label.setText(" : " + gl_go.getTeamScore(1));
        p2s_label.setText(" : " + gl_go.getTeamScore(2));
    }

    void resetGame()
    {
        gl_go.resetGame(7, 7);
        inGame = true;
        canRoll = false;
        back_x = 0;
        back_y = 0;
        p1s_label.setText(" : " + gl_go.getTeamScore(1));
        p2s_label.setText(" : " + gl_go.getTeamScore(2));
        btn1.setText("Pass");
        btn1.setId("buttonPass");
        btn2.setText("Roll Back");
        btn2.setId("buttonRollBack");
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
            {
                stone[i][j].setStone(0);
                territories[i][j].setStone(0);
            }
        turn_ellipse.setId("black_stone");
    }

    void passTurn()
    {
        if (gl_go.getCurrentPlayer() == 1)
            turn_ellipse.setId("white_stone");
        else
            turn_ellipse.setId("black_stone");
        gl_go.passTurn();
    }

    void showTerritories()
    {
        int[][] gl_territories = gl_go.getTerritoryBoard();

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
            {
                System.out.println("x : " + i + " | y : " + j + " | Stone : " + gl_territories[i][j]);
                territories[i][j].setStone(gl_territories[i][j]);
            }
        inGame = false;
    }

    private void hideTerritories()
    {
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
            {
                territories[i][j].setStone(0);
            }
    }

    void continueGame()
    {
        gl_go.continueGame();
        hideTerritories();
        inGame = true;
    }

    void rollBack()
    {
        if (canRoll)
        {
            gl_go.rollbackTurn();
            stone[back_x][back_y].setStone(0);
            if (gl_go.getCurrentPlayer() == 1)
                turn_ellipse.setId("white_stone");
            else
                turn_ellipse.setId("black_stone");
        }
    }

    void endGame()
    {
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background_board.setWidth(width * 0.75);
        background_board.setHeight(height);
        cell_width = ((width * 0.75) - 200) / 6;
        cell_height = (height - 200) / 6;
        resizeMenu(width, height);
        resizeHorizontal((width * 0.75), height);
        resizeVertical((width * 0.75), height);
        resizeScore(width, height);
        resizeStones();
    }

    private void resizeMenu(double width, double height)
    {
        background_menu.setX(width * 0.75);
        background_menu.setWidth(width * 0.25);
        background_menu.setHeight(height);
        p1s_label.setLayoutX((width * 0.75) + (width * 0.145 - (21 / 2)));
        p1s_label.setLayoutY(height * 0.10);
        p2s_label.setLayoutX((width * 0.75) + (width * 0.145 - (21 / 2)));
        p2s_label.setLayoutY(height * 0.22);
        turn_label.setLayoutX((width * 0.75) + (width * 0.125 - (166 / 2)));
        turn_label.setLayoutY(height * 0.34);
        turn_ellipse.setLayoutX((width * 0.75) + ((width * 0.125) - 25));
        turn_ellipse.setLayoutY(height * 0.45);
        separation.setEndY(height);
        separation_t.setX(width * 0.75);
        resizeButtons(width, height);
    }

    private void resizeButtons(double width, double height)
    {
        btn1 = resizeButton(btn1, width * 0.75, height * 0.60, width * 0.25, height * 0.10);
        btn2 = resizeButton(btn2, width * 0.75, height * 0.70, width * 0.25, height * 0.10);
        btn3 = resizeButton(btn3, width * 0.75, height * 0.80, width * 0.25, height * 0.10);
        btn4 = resizeButton(btn4, width * 0.75, height * 0.90, width * 0.25, height * 0.10);
    }

    private Button resizeButton(Button btn, double x, double y, double width, double height)
    {
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        btn.setMinWidth(width);
        btn.setMinHeight(height);
        return (btn);
    }

    private void resizeHorizontal(double width, double height)
    {
        for (int i = 0; i < 7; i++)
        {
            lb_horizontal_top[i].setLayoutX(93 + (cell_width * (i)));
            lb_horizontal_top[i].setLayoutY(height - (height - 25));
            lb_horizontal_bottom[i].setLayoutX(93 + (cell_width * (i)));
            lb_horizontal_bottom[i].setLayoutY(height - 60);
            horizontal[i].setEndX(width - 100);
            horizontal_t[i].setY(i * cell_height);
        }
    }

    private void resizeVertical(double width, double height)
    {
        for (int i = 0; i < 7; i++)
        {
            lb_vertical_left[i].setLayoutY(80 + (cell_height * (i)));
            lb_vertical_left[i].setLayoutX(width - (width - 35));
            lb_vertical_right[i].setLayoutY(80 + (cell_height * (i)));
            lb_vertical_right[i].setLayoutX(width - 50);
            vertical[i].setEndY(height - 100);
            vertical_t[i].setX(i * cell_width);
        }
    }

    private void resizeStones()
    {
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
            {
                stone[i][j].resize(cell_width, cell_height);
                stone[i][j].relocate((100 - stone[i][j].st_size) + (i * cell_width), (100 - stone[i][j].st_size) + (j * cell_height));
            }
    }

    private void resizeScore(double width, double height) {
        p1s_ellipse_t.setX((width * 0.75) + (width * 0.07));
        p1s_ellipse_t.setY(height * 0.11);
        p2s_ellipse_t.setX((width * 0.75) + (width * 0.07));
        p2s_ellipse_t.setY(height * 0.23);
    }

    private Label createLabel(String text, Color color, Font font)
    {
        Label label = new Label(text);
        label.setFont(font);
        label.setTextFill(color);
        return label;
    }

    private Line createLine(double start_x, double start_y, double end_x, double end_y, String style)
    {
        Line line = new Line();

        line.setStartX(start_x);
        line.setStartY(start_y);
        line.setEndX(end_x);
        line.setEndY(end_y);
        line.setStyle(style);
        return line;
    }

    private Ellipse createEllipse(double size, String id)
    {
        Ellipse ellipse = new Ellipse();
        ellipse.setId(id);
        ellipse.setRadiusX(size);
        ellipse.setRadiusY(size);
        ellipse.setCenterX(size / 2);
        ellipse.setCenterY(size / 2);
        return (ellipse);
    }

    Button btn1, btn2, btn3, btn4;
    private GameLogic gl_go;
    private Stone[][] stone;
    private Stone[][] territories;
    private Label p1s_label, p2s_label, turn_label;
    private Rectangle background_menu, background_board;
    private Ellipse p1s_ellipse, p2s_ellipse, turn_ellipse;
    private Translate p1s_ellipse_t, p2s_ellipse_t, turn_ellipse_t, separation_t;
    private Font score_font, turn_font, pos_font;
    private Line separation;
    private Label[] lb_horizontal_top, lb_horizontal_bottom, lb_vertical_left, lb_vertical_right;
    private Line[] horizontal, vertical;
    private Translate[] horizontal_t, vertical_t;
    private double cell_width, cell_height;
    private int back_x, back_y;
    private boolean inGame, canRoll;
}
