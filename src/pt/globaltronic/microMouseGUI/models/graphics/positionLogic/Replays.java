package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Vector;

public class Replays {
    private LinkedHashSet<File> replaysSet;
    private String filePath = "resources/backups/";
    private Vector<File> replaysSetVector;

    public Replays(){
        replaysSet = new LinkedHashSet<File>();
        replaysSetVector = new Vector<File>();
    }

    public void lookForReplays(){
        File file = new File(filePath);
        if (!file.isDirectory()) {
            System.out.println("Error with path to backups, path does not point to a directory");
            return;
        }
        File[] arr = file.listFiles();
        // a little crude, but it will look for replays from 1 to 200
        for(int i = 0; i < arr.length; ++i){
            File backupFile = arr[i];
            if(!backupFile.isFile()){
                continue;
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
