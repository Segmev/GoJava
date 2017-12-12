package stephane_karraz_2953046_jade_kadri_295333_go.UI;

import javafx.scene.Group;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

public class Stone extends Group {
    Stone(int teamId, double size)
    {
        stone = new Ellipse();
        stone_t = new Translate();
        stone.getTransforms().add(stone_t);
        st_size = size;
        setStone(teamId);
        getChildren().add(stone);
    }

    private void createStone(String id, double size, boolean visible)
    {
        st_size = size;
        stone.setId(id);
        stone.setVisible(visible);
    }

    void setStone(int teamId)
    {
        System.out.println("Size : " + st_size);
        if (teamId == 1)
            createStone("black_stone", st_size, true);
        else if (teamId == 2)
            createStone("white_stone", st_size, true);
        else if (teamId == 3)
            createStone("black_stone", st_size / 3, true);
        else if (teamId == 4)
            createStone("white_stone", st_size / 3, true);
        else
            createStone("", st_size, false);
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

    double st_size;
    private Ellipse stone;
    private Translate stone_t;
}
