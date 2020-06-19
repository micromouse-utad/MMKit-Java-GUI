package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Replays;
import pt.globaltronic.microMouseGUI.views.WelcomeView;

import javax.swing.*;
import java.io.File;
import java.util.LinkedHashSet;

public class mainTest {

    public static void main(String[] args) {

        JPanel panel = new JPanel();
        Engine3D engine3D = new Engine3D (panel,"Mouse 3d Trials", new MouseInputs(), 16, 16, 30, 10);

        engine3D.setOldYAngle(Math.PI);
        System.out.println(engine3D.calculateYRotationAngle("S"));


    }


}
