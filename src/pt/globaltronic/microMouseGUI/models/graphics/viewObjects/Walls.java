package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;

import java.awt.*;

public abstract class Walls {

    Image image;
    WallPosition position;

     public Walls(Image image, WallPosition position){
         this.image = image;
         this.position = position;
     }

    public Image getImage() {
        return image;
    }
}
