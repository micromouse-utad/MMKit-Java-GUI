package pt.globaltronic.microMouseGUI.views;

import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.BluetoothDevice;
import pt.globaltronic.microMouseGUI.models.bluetooth.services.FriendlyNameGetter;

import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Vector;

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
    private JList replayList;
    private JButton playReplayButton;
    private JButton refresh;
    private JButton deleteReplayButton;

    private WelcomeViewController welcomeViewController;
    private LinkedHashSet<BluetoothDevice> bluetoothDevices;
    private StreamConnection connection;


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
        playReplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = replayList.getSelectedIndex();
                LinkedHashSet<File> replaySet = welcomeViewController.getReplaysSet();
                File[] fileArray = new File[replaySet.size()];
                replaySet.toArray(fileArray);
                File selectedReplay = fileArray[selectedIndex];

                welcomeViewController.startReplay(selectedReplay);
            }
        });
        deleteReplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = replayList.getSelectedIndex();
                LinkedHashSet<File> replaySet = welcomeViewController.getReplaysSet();
                File[] fileArray = new File[replaySet.size()];

                replaySet.toArray(fileArray);
                File selectedReplay = fileArray[selectedIndex];

                welcomeViewController.removeReplayFile(selectedReplay);
                populateReplayList(welcomeViewController.getReplaysStringVector());
            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateReplayList(welcomeViewController.getReplaysStringVector());
            }
        });
    }

    public void populateDiscoveredDeviceList(LinkedHashSet<BluetoothDevice> discoveredDevices){
        discoveredDeviceList.setListData(FriendlyNameGetter.getFriendlyName(discoveredDevices));
    }

    public void populateReplayList(Vector<String> replayNamesVector){
        replayList.setListData(replayNamesVector);
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
        welcomeViewController.startDisplayView(connection, selectedDevice);
    }

    public void setBluetoothDevices(LinkedHashSet<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;

    }

    public LinkedHashSet<BluetoothDevice> getBluetoothDevices(){
        return bluetoothDevices;
    }

}
