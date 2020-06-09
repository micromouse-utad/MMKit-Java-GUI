package pt.globaltronic.microMouseGUI.models.bluetooth;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import java.io.IOException;
import java.util.LinkedHashSet;

public class BluetoothDiscoveryImp implements BluetoothDiscovery {

    LinkedHashSet<BluetoothDevice> devicesDiscovered;
    final Object inquiryCompletedEvent;

    public BluetoothDiscoveryImp(){
        devicesDiscovered = new LinkedHashSet<BluetoothDevice>();
        inquiryCompletedEvent = new Object();
    }

    @Override
    public LinkedHashSet<BluetoothDevice> scanForDevices(){
        //clearing devicesDiscovered Vector data, in case the scan had previously been performed
        devicesDiscovered.clear();
        BluetoothDiscoveryListener listener = new BluetoothDiscoveryListener(inquiryCompletedEvent, devicesDiscovered);

        try {
            synchronized (inquiryCompletedEvent) {
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    System.out.println("Searching for devices please wait...");
                    inquiryCompletedEvent.wait();
                    System.out.println(devicesDiscovered.size() + " device(s) found");
                }
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        return devicesDiscovered;
    }
}
