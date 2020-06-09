package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.DiscoveredDevices;
import pt.globaltronic.microMouseGUI.models.bluetooth.SyncedDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothConnection;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothConnectionImp;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDiscovery;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDiscoveryImp;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;

import javax.microedition.io.StreamConnection;
import java.util.LinkedHashSet;

public class WelcomeViewController {

    BluetoothDiscovery bluetoothDiscovery;
    BluetoothConnection bluetoothConnection;
    DiscoveredDevices discoveredDevices;
    SyncedDevice syncedDevice;
    MouseInputs mouseInputs;



    public WelcomeViewController(MouseInputs mouseInputs){
        this.bluetoothDiscovery = new BluetoothDiscoveryImp();
        this.bluetoothConnection = new BluetoothConnectionImp();
        this.discoveredDevices = new DiscoveredDevices();
        this.syncedDevice = new SyncedDevice();
        this.mouseInputs = mouseInputs;


    }

    public LinkedHashSet<BluetoothDevice> getDiscoveredDeviceSet(){
        discoveredDevices.setDiscoveredDevicesSet(bluetoothDiscovery.scanForDevices());
        return discoveredDevices.getDiscoveredDevicesSet();
    }

    public LinkedHashSet<BluetoothDevice> getSyncedDeviceSet(){
        syncedDevice.syncedDevicesFromFile();
        return syncedDevice.getSyncedDeviceSet();
    }

    public StreamConnection connectToDevice(BluetoothDevice remoteDevice){
        syncedDevice.addDeviceToSyncedSet(remoteDevice);
        syncedDevice.writeSyncedToFile();
        return bluetoothConnection.connectToDevice(remoteDevice);
        //probably will have to do some verrification of the connection status.
        //display that the connection was succesful.
        //an ok box from the view
        //and then dispose() of the view and go to the graphical part of the mouse.
    }

    public void removedSelectedFromSyncedDevices(BluetoothDevice device){
        syncedDevice.removeDeviceFromSyncedSet(device);
        syncedDevice.writeSyncedToFile();
    }

    public MouseInputs getMouseInputs() {
        return mouseInputs;
    }
}
