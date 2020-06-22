package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.controllers.WelcomeViewController;
import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Polygon2D;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Replays;
import pt.globaltronic.microMouseGUI.views.WelcomeView;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class mainTest {

    public static void main(String[] args) {

        int[] arr = {2,4,5,1,6,3};
        ArrayList<Integer> polygon2DS = new ArrayList<>();
        for (int i = 0; i < arr.length; i++){
            polygon2DS.add(arr[i]);
        }
        System.out.println(polygon2DS.size());

        int n = polygon2DS.size();
        for (int i = 1; i < n; ++i) {
            int key = polygon2DS.get(i);
            int j = i - 1;

            while (j >= 0 && polygon2DS.get(j)< key) {
                polygon2DS.set(j + 1, polygon2DS.get(j));
                j = j - 1;
            }
            polygon2DS.set(j + 1, key);
        }
        System.out.println(polygon2DS.size());
        for (int i = 0; i < polygon2DS.size(); i++){
            System.out.println(polygon2DS.get(i));
        }

    }


}
