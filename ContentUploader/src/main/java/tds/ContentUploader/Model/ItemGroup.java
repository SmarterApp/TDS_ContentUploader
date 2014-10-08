/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemGroup
{
  private String                    _id;
  private String                    _filePath;
  private long                      _bankKey;
  private long                      _itemKey;
  private int                       _groupItemsRequired;

  private List<TestItem>            _itemList   = new ArrayList<TestItem> ();
  private HashMap<String, TestItem> _itemLookup = new HashMap<String, TestItem> ();

  public ItemGroup ()
  {
  }

  public void addItem (TestItem testItem)
  {
    _itemList.add (testItem);
    _itemLookup.put (testItem.getItemID (), testItem);
  }

  public TestItem getItem (String itemID)
  {
    TestItem testItem = _itemLookup.get (itemID);
    return testItem;
  }

  @Override
  public String toString ()
  {
    return getID ();
  }

  public List<TestItem> getItems () {
    return _itemList;
  }

  public String getID () {
    return _id;
  }

  public void setID (String value) {
    this._id = value;
  }

  public String getFilePath () {
    return _filePath;
  }

  public void setFilePath (String value) {
    this._filePath = value;
  }

  public long getBankKey () {
    return _bankKey;
  }

  public void setBankKey (long value) {
    this._bankKey = value;
  }

  public long getItemKey () {
    return _itemKey;
  }

  public void setItemKey (long value) {
    this._itemKey = value;
  }

  public int getGroupItemsRequired () {
    return _groupItemsRequired;
  }

  public void setGroupItemsRequired (int value) {
    this._groupItemsRequired = value;
  }
}
