package pt.globaltronic.microMouseGUI.views;

import pt.globaltronic.microMouseGUI.controllers.DisplayViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class DisplayView extends JFrame {
    private JPanel mainPanel;
    private JPanel Panel2D;
    private JPanel Panel3D;
    private JMenuBar MenuBar;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;
    private JMenuItem Disconnect;
    private JMenuItem Replay;
    private JMenuItem Save;
    private JMenuItem Quit;
    private JMenuItem FirstPerson;
    private JMenuItem Roaming;
    private JMenuItem TopDownView;
    private JMenuItem Help;

    private DisplayViewController controller;

    public DisplayView(BluetoothDevice currentDevice, DisplayViewController controller){

        this.controller = controller;
        mainPanel.setLayout(new GridLayout(1, 2, 0,0));
        Box box = Box.createHorizontalBox();
        Panel2D.setLayout(new GridLayout(1, 1, 0,0));
        Panel3D.setLayout(new GridLayout(1,1, 0, 0));

        box.add(Panel3D);
        box.add(Panel2D);
        mainPanel.add(box);

        MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);
        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        Disconnect = new JMenuItem("Disconnect");
        Replay = new JMenuItem("Replay");
        Save = new JMenuItem("Save");
        Quit = new JMenuItem("Quit");

        FirstPerson = new JMenuItem("First Person");
        Roaming = new JMenuItem("Free Roaming");
        TopDownView = new JMenuItem("Top Down");

        Help = new JMenuItem("Help");

        fileMenu.add(Disconnect);
        fileMenu.add(Replay);
        fileMenu.add(Save);
        fileMenu.add(Quit);

        viewMenu.add(FirstPerson);
        viewMenu.add(Roaming);
        viewMenu.add(TopDownView);

        helpMenu.add(Help);

        MenuBar.add(fileMenu);
        MenuBar.add(viewMenu);
        MenuBar.add(helpMenu);

        this.setResizable(false);
        this.setUndecorated(true);

        Dimension fullScreen = Toolkit.getDefaultToolkit().getScreenSize();
        mainPanel.setPreferredSize(fullScreen);
        mainPanel.setMaximumSize(fullScreen);
        mainPanel.setMinimumSize(fullScreen);

        add(mainPanel);
        setTitle("Welcome to MicroMouse Views");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                if(controller.isDisconnected()) {
                    String backupName = JOptionPane.showInputDialog("Enter a backupName");
                    String fileName = controller.backupRunToFile(backupName);
                    if(fileName.equals("")) {
                        JOptionPane.showMessageDialog(rootPane, "Error backing up, try again with another file name");
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Your run was succesfully saved " + fileName + " file in the microMouseFiles/backups folder");
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

        Quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }
    public void close(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
