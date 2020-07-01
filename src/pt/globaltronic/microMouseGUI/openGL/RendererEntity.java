package pt.globaltronic.microMouseGUI.openGL;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import pt.globaltronic.microMouseGUI.openGL.entity.Entity;
import pt.globaltronic.microMouseGUI.openGL.models.RawModel;
import pt.globaltronic.microMouseGUI.openGL.models.TexturedModel;
import pt.globaltronic.microMouseGUI.openGL.textures.ModelTexture;
import pt.globaltronic.microMouseGUI.openGL.services.Maths;

import java.util.List;
import java.util.Map;

public class RendererEntity {

    private GL3 gl;
    private ShaderEntity shader;

    public RendererEntity(GL3 gl, ShaderEntity shader, float[] projectionMatrix){
        this.shader = shader;
        this.gl = gl;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity:batch){
                prepareInstance(entity);
                gl.glDrawElements(GL.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }

    }

    public void prepareTexturedModel (TexturedModel model){
        RawModel rawModel = model.getRawModel();
        gl.glBindVertexArray(rawModel.getVaoID());
        gl.glEnableVertexAttribArray(0);
        gl.glEnableVertexAttribArray(1);
        gl.glEnableVertexAttribArray(2);
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(gl.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
    }

    private void unbindTexturedModel(){
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glDisableVertexAttribArray(2);
        gl.glBindVertexArray(0);
    }

    private void prepareInstance (Entity entity){
        Matrix4 transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        ModelTexture texture = entity.getModel().getModelTexture();
        shader.loadShineVariable(texture.getShineDamper(), texture.getReflectivity());
    }

}
