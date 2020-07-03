package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.controllers.DisplayViewController;
import pt.globaltronic.microMouseGUI.controllers.ReplayViewController;
import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.*;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Replays;

import java.io.File;
import java.io.IOException;

public class Wiring {

    public void bootstrap(WelcomeViewController welcomeViewController, DisplayViewController displayViewController, ReplayViewController replayViewController) {
        BluetoothDiscovery bluetoothDiscovery = new BluetoothDiscoveryImp();
        BluetoothConnection bluetoothConnection = new BluetoothConnectionImp();
        DiscoveredDevices discoveredDevices = new DiscoveredDevices();
        SyncedDevice syncedDevice = new SyncedDevice();
        Replays replays = new Replays();
        MouseInputs mouseInputs = new MouseInputs();

        welcomeViewController.setBluetoothDiscovery(bluetoothDiscovery);
        welcomeViewController.setBluetoothConnection(bluetoothConnection);
        welcomeViewController.setDiscoveredDevices(discoveredDevices);
        welcomeViewController.setSyncedDevice(syncedDevice);
        welcomeViewController.setReplays(replays);
        welcomeViewController.setMouseInputs(mouseInputs);
        welcomeViewController.setDisplayViewController(displayViewController);
        welcomeViewController.setReplayViewController(replayViewController);

        displayViewController.setMouseInputs(mouseInputs);
        replayViewController.setMouseInputs(mouseInputs);

    }

    public void fileSystem() {
        File file = new File ("microMouseFiles");
        if(!file.exists()) {
            if(!file.mkdir()){
                System.out.println("cannot create microMouseFiles Folder, this folder is needed for the app to sava data");
            }
        }
        File backups = new File("microMouseFiles/backups");
        if(!backups.exists()) {
            backups.mkdir();
        }
        File synced = new File("microMouseFiles/synced.txt");
        try {
            synced.createNewFile();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
