package pt.globaltronic.microMouseGUI.openGL;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import pt.globaltronic.microMouseGUI.openGL.models.RawModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private GL3 gl;
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public Loader (GL3 gl){
        this.gl = gl;
    }

    public RawModel loadToVao(float[] positions, float[] textureCoords, float[] normals, int[]indices){
        int vaoID = createVAO();
        vaos.add(vaoID);
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public int loadTexture(String fileName){
        Texture texture = null;
        int textureId = 0;

        try{
            //making new texture with the .newTexture (inputsteam, mipmap, suffix);
            texture = TextureIO.newTexture(new FileInputStream("resources/" + fileName + ".PNG"), false, ".PNG");
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        }catch(IOException | GLException ex){
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }
        if (texture != null) {
            textureId = texture.getTextureObject();
        }
        if (textureId == 0){
            System.err.println("failed to import texture " + fileName);
            System.exit(-1);
        }
        textures.add(textureId);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_REPEAT);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT);
        return textureId;
    }

    public void cleanUpMemory(){
        int[] arr = new int[vaos.size()];
        for (int i = 0; i < arr.length; i++){
            arr[i] = vaos.get(i);
        }
        int[] arrVbos = new int[vbos.size()];
        for (int i = 0; i< arrVbos.length; i++){
            arrVbos[i] = vbos.get(i);
        }
        gl.glDeleteVertexArrays(arr.length, IntBuffer.wrap(arr));
        gl.glDeleteBuffers(arrVbos.length, IntBuffer.wrap(arrVbos));
    }

    private int createVAO(){
        int[] vaoIDArray = new int[1];
        gl.glGenVertexArrays(1, IntBuffer.wrap(vaoIDArray));
        gl.glBindVertexArray(vaoIDArray[0]);
        return vaoIDArray[0];
    }
    //only work with 1 vao
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
        int[] vboIdArray = new int[1];
       //generating the buffer and storing it in the vboIDarray that we need to wrap as IntBuffer
        gl.glGenBuffers(1, IntBuffer.wrap(vboIdArray));
        vaos.add(vboIdArray[0]);
        //bind the buffer using the ID stored in our vboidArray
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboIdArray[0]);
        //convert float[] data into a buffer and flipping it
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        //store the data in the bound VBO using glBufferData  we need the size in bytes
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, data.length*4, buffer, GL3.GL_STATIC_DRAW);
        //storing VBO in attributelist of VAO # of attribute list, for us attribNumb, the length of each vertex,
        // type of data float, normalised or not? data between values, offset from start
        gl.glVertexAttribPointer(attributeNumber, coordinateSize, GL.GL_FLOAT, false, 0, 0);
        //unbinding buffer passing 0 as ID
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

    }

    private void unbindVAO(){
        gl.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){
        int[] vboID = new int[1];
        gl.glGenBuffers(1, IntBuffer.wrap(vboID));
        vbos.add(vboID[0]);
        IntBuffer indicesBuffer = IntBuffer.wrap(indices);
        indicesBuffer.flip();
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vboID[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity()*4, indicesBuffer, GL.GL_STATIC_DRAW);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = FloatBuffer.wrap(data);
        buffer.flip();
        return buffer;
    }
}
