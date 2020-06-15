package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Cube;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.HorizontalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;

import java.awt.*;

public class HorizontalWalls extends Walls {

    boolean visible;

    public HorizontalWalls(Image image, WallPosition position) {
        super(image, position);
        visible = false;
    }

    public HorizontalWalls(WallPosition position) {
        super(position);
        visible = false;
    }

    public HorizontalWallPosition getPosition(){
        return (HorizontalWallPosition) this.position;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.cube.setVisible(visible);
    }

    public boolean isVisible() {
        return visible;
    }
}
