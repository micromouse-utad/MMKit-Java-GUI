package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;

import javax.swing.*;
import java.awt.*;

public class testMain {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static JTextField TF;

    public static void main(String[] args) {
        JFrame F = new JFrame();
        Screen ScreenObject = new Screen(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 15);
        F.add(ScreenObject);
        F.setUndecorated(true);
        F.setSize(ScreenSize);
        F.setVisible(true);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
