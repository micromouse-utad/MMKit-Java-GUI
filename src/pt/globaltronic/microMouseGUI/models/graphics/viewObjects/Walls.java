package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;
import pt.globaltronic.microMouseGUI.openGL.entity.Entity;


public abstract class Walls {

    private WallPosition position;
    private Entity entity;

    public Walls(WallPosition position){
        this.position = position;
    }

    public WallPosition getPosition() {
        return position;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
