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
        pos_font = Font.loadFont(GoBoard.class.getResource("../../resources/SuperMario256.ttf").toExternalForm(), 40);
        horizontal = new Line[8];
        vertical = new Line[8];
        horizontal_t = new Translate[8];
        vertical_t = new Translate[8];
        lb_horizontal_top = new Label[8];
        lb_horizontal_bottom = new Label[8];
        lb_vertical = new Label[8];
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
        bg_image = new Image("resources/board.jpg");
        background_board = new Rectangle();
        background_menu = new Rectangle();
        ImagePattern ip_background = new ImagePattern(bg_image);
        background_menu.setFill(Color.SADDLEBROWN);
        background_board.setFill(ip_background);
        getChildren().addAll(background_board, background_menu, p1s_label, p2s_label, turn_label);
    }

    public void initialiseStroke()
    {
        separation = new Line();
        separation.setStartX(0);
        separation.setStartY(0);
        separation.setEndX(0);
        separation.setStroke(Color.BLACK);
        separation_t = new Translate(0, 0);
        separation.getTransforms().add(separation_t);
        for (int i = 0; i < 8; i++)
        {
            horizontal[i] = new Line();
            horizontal[i].setStartX(100);
            horizontal[i].setStartY(100);
            horizontal[i].setEndY(100);
            horizontal[i].setStroke(Color.BLACK);
            horizontal_t[i] = new Translate(0, 0);
            horizontal[i].getTransforms().add(horizontal_t[i]);
            vertical[i] = new Line();
            vertical[i].setStartX(100);
            vertical[i].setStartY(100);
            vertical[i].setEndX(100);
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
            lb_vertical[i] = new Label("" + (pos_number - i));
            lb_vertical[i].setFont(pos_font);
            lb_vertical[i].setTextFill(Color.BLACK);
            getChildren().addAll(lb_horizontal_top[i], lb_horizontal_bottom[i], lb_vertical[i]);
        }
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background_board.setWidth(width * 0.75);
        background_board.setHeight(height);
        background_menu.setX(width * 0.75);
        background_menu.setWidth(width * 0.25);
        background_menu.setHeight(height);
        cell_width = width / (width / 100);
        cell_height = height / (height / 100);
        p1s_label.setLayoutX((width * 0.75) + 15);
        p1s_label.setLayoutY(0);
        p2s_label.setLayoutX((width * 0.75) + 15);
        p2s_label.setLayoutY(35);
        turn_label.setLayoutX((width * 0.75) + 15);
        turn_label.setLayoutY(height - 300);
        separation.setEndY(height);
        separation_t.setX(width * 0.75);
        for (int i = 0; i < 8; i++)
        {
            lb_horizontal_top[i].setLayoutX(((cell_width) * (i + 1)) - 10);
            lb_horizontal_top[i].setLayoutY(cell_height - 80);
            lb_horizontal_bottom[i].setLayoutX(((cell_width) * (i + 1)) - 10);
            lb_horizontal_bottom[i].setLayoutY((8 * cell_height) + 25);
            horizontal[i].setEndX(8 * cell_height);
            horizontal_t[i].setY(i * cell_height);
        }
        for (int i = 0; i < 8; i++)
        {
            lb_vertical[i].setLayoutY(((cell_height) * (i + 1)) - 25);
            lb_vertical[i].setLayoutX(cell_width - 70);
            vertical[i].setEndY(8 * cell_width);
            vertical_t[i].setX(i * cell_width);
        }
    }

    //private Rectangle background;
    private Label p1s_label;
    private Label p2s_label;
    private Label turn_label;
    private Rectangle background_menu;
    private Rectangle background_board;
    private Image bg_image;
    private Font score_font;
    private Font turn_font;
    private Font pos_font;
    private Line separation;
    private Translate separation_t;
    private Label[] lb_horizontal_top;
    private Label[] lb_horizontal_bottom;
    private Label[] lb_vertical;
    private Line[] horizontal;
    private Line[] vertical;
    private Translate[] horizontal_t;
    private Translate[] vertical_t;
    private double cell_width;
    private double cell_height;
}
