package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class ReceiveMeshInterfaceObjects {
  public void addInput(MeshInterfaceObject input) {
    inputList.add(input);
  }

  public MeshInterfaceObject getInput(int index) {
    return (MeshInterfaceObject)inputList.get( index );
  }

  public int sizeInputList() {
    return inputList.size();
  }

  protected ArrayList inputList = new ArrayList();

}
