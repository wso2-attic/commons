package org.apache.axis2;

import edu.indiana.extreme.www.wsdl.benchmark1.EchoBase64;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoDoubles;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoInts;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoStrings;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoVoid;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveBase64;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveBase64Response;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveDoubles;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveInts;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveStrings;
import edu.indiana.extreme.www.wsdl.benchmark1.SendBase64;
import edu.indiana.extreme.www.wsdl.benchmark1.SendDoubles;
import edu.indiana.extreme.www.wsdl.benchmark1.SendInts;
import edu.indiana.extreme.www.wsdl.benchmark1.SendStrings;
import edu.indiana.extreme.www.wsdl.benchmark1.SimpleEvent;
import edu.indiana.extreme.www.wsdl.benchmark1.MeshInterfaceObject;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoSimpleEventsRequest;
import edu.indiana.extreme.www.wsdl.benchmark1.ArrayOfSimpleEvent;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveSimpleEventsRequest;
import edu.indiana.extreme.www.wsdl.benchmark1.SendSimpleEventsRequest;
import edu.indiana.extreme.www.wsdl.benchmark1.EchoMeshInterfaceObjectsRequest;
import edu.indiana.extreme.www.wsdl.benchmark1.ReceiveMeshInterfaceObjectsRequest;
import edu.indiana.extreme.www.wsdl.benchmark1.ArrayOfMeshInterfaceObject;
import edu.indiana.extreme.www.wsdl.benchmark1.SendMeshInterfaceObjectsRequest;

import javax.activation.DataHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.attachments.utils.IOUtils;

public class BenchClient {
    private final static String SMOKE_TEST = "smoke_test";
    private final static boolean VERBOSE = true;

    private String location;
    private org.apache.axis2.BenchmarkStub stub;

