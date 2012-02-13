package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoStrings {
  public void addInput(String input) {
    inputList.add(input);
  }

  public String getInput(int index) {
    return (String)inputList.get( index );
  }

  public int sizeInputList() {
    return inputList.size();
  }

  protected ArrayList inputList = new ArrayList();

}
