package pt.globaltronic.microMouseGUI.models.bluetooth;

import java.util.LinkedHashSet;

public interface BluetoothDiscovery {

    public LinkedHashSet<BluetoothDevice> scanForDevices();

}
