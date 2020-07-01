package pt.globaltronic.microMouseGUI.openGL;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import pt.globaltronic.microMouseGUI.openGL.entity.Camera;
import pt.globaltronic.microMouseGUI.openGL.entity.Entity;
import pt.globaltronic.microMouseGUI.openGL.entity.Light;
import pt.globaltronic.microMouseGUI.openGL.models.TexturedModel;
import pt.globaltronic.microMouseGUI.openGL.terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private ShaderEntity shader;
    private ShaderTerrain shaderTerrain;
    private RendererEntity rendererEntity;
    private RendererTerrain rendererTerrain;
    private GL3 gl;
    private int width;
    private int height;

    private Map<TexturedModel, List<Entity>> entities;
    private List<Terrain> terrains = new ArrayList<>();

    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.5f;
    private static final float FAR_PLANE = 1000.0f;
    private float[] projectionMatrix;


    public MasterRenderer(GL3 gl, int width, int height){
        this.gl = gl;
        this.width = width;
        this.height = height;
        //cull face to remove hidden triangles, less graphics to render.
        gl.glEnable(gl.GL_CULL_FACE);
        gl.glCullFace(gl.GL_BACK);
        entities = new HashMap<TexturedModel, List<Entity>>();
        shader = new ShaderEntity(gl);
        shaderTerrain = new ShaderTerrain(gl);
        createProjectionMatrix();
        rendererEntity = new RendererEntity(gl, shader, projectionMatrix);
        rendererTerrain = new RendererTerrain(gl, shaderTerrain, projectionMatrix);
    }

    public void render(Light sun, Camera camera){
        prepare();
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        rendererEntity.render(entities);
        shader.stop();
        shaderTerrain.start();
        shaderTerrain.loadLight(sun);
        shaderTerrain.loadViewMatrix(camera);
        rendererTerrain.render(terrains);
        terrains.clear();
        entities.clear();
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch != null){
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void cleanUp(){
        shader.cleanUp();
        shaderTerrain.cleanUp();
    }

    public void prepare(){
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.53f, 0.81f, 0.922f, 0);
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) width/height;
        float y_scale = (float) ((1f / (Math.tan(Math.toRadians(FOV / 2f)))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;


        float[] projectionMatrixData = {
                x_scale, 0, 0, 0,
                0, y_scale, 0, 0,
                0, 0, -((FAR_PLANE + NEAR_PLANE) / frustum_length), -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length),
                0, 0, -1, 0
        };
        projectionMatrix = projectionMatrixData;
    }
}
