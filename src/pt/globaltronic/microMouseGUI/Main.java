package pt.globaltronic.microMouseGUI;
import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.bluetooth.*;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.views.WelcomeView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        Wiring wiring = new Wiring();
        WelcomeViewController welcomeViewController = new WelcomeViewController();
        wiring.bootstrap(welcomeViewController);



        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            WelcomeView welcomeView = new WelcomeView(welcomeViewController);
            welcomeView.setVisible(true);
        });


    }

}
