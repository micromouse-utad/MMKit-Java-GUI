package pt.globaltronic.microMouseGUI.views;

import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.services.FriendlyNameGetter;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputsReceiver;

import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;

public class WelcomeView extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel botLeftPanel;
    private JPanel botRightPanel;
    private JButton discoverButton;
    private JButton syncToKnownDeviceButton;
    private JList discoveredDeviceList;
    private JButton scanForDevicesButton;
    private JButton syncToSelectedButton;
    private JTabbedPane mainTabbedPane;
    private JButton connectToPreviouslySynced;
    private JButton deletedSelected;
    private JList syncedDevicesList;
    private JButton loadDevicesButton;

    private WelcomeViewController welcomeViewController;
    private LinkedHashSet<BluetoothDevice> bluetoothDevices;
    private StreamConnection connection;

    public void setBluetoothDevices(LinkedHashSet<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;

    }

    public LinkedHashSet<BluetoothDevice> getBluetoothDevices(){
        return bluetoothDevices;
    }

    public WelcomeView(WelcomeViewController welcomeViewController){
        this.welcomeViewController = welcomeViewController;

        add(mainPanel);
        setTitle("Welcome to MicroMouse GUI");
        //minimum size needed to display all the data
        pack();
        setResizable(false);
        //setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Using the discoverButton of the WelcomeView will discover devices and assign them to the view vector
        //it will then populate the discovered device list and switch to the scan tab
        discoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(rootPane, "We will begin searching for devices after you press OK... this may take a while... Please wait");
                setBluetoothDevices(welcomeViewController.getDiscoveredDeviceSet());
                populateDiscoveredDeviceList(getBluetoothDevices());
                mainTabbedPane.setSelectedIndex(1);

            }
        });

        scanForDevicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setBluetoothDevices(welcomeViewController.getDiscoveredDeviceSet());
                populateDiscoveredDeviceList(getBluetoothDevices());
            }
        });

        syncToSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = discoveredDeviceList.getSelectedIndex();
                BluetoothDevice[] deviceArray = new BluetoothDevice[getBluetoothDevices().size()];

                getBluetoothDevices().toArray(deviceArray);
                BluetoothDevice selectedDevice = deviceArray[selectedIndex];

                connectAndLaunchEngine(selectedDevice);
            }
        });

        connectToPreviouslySynced.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = syncedDevicesList.getSelectedIndex();
                LinkedHashSet<BluetoothDevice> syncedDevicesSet = welcomeViewController.getSyncedDeviceSet();
                BluetoothDevice[] deviceArray = new BluetoothDevice[syncedDevicesSet.size()];

                syncedDevicesSet.toArray(deviceArray);
                BluetoothDevice selectedDevice = deviceArray[selectedIndex];

                connectAndLaunchEngine(selectedDevice);

            }
        });

        deletedSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = syncedDevicesList.getSelectedIndex();
                LinkedHashSet<BluetoothDevice> syncedDevicesSet = welcomeViewController.getSyncedDeviceSet();
                BluetoothDevice[] deviceArray = new BluetoothDevice[syncedDevicesSet.size()];

                syncedDevicesSet.toArray(deviceArray);
                BluetoothDevice selectedDevice = deviceArray[selectedIndex];

                welcomeViewController.removedSelectedFromSyncedDevices(selectedDevice);
                populateSyncedDeviceList(welcomeViewController.getSyncedDeviceSet());

            }
        });

        syncToKnownDeviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateSyncedDeviceList(welcomeViewController.getSyncedDeviceSet());
                mainTabbedPane.setSelectedIndex(2);
            }
        });
        loadDevicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateSyncedDeviceList(welcomeViewController.getSyncedDeviceSet());
            }
        });
    }

    public void populateDiscoveredDeviceList(LinkedHashSet<BluetoothDevice> discoveredDevices){
        discoveredDeviceList.setListData(FriendlyNameGetter.getFriendlyName(discoveredDevices));
    }

    public void populateSyncedDeviceList(LinkedHashSet<BluetoothDevice> syncedDevices){
        syncedDevicesList.setListData(FriendlyNameGetter.getFriendlyName(syncedDevices));
    }

    public void connectAndLaunchEngine(BluetoothDevice selectedDevice){
        connection = welcomeViewController.connectToDevice(selectedDevice);
        if(connection == null){
            System.out.println("Failed to connect try again");
            return;
        }
        MouseInputs mouseInputs = welcomeViewController.getMouseInputs();
        MouseInputsReceiver receiver = new MouseInputsReceiver(mouseInputs, connection);
        receiver.start();
        SwingUtilities.invokeLater(() -> {
            DisplayView displayView= new DisplayView(mouseInputs);
            displayView.setVisible(true);
        });
    }

}
