package pt.globaltronic.microMouseGUI.models.graphics.services;

public class MouseInputsTranslator {

    //String from mouse should be in format
    // XY:D W=000
    //X and Y in hexadecimal
    // D written as N W E S for north west east south
    // W=000 W stands for walls, and the walls come in binary from left to right.
    public static int parseCol(String mouseInput){
        String xStr = mouseInput.substring(0,1);
        return Integer.parseInt(xStr, 16);
    }

    public static int parseRow(String mouseInput){
        String yStr = mouseInput.substring(1,2);
        return Integer.parseInt(yStr, 16);
    }

    public static String parseDirection(String mouseInput){
        return mouseInput.substring(3,4);
    }

    public static boolean parseLeftWall(String mouseInput){
        return mouseInput.substring(7,8).equals("1");
    }

    public static boolean parseFrontWall(String mouseInput){
        return mouseInput.substring(8,9).equals("1");
    }

    public static boolean parseRightWall(String mouseInput){
        return mouseInput.substring(9,10).equals("1");
    }

}
