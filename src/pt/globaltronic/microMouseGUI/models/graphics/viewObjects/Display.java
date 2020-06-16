package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import javax.swing.*;
import java.awt.*;

public class Display {

    private JFrame frame;
    private JPanel panel;
    private Canvas canvas;

    private String title;
    private int width;
    private int height;

    public Display(JPanel panel, String title, int width, int height){
        this.title = title;
        this.panel = panel;
        this.width = width;
        this.height= height;
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));

        this.panel.add(canvas);
        //createDisplay();
    }

    private void createDisplay(){
        //frame = new JFrame(title);
        //frame.setSize(width, height);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        //frame.setLocationRelativeTo(null);
        //frame.setVisible(true);


        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));

        panel.add(canvas);
        //frame.add(canvas);
        //frame.pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
