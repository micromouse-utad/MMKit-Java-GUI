package pt.globaltronic.microMouseGUI.openGL.services;

import com.jogamp.opengl.math.Matrix4;
import pt.globaltronic.microMouseGUI.openGL.entity.Camera;


public class Maths {

    public static Matrix4 createTransformationMatrix(Vec3f translation, float rx, float ry, float rz, float scale){
        Matrix4 matrix = new Matrix4();
        //get identity matrix
        matrix.loadIdentity();
        //translate the matrix by the given vector.
        matrix.translate(translation.x, translation.y, translation.z);
        //roate around x, y ,z respectively
        matrix.rotate((float)Math.toRadians(rx), 1, 0, 0);
        matrix.rotate((float)Math.toRadians(ry), 0, 1, 0);
        matrix.rotate((float)Math.toRadians(rz), 0, 0, 1);
        //applies the scale evenly to all 3 coordinates
        matrix.scale(scale,scale,scale);

        return matrix;
    }

    public static Matrix4 createViewMatrix(Camera camera){
        Matrix4 matrix = new Matrix4();
        //get identity matrix
        matrix.loadIdentity();
        Vec3f cameraPos = camera.getPosition();
        matrix.rotate((float)Math.toRadians(camera.getPitch()), 1, 0, 0);
        matrix.rotate((float)Math.toRadians(camera.getYaw()), 0,1 , 0);
        matrix.rotate((float)Math.toRadians(camera.getRoll()), 0, 0, 1);
        matrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        //applies the scale evenly to all 3 coordinates
        matrix.scale(1f,1f,1f);

        return matrix;
    }
}
