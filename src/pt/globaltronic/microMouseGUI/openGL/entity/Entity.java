package pt.globaltronic.microMouseGUI.openGL.entity;

import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.microMouseGUI.openGL.models.TexturedModel;

public class Entity {

    private TexturedModel model;
    private Vec3f position;
    private float rotX,rotY,rotZ;
    private float scale;

    public Entity(TexturedModel model, Vec3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vec3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }

}
