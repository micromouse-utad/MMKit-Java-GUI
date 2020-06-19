package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.Engine2D;
import pt.globaltronic.microMouseGUI.Engine3D;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.services.ReplayInputFeeder;
import pt.globaltronic.microMouseGUI.views.ReplayView;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class ReplayViewController {

    private ReplayInputFeeder replayInputFeeder;
    private MouseInputs mouseInputs;
    private ReplayView replayView;
    private Engine3D engine3D;
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
        engine3D = new Engine3D(Panel3D,"Mouse 3d Trials", mouseInputs, 16, 16, 30, 10);
        engine2D = new Engine2D(Panel2D, "Mouse Trial", mouseInputs, 16,16, 30);
        Panel3D.setVisible(true);
        Panel2D.setVisible(true);
        mainPanel.setVisible(true);
        engine3D.start();
        engine2D.start();
    }

    public void restartReplay(){
        Queue<String> replayInputs = getMouseInputsFromReplayFile(file);
        replayInputFeeder.reStart(replayInputs);
        replay();
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

    public void disconnect() {
        replayInputFeeder.stop();
    }

    public void replay() {
        if(!replayed) {
            engine3D.replay();
            engine2D.replay();
            replayed = true;
        }
        if(replayed){
            engine2D.reReplay();
            engine3D.reReplay();
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

}
