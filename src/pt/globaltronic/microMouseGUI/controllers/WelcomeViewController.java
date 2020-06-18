package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.DiscoveredDevices;
import pt.globaltronic.microMouseGUI.models.bluetooth.SyncedDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothConnection;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDiscovery;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputsReceiver;
import pt.globaltronic.microMouseGUI.views.DisplayView;

import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.util.LinkedHashSet;

public class WelcomeViewController {

    BluetoothDiscovery bluetoothDiscovery;
    BluetoothConnection bluetoothConnection;
    DiscoveredDevices discoveredDevices;
    SyncedDevice syncedDevice;
    MouseInputs mouseInputs;
    DisplayViewController displayViewController;

    public WelcomeViewController(){}

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
        StreamConnection conn = bluetoothConnection.connectToDevice(remoteDevice);
        if(conn == null){
            System.out.println("error, no connection was established");
        }
        System.out.println("connection established to:" + remoteDevice.getName());
        return conn;
    }

    public void startDisplayView(StreamConnection connection, BluetoothDevice selectedDevice){
        MouseInputs mouseInputs = this.getMouseInputs();
        MouseInputsReceiver receiver = new MouseInputsReceiver(mouseInputs, connection);
        receiver.start();
        displayViewController.setConnection(connection);
        displayViewController.setMouseInputsReceiver(receiver);
        displayViewController.startView(selectedDevice);
    }

    public void replayDisplayView(String filePath){

    }

    public void removedSelectedFromSyncedDevices(BluetoothDevice device){
        syncedDevice.removeDeviceFromSyncedSet(device);
        syncedDevice.writeSyncedToFile();
    }

    public MouseInputs getMouseInputs() {
        return mouseInputs;
    }

    public void setBluetoothConnection(BluetoothConnection bluetoothConnection) {
        this.bluetoothConnection = bluetoothConnection;
    }

    public void setBluetoothDiscovery(BluetoothDiscovery bluetoothDiscovery) {
        this.bluetoothDiscovery = bluetoothDiscovery;
    }

    public void setDiscoveredDevices(DiscoveredDevices discoveredDevices) {
        this.discoveredDevices = discoveredDevices;
    }

    public void setMouseInputs(MouseInputs mouseInputs) {
        this.mouseInputs = mouseInputs;
    }

    public void setSyncedDevice(SyncedDevice syncedDevice) {
        this.syncedDevice = syncedDevice;
    }

    public void setDisplayViewController(DisplayViewController displayViewController) {
        this.displayViewController = displayViewController;
    }

}
