package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class EchoMeshInterfaceObjectsResponse {
  public void addItem(MeshInterfaceObject item) {
    itemList.add(item);
  }

  public MeshInterfaceObject getItem(int index) {
    return (MeshInterfaceObject)itemList.get( index );
  }

  public int sizeItemList() {
    return itemList.size();
  }

  protected ArrayList itemList = new ArrayList();

}
