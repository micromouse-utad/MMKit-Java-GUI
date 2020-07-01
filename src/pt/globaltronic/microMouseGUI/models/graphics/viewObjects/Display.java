package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import javax.swing.*;
import java.awt.*;

public class Display {

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

    }

    public Canvas getCanvas() {
        return canvas;
    }
}
