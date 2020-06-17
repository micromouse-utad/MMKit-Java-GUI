package pt.globaltronic.microMouseGUI.views;

import pt.globaltronic.microMouseGUI.Engine2D;
import pt.globaltronic.microMouseGUI.Engine3D;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;

import javax.swing.*;
import java.awt.*;

public class DisplayView extends JFrame {
    private JPanel mainPanel;
    private JPanel Panel2D;
    private JPanel Panel3D;

    public DisplayView(MouseInputs mouseInputs){

        mainPanel.setLayout(new GridLayout(1, 2, 1, 0));

        Panel2D.setLayout(new GridLayout(1, 1));
        Panel3D.setLayout(new GridLayout(1,1, 0, 0));
        this.setUndecorated(true);
        //setResizable(true);

        setPreferredSize(new Dimension(16*30*2, 16*30));
        setMinimumSize(new Dimension(16*30*2, 16*30));
        setMaximumSize(new Dimension(16*30*2, 16*30));

        mainPanel.setPreferredSize(new Dimension(16*30*2, 16*30));
        mainPanel.setMinimumSize(new Dimension(16*30*2, 16*30));
        mainPanel.setMaximumSize(new Dimension(16*30*2, 16*30));

        add(mainPanel);
        setTitle("Welcome to MicroMouse Views");
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        setVisible(true);

        Engine3D engine3D = new Engine3D(Panel3D,"Mouse 3d Trials", mouseInputs, 16, 16, 30, 10);
        Engine2D engine2D = new Engine2D(Panel2D, "Mouse Trial", mouseInputs, 16,16, 30);
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        setVisible(true);
        engine3D.start();
        engine2D.start();
    }
}
