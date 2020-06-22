package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;

public class Calculator {

    private Screen screen;
    private double t = 0;
    private Vector W1;
    private Vector W2;
    private Vector ViewVector;
    private Vector RotationVector;
    private Vector DirectionVector;
    private Vector PlaneVector1;
    private Vector PlaneVector2;
    private Plane P;
    private double[] CalcFocusPos = new double[2];

    public Calculator(Screen screen){
        this.screen = screen;
    }

    public double[] calculatePositionP(double[] ViewFrom, double[] ViewTo, double x, double y, double z) {
        double[] projP = getProj(ViewFrom, ViewTo, x, y, z, P);
        double[] drawP = getDrawP(projP[0], projP[1], projP[2]);
        return drawP;
    }

    public double[] getProj(double[] ViewFrom, double[] ViewTo, double x, double y, double z, Plane P) {
        Vector ViewToPoint = new Vector(x - ViewFrom[0], y - ViewFrom[1], z - ViewFrom[2]);

        t = (P.getNV().getX() * P.getP()[0] + P.getNV().getY() * P.getP()[1] + P.getNV().getZ() * P.getP()[2]
                - (P.getNV().getX() * ViewFrom[0] + P.getNV().getY() * ViewFrom[1] + P.getNV().getZ() * ViewFrom[2]))
                / (P.getNV().getX() * ViewToPoint.getX() + P.getNV().getY() * ViewToPoint.getY() + P.getNV().getZ() * ViewToPoint.getZ());

        x = ViewFrom[0] + ViewToPoint.getX() * t;
        y = ViewFrom[1] + ViewToPoint.getY() * t;
        z = ViewFrom[2] + ViewToPoint.getZ() * t;

        return new double[]{x, y, z};
    }

    public double[] getDrawP(double x, double y, double z) {
        double DrawX = W2.getX() * x + W2.getY() * y + W2.getZ() * z;
        double DrawY = W1.getX() * x + W1.getY() * y + W1.getZ() * z;
        return new double[]{DrawX, DrawY};
    }

    public Vector getRotationVector(double[] ViewFrom, double[] ViewTo) {
        double dx = Math.abs(ViewFrom[0] - ViewTo[0]);
        double dy = Math.abs(ViewFrom[1] - ViewTo[1]);
        double xRot, yRot;
        xRot = dy / (dx + dy);
        yRot = dx / (dx + dy);

        if (ViewFrom[1] > ViewTo[1])
            xRot = -xRot;
        if (ViewFrom[0] < ViewTo[0])
            yRot = -yRot;

        Vector V = new Vector(xRot, yRot, 0);
        return V;
    }

    public void SetPrederterminedInfo() {
        ViewVector = new Vector(screen.getViewTo()[0] - screen.getViewFrom()[0], screen.getViewTo()[1] - screen.getViewFrom()[1], screen.getViewTo()[2] - screen.getViewFrom()[2]);
        DirectionVector = new Vector(1, 1, 1);
        PlaneVector1 = ViewVector.CrossProduct(DirectionVector);
        PlaneVector2 = ViewVector.CrossProduct(PlaneVector1);
        P = new Plane(PlaneVector1, PlaneVector2, screen.getViewTo());

        RotationVector = getRotationVector(screen.getViewFrom(), screen.getViewTo());
        W1 = ViewVector.CrossProduct(RotationVector);
        W2 = ViewVector.CrossProduct(W1);

        CalcFocusPos = calculatePositionP(screen.getViewFrom(), screen.getViewTo(), screen.getViewTo()[0], screen.getViewTo()[1], screen.getViewTo()[2]);
        CalcFocusPos[0] = screen.getZoom() * CalcFocusPos[0];
        CalcFocusPos[1] = screen.getZoom() * CalcFocusPos[1];


    }

    public double[] getCalcFocusPos() {
        return CalcFocusPos;
    }

    public double getT() {
        return t;
    }
}
