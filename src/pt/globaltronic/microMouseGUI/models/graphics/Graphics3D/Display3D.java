package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import javax.swing.*;
import java.awt.*;

public class Display3D {

    private JFrame frame;
    private Screen screen;

    private String title;
    private double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private double height = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public Display3D(String title, int width, int height){
        this.title = title;
        createDisplay();
    }

    private void createDisplay(){
        frame = new JFrame(title);
        screen = new Screen(width, height);
        frame.add(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }

    public Screen getScreen() {
        return screen;
    }
}