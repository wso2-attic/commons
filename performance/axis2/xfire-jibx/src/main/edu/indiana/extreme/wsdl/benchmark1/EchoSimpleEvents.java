package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoSimpleEvents {
  public void addInput(SimpleEvent input) {
    inputList.add(input);
  }

  public SimpleEvent getInput(int index) {
    return (SimpleEvent)inputList.get( index );
  }

  public int sizeInputList() {
    return inputList.size();
  }

  protected ArrayList inputList = new ArrayList();

}
