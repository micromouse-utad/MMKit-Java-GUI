package pt.globaltronic.microMouseGUI.controllers;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.DiscoveredDevices;
import pt.globaltronic.microMouseGUI.models.bluetooth.SyncedDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothConnection;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDiscovery;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputsReceiver;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Replays;

import javax.microedition.io.StreamConnection;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Vector;

public class WelcomeViewController {

    private BluetoothDiscovery bluetoothDiscovery;
    private BluetoothConnection bluetoothConnection;
    private DiscoveredDevices discoveredDevices;
    private SyncedDevice syncedDevice;
    private MouseInputs mouseInputs;
    private DisplayViewController displayViewController;
    private ReplayViewController replayViewController;
    private Replays replays;

    public WelcomeViewController(){}

    public LinkedHashSet<BluetoothDevice> getDiscoveredDeviceSet(){
        discoveredDevices.setDiscoveredDevicesSet(bluetoothDiscovery.scanForDevices());
        return discoveredDevices.getDiscoveredDevicesSet();
    }

    public LinkedHashSet<BluetoothDevice> getSyncedDeviceSet(){
        syncedDevice.syncedDevicesFromFile();
        return syncedDevice.getSyncedDeviceSet();
    }

    public LinkedHashSet<File> getReplaysSet(){
        replays.lookForReplays();
        return replays.getReplaysSet();
    }

    public Vector<String> getReplaysStringVector(){
        replays.lookForReplays();
        Vector<String> replayNames = new Vector<String>();
        LinkedHashSet<File> replaysSet = replays.getReplaysSet();

        replaysSet.forEach(file -> replayNames.addElement(file.getName()));

        return replayNames;
    }

    public void removeReplayFile(File replay){
        replays.removeFile(replay);
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

    public void startDisplayView(StreamConnection connection, boolean only3D, BluetoothDevice selectedDevice, int cols, int rows){
        MouseInputsReceiver receiver = new MouseInputsReceiver(mouseInputs, connection);
        receiver.start();
        displayViewController.setConnection(connection);
        displayViewController.setMouseInputsReceiver(receiver);
        displayViewController.setCols(cols);
        displayViewController.setRows(rows);
        if(only3D){
            displayViewController.start3DonlyView(selectedDevice);
            return;
        }
        displayViewController.startView(selectedDevice);
    }

    public void startReplayView(File selectedReplay, boolean only3D, int cols, int rows){
        replayViewController.setCols(cols);
        replayViewController.setRows(rows);
        if(only3D){
            replayViewController.start3DonlyView(selectedReplay);
            return;
        }
        replayViewController.startView(selectedReplay);
    }

    public void removedSelectedFromSyncedDevices(BluetoothDevice device){
        syncedDevice.removeDeviceFromSyncedSet(device);
        syncedDevice.writeSyncedToFile();
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

    public void setReplays(Replays replays) {
        this.replays = replays;
    }

    public void setReplayViewController(ReplayViewController replayViewController){
        this.replayViewController = replayViewController;
    }

}
