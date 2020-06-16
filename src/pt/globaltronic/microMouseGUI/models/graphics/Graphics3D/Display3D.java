package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import javax.swing.*;
import java.awt.*;

public class Display3D {

    private JFrame frame;
    private JPanel panel;
    private Screen screen;

    private String title;
    private double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private double height = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public Display3D(JPanel panel, String title, int width, int height){
        this.panel = panel;
        this.title = title;
        this.width = width;
        this.height = height;
        createDisplay();
    }

    private void createDisplay(){
        //frame = new JFrame(title);
        //frame.setSize((int)width, (int)height);
        screen = new Screen(width, height);
        //frame.add(screen);
        panel.add(screen);
        panel.setPreferredSize(new Dimension(16*30, 16*30));
        panel.setMinimumSize(new Dimension(16*30, 16*30));
        panel.setMaximumSize(new Dimension(16*30, 16*30));
        //frame.setResizable(false);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true);
        panel.setVisible(true);
        //frame.setVisible(true);
    }

    public Screen getScreen() {
        return screen;
    }
}