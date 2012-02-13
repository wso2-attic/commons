
package edu.indiana.extreme.wsdl.benchmark1;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "Benchmark", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Benchmark {


    @WebMethod(operationName = "echoVoid", action = "echoVoid")
    @WebResult(name = "echoVoidResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoVoidResponse echoVoid(
        @WebParam(name = "echoVoid", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoVoid echoVoid);

    @WebMethod(operationName = "echoStrings", action = "echoStrings")
    @WebResult(name = "echoStringsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoStringsResponse echoStrings(
        @WebParam(name = "echoStrings", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoStrings echoStrings);

    @WebMethod(operationName = "sendInts", action = "sendInts")
    @WebResult(name = "sendIntsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public SendIntsResponse sendInts(
        @WebParam(name = "sendInts", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        SendInts sendInts);

    @WebMethod(operationName = "receiveBase64", action = "receiveBase64")
    @WebResult(name = "receiveBase64Response", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public ReceiveBase64Response receiveBase64(
        @WebParam(name = "receiveBase64", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        ReceiveBase64 receiveBase64);

    @WebMethod(operationName = "receiveDoubles", action = "receiveDoubles")
    @WebResult(name = "receiveDoublesResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public ReceiveDoublesResponse receiveDoubles(
        @WebParam(name = "receiveDoubles", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        ReceiveDoubles receiveDoubles);

    @WebMethod(operationName = "sendMeshInterfaceObjects", action = "sendMeshInterfaceObjects")
    @WebResult(name = "sendMeshInterfaceObjectsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public SendMeshInterfaceObjectsResponse sendMeshInterfaceObjects(
        @WebParam(name = "sendMeshInterfaceObjects", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        SendMeshInterfaceObjects sendMeshInterfaceObjects);

    @WebMethod(operationName = "receiveMeshInterfaceObjects", action = "receiveMeshInterfaceObjects")
    @WebResult(name = "receiveMeshInterfaceObjectsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public ReceiveMeshInterfaceObjectsResponse receiveMeshInterfaceObjects(
        @WebParam(name = "receiveMeshInterfaceObjects", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        ReceiveMeshInterfaceObjects receiveMeshInterfaceObjects);

    @WebMethod(operationName = "echoBase64", action = "echoBase64")
    @WebResult(name = "echoBase64Response", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoBase64Response echoBase64(
        @WebParam(name = "echoBase64", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoBase64 echoBase64);

    @WebMethod(operationName = "echoSimpleEvents", action = "echoSimpleEvents")
    @WebResult(name = "echoSimpleEventsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoSimpleEventsResponse echoSimpleEvents(
        @WebParam(name = "echoSimpleEvents", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoSimpleEvents echoSimpleEvents);

    @WebMethod(operationName = "receiveStrings", action = "receiveStrings")
    @WebResult(name = "receiveStringsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public ReceiveStringsResponse receiveStrings(
        @WebParam(name = "receiveStrings", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        ReceiveStrings receiveStrings);

    @WebMethod(operationName = "echoInts", action = "echoInts")
    @WebResult(name = "echoIntsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoIntsResponse echoInts(
        @WebParam(name = "echoInts", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoInts echoInts);

    @WebMethod(operationName = "receiveInts", action = "receiveInts")
    @WebResult(name = "receiveIntsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public ReceiveIntsResponse receiveInts(
        @WebParam(name = "receiveInts", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        ReceiveInts receiveInts);

    @WebMethod(operationName = "sendDoubles", action = "sendDoubles")
    @WebResult(name = "sendDoublesResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public SendDoublesResponse sendDoubles(
        @WebParam(name = "sendDoubles", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        SendDoubles sendDoubles);

    @WebMethod(operationName = "sendBase64", action = "sendBase64")
    @WebResult(name = "sendBase64Response", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public SendBase64Response sendBase64(
        @WebParam(name = "sendBase64", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        SendBase64 sendBase64);

    @WebMethod(operationName = "echoDoubles", action = "echoDoubles")
    @WebResult(name = "echoDoublesResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoDoublesResponse echoDoubles(
        @WebParam(name = "echoDoubles", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoDoubles echoDoubles);

    @WebMethod(operationName = "sendSimpleEvents", action = "sendSimpleEvents")
    @WebResult(name = "sendSimpleEventsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public SendSimpleEventsResponse sendSimpleEvents(
        @WebParam(name = "sendSimpleEvents", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        SendSimpleEvents sendSimpleEvents);

    @WebMethod(operationName = "sendStrings", action = "sendStrings")
    @WebResult(name = "sendStringsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public SendStringsResponse sendStrings(
        @WebParam(name = "sendStrings", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        SendStrings sendStrings);

    @WebMethod(operationName = "echoMeshInterfaceObjects", action = "echoMeshInterfaceObjects")
    @WebResult(name = "echoMeshInterfaceObjectsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public EchoMeshInterfaceObjectsResponse echoMeshInterfaceObjects(
        @WebParam(name = "echoMeshInterfaceObjects", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        EchoMeshInterfaceObjects echoMeshInterfaceObjects);

    @WebMethod(operationName = "receiveSimpleEvents", action = "receiveSimpleEvents")
    @WebResult(name = "receiveSimpleEventsResponse", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
    public ReceiveSimpleEventsResponse receiveSimpleEvents(
        @WebParam(name = "receiveSimpleEvents", targetNamespace = "http://www.extreme.indiana.edu/wsdl/Benchmark1")
        ReceiveSimpleEvents receiveSimpleEvents);

}
