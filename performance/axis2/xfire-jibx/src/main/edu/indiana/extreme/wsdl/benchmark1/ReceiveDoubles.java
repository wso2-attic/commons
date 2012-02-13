package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class ReceiveDoubles {
  public void addInput(java.lang.Double input) {
    inputList.add(input);
  }

  public java.lang.Double getInput(int index) {
    return (java.lang.Double)inputList.get( index );
  }

  public int sizeInputList() {
    return inputList.size();
  }

  protected ArrayList inputList = new ArrayList();

}
