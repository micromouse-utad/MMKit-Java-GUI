package pt.globaltronic.microMouseGUI.views;

import pt.globaltronic.microMouseGUI.controllers.DisplayViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayView extends JFrame {
    private JPanel mainPanel;
    private JPanel Panel2D;
    private JPanel Panel3D;
    private JPanel footer;
    private JLabel label3D;
    private JLabel label2D;
    private JLabel spacing;
    private JMenuBar MenuBar;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;
    private JMenuItem Disconnect;
    private JMenuItem Replay;
    private JMenuItem Save;
    private JMenuItem FirstPerson;
    private JMenuItem Roaming;
    private JMenuItem TopDownView;
    private JMenuItem Help;

    private DisplayViewController controller;

    public DisplayView(BluetoothDevice currentDevice, DisplayViewController controller){

        this.controller = controller;
        mainPanel.setLayout(new GridBagLayout());
        MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);
        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        Disconnect = new JMenuItem("Disconnect");
        Replay = new JMenuItem("Replay");
        Save = new JMenuItem("Save");

        FirstPerson = new JMenuItem("First Person");
        Roaming = new JMenuItem("Free Roaming");
        TopDownView = new JMenuItem("Top Down");

        Help = new JMenuItem("Help");

        fileMenu.add(Disconnect);
        fileMenu.add(Replay);
        fileMenu.add(Save);

        viewMenu.add(FirstPerson);
        viewMenu.add(Roaming);
        viewMenu.add(TopDownView);

        helpMenu.add(Help);

        MenuBar.add(fileMenu);
        MenuBar.add(viewMenu);
        MenuBar.add(helpMenu);

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

        label3D.setText("3D View of " + (currentDevice.getName() != ""?currentDevice.getName(): "replay"));
        label3D.setFont(new Font("Serif", Font.PLAIN, 16));
        label2D.setText("Top down 2D View of " + currentDevice.getName());
        label2D.setFont(new Font("Serif", Font.PLAIN, 16));
        spacing.setText("                                                                              ");
        spacing.setFont(new Font("Serif", Font.PLAIN, 16));

        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        setVisible(true);

        controller.startEngines(Panel3D, Panel2D, mainPanel);


        Roaming.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.freeRoamMode();
            }
        });

        FirstPerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.firstPersonMode();
            }
        });

        TopDownView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.topDownMode();
            }
        });

        Help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, "The display will continue to draw until the device is disconnected \n\n" +
                        "While running you can switch camera styles by clicking on the View menu \n"+
                        '"'+ "Roaming View"+'"'+" use keys WASD to move and hold right-click to cursor pan\n" +
                        '"'+ "First Person View" +'"' + " default view that follows the MicroMouse around\n" +
                        '"'+ "TopDown View" +'"' + " to replicate a 2d view\n\n" +
                        "Use the " + '"' + "Disconnect option in File" + '"' +" to shut down the feed\n" +
                        "Use the " + '"' + "Replay option in File" +'"'+" button once disconnected to re-trace the steps of the mouse\n" +
                        "Use the " + '"' + "Save option in File"+ '"' + " to create a file containing the MicroMouse outputs of this run");

            }
        });

        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.isDisconnected()) {
                    JOptionPane.showMessageDialog(rootPane, "You need to disconnect from the live feed before initiating save sequence");
                }
                int x;
                if(controller.isDisconnected()) {
                    if ((x = controller.backupRunToFile()) > -1) {
                        JOptionPane.showMessageDialog(rootPane, "Your run was succesfully saved to the backup" +x+".txt file int he resources folder of this app, please make a copy");
                    }
                }
            }
        });

        Replay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controller.isDisconnected()) {
                    JOptionPane.showMessageDialog(rootPane, "You need to disconnect from the live feed before initiating replay sequence");
                }
                if(controller.isDisconnected()) {
                    controller.replay();
                }
            }
        });

        Disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.disconnect();
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
