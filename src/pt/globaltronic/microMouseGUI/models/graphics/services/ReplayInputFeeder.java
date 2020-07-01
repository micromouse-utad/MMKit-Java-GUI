package pt.globaltronic.microMouseGUI.models.graphics.services;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import java.util.Queue;

public class ReplayInputFeeder implements Runnable {
    //class to simulate a microMouse to send mouseInputs from a replay every 3sec.
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
                    String line = replayHistory.poll();
                    mouseInputs.replayPut(line);
                    System.out.println(line);
                    Thread.sleep(3000);
                }catch (InterruptedException ex){
                    System.out.println(ex.getMessage());
                }
            }
            try {
                Thread.sleep(50);
            }catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public synchronized void start(){
        if(running){return;}

        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void reStart(Queue<String> replay){
        replayHistory = replay;
    }

    public synchronized void stop(){
        replayHistory.clear();
    }
}
