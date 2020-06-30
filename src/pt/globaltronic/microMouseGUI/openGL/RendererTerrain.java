package pt.globaltronic.microMouseGUI.openGL;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;
import com.sun.javafx.geom.Vec3f;

import pt.globaltronic.microMouseGUI.openGL.models.RawModel;
import pt.globaltronic.microMouseGUI.openGL.terrain.Terrain;
import pt.globaltronic.microMouseGUI.openGL.services.Maths;

import java.util.List;

public class RendererTerrain {

    private TerrainShader shader;
    private GL3 gl;

    public RendererTerrain(GL3 gl, TerrainShader shader, float[] projMatrix){
        this.shader = shader;
        this.gl = gl;
        shader.start();
        shader.loadProjectionMatrix(projMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains){
        for(Terrain terrain: terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            gl.glDrawElements(GL.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL.GL_UNSIGNED_INT, 0);
            unbindTerraindModel();
        }

    }



    public void prepareTerrain (Terrain terrain){
        RawModel rawModel = terrain.getModel();
        gl.glBindVertexArray(rawModel.getVaoID());
        gl.glEnableVertexAttribArray(0);
        gl.glEnableVertexAttribArray(1);
        gl.glEnableVertexAttribArray(2);
        //shader.loadShineVariables(texture.getShineDamper(), texture.getRelectivity());
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(gl.GL_TEXTURE_2D, terrain.getTexture().getTextureID());
    }

    private void unbindTerraindModel(){
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glDisableVertexAttribArray(2);
        gl.glBindVertexArray(0);
    }

    private void loadModelMatrix (Terrain terrain){
        Matrix4 transformationMatrix = Maths.createTransformationMatrix(
                new Vec3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

}
