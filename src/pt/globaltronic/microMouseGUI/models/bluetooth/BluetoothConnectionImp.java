package pt.globaltronic.microMouseGUI.models.bluetooth;

import pt.globaltronic.microMouseGUI.models.bluetooth.services.BluetoothURLBuilder;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;

public class BluetoothConnectionImp implements BluetoothConnection {

    @Override
    public StreamConnection connectToDevice(BluetoothDevice remoteDevice){
        StreamConnection conn = null;
        try {

            //setting up the connection
            String deviceURL = remoteDevice.getURL();
            String URL = BluetoothURLBuilder.buildURL(deviceURL, 1);
            conn = (StreamConnection) Connector.open(URL);

            /*
            //setting up the usual input - output streams, use printWriter don't forget to flush


            InputStream in = conn.openInputStream();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));


            // START OF TRIAL CODE, ERASE BEFORE GOING TO PROD
            String line = null;

            while(true){
                line = bufferedReader.readLine();
                System.out.println(line);
            }


             */
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        return conn;
    }
}
