package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoDoublesResponse {
  public void addEchoDoublesReturn(java.lang.Double echoDoublesReturn) {
    echoDoublesReturnList.add(echoDoublesReturn);
  }

  public java.lang.Double getEchoDoublesReturn(int index) {
    return (java.lang.Double)echoDoublesReturnList.get( index );
  }

  public int sizeEchoDoublesReturnList() {
    return echoDoublesReturnList.size();
  }

  protected ArrayList echoDoublesReturnList = new ArrayList();

}
