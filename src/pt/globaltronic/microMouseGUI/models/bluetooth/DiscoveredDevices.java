package pt.globaltronic.microMouseGUI.models.bluetooth;

import java.util.LinkedHashSet;

public class DiscoveredDevices {

    LinkedHashSet<BluetoothDevice> discoveredDevicesSet;

    public void setDiscoveredDevicesSet(LinkedHashSet<BluetoothDevice> discoveredDevicesSet) {
        this.discoveredDevicesSet = discoveredDevicesSet;
    }

    public LinkedHashSet<BluetoothDevice> getDiscoveredDevicesSet() {
        return discoveredDevicesSet;
    }
}
