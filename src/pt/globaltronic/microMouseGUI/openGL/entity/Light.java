package pt.globaltronic.microMouseGUI.openGL.entity;

import pt.globaltronic.microMouseGUI.openGL.services.Vec3f;

public class Light {

    private Vec3f position;
    private Vec3f color;

    public Light(Vec3f position, Vec3f color) {
        this.position = position;
        this.color = color;
    }

    public Vec3f getPosition() {
        return position;
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }

    public Vec3f getColor() {
        return color;
    }

}
