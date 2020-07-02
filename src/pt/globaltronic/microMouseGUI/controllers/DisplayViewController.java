package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.Engine2D;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputsReceiver;
import pt.globaltronic.microMouseGUI.models.graphics.services.ReplayInputFeeder;
import pt.globaltronic.microMouseGUI.OpenGLEngine;
import pt.globaltronic.microMouseGUI.views.DisplayView;

import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class DisplayViewController {

    private StreamConnection connection;
    private MouseInputsReceiver receiver;
    private DisplayView displayView;
    private MouseInputs mouseInputs;
    private Engine2D engine2D;
    private OpenGLEngine openGLEngine;
    private boolean disconnected = false;
    private Queue<String> History;
    private boolean replayed = false;
    private ReplayInputFeeder replayInputFeeder;
    private int cols;
    private int rows;

    public DisplayViewController() {
    }

    public void startView(BluetoothDevice selectedDevice) {
        SwingUtilities.invokeLater(() -> {
            displayView = new DisplayView(selectedDevice, this);
            displayView.setVisible(true);
        });
    }

    public void startEngines(JPanel Panel3D, JPanel Panel2D, JPanel mainPanel) {
        openGLEngine = new OpenGLEngine(Panel3D, mouseInputs, cols, rows, 10);
        engine2D = new Engine2D(Panel2D, "Mouse Trial", mouseInputs, cols, rows);
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        openGLEngine.start();
        engine2D.start();
    }

    public void disconnect() {
        if (connection != null) {
            try {
                receiver.stop();
                connection.close();
                disconnected = true;
                if (replayInputFeeder != null) {
                    replayInputFeeder.stop();
                }

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
        openGLEngine.setFirstPersonView(false);
    }

    public void firstPersonMode() {
        openGLEngine.setFirstPersonView(true);
    }

    public void topDownMode() {
        openGLEngine.setFirstPersonView(false);
        openGLEngine.setTopDownView();
    }

    public void replay() {
        if (!replayed) {
            History = new LinkedList<String>(mouseInputs.getMouseInputHistory());
            replayInputFeeder = new ReplayInputFeeder(mouseInputs, History);
            engine2D.replay();
            replayed = true;
            replayInputFeeder.start();
        }
        if (replayed) {
            History = new LinkedList<String>(mouseInputs.getMouseInputHistory());
            engine2D.reReplay();
            openGLEngine.replay();
            replayInputFeeder.reStart(History);
        }
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public String backupRunToFile(String name) {
        History = new LinkedList<String>(mouseInputs.getMouseInputHistory());
        String pathWithoutTxt = "resources/backups/";
        String fileName = pathWithoutTxt + name + "Grid" + cols + "x" + rows + ".txt";
        File backupFile = new File(fileName);
        String outputString = "";
        try {
            if (tryToCreateFile(backupFile)) {
                outputString = fileName;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            PrintWriter pWriter = new PrintWriter(backupFile.getAbsoluteFile());
            History.forEach((input) -> {
                pWriter.println(input);
                pWriter.flush();
            });
        } catch (
                IOException ex) {
            System.out.println(ex.getMessage());
        }
        return outputString;
    }

    public boolean tryToCreateFile(File file) throws IOException {
        return file.createNewFile();
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
