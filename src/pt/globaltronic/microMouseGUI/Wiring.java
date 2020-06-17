package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.controllers.DisplayViewController;
import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.*;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;

public class Wiring {

    public void bootstrap(WelcomeViewController welcomeViewController, DisplayViewController displayViewController) {
        BluetoothDiscovery bluetoothDiscovery = new BluetoothDiscoveryImp();
        BluetoothConnection bluetoothConnection = new BluetoothConnectionImp();
        DiscoveredDevices discoveredDevices = new DiscoveredDevices();
        SyncedDevice syncedDevice = new SyncedDevice();
        MouseInputs mouseInputs = new MouseInputs();

        welcomeViewController.setBluetoothDiscovery(bluetoothDiscovery);
        welcomeViewController.setBluetoothConnection(bluetoothConnection);
        welcomeViewController.setDiscoveredDevices(discoveredDevices);
        welcomeViewController.setSyncedDevice(syncedDevice);
        welcomeViewController.setMouseInputs(mouseInputs);
        welcomeViewController.setDisplayViewController(displayViewController);

        displayViewController.setMouseInputs(mouseInputs);
    }
}
