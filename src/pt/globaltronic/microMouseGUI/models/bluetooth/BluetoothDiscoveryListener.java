package pt.globaltronic.microMouseGUI.models.bluetooth;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.io.IOException;
import java.util.LinkedHashSet;

public class BluetoothDiscoveryListener implements DiscoveryListener {

    private LinkedHashSet<BluetoothDevice> devicesDiscovered;
    private Object inquiryCompletedEvent;

    public BluetoothDiscoveryListener(Object inquiryCompletedEvent, LinkedHashSet<BluetoothDevice> devicesDiscovered){
        this.inquiryCompletedEvent = inquiryCompletedEvent;
        this.devicesDiscovered = devicesDiscovered;

    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        System.out.println("Device " + remoteDevice.getBluetoothAddress() + " found");
        BluetoothDevice device = new BluetoothDevice();
        try{
            device.setName(remoteDevice.getFriendlyName(false));
            device.setURL(remoteDevice.getBluetoothAddress());
            devicesDiscovered.add(device);

            System.out.println("     name " + remoteDevice.getFriendlyName(false));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

    }

    @Override
    public void serviceSearchCompleted(int i, int i1) {

    }

    @Override
    public void inquiryCompleted(int i) {
        System.out.println("Device Inquiry completed!");
        synchronized(inquiryCompletedEvent){
            inquiryCompletedEvent.notifyAll();
        }
    }
}
