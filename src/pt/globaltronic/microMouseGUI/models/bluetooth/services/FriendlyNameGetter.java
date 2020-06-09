package pt.globaltronic.microMouseGUI.models.bluetooth.services;

import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;

import java.util.LinkedHashSet;
import java.util.Vector;

public class FriendlyNameGetter {

    public static Vector<String> getFriendlyName(LinkedHashSet<BluetoothDevice> bluetoothDevices){
        Vector friendlyNames = new Vector();

        bluetoothDevices.forEach((device) -> friendlyNames.add(device.getName()));

        return friendlyNames;
    }


}
