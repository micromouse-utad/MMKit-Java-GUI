package pt.globaltronic.microMouseGUI.openGL;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OpenGLMain implements GLEventListener {

    private static FPSAnimator animator;
    private static int width;
    private static int height;
    private static GL3 gl;
    public static Rectangle screenSize;
    public static JFrame frame;

    public static void main(String[] args) {
        GLProfile glprofile = GLProfile.getMaximum(true);
        GLCapabilities capabilities = new GLCapabilities(glprofile);
        GLCanvas canvas = new GLCanvas(capabilities);

        screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();

        frame = new JFrame("Frame name");
        frame.setAlwaysOnTop(false);
        frame.setSize(width, height);
        frame.add(canvas);
        frame.setUndecorated(true);
        frame.setVisible(true);

        animator = new FPSAnimator(25);
        animator.add(canvas);
        animator.start();

        canvas.addGLEventListener(new OpenGLMain());
        canvas.requestFocus();


        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void display(GLAutoDrawable drawable) {
        update();
        //while (animation){
          //  update the data with the animation;
            render(drawable);

        //render(drawable);
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL3();

    }

    public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
    }

    private void update() {
       // set stuff
         //       launch animation
    }

    private void render(GLAutoDrawable drawable) {
       // Scenes.render(drawable);
    }

}
