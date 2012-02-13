package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoIntsResponse {
  public void addEchoIntsReturn(java.lang.Integer echoIntsReturn) {
    echoIntsReturnList.add(echoIntsReturn);
  }

  public java.lang.Integer getEchoIntsReturn(int index) {
    return (java.lang.Integer)echoIntsReturnList.get( index );
  }

  public int sizeEchoIntsReturnList() {
    return echoIntsReturnList.size();
  }

  protected ArrayList echoIntsReturnList = new ArrayList();

}
