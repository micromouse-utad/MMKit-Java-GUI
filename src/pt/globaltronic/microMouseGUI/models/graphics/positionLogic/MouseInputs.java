package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import java.util.LinkedList;
import java.util.Queue;

public class MouseInputs {

    LinkedList<String> mouseInputList;
    Queue<String> mouseInputQueue;

    public MouseInputs(){
        mouseInputList = new LinkedList<String>();
        mouseInputQueue = new LinkedList<String>();
    }

    public LinkedList<String> getMouseInputList() {
        return mouseInputList;
    }

    public synchronized String getMouseInput(){
        while(mouseInputQueue.isEmpty()){
            try{
                wait();
            }catch (Exception ex) {}
        }
        notifyAll();
        return mouseInputQueue.poll();
    }

    public synchronized void putMouseInput(String mouseInput){
        mouseInputQueue.offer(mouseInput);
        mouseInputList.offer(mouseInput);
        notifyAll();
    }

    public Queue<String> getMouseInputQueue() {
        return mouseInputQueue;
    }
}