    public BenchClient(String location) throws Exception {
        this.location = location;
        stub = new org.apache.axis2.BenchmarkStub(location);
        org.apache.axis2.client.Options options = stub._getServiceClient().getOptions();
        options.setProperty(org.apache.axis2.context.MessageContextConstants.CHUNKED, Constants.VALUE_FALSE);
        options.setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT,new Integer(480000));
        options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(480000));
    }

    //URL or port of service
    //total number of elements to send (default 10K)
    //[rse] means receive, send, or echo (a == all)
    //[bdisva] means base64, double, int, string, void (only applies to echo), a == all methods;
    //arraySize (optional for void) - default to 10
    //jaav -Dmachine.name=... -Dserver.name=... Client URL total {rsea}{bdisva} [arraySize]
    public static void main(String[] args) throws Exception {
        long benchmarkStart = System.currentTimeMillis();
        final String BENCHMARK_DRIVER_VERSION = "$Date: 2004/06/17 12:09:59 $";
        final String ID = "Benchmark1 Driver Version 1.0 (" + BENCHMARK_DRIVER_VERSION + ")";
        verbose("Starting " + ID + " at " + (new Date()));

        // allow multiple URLs (each must start with http"
        List locationList = new ArrayList();
        //int port = Integer.parseInt(args[0]);
        int pos = 0;
        while (pos < args.length) {
            String s = args[pos];
            if (s.startsWith("http")) {
                locationList.add(s);
                //} else {
                //port = Integer.parseInt(s);
            } else {
                break;
            }
            ++pos;
        }
        if (locationList.isEmpty()) {
            int port = 34321;
            locationList.add("http://localhost:" + port);
        }

        final int elementsToSend = args.length > pos ? Integer.parseInt(args[pos]) : 10000;

        String testType = "aa";
        if (args.length > (pos + 1)) {
            testType = args[(pos + 1)];
        }

        String arrSizeToSend = "10";

        if (args.length > (pos + 2)) {
            arrSizeToSend = args[(pos + 2)];
        }
        //        System.out.println("invoking "+td.serverLocation+" for  elements="+td.elementsToSend+" "
        //                               +" direction="+td.direction+" method="+td.method+" arraySize="+td.arrSizeToSend);

        //new String[1];
        String[] locations = new String[locationList.size()];
        locationList.toArray(locations);

        for (int i = 0; i < locations.length; i++) {
            String location = locations[i];
            verbose("connecting to " + location);
            runTestsForSize(location, elementsToSend, testType, arrSizeToSend);
        }
        long benchmarkEnd = System.currentTimeMillis();
        double seconds = ((benchmarkEnd - benchmarkStart) / 1000.0);
        System.out.println("Finished " + ID + " in " + seconds + " seconds at " + (new Date()));
    }

    private static void runTestsForSize(String location,
                                        final int elementsToSend,
                                        String testType,
                                        String arrSizeToSend)
            throws Exception {
        TestDescriptor td = new TestDescriptor(location, elementsToSend);
        int commaPos = -1;
        boolean finished = false;
        while (!finished) {
            td.setDirection(testType.charAt(0));
            td.setMethod(testType.charAt(1));
            int prevPos = commaPos;
            commaPos = arrSizeToSend.indexOf(",", prevPos + 1);
            String size;
            if (commaPos > 0) {
                size = arrSizeToSend.substring(prevPos + 1, commaPos);
            } else {
                size = arrSizeToSend.substring(prevPos + 1);
                finished = true;
            }
            td.arrSizeToSend = Integer.parseInt(size);
            //System.out.println("runnig test with size=" + size + " " + (new Date()));
            final char direction = td.getDirection();
            if (direction == 'a') {
                td.setDirection('e');
                runTestsForDirection(td);
                td.setDirection('r');
                runTestsForDirection(td);
                td.setDirection('s');
                runTestsForDirection(td);
                td.setDirection('a'); //restore
            } else {
                runTestsForDirection(td);
            }
        }
    }

    public static void runTestsForDirection(TestDescriptor td)
            throws Exception {
        final char direction = td.direction;
        final char method = td.method;
        if (method == 'a') {
            if (direction == 'e') {
                //runTestsForDirection(direction, 'v', td);
                td.setMethod('v');
                runOneTest(td);
            }
            td.setMethod('b');
            runOneTest(td);
            td.setMethod('d');
            runOneTest(td);
            td.setMethod('i');
            runOneTest(td);
            td.setMethod('s');
            runOneTest(td);
            td.setMethod('m');
            runOneTest(td);
            td.setMethod('e');
            runOneTest(td);
            td.setMethod('a'); //restore
        } else {
            runOneTest(td);
        }
    }

    public static void runOneTest(TestDescriptor td)
            throws Exception {
        final char direction = td.direction;
        final char method = td.method;
        //int arrSize = method == 'v' ? 1 : td.arrSizeToSend;
        int arrSize = td.arrSizeToSend;
        int N = td.elementsToSend / arrSize; // + 1;
        if (N == 0) {
            N = 1;
        }
        final boolean smokeTest = System.getProperty(SMOKE_TEST) != null;
        if (smokeTest) { 
			N = 1;
			System.out.println(" (SMOKE TEST)");
		}

        int totalInv = N * td.arrSizeToSend;

        byte[] barr = null;
        byte[] ba = null;
        if (method == 'b') {
            ba = new byte[td.arrSizeToSend];
            barr = new byte[totalInv];
            for (int i = 0; i < barr.length; i++) {
                barr[i] = (byte) i;
            }
        }

        double[] darr = null;
        double[] da = null;
        if (method == 'd') {
            da = new double[td.arrSizeToSend];
            darr = new double[totalInv];
            for (int i = 0; i < darr.length; i++) {
                darr[i] = i;
            }
        }

        int[] iarr = null;
        int[] ia = null;
        if (method == 'i') {
            ia = new int[td.arrSizeToSend];
            iarr = new int[totalInv];
            for (int i = 0; i < iarr.length; i++) {
                iarr[i] = i;
            }
        }

        String[] sarr = null;
        String[] sa = null;
        if (method == 's') {
            sa = new String[td.arrSizeToSend];
            sarr = new String[totalInv];
            for (int i = 0; i < sarr.length; i++) {
                sarr[i] = "s" + i;
            }
        }

        MeshInterfaceObject[] marr = null;
        MeshInterfaceObject[] ma = null;
        if (method == 'm') {
            ma = new MeshInterfaceObject[td.arrSizeToSend];
            marr = new MeshInterfaceObject[totalInv];
            for (int i = 0; i < marr.length; i++) {
                marr[i] = new MeshInterfaceObject();
                marr[i].setX(i);
                marr[i].setY(i);
                marr[i].setValue(Math.sqrt(i));
            }
        }

        SimpleEvent[] earr = null;
        SimpleEvent[] ea = null;
        if (method == 'e') {
            ea = new SimpleEvent[td.arrSizeToSend];
            earr = new SimpleEvent[totalInv];
            for (int i = 0; i < earr.length; i++) {
                earr[i] = new SimpleEvent();
                earr[i].setSequenceNumber(i);
                earr[i].setMessage("Message #"+i);
                earr[i].setTimestamp(Math.sqrt(i));
            }
        }

        BenchClient client = new BenchClient(td.serverLocation);

//        System.out.println("invoking " + N + (smokeTest ? " (SMOKE TEST)" : "")
//                + " times for test " + method + " arraysSize=" + td.arrSizeToSend
//                + " " + (new Date()));
        //boolean validate = true;
        long start = System.currentTimeMillis();
        for (int count = 0; count < N; count++) {
            int off = count * arrSize;
            //String arg = "echo"+i;
            if (method == 'v') {
                if (direction == 'e') {
                    client.echoVoid();
                } else if (direction == 'r' || direction == 's') {
                    throw new RuntimeException("usupported direction " + direction + " for void method");
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
            } else if (method == 'b') {
                System.arraycopy(barr, off, ba, 0, ba.length);
                byte[] uba = null;
                int ulen = -1;
                if (direction == 'e') {
                    uba = client.echoBase64(ba);
                } else if (direction == 'r') {
                    ulen = client.receiveBase64(ba);
                    if (ulen != ba.length) fail(method2s(direction, method) + " returned wrong size");
                } else if (direction == 's') {
                    uba = client.sendBase64(arrSize);
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
                if ((count == 0 || count == N - 1) && (direction == 'e' || direction == 's')) {
                    // bruta force
                    if (direction == 's') off = 0;
                    if (uba == null) fail(method2s(direction, method) + " byte array response was null");
                    if (uba.length != ba.length) {
                        fail(method2s(direction, method) + " byte array had wrong size " + uba.length
                                + " (expected " + ba.length + ")");
                    }
                    for (int i = 0; i < ba.length; i++) {
                        if (uba[i] != barr[i + off]) {
                            fail("byte array response had wrong content");
                        }
                    }
                }
            } else if (method == 'd') {
                System.arraycopy(darr, off, da, 0, da.length);
                double[] uda = null;
                int dlen = -1;
                if (direction == 'e') {
                    uda = client.echoDoubles(da);
                } else if (direction == 'r') {
                    dlen = client.receiveDoubles(da);
                    if (dlen != da.length) fail("receive double array returned wrong size");
                } else if (direction == 's') {
                    uda = client.sendDoubles(arrSize);
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
                if ((count == 0 || count == N - 1) && (direction == 'e' || direction == 's')) {
                    // bruta force verification
                    if (direction == 's') off = 0;
                    if (uda == null) fail(method2s(direction, method) + " double array response was null");
                    if (uda.length != da.length) {
                        fail(method2s(direction, method) + " double array had wrong size " + uda.length
                                + " (expected " + da.length + ")");
                    }
                    for (int i = 0; i < uda.length; i++) {
                        if (uda[i] != darr[i + off]) {
                            fail(method2s(direction, method) + " double array response had wrong content");
                        }
                    }
                }
            } else if (method == 'i') {
                System.arraycopy(iarr, off, ia, 0, ia.length);
                int[] uia = null;
                int ulen = -1;
                if (direction == 'e') {
                    uia = client.echoInts(ia);
                } else if (direction == 'r') {
                    ulen = client.receiveInts(ia);
                    if (ulen != ia.length) {
                        fail(method2s(direction, method) + " receive byte array returned wrong size");
                    }
                } else if (direction == 's') {
                    uia = client.sendInts(arrSize);
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
                if ((count == 0 || count == N - 1) && (direction == 'e' || direction == 's')) {
                    // bruta force verification
                    if (direction == 's') off = 0;
                    if (uia == null) fail(method2s(direction, method) + " int array response was null");
                    if (uia.length != ia.length) {
                        fail(method2s(direction, method) + " int array had wrong size " + uia.length
                                + " (expected " + ia.length + ")");
                    }
                    for (int i = 0; i < uia.length; i++) {
                        if (uia[i] != iarr[i + off]) {
                            fail(method2s(direction, method) + " int array response had wrong content");
                        }
                    }
                }
            } else if (method == 's') {
                System.arraycopy(sarr, off, sa, 0, sa.length);
                String[] usa = null;
                int slen = -1;
                if (direction == 'e') {
                    usa = client.echoStrings(sa);
                } else if (direction == 'r') {
                    slen = client.receiveStrings(sa);
                    if (slen != sa.length)
                        fail(method2s(direction, method) + " receive string array returned wrong size");
                } else if (direction == 's') {
                    usa = client.sendStrings(arrSize);
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
                if (start > 0 && (count == 0 || count == N - 1) && (direction == 'e' || direction == 's')) {
                    // bruta force verification
                    if (direction == 's') off = 0;
                    if (usa == null) fail(method2s(direction, method) + " string array response was null");
                    if (usa.length != sa.length) {
                        fail(method2s(direction, method) + " string array had wrong size " + usa.length
                                + " (expected " + sa.length + ")");
                    }
                    for (int i = 0; i < usa.length; i++) {
                        String s1 = usa[i];
                        String s2 = sarr[i + off];
                        if (!s1.equals(s2)) {
                            fail(method2s(direction, method) + " string array response"
                                    + " had wrong content (s1=" + s1 + " s2=" + s2 + " i=" + i + ")");
                        }
                    }
                }
            } else if (method == 'm') {
                System.arraycopy(marr, off, ma, 0, ma.length);
                MeshInterfaceObject[] uma = null;
                int slen = -1;
                if (direction == 'e') {
                    uma = client.echoMeshInterfaceObjects(ma);
                } else if (direction == 'r') {
                    slen = client.receiveMeshInterfaceObjects(ma);
                    if (slen != ma.length)
                        fail(method2s(direction, method) + " receive MeshInterfaceObject array returned wrong size");
                } else if (direction == 's') {
                    uma = client.sendMeshInterfaceObjects(arrSize);
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
                if (start > 0 && (count == 0 || count == N - 1) && (direction == 'e' || direction == 's')) {
                    // bruta force verification
                    if (direction == 's') off = 0;
                    if (uma == null) fail(method2s(direction, method) + " MeshInterfaceObject array response was null");
                    if (uma.length != ma.length) {
                        fail(method2s(direction, method) + " string MeshInterfaceObject had wrong size " + uma.length
                                + " (expected " + ma.length + ")");
                    }
                    for (int i = 0; i < uma.length; i++) {
                        MeshInterfaceObject s1 = uma[i];
                        MeshInterfaceObject s2 = marr[i + off];
                        if (!toString(s1).equals(toString(s2))) {
                            fail(method2s(direction, method) + " MeshInterfaceObject array response"
                                    + " had wrong content (s1=" + s1 + " s2=" + s2 + " i=" + i + ")");
                        }
                    }
                }
            } else if (method == 'e') {
                System.arraycopy(earr, off, ea, 0, ea.length);
                SimpleEvent[] uea = null;
                int slen = -1;
                if (direction == 'e') {
                    uea = client.echoSimpleEvents(ea);
                } else if (direction == 'r') {
                    slen = client.receiveSimpleEvents(ea);
                    if (slen != ea.length)
                        fail(method2s(direction, method) + " receive SimpleEvent array returned wrong size");
                } else if (direction == 's') {
                    uea = client.sendSimpleEvents(arrSize);
                } else {
                    throw new RuntimeException("unrecongized direction " + direction);
                }
                if (start > 0 && (count == 0 || count == N - 1) && (direction == 'e' || direction == 's')) {
                    // bruta force verification
                    if (direction == 's') off = 0;
                    if (uea == null) fail(method2s(direction, method) + " SimpleEvent array response was null");
                    if (uea.length != ea.length) {
                        fail(method2s(direction, method) + " string SimpleEvent had wrong size " + uea.length
                                + " (expected " + ea.length + ")");
                    }
                    for (int i = 0; i < uea.length; i++) {
                        SimpleEvent s1 = uea[i];
                        SimpleEvent s2 = earr[i + off];
                        if (!toString(s1).equals(toString(s2))) {
                            fail(method2s(direction, method) + " SimpleEvent array response"
                                    + " had wrong content (s1=" + s1 + " s2=" + s2 + " i=" + i + ")");
                        }
                    }
                }
            } else {
                throw new RuntimeException("unrecongized method " + method);
            }

            if (start > 0 && smokeTest) {
//                String resp = builder.serializeToString(handler.getLastResponse());
//                System.out.println(method2s(direction, method)+" response=\n"+resp+"---\n");
            }
        }
        if (start > 0) {
            long end = System.currentTimeMillis();
            long total = (end == start) ? 1 : (end - start);
            double seconds = (total / 1000.0);
            double invPerSecs = (double) N / seconds;
            double avgInvTimeInMs = (double) total / (double) N;
//            System.out.println("N=" + N + " avg invocation:" + avgInvTimeInMs + " [ms]" +
//                    "total:"+total+" [ms] "+
//                    " throughput:" + invPerSecs + " [invocations/second]" +
//                    " arraysSize=" + arrSize +
//                    " direction=" + direction +
//                    " method=" + method
//                    + " " + (new Date())
//            );
            td.printResult(avgInvTimeInMs / 1000.0, invPerSecs);
        }
    }

    private static String toString(SimpleEvent event){
        return "[" + event.getMessage() + ":" + event.getSequenceNumber() + ":" + event.getTimestamp() + "]";
    }

    private static String toString(MeshInterfaceObject object){
        return "[" + object.getValue() + ":" + object.getX() + ":" + object.getY() + "]";
    }

    private static void verbose(String msg) {
        System.out.println("B1> " + msg);
    }

    private static void fail(String msg) {
        String s = "FATAL ERROR: service is not following benchmark requirement: " + msg;
        System.out.println(s);
        throw new RuntimeException(s);
        //System.exit(-1);
    }

    private static String method2s(char direction, char method) {
        StringBuffer sb = new StringBuffer(20);
        if (direction == 'e') {
            sb.append("echo");
        } else if (direction == 's') {
            sb.append("send");
        } else if (direction == 'r') {
            sb.append("receive");
        }
        if (method == 'v') {
            sb.append("Void");
        } else if (method == 'b') {
            sb.append("Base64");
        } else if (method == 'd') {
            sb.append("Doubles");
        } else if (method == 'i') {
            sb.append("Ints");
        } else if (method == 's') {
            sb.append("Strings");
        } else if (method == 'm') {
            sb.append("MeshInterfaceObjects");
        } else if (method == 'e') {
            sb.append("SimpleEvent");
        }
        return sb.toString();
    }

    private final static class TestDescriptor {
        private java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000000");
        private java.text.DecimalFormat df2 = new java.text.DecimalFormat("##0.0000");
        private String testSetup;
        private String clientName = "AXIS2";
        private String serverName = null;
        private String serverLocation;
        private int arrSizeToSend;
        private int elementsToSend;

        private char direction;
        private char method;

        TestDescriptor(//String serverName,
                       String location,
                       //final char direction,
                       //final char method,
                       int elementsToSend)
        //int arrSizeToSend)
        {
            this.testSetup = System.getProperty("test.setup");
            if (this.testSetup == null) {
                this.testSetup = System.getProperty("machine.name", "UDISCLOSED_SETUP");
            }
            final String SERVER_NAME = "server.name";
            this.serverName = System.getProperty(SERVER_NAME);
            if (serverName == null) {
                throw new RuntimeException(SERVER_NAME + " must be specified as system property");
            }
            this.serverName = serverName;
            this.serverLocation = location;
            //this.direction =  direction;
            //this.method = method;
            this.elementsToSend = elementsToSend;
            //this.arrSizeToSend = arrSizeToSend;
        }

        public void setDirection(char direction) {
            this.direction = direction;
        }

        public char getDirection() {
            return direction;
        }

        public void setMethod(char method) {
            this.method = method;
        }

        public char getMethod() {
            return method;
        }

        public void printResult(double timeSecs, double throughput) throws IOException {
            PrintWriter results = new PrintWriter(System.out, true);
            results.print(testSetup + '\t'
                    + clientName + '\t'
                    + serverName + '\t'
                    + method2s(direction, method) + ((method == 'm') ? "\t" : ((method=='e' && direction=='r') ?"\t\t":"\t\t\t"))
                    + arrSizeToSend + '\t'
                    + df.format(timeSecs) + '\t'
                    + df2.format(throughput)
                    + "\r\n");
            results.flush();
        }

    }

    public void echoVoid() throws java.lang.Exception {
        EchoVoid param182 = new EchoVoid();
        stub.echoVoid(param182);
    }

    public String[] echoStrings(String[] input) throws java.lang.Exception {
        EchoStrings param184 = new EchoStrings();
        param184.setInput(input);
        return stub.echoStrings(param184).getEchoStringsReturn();
    }

    public int receiveBase64(byte[] input) throws java.lang.Exception {
        ReceiveBase64 param186 = new ReceiveBase64();
        param186.setInput(new DataHandler(new ByteArrayDataSource(input)));
        ReceiveBase64Response ret = stub.receiveBase64(param186);
        return ret.getReceiveBase64Return();
    }

    public int receiveDoubles(double[] input) throws java.lang.Exception {
        ReceiveDoubles param188 = new ReceiveDoubles();
        param188.setInput(input);
        return stub.receiveDoubles(param188).getReceiveDoublesReturn();
    }

    public int[] sendInts(int input) throws java.lang.Exception {
        SendInts param190 = new SendInts();
        param190.setSize(input);
        return stub.sendInts(param190).getSendIntsReturn();
    }

    public byte[] echoBase64(byte[] input) throws java.lang.Exception {
        EchoBase64 param192 = new EchoBase64();
        param192.setInput(new DataHandler(new ByteArrayDataSource(input)));
        return getArray(stub.echoBase64(param192).getEchoBase64Return());
    }

    public int receiveStrings(String[] input) throws java.lang.Exception {
        ReceiveStrings param194 = new ReceiveStrings();
        param194.setInput(input);
        return stub.receiveStrings(param194).getReceiveStringsReturn();
    }

    public int[] echoInts(int []input) throws java.lang.Exception {
        EchoInts param196 = new EchoInts();
        param196.setInput(input);
        return stub.echoInts(param196).getEchoIntsReturn();
    }

    public int receiveInts(int[] input) throws java.lang.Exception {
        ReceiveInts param198 = new ReceiveInts();
        param198.setInput(input);
        return stub.receiveInts(param198).getReceiveIntsReturn();
    }

    public double[] sendDoubles(int input) throws java.lang.Exception {
        SendDoubles param200 = new SendDoubles();
        param200.setSize(input);
        return stub.sendDoubles(param200).getSendDoublesReturn();
    }

    public byte[] sendBase64(int input) throws java.lang.Exception {
        SendBase64 param202 = new SendBase64();
        param202.setSize(input);
        return getArray(stub.sendBase64(param202).getSendBase64Return());
    }

    public double[] echoDoubles(double[] input) throws java.lang.Exception {
        EchoDoubles param204 = new EchoDoubles();
        param204.setInput(input);
        return stub.echoDoubles(param204).getEchoDoublesReturn();
    }

    public String[] sendStrings(int input) throws java.lang.Exception {
        SendStrings param206 = new SendStrings();
        param206.setSize(input);
        return stub.sendStrings(param206).getSendStringsReturn();
    }

    public SimpleEvent[] echoSimpleEvents(SimpleEvent[] input) throws java.lang.Exception {
        EchoSimpleEventsRequest request = new EchoSimpleEventsRequest();
        ArrayOfSimpleEvent array = new ArrayOfSimpleEvent();
        array.setItem(input);
        request.setInput(array);
        return stub.echoSimpleEvents(request).getEchoSimpleEventsReturn().getItem();
    }

    public int receiveSimpleEvents(SimpleEvent[] input) throws java.lang.Exception {
        ReceiveSimpleEventsRequest request = new ReceiveSimpleEventsRequest();
        ArrayOfSimpleEvent array = new ArrayOfSimpleEvent();
        array.setItem(input);
        request.setInput(array);
        return stub.receiveSimpleEvents(request).getReceiveSimpleEventsReturn();
    }

    public SimpleEvent[] sendSimpleEvents(int size) throws java.lang.Exception {
        SendSimpleEventsRequest request = new SendSimpleEventsRequest();
        request.setSize(size);
        return stub.sendSimpleEvents(request).getSendSimpleEventsReturn().getItem();
    }

    public MeshInterfaceObject[] echoMeshInterfaceObjects(MeshInterfaceObject[] input) throws java.lang.Exception {
        EchoMeshInterfaceObjectsRequest request = new EchoMeshInterfaceObjectsRequest();
        request.setInput(input);
        return stub.echoMeshInterfaceObjects(request).getEchoMeshInterfaceObjectReturn();
    }

    public int receiveMeshInterfaceObjects(MeshInterfaceObject[] input) throws java.lang.Exception {
        ReceiveMeshInterfaceObjectsRequest request = new ReceiveMeshInterfaceObjectsRequest();
        ArrayOfMeshInterfaceObject array = new ArrayOfMeshInterfaceObject();
        array.setItem(input);
        request.setInput(array);
        return stub.receiveMeshInterfaceObjects(request).getReceiveMeshInterfaceObjectsReturn();
    }

    public MeshInterfaceObject[] sendMeshInterfaceObjects(int size) throws java.lang.Exception {
        SendMeshInterfaceObjectsRequest request = new SendMeshInterfaceObjectsRequest();
        request.setSize(size);
        return stub.sendMeshInterfaceObjects(request).getSendMeshInterfaceObjectsReturn().getItem();
    }

    private byte[] getArray(DataHandler data) {
        byte[] array;
        try {
            return IOUtils.getStreamAsByteArray(data.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


