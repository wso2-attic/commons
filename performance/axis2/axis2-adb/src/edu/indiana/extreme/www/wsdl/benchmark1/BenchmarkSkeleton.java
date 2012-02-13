package edu.indiana.extreme.www.wsdl.benchmark1;

import java.util.Date;
import java.io.IOException;

import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.axiom.attachments.ByteArrayDataSource;

import javax.activation.DataHandler;

/**
 * BenchmarkSkeleton java skeleton for the axisService
 */
public class BenchmarkSkeleton implements BenchmarkSkeletonInterface {
    /**
     * Auto generated method signature
     *
     * @param param0
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoVoidResponse echoVoid(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoVoid param0) {
        EchoVoidResponse ret = new EchoVoidResponse();
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param2
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoStringsResponse echoStrings(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoStrings param2) {
        EchoStringsResponse ret = new EchoStringsResponse();
        ret.setEchoStringsReturn(param2.getInput());
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param4
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.ReceiveBase64Response receiveBase64(
            edu.indiana.extreme.www.wsdl.benchmark1.ReceiveBase64 param4) {
        ReceiveBase64Response ret = new ReceiveBase64Response();
        byte[] array = getArraySize(param4.getInput());
        ret.setReceiveBase64Return(array.length);
        return ret;
    }

    private byte[] getArraySize(DataHandler data) {
        byte[] array;
        try {
            array = IOUtils.getStreamAsByteArray(data.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return array;
    }

    /**
     * Auto generated method signature
     *
     * @param param6
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.ReceiveDoublesResponse receiveDoubles(
            edu.indiana.extreme.www.wsdl.benchmark1.ReceiveDoubles param6) {
        ReceiveDoublesResponse ret = new ReceiveDoublesResponse();
        ret.setReceiveDoublesReturn(param6.getInput().length);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param8
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.SendIntsResponse sendInts(
            edu.indiana.extreme.www.wsdl.benchmark1.SendInts param8) {
        SendIntsResponse ret = new SendIntsResponse();
        int[] array = new int [param8.getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        ret.setSendIntsReturn(array);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param10
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoBase64Response echoBase64(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoBase64 param10) {
        EchoBase64Response ret = new EchoBase64Response();
        ret.setEchoBase64Return(param10.getInput());
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param12
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.ReceiveStringsResponse receiveStrings(
            edu.indiana.extreme.www.wsdl.benchmark1.ReceiveStrings param12) {
        ReceiveStringsResponse ret = new ReceiveStringsResponse();
        ret.setReceiveStringsReturn(param12.getInput().length);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param14
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoIntsResponse echoInts(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoInts param14) {
        EchoIntsResponse ret = new EchoIntsResponse();
        ret.setEchoIntsReturn(param14.getInput());
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param16
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.ReceiveIntsResponse receiveInts(
            edu.indiana.extreme.www.wsdl.benchmark1.ReceiveInts param16) {
        ReceiveIntsResponse ret = new ReceiveIntsResponse();
        ret.setReceiveIntsReturn(param16.getInput().length);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param18
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.SendDoublesResponse sendDoubles(
            edu.indiana.extreme.www.wsdl.benchmark1.SendDoubles param18) {
        SendDoublesResponse ret = new SendDoublesResponse();
        double[] array = new double [param18.getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        ret.setSendDoublesReturn(array);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param20
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.SendBase64Response sendBase64(
            edu.indiana.extreme.www.wsdl.benchmark1.SendBase64 param20) {
        SendBase64Response ret = new SendBase64Response();
        byte[] array = new byte [param20.getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) i;
        }
        ret.setSendBase64Return(new DataHandler(new ByteArrayDataSource(array)));
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param22
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoDoublesResponse echoDoubles(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoDoubles param22) {
        EchoDoublesResponse ret = new EchoDoublesResponse();
        ret.setEchoDoublesReturn(param22.getInput());
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param24
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.SendStringsResponse sendStrings(
            edu.indiana.extreme.www.wsdl.benchmark1.SendStrings param24) {
        SendStringsResponse ret = new SendStringsResponse();
        String[] array = new String [param24.getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = "s" + i;
        }
        ret.setSendStringsReturn(array);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param10
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.SendMeshInterfaceObjectsResponse sendMeshInterfaceObjects(
            edu.indiana.extreme.www.wsdl.benchmark1.SendMeshInterfaceObjects param10) {
        SendMeshInterfaceObjectsResponse ret = new SendMeshInterfaceObjectsResponse();
        MeshInterfaceObject[] objects = new MeshInterfaceObject[param10.getSize()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new MeshInterfaceObject();
            objects[i].setX(i);
            objects[i].setY(i);
            objects[i].setValue(Math.sqrt(i));
        }
        ret.setItem(objects);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param12
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.ReceiveMeshInterfaceObjectsResponse receiveMeshInterfaceObjects(
            edu.indiana.extreme.www.wsdl.benchmark1.ReceiveMeshInterfaceObjects param12) {
        ReceiveMeshInterfaceObjectsResponse ret = new ReceiveMeshInterfaceObjectsResponse();
        ret.setReceiveMeshInterfaceObjectsReturn(param12.getInput().length);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param16
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoSimpleEventsResponse echoSimpleEvents(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoSimpleEvents param16) {
        EchoSimpleEventsResponse ret = new EchoSimpleEventsResponse();
        ret.setEchoSimpleEventsReturn(param16.getInput());
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param30
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.SendSimpleEventsResponse sendSimpleEvents(
            edu.indiana.extreme.www.wsdl.benchmark1.SendSimpleEvents param30) {
        SendSimpleEventsResponse ret = new SendSimpleEventsResponse();
        Date date = new Date();
        SimpleEvent[] objects = new SimpleEvent[param30.getSize()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new SimpleEvent();
            objects[i].setSequenceNumber(i);
            objects[i].setMessage("Message #"+i);
            objects[i].setTimestamp(Math.sqrt(i));
        }
        ret.setItem(objects);
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param34
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.EchoMeshInterfaceObjectsResponse echoMeshInterfaceObjects(
            edu.indiana.extreme.www.wsdl.benchmark1.EchoMeshInterfaceObjects param34) {
        EchoMeshInterfaceObjectsResponse ret = new EchoMeshInterfaceObjectsResponse();
        ret.setItem(param34.getInput());
        return ret;
    }

    /**
     * Auto generated method signature
     *
     * @param param36
     */
    public edu.indiana.extreme.www.wsdl.benchmark1.ReceiveSimpleEventsResponse receiveSimpleEvents(
            edu.indiana.extreme.www.wsdl.benchmark1.ReceiveSimpleEvents param36) {
        ReceiveSimpleEventsResponse ret = new ReceiveSimpleEventsResponse();
        ret.setReceiveSimpleEventsReturn(param36.getInput().length);
        return ret;
    }
}
