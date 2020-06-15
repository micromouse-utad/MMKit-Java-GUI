package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import java.util.LinkedList;
import java.util.Queue;

public class MouseInputs {

    LinkedList<String> mouseInputList;
    Queue<String> mouseInputQueue3D;
    Queue<String> mouseInputQueue;

    public MouseInputs(){
        mouseInputList = new LinkedList<String>();
        mouseInputQueue = new LinkedList<String>();
        mouseInputQueue3D = new LinkedList<String>();
    }

    public LinkedList<String> getMouseInputList() {
        return mouseInputList;
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
        mouseInputList.offer(mouseInput);
        notifyAll();
    }

    public Queue<String> getMouseInputQueue() {
        return mouseInputQueue;
    }
}
