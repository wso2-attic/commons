package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoSimpleEventsResponse {
  public void addEchoSimpleEventsReturn(SimpleEvent echoSimpleEventsReturn) {
    echoSimpleEventsReturnList.add(echoSimpleEventsReturn);
  }

  public SimpleEvent getEchoSimpleEventsReturn(int index) {
    return (SimpleEvent)echoSimpleEventsReturnList.get( index );
  }

  public int sizeEchoSimpleEventsReturnList() {
    return echoSimpleEventsReturnList.size();
  }

  protected ArrayList echoSimpleEventsReturnList = new ArrayList();

}
