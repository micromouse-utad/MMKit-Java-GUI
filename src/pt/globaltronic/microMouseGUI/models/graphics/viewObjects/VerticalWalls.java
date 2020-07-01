package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.VerticalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;
import pt.globaltronic.microMouseGUI.OpenGLEngine;


public class VerticalWalls extends Walls{

    private boolean visible;

    public VerticalWalls(WallPosition position) {
        super(position);
        visible = false;
    }

    public VerticalWallPosition getPosition(){
        return (VerticalWallPosition) super.getPosition();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if(getEntity() != null) {
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
