package stephane_karraz_2953046_jade_kadri_295333_go.UI;

import javafx.scene.Group;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

public class Stone extends Group {
    Stone(int teamId, double op)
    {
        this.teamId = teamId;
        this.op = op;
        stone = new Ellipse();
        stone_t = new Translate();
        stone.getTransforms().add(stone_t);
        setStone(teamId);
        getChildren().add(stone);
    }

    private void createStone(String id, double size, boolean visible)
    {
        st_size = size;
        stone.setId(id);
        stone.setVisible(visible);
        stone.setOpacity(op);
    }

    void setStone(int teamId)
    {
        this.teamId = teamId;
        if (teamId == 1 || (teamId == 5 && op == 0.7))
            createStone("black_stone", 30, true);
        else if (teamId == 2 || (teamId == 6 && op == 0.7))
            createStone("white_stone", 30, true);
        else if (teamId == 3 || (teamId == 6 && op == 1))
            createStone("black_stone", 11, true);
        else if (teamId == 4 || (teamId == 5 && op == 1))
            createStone("white_stone", 11, true);
        else
            createStone("", 30, false);
    }

    int getTeamId() {
        return teamId;
    }

    double getSize() {
        return st_size;
    }


    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        stone.setRadiusX(st_size);
        stone.setRadiusY(st_size);
        stone.setCenterX(st_size / 2);
        stone.setCenterY(st_size / 2);
    }

    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);
        stone_t.setX(x);
        stone_t.setY(y);
    }

    private double st_size;
    private int teamId;
    private double op;
    private Ellipse stone;
    private Translate stone_t;
}
