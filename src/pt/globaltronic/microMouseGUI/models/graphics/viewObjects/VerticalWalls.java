package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Cube;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.HorizontalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.VerticalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;

import java.awt.*;

public class VerticalWalls extends Walls{

    boolean visible;

    public VerticalWalls(Image image, WallPosition position) {
        super(image, position);
        visible = false;
    }

    public VerticalWalls(WallPosition position) {
        super(position);
        visible = false;
    }

    public VerticalWallPosition getPosition(){
        return (VerticalWallPosition) this.position;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (this.cube != null){
        this.cube.setVisible(visible);
        }
    }

    public boolean isVisible() {
        return visible;
    }

}
