package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;

public class Plane {

    private Vector V1;
    private Vector V2;
    private Vector NV;
    private double[] P = new double[3];

    public Plane(Polygon2D DP)
    {
        P[0] = DP.getX()[0];
        P[1] = DP.getY()[0];
        P[2] = DP.getZ()[0];

        V1 = new Vector(DP.getX()[1] - DP.getX()[0],
                DP.getY()[1] - DP.getY()[0],
                DP.getZ()[1] - DP.getZ()[0]);

        V2 = new Vector(DP.getX()[2] - DP.getX()[0],
                DP.getY()[2] - DP.getY()[0],
                DP.getZ()[2] - DP.getZ()[0]);

        NV = V1.CrossProduct(V2);
    }

    public Plane(Vector VE1, Vector VE2, double[] Z)
    {
        P = Z;
        V1 = VE1;
        V2 = VE2;
        NV = V1.CrossProduct(V2);
    }

    public double[] getP() {
        return P;
    }

    public Vector getNV() {
        return NV;
    }

    public Vector getV1() {
        return V1;
    }

    public Vector getV2() {
        return V2;
    }
}
