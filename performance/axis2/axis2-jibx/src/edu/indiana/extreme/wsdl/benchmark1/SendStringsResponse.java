package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class SendStringsResponse {
  public void addSendStringsReturn(String sendStringsReturn) {
    sendStringsReturnList.add(sendStringsReturn);
  }

  public String getSendStringsReturn(int index) {
    return (String)sendStringsReturnList.get( index );
  }

  public int sizeSendStringsReturnList() {
    return sendStringsReturnList.size();
  }

  protected ArrayList sendStringsReturnList = new ArrayList();

}
