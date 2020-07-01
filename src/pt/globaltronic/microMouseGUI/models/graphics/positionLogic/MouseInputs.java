package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import java.util.LinkedList;
import java.util.Queue;

public class MouseInputs {
    //class to store inputs from the microMouse or a replay, each engine will use an independent queue, the 3d and the 2d
    //but the data they keep is identical
    //mouse history is used to generate replays and backups, it only receives inputs and is not accessed during run
    private Queue<String> mouseInputHistory;
    private Queue<String> mouseInputQueue3D;
    private Queue<String> mouseInputQueue;

    public MouseInputs(){
        mouseInputHistory = new LinkedList<String>();
        mouseInputQueue = new LinkedList<String>();
        mouseInputQueue3D = new LinkedList<String>();
    }

    public Queue<String> getMouseInputHistory() {
        return mouseInputHistory;
    }

    public String getMouseInput3D(){
        if(mouseInputQueue3D.isEmpty()) {
            return "";
        }
        return mouseInputQueue3D.poll();
    }

    public String getMouseInput(){
        if(mouseInputQueue.isEmpty()){
            return "";
        }
        return mouseInputQueue.poll();
    }

    public synchronized void putMouseInput(String mouseInput){
        mouseInputQueue.offer(mouseInput);
        mouseInputQueue3D.offer(mouseInput);
        mouseInputHistory.offer(mouseInput);
        notifyAll();
    }

    public void replayPut(String replayInput){
        mouseInputQueue3D.offer(replayInput);
        mouseInputQueue.offer(replayInput);
    }
}
