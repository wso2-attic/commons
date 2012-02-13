package edu.indiana.extreme.wsdl.benchmark1;

import java.util.ArrayList;


public class SendSimpleEventsResponse {
  public void addItem(SimpleEvent item) {
    itemList.add(item);
  }

  public SimpleEvent getItem(int index) {
    return (SimpleEvent)itemList.get( index );
  }

  public int sizeItemList() {
    return itemList.size();
  }

  protected ArrayList itemList = new ArrayList();

}
