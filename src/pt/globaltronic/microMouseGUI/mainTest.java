package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Replays;
import pt.globaltronic.microMouseGUI.views.WelcomeView;

import java.io.File;
import java.util.LinkedHashSet;

public class mainTest {

    public static void main(String[] args) {
        Replays replays = new Replays();
        replays.lookForReplays();

        WelcomeViewController wv = new WelcomeViewController();

        wv.setReplays(replays);

        System.out.println(wv.getReplaysVector().get(1).getName());



    }


}
