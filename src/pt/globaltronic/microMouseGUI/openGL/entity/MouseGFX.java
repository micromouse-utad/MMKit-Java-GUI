package pt.globaltronic.microMouseGUI.openGL.entity;

import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.microMouseGUI.OpenGLEngine;
import pt.globaltronic.microMouseGUI.openGL.models.TexturedModel;

public class MouseGFX extends Entity{

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;

    public MouseGFX(TexturedModel model, Vec3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(){
        super.increaseRotation(0, currentTurnSpeed * OpenGLEngine.getFrameTimeSeconds() , 0);
        float distance = currentSpeed * OpenGLEngine.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed * RUN_SPEED;
    }

    public void setCurrentTurnSpeed(float currentTurnSpeed) {
        this.currentTurnSpeed = currentTurnSpeed * TURN_SPEED;
    }
}
