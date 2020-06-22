package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Polygon2D;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Replays;
import pt.globaltronic.microMouseGUI.views.WelcomeView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class mainTest {

    public static void main(String[] args) {
        JFrame frame;
        JMenu jmenu;
        JMenuItem menuItem;
        JMenuBar menuBar;


        frame = new JFrame("Notepad");

        menuBar = new JMenuBar();
        menuBar.setVisible(true);
        jmenu = new JMenu("Test");
        menuItem = new JMenuItem("Open");

        jmenu.add(menuItem);



        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(660, 350);

        // Set a main menu
        frame.setJMenuBar(menuBar);
        menuBar.add(jmenu);

        frame.setVisible(true);
    }


}
