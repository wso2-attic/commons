/**
 * BenchmarkSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.1.1-SNAPSHOT Jan 17, 2007 (05:59:25 IST)
 */
package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;

/**
 * BenchmarkSkeleton java skeleton for the axisService
 */
public class BenchmarkSkeleton implements BenchmarkSkeletonInterface {


    public EchoVoidResponse echoVoid(EchoVoid echoVoid) {
        EchoVoidResponse response = new EchoVoidResponse();
        return response;
    }

    public EchoStringsResponse echoStrings(EchoStrings echoStrings) {
        EchoStringsResponse response = new EchoStringsResponse();
        response.echoStringsReturnList = echoStrings.inputList;
        return response;
    }

    public SendIntsResponse sendInts(SendInts sendInts) {
        SendIntsResponse response = new SendIntsResponse();
        java.util.ArrayList<java.lang.Integer> output = new java.util.ArrayList<java.lang.Integer>();
        for (int i = 0; i < sendInts.getSize(); i++) {
            output.add(i);
        }
        response.sendIntsReturnList = output;
        return response;
    }

    public ReceiveBase64Response receiveBase64(ReceiveBase64 receiveBase64) {
        ReceiveBase64Response response = new ReceiveBase64Response();
        response.setReceiveBase64Return(receiveBase64.getInput().length);
        return response;
    }

    public ReceiveDoublesResponse receiveDoubles(ReceiveDoubles receiveDoubles) {
        ReceiveDoublesResponse response = new ReceiveDoublesResponse();
        response.setReceiveDoublesReturn(receiveDoubles.inputList.size());
        return response;
    }

    public SendMeshInterfaceObjectsResponse sendMeshInterfaceObjects(SendMeshInterfaceObjects sendMeshInterfaceObjectsRequest) {
        SendMeshInterfaceObjectsResponse response = new SendMeshInterfaceObjectsResponse();
        ArrayList<MeshInterfaceObject> list = new ArrayList<MeshInterfaceObject>();

        for (int i = 0; i < sendMeshInterfaceObjectsRequest.getSize(); i++) {
            MeshInterfaceObject struct = new MeshInterfaceObject();
            struct.setX(i);
            struct.setY(i);
            struct.setValue(Math.sqrt(i));
            list.add(struct);
        }

        response.itemList = list;
        return response;
    }

    public ReceiveMeshInterfaceObjectsResponse receiveMeshInterfaceObjects(ReceiveMeshInterfaceObjects receiveMeshInterfaceObjectsRequest) {
        ReceiveMeshInterfaceObjectsResponse response = new ReceiveMeshInterfaceObjectsResponse();
        response.receiveMeshInterfaceObjectsReturn = receiveMeshInterfaceObjectsRequest.inputList.size();
        return response;
    }

    public EchoBase64Response echoBase64(EchoBase64 echoBase64) {
        EchoBase64Response response = new EchoBase64Response();
        response.echoBase64Return = echoBase64.getInput();
        return response;
    }

    public EchoSimpleEventsResponse echoSimpleEvents(EchoSimpleEvents echoSimpleEventsRequest) {
        EchoSimpleEventsResponse response = new EchoSimpleEventsResponse();
        response.echoSimpleEventsReturnList = echoSimpleEventsRequest.inputList;
        return response;
    }

    public ReceiveStringsResponse receiveStrings(ReceiveStrings receiveStrings) {
        ReceiveStringsResponse response = new ReceiveStringsResponse();
        response.receiveStringsReturn = receiveStrings.inputList.size();
        return response;
    }

    public EchoIntsResponse echoInts(EchoInts echoInts) {
        EchoIntsResponse response = new EchoIntsResponse();
        response.echoIntsReturnList = echoInts.inputList;
        return response;
    }

    public ReceiveIntsResponse receiveInts(ReceiveInts receiveInts) {
        ReceiveIntsResponse response = new ReceiveIntsResponse();
        response.receiveIntsReturn = receiveInts.inputList.size();
        return response;
    }

    public SendDoublesResponse sendDoubles(SendDoubles sendDoubles) {
        SendDoublesResponse response = new SendDoublesResponse();
        java.util.ArrayList<java.lang.Double> output = new java.util.ArrayList<java.lang.Double>();
        for (int i = 0; i < sendDoubles.getSize(); i++) {
            output.add((double) i);
        }
        response.sendDoublesReturnList = output;
        return response;
    }

    public SendBase64Response sendBase64(SendBase64 sendBase64) {
        SendBase64Response response = new SendBase64Response();
        byte[] output = new byte[sendBase64.getSize()];
        for (int i = 0; i < sendBase64.getSize(); i++) {
            output[i] = (byte) i;
        }
        response.sendBase64Return = output;
        return response;
    }

    public EchoDoublesResponse echoDoubles(EchoDoubles echoDoubles) {
        EchoDoublesResponse response = new EchoDoublesResponse();
        response.echoDoublesReturnList = echoDoubles.inputList;
        return response;
    }

    public SendSimpleEventsResponse sendSimpleEvents(SendSimpleEvents sendSimpleEventsRequest) {
        SendSimpleEventsResponse response = new SendSimpleEventsResponse();
        ArrayList<SimpleEvent> list = new ArrayList<SimpleEvent>();

        for (int i = 0; i < sendSimpleEventsRequest.getSize(); i++) {
            SimpleEvent struct = new SimpleEvent();
            struct.setSequenceNumber(i);
            struct.setMessage("Message #" + i);
            struct.setTimestamp(Math.sqrt(i));
            list.add(struct);
        }

        response.itemList = list;
        return response;
    }

    public SendStringsResponse sendStrings(SendStrings sendStrings) {
        SendStringsResponse response = new SendStringsResponse();
        java.util.ArrayList<java.lang.String> output = new java.util.ArrayList<java.lang.String>();
        for (int i = 0; i < sendStrings.getSize(); i++) {
            output.add("s" + i);
        }
        response.sendStringsReturnList = output;
        return response;
    }

    public EchoMeshInterfaceObjectsResponse echoMeshInterfaceObjects(EchoMeshInterfaceObjects echoMeshInterfaceObjectsRequest) {
        EchoMeshInterfaceObjectsResponse response = new EchoMeshInterfaceObjectsResponse();
        response.itemList = echoMeshInterfaceObjectsRequest.inputList;
        return response;
    }

    public ReceiveSimpleEventsResponse receiveSimpleEvents(ReceiveSimpleEvents receiveSimpleEventsRequest) {
        ReceiveSimpleEventsResponse response = new ReceiveSimpleEventsResponse();
        response.receiveSimpleEventsReturn = receiveSimpleEventsRequest.inputList.size();
        return response;
    }


}
