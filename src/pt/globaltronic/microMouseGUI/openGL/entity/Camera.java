package pt.globaltronic.microMouseGUI.openGL.entity;

import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.microMouseGUI.openGL.OpenGLEngine;

public class Camera {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;

    private float distanceFromPlayer = 20;
    private float angleAroundPlayer = 180;

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
        this.yaw = 180 -(mouseGFX.getRotY() + angleAroundPlayer);
    }

    public void move(boolean freeroaming){
        increaseRotation(0, currentTurnSpeed * OpenGLEngine.getFrameTimeSeconds() , 0);
        float distance = currentSpeed * OpenGLEngine.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(yaw)));
        float dz = (float) (distance * Math.cos(Math.toRadians(yaw)));
        increasePosition(-dx, 0, -dz);
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        this.pitch += dx;
        this.yaw += dy;
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
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = mouseGFX.getRotY() + angleAroundPlayer;
        float offsetX =(float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ =(float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = mouseGFX.getPosition().x - offsetX;
        position.y = mouseGFX.getPosition().y + verticalDistance;
        position.z = mouseGFX.getPosition().z - offsetZ;

    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    public float getAngleAroundPlayer() {
        return angleAroundPlayer;
    }

    public float getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public MouseGFX getMouseGFX() {
        return mouseGFX;
    }

    public void setAngleAroundPlayer(float angleAroundPlayer) {
        this.angleAroundPlayer = angleAroundPlayer;
    }

    public void setDistanceFromPlayer(float distanceFromPlayer) {
        if (distanceFromPlayer > 50) {
            return;
        }
        this.distanceFromPlayer = distanceFromPlayer;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }
}
