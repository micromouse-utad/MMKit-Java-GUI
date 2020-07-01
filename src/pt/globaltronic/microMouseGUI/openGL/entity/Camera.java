package pt.globaltronic.microMouseGUI.openGL.entity;

import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.microMouseGUI.OpenGLEngine;

public class Camera {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private float rotY = 0;
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float distanceFromMouse = 20;
    private float angleAroundMouse = 180;
    private Vec3f position = new Vec3f(0,100,0);
    private float pitch = 30;
    private float yaw;
    private float roll;

    private MouseGFX mouseGFX;

    public Camera(MouseGFX mouseGFX){
        this.mouseGFX = mouseGFX;
    }

    public void move(){
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 -(mouseGFX.getRotY() + angleAroundMouse);
    }

    public void move(boolean freeroaming){
        increaseRotation(0, currentTurnSpeed * OpenGLEngine.getFrameTimeSeconds() , 0);
        float theta = rotY + angleAroundMouse;
        float distance = currentSpeed * OpenGLEngine.getFrameTimeSeconds();
        float dx =(float) (distance * Math.sin(Math.toRadians(theta)));
        float dy = (float) (distance * Math.sin(Math.toRadians(pitch)));
        float dz =(float) (distance * Math.cos(Math.toRadians(theta)));
        increasePosition(-dx, dy, -dz);
        this.yaw = 180 -(rotY + angleAroundMouse);

    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        this.pitch += dx;
        this.rotY += dy;
        this.roll += dz;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed * RUN_SPEED;
    }

    public void setCurrentTurnSpeed(float currentTurnSpeed) {
        this.currentTurnSpeed = currentTurnSpeed * TURN_SPEED;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public Vec3f getPosition() {
        return position;
    }

    public void setPitch(float pitch) {
        if (pitch > 90){
            this.pitch = 90;
            return;
        }
        if(pitch < -90){
            this.pitch = -90;
            return;
        }
        this.pitch = pitch;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = mouseGFX.getRotY() + angleAroundMouse;
        float offsetX =(float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ =(float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = mouseGFX.getPosition().x - offsetX;
        position.y = mouseGFX.getPosition().y + verticalDistance;
        position.z = mouseGFX.getPosition().z - offsetZ;
    }


    private float calculateHorizontalDistance(){
        return (float) (distanceFromMouse * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromMouse * Math.sin(Math.toRadians(pitch)));
    }

    public float getAngleAroundMouse() {
        return angleAroundMouse;
    }

    public float getDistanceFromMouse() {
        return distanceFromMouse;
    }

    public void setAngleAroundMouse(float angleAroundMouse) {
        this.angleAroundMouse = angleAroundMouse;
    }

    public void setDistanceFromPlayer(float distanceFromPlayer) {
        if (distanceFromPlayer > 300) {
            return;
        }
        if(distanceFromPlayer < 5){
            this.distanceFromMouse = 5.0f;
            return;
        }
        this.distanceFromMouse = distanceFromPlayer;
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }
}
