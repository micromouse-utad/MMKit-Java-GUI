package pt.globaltronic.microMouseGUI;
import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.views.WelcomeView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {


        //instantiation in welcomeViewController should be done in main and wired in.
/*
        Engine engine = new Engine("Mouse Trial", 16,16, 60);
        engine.start();

 */



        MouseInputs mouseInputs = new MouseInputs();
        WelcomeViewController welcomeViewController = new WelcomeViewController(mouseInputs);


        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            WelcomeView welcomeView = new WelcomeView(welcomeViewController);
            welcomeView.setVisible(true);
        });













    }
}
