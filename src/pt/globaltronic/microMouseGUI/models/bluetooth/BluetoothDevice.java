package pt.globaltronic.microMouseGUI.models.bluetooth;

public class BluetoothDevice {

    String name;
    String URL;

    public boolean equals(BluetoothDevice device) {
        if(this.URL.equals(device.URL)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name + "%%%" + this.URL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }
}

