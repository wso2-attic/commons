package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class SendDoublesResponse {
  public void addSendDoublesReturn(java.lang.Double sendDoublesReturn) {
    sendDoublesReturnList.add(sendDoublesReturn);
  }

  public java.lang.Double getSendDoublesReturn(int index) {
    return (java.lang.Double)sendDoublesReturnList.get( index );
  }

  public int sizeSendDoublesReturnList() {
    return sendDoublesReturnList.size();
  }

  protected ArrayList sendDoublesReturnList = new ArrayList();

}
