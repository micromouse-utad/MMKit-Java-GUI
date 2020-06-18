package pt.globaltronic.microMouseGUI.models.graphics.services;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Mouse;

import java.util.Queue;

public class ReplayInputFeeder implements Runnable {

    private MouseInputs mouseInputs;
    private Queue<String> replayHistory;
    private boolean running;
    private Thread thread;

    public ReplayInputFeeder(MouseInputs mouseInputs, Queue<String> replayHistory){
        this.mouseInputs = mouseInputs;
        this.replayHistory = replayHistory;
    }

    @Override
    public void run() {
        while (running){
            if(!replayHistory.isEmpty()){
                try {
                    Thread.sleep(3000);
                    String line = replayHistory.poll();
                    mouseInputs.replayPut(line);
                    System.out.println(line);
                }catch (InterruptedException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public synchronized void start(){
        if(running){return;}

        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void reStart(Queue<String> history){
        replayHistory = history;
    }

    public synchronized void stop(){}
}
