package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoStringsResponse {
  public void addEchoStringsReturn(String echoStringsReturn) {
    echoStringsReturnList.add(echoStringsReturn);
  }

  public String getEchoStringsReturn(int index) {
    return (String)echoStringsReturnList.get( index );
  }

  public int sizeEchoStringsReturnList() {
    return echoStringsReturnList.size();
  }

  protected ArrayList echoStringsReturnList = new ArrayList();

}
