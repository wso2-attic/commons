package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class SendIntsResponse {
  public void addSendIntsReturn(java.lang.Integer sendIntsReturn) {
    sendIntsReturnList.add(sendIntsReturn);
  }

  public java.lang.Integer getSendIntsReturn(int index) {
    return (java.lang.Integer)sendIntsReturnList.get( index );
  }

  public int sizeSendIntsReturnList() {
    return sendIntsReturnList.size();
  }

  protected ArrayList sendIntsReturnList = new ArrayList();

}
