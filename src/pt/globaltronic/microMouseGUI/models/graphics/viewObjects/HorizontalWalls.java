package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.HorizontalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;
import pt.globaltronic.microMouseGUI.OpenGLEngine;

import java.awt.*;

public class HorizontalWalls extends Walls {

    private boolean visible;

    public HorizontalWalls(Image image, WallPosition position) {
        super(image, position);
        visible = false;
    }

    public HorizontalWalls(WallPosition position) {
        super(position);
        visible = false;
    }

    public HorizontalWallPosition getPosition() {
        return (HorizontalWallPosition) super.getPosition();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (getEntity() != null) {
            if (visible) {
                OpenGLEngine.VISIBLE_WALLS.add(this.getEntity());
            }
            if (!visible) {
                OpenGLEngine.VISIBLE_WALLS.remove(this.getEntity());
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }
}
