package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Vector;

public class Replays {
    private LinkedHashSet<File> replaysSet;
    private String filePath = "resources/backup";
    private Vector<File> replaysSetVector;

    public Replays(){
        replaysSet = new LinkedHashSet<File>();
        replaysSetVector = new Vector<File>();
    }

    public void lookForReplays(){
        boolean running = true;
        int i = 0;

        while (running){
            File backupFile = new File(filePath + ++i + ".txt");
            if(!backupFile.isFile()){
                return;
            }
            replaysSet.add(backupFile);
            replaysSetVector.add(backupFile);
        }
    }

    public boolean removeFile(File file){
        replaysSet.remove(file);
        if(file.delete()){
            return true;
        }
        return false;
    }

    public LinkedHashSet<File> getReplaysSet() {
        return replaysSet;
    }

    public Vector<File> getReplaysSetVector(){
        return replaysSetVector;
    }
}
