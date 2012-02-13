package edu.indiana.extreme.wsdl.benchmark1;

public class MeshInterfaceObject {
    private int localX;
    private int localY;
    private double localValue;

    public MeshInterfaceObject() {
    }

    public MeshInterfaceObject(int localX, int localY, double localValue) {
        this.localX = localX;
        this.localY = localY;
        this.localValue = localValue;
    }

    public int getX() {
        return localX;
    }
    public void setX(int param) {
        this.localX = param;
    }

    public int getY() {
        return localY;
    }

    public void setY(int param) {
        this.localY = param;
    }
    public double getValue() {
        return localValue;
    }

    public void setValue(double param) {
        this.localValue = param;
    }
}
