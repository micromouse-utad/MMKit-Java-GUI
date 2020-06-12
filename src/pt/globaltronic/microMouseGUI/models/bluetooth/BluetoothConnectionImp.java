package pt.globaltronic.microMouseGUI.models.bluetooth;

import pt.globaltronic.microMouseGUI.models.bluetooth.services.BluetoothURLBuilder;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


public class BluetoothConnectionImp implements BluetoothConnection {

    @Override
    public StreamConnection connectToDevice(BluetoothDevice remoteDevice){
        StreamConnection conn = null;
        try {

            //setting up the connection
            String deviceURL = remoteDevice.getURL();
            String URL = BluetoothURLBuilder.buildURL(deviceURL, 1);
            conn = (StreamConnection) Connector.open(URL);


        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return conn;
    }
}
