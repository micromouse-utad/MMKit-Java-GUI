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
    private JLabel label3D;
    private JLabel label2D;
    private JLabel spacing;
    private JMenuBar MenuBar;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;
    private JMenuItem Disconnect;
    private JMenuItem Replay;
    private JMenuItem FirstPerson;
    private JMenuItem Roaming;
    private JMenuItem TopDownView;
    private JMenuItem Help;

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

        MenuBar = new JMenuBar();
        this.setJMenuBar(MenuBar);
        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        Disconnect = new JMenuItem("End");
        Replay = new JMenuItem("Replay");

        FirstPerson = new JMenuItem("First Person");
        Roaming = new JMenuItem("Free Roaming");
        TopDownView = new JMenuItem("Top Down");

        Help = new JMenuItem("Help");

        fileMenu.add(Disconnect);
        fileMenu.add(Replay);

        viewMenu.add(FirstPerson);
        viewMenu.add(Roaming);
        viewMenu.add(TopDownView);

        helpMenu.add(Help);

        MenuBar.add(fileMenu);
        MenuBar.add(viewMenu);
        MenuBar.add(helpMenu);

        label3D.setText("3D View of " + (file.getName() != ""?file.getName(): "replay"));
        label2D.setText("Top down 2D View of " + (file.getName() != ""?file.getName(): "replay"));
        label3D.setFont(new Font("Serif", Font.PLAIN, 16));
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

        Disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.disconnect();
            }
        });

        Replay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restartReplay();
            }
        });

        Help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, "The display will continue to draw until the feed is ended \n\n" +
                        "While running you can switch camera styles by clicking on the view menus \n"+
                        '"'+ "Roaming View"+'"'+" use keys WASD to move and hold right-click to cursor pan\n" +
                        '"'+ "First Person View" +'"' + " default view that follows the MicroMouse around\n" +
                        '"'+ "TopDown View" +'"' + " to replicate a 2d view\n\n" +
                        "Use the " + '"' + "File menu End option" + '"' +" to shut down the feed\n" +
                        "Use the " + '"' + "File menu Replay" +'"'+" button to restart the replay\n"
                        );

            }
        });
    }

    public void formatError() {
        JOptionPane.showMessageDialog(rootPane, "The replay you selected caused an error, its formatting may not be to convention");
        System.exit(-1);
    }

}

