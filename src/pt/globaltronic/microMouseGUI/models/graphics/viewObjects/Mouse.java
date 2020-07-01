package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.openGL.entity.Entity;

public class Mouse {

    private Position position;
    private Entity mouseGFX;

    public Mouse (Position position){
        this.position = position;
    }

    public Mouse(Position position, Entity mouseGFX){
        this.position = position;
        this.mouseGFX = mouseGFX;
    }


    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

}
