package edu.indiana.extreme.wsdl.benchmark1;

public class SimpleEvent {
    protected double localTimestamp;
    protected int localSequenceNumber;
    protected java.lang.String localMessage;

    public SimpleEvent() {
    }

    public SimpleEvent(double localTimestamp, int localSequenceNumber, java.lang.String localMessage) {
        this.localTimestamp = localTimestamp;
        this.localSequenceNumber = localSequenceNumber;
        this.localMessage = localMessage;
    }

    public int getSequenceNumber() {
        return localSequenceNumber;
    }

    public void setSequenceNumber(int param) {
        this.localSequenceNumber = param;
    }

    public double getTimestamp() {
        return localTimestamp;
    }

    public void setTimestamp(double param) {
        this.localTimestamp = param;
    }

    public java.lang.String getMessage() {
        return localMessage;
    }

    public void setMessage(java.lang.String param) {
        this.localMessage = param;
    }
}
