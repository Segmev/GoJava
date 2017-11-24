package stephane_karraz_2953046_jade_kadri_295333_go.UI;

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
        horizontal = new Line[8];
        vertical = new Line[8];
        horizontal_t = new Translate[8];
        vertical_t = new Translate[8];
        lb_horizontal_top = new Label[8];
        lb_horizontal_bottom = new Label[8];
        lb_vertical_left = new Label[8];
        lb_vertical_right = new Label[8];
        gl_go = new GameLogic();
        initialiseBoardUI();
        initialisePosLabel();
        initialiseStroke();
        gl_go.resetGame(7, 7);
        initialiseScore();
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

    private void initialiseStroke()
    {
        separation = new Line();
        separation.setStartX(0);
        separation.setStartY(0);
        separation.setEndX(0);
        separation.setStyle("-fx-stroke-width: 6;");
        separation.setStroke(Color.BLACK);
        separation_t = new Translate(0, 0);
        separation.getTransforms().add(separation_t);
        for (int i = 0; i < 8; i++)
        {
            horizontal[i] = new Line();
            horizontal[i].setStartX(100);
            horizontal[i].setStartY(100);
            horizontal[i].setEndY(100);
            horizontal[i].setStyle("-fx-stroke-width: 3;");
            horizontal[i].setStroke(Color.BLACK);
            horizontal_t[i] = new Translate(0, 0);
            horizontal[i].getTransforms().add(horizontal_t[i]);
            vertical[i] = new Line();
            vertical[i].setStartX(100);
            vertical[i].setStartY(100);
            vertical[i].setEndX(100);
            vertical[i].setStyle("-fx-stroke-width: 3;");
            vertical[i].setStroke(Color.BLACK);
            vertical_t[i] = new Translate(0, 0);
            vertical[i].getTransforms().add(vertical_t[i]);
            getChildren().addAll(horizontal[i], vertical[i]);
        }
        getChildren().add(separation);
    }

    private void initialiseScore()
    {
        p1s_label = new Label("" + gl_go.getTeamScore(1));
        p1s_label.setFont(score_font);
        p1s_label.setTextFill(Color.WHITE);
        p1s_ellipse = new Ellipse();
        p1s_ellipse_t = new Translate();
        p1s_ellipse.setFill(Color.BLACK);
        p1s_ellipse.getTransforms().add(p1s_ellipse_t);
        p2s_label = new Label("" + gl_go.getTeamScore(2));
        p2s_label.setFont(score_font);
        p2s_label.setTextFill(Color.BLACK);
        p2s_ellipse = new Ellipse();
        p2s_ellipse_t = new Translate();
        p2s_ellipse.setFill(Color.WHITE);
        p2s_ellipse.getTransforms().add(p2s_ellipse_t);
        turn_label = new Label("Player turn : Black");
        turn_label.setFont(turn_font);
        turn_label.setTextFill(Color.BLACK);
        getChildren().addAll(p1s_ellipse, p2s_ellipse, p1s_label, p2s_label, turn_label);
    }

    private void initialisePosLabel()
    {
        char pos_letter = 'A';
        int pos_number = 8;
        for (char i = 0; i < 8; i++)
        {
            char letter = (char) (pos_letter + i);
            lb_horizontal_top[i] = new Label("" + letter);
            lb_horizontal_top[i].setFont(pos_font);
            lb_horizontal_top[i].setTextFill(Color.BLACK);
            lb_horizontal_bottom[i] = new Label("" + letter);
            lb_horizontal_bottom[i].setFont(pos_font);
            lb_horizontal_bottom[i].setTextFill(Color.BLACK);
            lb_vertical_left[i] = new Label("" + (pos_number - i));
            lb_vertical_left[i].setFont(pos_font);
            lb_vertical_left[i].setTextFill(Color.BLACK);
            lb_vertical_right[i] = new Label("" + (pos_number - i));
            lb_vertical_right[i].setFont(pos_font);
            lb_vertical_right[i].setTextFill(Color.BLACK);
            getChildren().addAll(lb_horizontal_top[i], lb_horizontal_bottom[i], lb_vertical_left[i], lb_vertical_right[i]);
        }
    }

    public void placePiece(double x, double y)
    {
        int cell_x = (int) (x / cell_width);
        int cell_y = (int) (y / cell_height);

        gl_go.playPos(cell_x, cell_y);

        p1s_label.setText("" + gl_go.getTeamScore(1));
        p2s_label.setText("" + gl_go.getTeamScore(2));
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background_board.setWidth(width * 0.75);
        background_board.setHeight(height);
        cell_width = ((width * 0.75) - 200) / 7;
        cell_height = (height - 200) / 7;
        resizeMenu(width, height);
        resizeHorizontal((width * 0.75), height);
        resizeVertical((width * 0.75), height);
        resizeScore(width, height, cell_width, cell_height);
    }

    private void resizeMenu(double width, double height)
    {
        background_menu.setX(width * 0.75);
        background_menu.setWidth(width * 0.25);
        background_menu.setHeight(height);
        p1s_label.setLayoutX((width * 0.75) + ((width * 0.235) / 2));
        p1s_label.setLayoutY(height * 0.17);
        p2s_label.setLayoutX((width * 0.75) + ((width * 0.235) / 2));
        p2s_label.setLayoutY(height * 0.35);
        turn_label.setLayoutX((width * 0.75) + 15);
        turn_label.setLayoutY(height - 300);
        separation.setEndY(height);
        separation_t.setX(width * 0.75);
    }

    private void resizeHorizontal(double width, double height)
    {
        for (int i = 0; i < 8; i++)
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
        for (int i = 0; i < 8; i++)
        {
            lb_vertical_left[i].setLayoutY(80 + (cell_height * (i)));
            lb_vertical_left[i].setLayoutX(width - (width - 35));
            lb_vertical_right[i].setLayoutY(80 + (cell_height * (i)));
            lb_vertical_right[i].setLayoutX(width - 50);
            vertical[i].setEndY(height - 100);
            vertical_t[i].setX(i * cell_width);
        }
    }

    private void resizeScore(double width, double height, double ellipse_width, double ellipse_height)
    {
        p1s_ellipse.setRadiusX(30);
        p1s_ellipse.setRadiusY(30);
        p1s_ellipse.setCenterX(30 / 2);
        p1s_ellipse.setCenterY(30 / 2);
        p2s_ellipse.setRadiusX(30);
        p2s_ellipse.setRadiusY(30);
        p2s_ellipse.setCenterX(30 / 2);
        p2s_ellipse.setCenterY(30 / 2);
        System.out.println(ellipse_width / 2);
        p1s_ellipse_t.setX((width * 0.75) + ((width * 0.225) / 2));
        p1s_ellipse_t.setY(height * 0.18);
        p2s_ellipse_t.setX((width * 0.75) + ((width * 0.225) / 2));
        p2s_ellipse_t.setY(height * 0.36);
    }

    private Label p1s_label;
    private Label p2s_label;
    private Label turn_label;
    private Rectangle background_menu;
    private Rectangle background_board;
    private Ellipse p1s_ellipse;
    private Ellipse p2s_ellipse;
    private Translate p1s_ellipse_t;
    private Translate p2s_ellipse_t;
    private Font score_font;
    private Font turn_font;
    private Font pos_font;
    private Line separation;
    private Translate separation_t;
    private Label[] lb_horizontal_top;
    private Label[] lb_horizontal_bottom;
    private Label[] lb_vertical_left;
    private Label[] lb_vertical_right;
    private Line[] horizontal;
    private Line[] vertical;
    private Translate[] horizontal_t;
    private Translate[] vertical_t;
    private double cell_width;
    private double cell_height;
    private GameLogic gl_go;
}
