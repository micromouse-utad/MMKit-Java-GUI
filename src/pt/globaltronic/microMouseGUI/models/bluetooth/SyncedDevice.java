package pt.globaltronic.microMouseGUI.models.bluetooth;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;

import java.io.*;
import java.util.LinkedHashSet;


public class SyncedDevice {

    LinkedHashSet<BluetoothDevice> syncedDeviceSet;
    String filePath = "resources\\synced.txt";

    public SyncedDevice(){
        this.syncedDeviceSet = new LinkedHashSet<BluetoothDevice>();
    }

    public LinkedHashSet<BluetoothDevice> getSyncedDeviceSet() {
        return syncedDeviceSet;
    }

    public void addDeviceToSyncedSet (BluetoothDevice device){
        syncedDeviceSet.add(device);
    }

    public void removeDeviceFromSyncedSet (BluetoothDevice device){
        syncedDeviceSet.remove(device);
    }

    public void syncedDevicesFromFile(){
        syncedDeviceSet.clear();
        try{
            String line;
            BufferedReader bReader = new BufferedReader(new FileReader(filePath));
            while ((line = bReader.readLine()) != null){

                BluetoothDevice device = new BluetoothDevice();

                String[] stringParts = line.split("%%%");
                device.setName(stringParts[0]);
                device.setURL(stringParts[1]);

                syncedDeviceSet.add(device);

            }
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }

    public void writeSyncedToFile(){
        try {
            PrintWriter pWriter = new PrintWriter(filePath);
            syncedDeviceSet.forEach((device) -> {
                pWriter.println(device.getName() + "%%%" + device.getURL());
                pWriter.flush();
            });
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }


}
