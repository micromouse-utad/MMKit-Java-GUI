package pt.globaltronic.microMouseGUI.models.bluetooth;

public class BluetoothDevice {

    private String name;
    private String URL;

    //overriding equals and hashcode to be able to keep them in hashsets
    @Override
    public boolean equals(Object device) {
        if(device == null){
            return false;
        }
        BluetoothDevice ph = (BluetoothDevice) device;
        if(this.URL.equals(ph.URL)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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

