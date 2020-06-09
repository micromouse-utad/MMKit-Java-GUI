package pt.globaltronic.microMouseGUI.models.bluetooth.services;

public class BluetoothURLBuilder {

    public static String buildURL(String deviceBluetoothAddress, int port){
        return "btspp://" + deviceBluetoothAddress + ":" + port +";";
    }
}
