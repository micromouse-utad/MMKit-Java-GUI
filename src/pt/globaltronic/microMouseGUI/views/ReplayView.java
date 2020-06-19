package pt.globaltronic.microMouseGUI.views;

import pt.globaltronic.microMouseGUI.controllers.DisplayViewController;
import pt.globaltronic.microMouseGUI.controllers.ReplayViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ReplayView extends JFrame {
    private JPanel mainPanel;
    private JPanel Panel2D;
    private JPanel Panel3D;
    private JPanel footer;
    private JButton roamingView;
    private JButton help;
    private JButton disconnect;
    private JButton firstPersonView;
    private JButton replay;
    private JButton TopDown;
    private JLabel label3D;
    private JLabel label2D;

    private ReplayViewController controller;

    public ReplayView(File file, ReplayViewController controller){

        this.controller = controller;
        mainPanel.setLayout(new GridBagLayout());

        Panel2D.setLayout(new GridLayout(1, 1));
        Panel3D.setLayout(new GridLayout(1,1, 0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(Panel3D, c);
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(Panel2D, c);
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        mainPanel.add(footer, c);

        this.setResizable(false);

        mainPanel.setPreferredSize(new Dimension(16*30*2 + 10, 16*30 + 50));
        mainPanel.setMinimumSize(new Dimension(16*30*2, 16*30));
        mainPanel.setMaximumSize(new Dimension(16*30*2, 16*30));


        add(mainPanel);
        setTitle("Welcome to MicroMouse Views");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label3D.setText("3D View of " + (file.getName() != ""?file.getName(): "replay"));
        label2D.setText("Top down 2D View of " + (file.getName() != ""?file.getName(): "replay"));
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        setVisible(true);

        controller.startEngines(Panel3D, Panel2D, mainPanel);


        roamingView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.freeRoamMode();
            }
        });

        firstPersonView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.firstPersonMode();
            }
        });

        TopDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.topDownMode();
            }
        });

        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.disconnect();
            }
        });

        replay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restartReplay();
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, "The display will continue to draw until the device is disconnected \n\n" +
                        "While running you can switch camera styles by clicking on the buttons \n"+
                        '"'+ "Roaming View"+'"'+" use keys WASD to move and hold right-click to cursor pan\n" +
                        '"'+ "First Person View" +'"' + " default view that follows the MicroMouse around\n" +
                        '"'+ "TopDown View" +'"' + " to replicate a 2d view\n\n" +
                        "Use the " + '"' + "End / Disconnect button" + '"' +" to shut down the feed\n" +
                        "Use the " + '"' + "Off-line replay" +'"'+" button once disconnected to re-trace the steps of the mouse\n"
                        );

            }
        });
    }

    public JPanel getPanel3D() {
        return Panel3D;
    }

    public JPanel getPanel2D() {
        return Panel2D;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}

