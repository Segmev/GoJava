package stephane_karraz_2953046_jade_kadri_295333_go.UI;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;

public class GoBoard extends Pane {
    public GoBoard() {
        score_font = Font.loadFont(GoBoard.class.getResource("../../resources/SuperMario256.ttf").toExternalForm(), 25);
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
        initialiseBoardUI();
        initialisePosLabel();
        initialiseStroke();
    }

    private void initialiseBoardUI()
    {
        p1s_label = new Label("Player 1 : 0");
        p1s_label.setFont(score_font);
        p1s_label.setTextFill(Color.BLACK);
        p2s_label = new Label("Player 2 : 0");
        p2s_label.setFont(score_font);
        p2s_label.setTextFill(Color.WHITE);
        turn_label = new Label("Player turn : Black");
        turn_label.setFont(turn_font);
        turn_label.setTextFill(Color.BLACK);
        bg_image_board = new Image("resources/board.jpg");
        bg_image_menu = new Image("resources/menu.jpg");
        background_board = new Rectangle();
        background_menu = new Rectangle();
        ImagePattern ip_background_board = new ImagePattern(bg_image_board);
        ImagePattern ip_background_menu = new ImagePattern(bg_image_menu);
        background_board.setFill(ip_background_board);
        background_menu.setFill(ip_background_menu);
        getChildren().addAll(background_board, background_menu, p1s_label, p2s_label, turn_label);
    }

    public void initialiseStroke()
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

    public void initialisePosLabel()
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
    }

    public void resizeMenu(double width, double height)
    {
        background_menu.setX(width * 0.75);
        background_menu.setWidth(width * 0.25);
        background_menu.setHeight(height);
        p1s_label.setLayoutX((width * 0.75) + 15);
        p1s_label.setLayoutY(0);
        p2s_label.setLayoutX((width * 0.75) + 15);
        p2s_label.setLayoutY(35);
        turn_label.setLayoutX((width * 0.75) + 15);
        turn_label.setLayoutY(height - 300);
        separation.setEndY(height);
        separation_t.setX(width * 0.75);
    }

    public void resizeHorizontal(double width, double height)
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

    public void resizeVertical(double width, double height)
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

    private Label p1s_label;
    private Label p2s_label;
    private Label turn_label;
    private Rectangle background_menu;
    private Rectangle background_board;
    private Image bg_image_board;
    private Image bg_image_menu;
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
}
