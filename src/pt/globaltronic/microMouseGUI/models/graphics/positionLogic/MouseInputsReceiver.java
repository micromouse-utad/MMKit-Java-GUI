package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import javax.microedition.io.StreamConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MouseInputsReceiver implements Runnable{

    private MouseInputs mouseInputs;
    private StreamConnection connection;
    private boolean running;
    private Thread thread;
    private BufferedReader bReader;

    public MouseInputsReceiver(MouseInputs mouseInputs, StreamConnection connection){
        this.mouseInputs = mouseInputs;
        this.connection = connection;
    }

    @Override
    public void run() {
        init();
        String line = null;
        while(running){
            try {
                line = bReader.readLine();
                if (line != null){
                    mouseInputs.putMouseInput(line);
                }
            }catch (IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public void init(){
        try {
            bReader = new BufferedReader(new InputStreamReader(connection.openInputStream()));
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public synchronized void start(){
        if(running){return;}

        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public synchronized void stop(){
        if (!running){return;}
        try{
            thread.join();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
