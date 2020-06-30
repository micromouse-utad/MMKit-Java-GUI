package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.Engine2D;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.services.ReplayInputFeeder;
import pt.globaltronic.microMouseGUI.OpenGLEngine;
import pt.globaltronic.microMouseGUI.views.ReplayView;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class ReplayViewController {

    private ReplayInputFeeder replayInputFeeder;
    private MouseInputs mouseInputs;
    private ReplayView replayView;
    private OpenGLEngine openGLEngine;
    private Engine2D engine2D;
    private File file;
    boolean replayed = false;

    public void startView(File selectedReplay) {
        this.file = selectedReplay;
        Queue<String> replayInputs = getMouseInputsFromReplayFile(selectedReplay);
        replayInputFeeder = new ReplayInputFeeder(mouseInputs, replayInputs);



        SwingUtilities.invokeLater(() -> {
            replayView = new ReplayView(selectedReplay, this);
            replayView.setVisible(true);
            replayInputFeeder.start();
        });

    }

    public Queue<String> getMouseInputsFromReplayFile(File file) {
        Queue<String> replayInputs = new LinkedList<String>();

        try {
            String line;
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            while ((line = bReader.readLine()) != null) {
                replayInputs.add(line);
            }
            bReader.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return replayInputs;
    }

    public void setMouseInputs(MouseInputs mouseInputs) {
        this.mouseInputs = mouseInputs;
    }

    public void startEngines(JPanel Panel3D, JPanel Panel2D, JPanel mainPanel) {
        openGLEngine = new OpenGLEngine(Panel3D, mouseInputs, 16, 16, 10, 30);
        engine2D = new Engine2D(Panel2D, "Mouse Trial", mouseInputs, 16,16, 30);
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        openGLEngine.start();
        engine2D.start();
    }

    public void restartReplay(){
        Queue<String> replayInputs = getMouseInputsFromReplayFile(file);
        replayInputFeeder.reStart(replayInputs);
        replay();
    }

    public void freeRoamMode() {
        openGLEngine.setFirstPersonView(false);
    }

    public void firstPersonMode() {
        openGLEngine.setFirstPersonView(true);
        //need to implement the view change
    }

    public void topDownMode() {
        openGLEngine.setFirstPersonView(false);
        /*
        engine3D.getScreen().setVertLook(-0.999);
        engine3D.getScreen().setHorLook(3*Math.PI/2);
        engine3D.getScreen().setViewFrom(new double[]{engine3D.getCols() * engine3D.getSize() / 2, ((engine3D.getRows() * engine3D.getSize()) + engine3D.getScreen().getCorrection()) / 2, 350});

         */
    }

    public void disconnect() {
        replayInputFeeder.stop();
    }

    public void replay() {
        if(!replayed) {
            engine2D.replay();
            replayed = true;
        }
        if(replayed){
            engine2D.reReplay();
            openGLEngine.replay();
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

}
