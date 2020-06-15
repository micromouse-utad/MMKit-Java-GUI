package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Cube;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;

import java.awt.*;

public abstract class Walls {

    Image image;
    Cube cube;
    WallPosition position;

     public Walls(Image image, WallPosition position){
         this.image = image;
         this.position = position;
     }

    public Walls(WallPosition position){
        this.position = position;
    }

    public Image getImage() {
        return image;
    }

    public Cube getCube() {
        return cube;
    }

    public void setCube(Cube cube) {
        this.cube = cube;
    }

}
