package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.Engine2D;
import pt.globaltronic.microMouseGUI.Engine3D;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputsReceiver;
import pt.globaltronic.microMouseGUI.views.DisplayView;

import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.io.IOException;

public class DisplayViewController {

    private StreamConnection connection;
    private MouseInputsReceiver receiver;
    private DisplayView displayView;
    private MouseInputs mouseInputs;
    private Engine2D engine2D;
    private Engine3D engine3D;

    public DisplayViewController(){
    }

    public void startView(BluetoothDevice selectedDevice){
        SwingUtilities.invokeLater(() -> {
            displayView = new DisplayView(mouseInputs, selectedDevice, this);
            displayView.setVisible(true);
        });
    }

    public void startEngines(JPanel Panel3D, JPanel Panel2D, JPanel mainPanel){

        engine3D = new Engine3D(Panel3D,"Mouse 3d Trials", mouseInputs, 16, 16, 30, 10);
        engine2D = new Engine2D(Panel2D, "Mouse Trial", mouseInputs, 16,16, 30);
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        engine3D.start();
        engine2D.start();
    }

    public void disconnect(){
        if (connection != null) {
            try {
                receiver.stop();
                connection.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void setConnection(StreamConnection connection) {
        this.connection = connection;
    }

    public void setMouseInputs(MouseInputs mouseInputs) {
        this.mouseInputs = mouseInputs;
    }

    public void setMouseInputsReceiver(MouseInputsReceiver receiver) {
        this.receiver = receiver;
    }

    public void freeRoamMode() {
        engine3D.setFirstPersonView(false);
    }

    public void firstPersonMode() {
        engine3D.setFirstPersonView(true);
        engine3D.getScreen().setVertLook(-0.2);
    }

    public void topDownMode() {
        engine3D.setFirstPersonView(false);
        engine3D.getScreen().setVertLook(-0.999);
        engine3D.getScreen().setHorLook(3*Math.PI/2);
        engine3D.getScreen().setViewFrom(new double[]{engine3D.getCols() * engine3D.getSize() / 2, ((engine3D.getRows() * engine3D.getSize()) + engine3D.getScreen().getCorrection()) / 2, 350});
    }
}
