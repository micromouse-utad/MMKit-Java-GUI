package pt.globaltronic.microMouseGUI.models.bluetooth;

import javax.microedition.io.StreamConnection;

public interface BluetoothConnection {

    StreamConnection connectToDevice(BluetoothDevice remoteDevice);

}
