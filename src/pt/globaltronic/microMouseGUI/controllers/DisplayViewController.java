package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.Engine2D;
import pt.globaltronic.microMouseGUI.Engine3D;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputsReceiver;
import pt.globaltronic.microMouseGUI.models.graphics.services.ReplayInputFeeder;
import pt.globaltronic.microMouseGUI.openGL.OpenGLEngine;
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

    public DisplayViewController(){
    }

    public void startView(BluetoothDevice selectedDevice){
        SwingUtilities.invokeLater(() -> {
            displayView = new DisplayView(selectedDevice, this);
            displayView.setVisible(true);
        });
    }

    public void startEngines(JPanel Panel3D, JPanel Panel2D, JPanel mainPanel){

        openGLEngine = new OpenGLEngine(Panel3D, mouseInputs, 16, 16, 10, 30);
        engine2D = new Engine2D(Panel2D, "Mouse Trial", mouseInputs, 16,16, 30);
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        openGLEngine.start();
        engine2D.start();
    }

    public void disconnect(){
        if (connection != null) {
            try {
                receiver.stop();
                connection.close();
                disconnected = true;
                if(replayInputFeeder != null){
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
        /*
        engine3D.getScreen().setVertLook(-0.999);
        engine3D.getScreen().setHorLook(3*Math.PI/2);
        engine3D.getScreen().setViewFrom(new double[]{engine3D.getCols() * engine3D.getSize() / 2, ((engine3D.getRows() * engine3D.getSize()) + engine3D.getScreen().getCorrection()) / 2, 350});

         */

    }


    public void replay() {
        if(!replayed) {
            History = new LinkedList<String>(mouseInputs.getMouseInputHistory());
            replayInputFeeder = new ReplayInputFeeder(mouseInputs, History);
            engine2D.replay();
            replayed = true;
            replayInputFeeder.start();
        }
        if(replayed){
            History = new LinkedList<String>(mouseInputs.getMouseInputHistory());
            engine2D.reReplay();
            openGLEngine.replay();
            replayInputFeeder.reStart(History);
        }
    }

    public boolean isDisconnected(){
        return disconnected;
    }

    public int backupRunToFile(){
        History = new LinkedList<String>(mouseInputs.getMouseInputHistory());
        String pathWithoutTxt = "resources/backups/backup";
        boolean created = false;
        int i = 1;
        File backupFile = null;
        while (!created) {
            backupFile = new File(pathWithoutTxt + i++ + ".txt");
            try{
                if (tryToCreateFile(backupFile)) {
                    created = true;
                }
            } catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
        try {
            PrintWriter pWriter = new PrintWriter(backupFile.getAbsoluteFile());
            History.forEach((input) -> {
                pWriter.println(input);
                pWriter.flush();
            });
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return i-1;
    }

    public boolean tryToCreateFile(File file) throws IOException{
        return file.createNewFile();
    }
}
