package pt.globaltronic.microMouseGUI.models.bluetooth;

import java.util.LinkedHashSet;

public interface BluetoothDiscovery {

    LinkedHashSet<BluetoothDevice> scanForDevices();

}
