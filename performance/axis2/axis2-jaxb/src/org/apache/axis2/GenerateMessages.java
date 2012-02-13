package org.apache.axis2;

import java.util.Date;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import edu.indiana.extreme.www.wsdl.benchmark1.EchoBase64;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoDoubles;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoInts;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoMeshInterfaceObjects;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoSimpleEvents;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoStrings;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoVoid;
import edu.indiana.extreme.www.wsdl.benchmark1.MeshInterfaceObject;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveBase64;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveDoubles;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveInts;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveMeshInterfaceObjects;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveSimpleEvents;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveStrings;
import edu.indiana.extreme.www.wsdl.benchmark1.SendBase64;
import edu.indiana.extreme.www.wsdl.benchmark1.SendDoubles;
import edu.indiana.extreme.www.wsdl.benchmark1.SendInts;
import edu.indiana.extreme.www.wsdl.benchmark1.SendMeshInterfaceObjects;
import edu.indiana.extreme.www.wsdl.benchmark1.SendSimpleEvents;
import edu.indiana.extreme.www.wsdl.benchmark1.SendStrings;
import edu.indiana.extreme.www.wsdl.benchmark1.SimpleEvent;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12Factory;
import org.apache.axiom.om.OMElement;

public class GenerateMessages {
    private static SOAP12Factory factory = new SOAP12Factory();

    public static void main(String[] args) throws Exception {
        GenerateMessages generator = new GenerateMessages();
        generator.generate(1);
        generator.generate(10);
        generator.generate(50);
        generator.generate(100);
        generator.generate(500);
        generator.generate(1000);
        generator.generate(5000);
        generator.generate(10000);
        generator.generate(15000);
        generator.generate(20000);
        generator.generate(30000);
        generator.generate(40000);
        generator.generate(50000);
    }

    public void generate(int size) throws Exception {
        generate2(echoVoid(size).getOMElement(EchoVoid.MY_QNAME,factory),
                new FileOutputStream("xmls/echoVoid-" + size + ".xml"));
        generate2(echoStrings(size).getOMElement(EchoStrings.MY_QNAME,factory),
                new FileOutputStream("xmls/echoStrings-" + size + ".xml"));
        generate2(receiveBase64(size).getOMElement(ReceiveBase64.MY_QNAME,factory),
                new FileOutputStream("xmls/receiveBase64-" + size + ".xml"));
        generate2(receiveDoubles(size).getOMElement(ReceiveDoubles.MY_QNAME,factory),
                new FileOutputStream("xmls/receiveDoubles-" + size + ".xml"));
        generate2(sendInts(size).getOMElement(SendInts.MY_QNAME,factory),
                new FileOutputStream("xmls/sendInts-" + size + ".xml"));
        generate2(echoBase64(size).getOMElement(EchoBase64.MY_QNAME,factory),
                new FileOutputStream("xmls/echoBase64-" + size + ".xml"));
        generate2(receiveStrings(size).getOMElement(ReceiveStrings.MY_QNAME,factory),
                new FileOutputStream("xmls/receiveStrings-" + size + ".xml"));
        generate2(echoInts(size).getOMElement(EchoInts.MY_QNAME,factory),
                new FileOutputStream("xmls/echoInts-" + size + ".xml"));
        generate2(receiveInts(size).getOMElement(ReceiveInts.MY_QNAME,factory),
                new FileOutputStream("xmls/receiveInts-" + size + ".xml"));
        generate2(sendDoubles(size).getOMElement(SendDoubles.MY_QNAME,factory),
                new FileOutputStream("xmls/sendDoubles-" + size + ".xml"));
        generate2(sendBase64(size).getOMElement(SendBase64.MY_QNAME,factory),
                new FileOutputStream("xmls/sendBase64-" + size + ".xml"));
        generate2(echoDoubles(size).getOMElement(EchoDoubles.MY_QNAME,factory),
                new FileOutputStream("xmls/echoDoubles-" + size + ".xml"));
        generate2(sendStrings(size).getOMElement(SendStrings.MY_QNAME,factory),
                new FileOutputStream("xmls/sendStrings-" + size + ".xml"));
        generate2(sendMeshInterfaceObjects(size).getOMElement(SendMeshInterfaceObjects.MY_QNAME,factory),
                new FileOutputStream("xmls/sendMeshInterfaceObjects-" + size + ".xml"));
        generate2(receiveMeshInterfaceObjects(size).getOMElement(ReceiveMeshInterfaceObjects.MY_QNAME,factory),
                new FileOutputStream("xmls/receiveMeshInterfaceObjects-" + size + ".xml"));
        generate2(echoSimpleEvents(size).getOMElement(EchoSimpleEvents.MY_QNAME,factory),
                new FileOutputStream("xmls/echoSimpleEvents-" + size + ".xml"));
        generate2(sendSimpleEvents(size).getOMElement(SendSimpleEvents.MY_QNAME,factory),
                new FileOutputStream("xmls/sendSimpleEvents-" + size + ".xml"));
        generate2(echoMeshInterfaceObjects(size).getOMElement(EchoMeshInterfaceObjects.MY_QNAME,factory),
                new FileOutputStream("xmls/echoMeshInterfaceObjects-" + size + ".xml"));
        generate2(receiveSimpleEvents(size).getOMElement(ReceiveSimpleEvents.MY_QNAME,factory),
                new FileOutputStream("xmls/receiveSimpleEvents-" + size + ".xml"));
    }

