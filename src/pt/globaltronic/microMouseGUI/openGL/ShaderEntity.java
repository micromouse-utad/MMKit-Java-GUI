package pt.globaltronic.microMouseGUI.openGL;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.sun.javafx.geom.Vec3f;

import pt.globaltronic.microMouseGUI.openGL.entity.Camera;
import pt.globaltronic.microMouseGUI.openGL.entity.Light;
import pt.globaltronic.microMouseGUI.openGL.services.Maths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public class ShaderEntity {
        // this class is a bit weird, in the tutorial it was abstract and dude created an extending class to put
    //the class specific methods, like the loadtransformationmatrix() where the matrix id is a local variable of child class.
    private int programID;
    private int vertexshaderID;
    private int fragmentShaderID;
    private GL3 gl;
    //buffer for our matrices that are 4x4
    private static FloatBuffer matrixBuffer = FloatBuffer.wrap(new float[16]);
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;

    public ShaderEntity(GL3 gl){
        this.gl = gl;

        //using new methods to get shadercode, importing the shaders. type, quantity, class, folder name, string in array necessary if importing mroe than 1, null, path to bin, name of shader, null, true
        ShaderCode vpO = ShaderCode.create(gl, gl.GL_VERTEX_SHADER, 1, this.getClass(), "shaders", new String[]{"vertexShader"}, null, "shaders/bin", "vertexShader", null, true);
        ShaderCode fp0 = ShaderCode.create(gl, gl.GL_FRAGMENT_SHADER, 1, this.getClass(), "shaders", new String[]{"fragmentShader"}, null, "shaders/bin", "fragmentShader", null, true);

        //creating a new shaderprogram and adding the shaders
        ShaderProgram sp0 = new ShaderProgram();
        vertexshaderID = vpO.id();
        fragmentShaderID = fp0.id();
        sp0.add(vpO);
        sp0.add(fp0);
        //binding the attribute in the voa, no error when put after linking
        bindAttributes();
        //linking the shaderprogram and getting its id with the .program() function. the .id() in that class is something else.
        sp0.link(gl, System.err);
        programID = sp0.program();
        getAllUniformLocations();

    }

    protected void getAllUniformLocations(){
        location_transformationMatrix = getUniformLocation("transformationMatrix");
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_lightPosition = getUniformLocation("lightPosition");
        location_lightColor = getUniformLocation("lightColor");
        location_shineDamper = getUniformLocation("shineDamper");
        location_reflectivity = getUniformLocation("reflectivity");
    }

    //used to get at the uniform variable in the shader code so we can modify it.
    // uniformName here is the name of the variable as it appears in shadercode. in given shaderprogram
    protected int getUniformLocation(String uniformName){
        return gl.glGetUniformLocation(programID, uniformName);
    }

    //enables to modify a uniform variable and load a float to it. need id of location
    protected void loadFloat(int location, float value){
        gl.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vec3f vector){
        gl.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int location, boolean value){
        float toLoad = 0;
        if(value){
            toLoad = 1;
        }
        gl.glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4 matrix){
        matrixBuffer = FloatBuffer.wrap(matrix.getMatrix());
        matrixBuffer.flip();
        //pass location, 1 for count of matrix to be modified (if locaiton poitns to an array of matrices you gotta say how many are changed), transpose boolean, buffer)
        gl.glUniformMatrix4fv(location, 1, false, matrixBuffer);
    }

    protected void loadMatrix(int location, float[] matrix){
        matrixBuffer = FloatBuffer.wrap(matrix);
        matrixBuffer.flip();
        //pass location, 1 for count of matrix to be modified (if locaiton poitns to an array of matrices you gotta say how many are changed), transpose boolean, buffer)
        gl.glUniformMatrix4fv(location, 1, false, matrixBuffer);
    }


    //supposed to be in the child class
    public void loadTransformationMatrix(Matrix4 matrix){
        loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(float[] matrix){
        loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4 viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadLight(Light light){
        loadVector(location_lightPosition, light.getPosition());
        loadVector(location_lightColor, light.getColor());
    }

    public void loadShineVariable(float damper, float reflectivity){
        loadFloat(location_shineDamper, damper);
        loadFloat(location_reflectivity, reflectivity);
    }


    public void start(){
        gl.glUseProgram(programID);
    }

    public void stop(){
        gl.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        gl.glDetachShader(programID, vertexshaderID);
        gl.glDetachShader(programID, fragmentShaderID);
        gl.glDeleteShader(vertexshaderID);
        gl.glDeleteShader(fragmentShaderID);
        gl.glDeleteProgram(programID);
    }

    public void bindAttribute(int attribute, String variableName){
        gl.glBindAttribLocation(programID, attribute, variableName);
    }
    // supposed oteb in child class super.bindattribute...
    public void bindAttributes(){
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }

    private int loadShader (String file, int type){
        StringBuilder shaderSource = new StringBuilder();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
                shaderSource.append(line).append("\n");
            }
            reader.close();
        }catch(IOException ex){
            System.err.println("could not read file!");
            ex.printStackTrace();
            System.exit(-1);
        }

        String[] stringArr = shaderSource.toString().split("\n");


        int shaderID = gl.glCreateShader(type);
        System.out.println(shaderID);
        int[] arrayOfLength = getStrLen(stringArr);
        int[] compilingResult = new int[1];
        gl.glShaderSource(shaderID,arrayOfLength.length,stringArr, arrayOfLength, 0);
        gl.glCompileShader(shaderID);
        gl.glGetShaderiv(shaderID, GL2ES2.GL_COMPILE_STATUS, IntBuffer.wrap(compilingResult));


        if(compilingResult[0] == GL.GL_FALSE){
            System.out.println("could not compile shader." + type);
            //System.exit(-1);
        }
        return shaderID;
    }

    int[] getStrLen(String[] arr){
        int[] output = new int[arr.length];

        for (int i = 0; i < arr.length; i++){
            output[i] = arr[i].length();
        }
        return output;
    }

    public int getProgramID() {
        return programID;
    }
}
