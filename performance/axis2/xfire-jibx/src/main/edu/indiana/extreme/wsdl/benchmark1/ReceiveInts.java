package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class ReceiveInts {
  public void addInput(java.lang.Integer input) {
    inputList.add(input);
  }

  public java.lang.Integer getInput(int index) {
    return (java.lang.Integer)inputList.get( index );
  }

  public int sizeInputList() {
    return inputList.size();
  }

  protected ArrayList inputList = new ArrayList();

}