    public void generate2(OMElement elem, OutputStream os) throws Exception {
        SOAPEnvelope env = factory.getDefaultEnvelope();
        env.getBody().addChild(elem);
        env.serialize(os);
        os.flush();
        os.close();
    }

    public EchoVoid echoVoid(int size) {
        EchoVoid ret = new EchoVoid();
        return ret;
    }

    public EchoStrings echoStrings(int size) {
        EchoStrings ret = new EchoStrings();
        String[] sarr = new String[size];
        for (int i = 0; i < sarr.length; i++) {
            sarr[i] = "s" + i;
        }
        ret.setInput(sarr);
        return ret;
    }

    public ReceiveBase64 receiveBase64(int size) {
        ReceiveBase64 ret = new ReceiveBase64();
        byte barr[] = new byte[size];
        for (int i = 0; i < barr.length; i++) {
            barr[i] = (byte) i;
        }
        ret.setInput(new DataHandler(new ByteArrayDataSource(barr)));
        return ret;
    }

    public ReceiveDoubles receiveDoubles(int size) {
        ReceiveDoubles ret = new ReceiveDoubles();
        double darr[] = new double[size];
        for (int i = 0; i < darr.length; i++) {
            darr[i] = i;
        }
        ret.setInput(darr);
        return ret;
    }

    public SendInts sendInts(int size) {
        SendInts ret = new SendInts();
        ret.setSize(size);
        return ret;
    }

    public EchoBase64 echoBase64(int size) {
        EchoBase64 ret = new EchoBase64();
        byte barr[] = new byte[size];
        for (int i = 0; i < barr.length; i++) {
            barr[i] = (byte) i;
        }
        ret.setInput(new DataHandler(new ByteArrayDataSource(barr)));
        return ret;
    }

    public ReceiveStrings receiveStrings(int size) {
        ReceiveStrings ret = new ReceiveStrings();
        String[] sarr = new String[size];
        for (int i = 0; i < sarr.length; i++) {
            sarr[i] = "s" + i;
        }
        ret.setInput(sarr);
        return ret;
    }

    public EchoInts echoInts(int size) {
        EchoInts ret = new EchoInts();
        int iarr[] = new int[size];
        for (int i = 0; i < iarr.length; i++) {
            iarr[i] = i;
        }
        ret.setInput(iarr);
        return ret;
    }

    public ReceiveInts receiveInts(int size) {
        ReceiveInts ret = new ReceiveInts();
        int iarr[] = new int[size];
        for (int i = 0; i < iarr.length; i++) {
            iarr[i] = i;
        }
        ret.setInput(iarr);
        return ret;
    }

    public SendDoubles sendDoubles(int size) {
        SendDoubles ret = new SendDoubles();
        ret.setSize(size);
        return ret;
    }

    public SendBase64 sendBase64(int size) {
        SendBase64 ret = new SendBase64();
        ret.setSize(size);
        return ret;
    }

    public EchoDoubles echoDoubles(int size) {
        EchoDoubles ret = new EchoDoubles();
        double darr[] = new double[size];
        for (int i = 0; i < darr.length; i++) {
            darr[i] = i;
        }
        ret.setInput(darr);
        return ret;
    }

    public SendStrings sendStrings(int size) {
        SendStrings ret = new SendStrings();
        ret.setSize(size);
        return ret;
    }

    public SendMeshInterfaceObjects sendMeshInterfaceObjects(int size) {
        SendMeshInterfaceObjects ret = new SendMeshInterfaceObjects();
        ret.setSize(size);
        return ret;
    }

    public ReceiveMeshInterfaceObjects receiveMeshInterfaceObjects(int size) {
        ReceiveMeshInterfaceObjects ret = new ReceiveMeshInterfaceObjects();
        MeshInterfaceObject[] objects = new MeshInterfaceObject[size];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new MeshInterfaceObject();
            objects[i].setX(i);
            objects[i].setY(i);
            objects[i].setValue(Math.sqrt(i));
        }
        ret.setInput(objects);
        return ret;
    }

    public EchoSimpleEvents echoSimpleEvents(int size) {
        EchoSimpleEvents ret = new EchoSimpleEvents();
        Date date = new Date();
        SimpleEvent[] objects = new SimpleEvent[size];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new SimpleEvent();
            objects[i].setSequenceNumber(i);
            objects[i].setMessage("Message #"+i);
            objects[i].setTimestamp(Math.sqrt(i));
        }
        ret.setInput(objects);
        return ret;
    }

    public SendSimpleEvents sendSimpleEvents(int size) {
        SendSimpleEvents ret = new SendSimpleEvents();
        ret.setSize(size);
        return ret;
    }

    public EchoMeshInterfaceObjects echoMeshInterfaceObjects(int size) {
        EchoMeshInterfaceObjects ret = new EchoMeshInterfaceObjects();
        MeshInterfaceObject[] objects = new MeshInterfaceObject[size];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new MeshInterfaceObject();
            objects[i].setX(i);
            objects[i].setY(i);
            objects[i].setValue(Math.sqrt(i));
        }
        ret.setInput(objects);
        return ret;
    }

    public ReceiveSimpleEvents receiveSimpleEvents(int size) {
        ReceiveSimpleEvents ret = new ReceiveSimpleEvents();
        Date date = new Date();
        SimpleEvent[] objects = new SimpleEvent[size];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new SimpleEvent();
            objects[i].setSequenceNumber(i);
            objects[i].setMessage("Message #"+i);
            objects[i].setTimestamp(Math.sqrt(i));
        }
        ret.setInput(objects);
        return ret;
    }
}
