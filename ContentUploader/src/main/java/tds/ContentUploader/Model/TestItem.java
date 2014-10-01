/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Model;

public class TestItem
{
  private String  _itemID;
  private String  _groupID;
  private String  _blockID;
  private String  _itemType;
  private String  _answer;
  private String  _contentLevel;
  private String  _filePath;
  private long    _bankKey;
  private long    _itemKey;
  private int     _scorePoint;
  private int     _itemPosition;
  private boolean _isRequired;
  private boolean _isActive;
  private boolean _isFieldTest;
  private boolean _isPrintable;

  @Override
  public String toString ()
  {
    return getGroupID ();
  }

  public String getItemID () {
    return _itemID;
  }

  public void setItemID (String value) {
    this._itemID = value;
  }

  public String getGroupID () {
    return _groupID;
  }

  public void setGroupID (String value) {
    this._groupID = value;
  }

  public String getBlockID () {
    return _blockID;
  }

  public void setBlockID (String value) {
    this._blockID = value;
  }

  public String getItemType () {
    return _itemType;
  }

  public void setItemType (String value) {
    this._itemType = value;
  }

  public String getAnswer () {
    return _answer;
  }

  public void setAnswer (String value) {
    this._answer = value;
  }

  public String getContentLevel () {
    return _contentLevel;
  }

  public void setContentLevel (String value) {
    this._contentLevel = value;
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

  public int getScorePoint () {
    return _scorePoint;
  }

  public void setScorePoint (int value) {
    this._scorePoint = value;
  }

  public int getItemPosition () {
    return _itemPosition;
  }

  public void setItemPosition (int value) {
    this._itemPosition = value;
  }

  public boolean getIsRequired () {
    return _isRequired;
  }

  public void setIsRequired (boolean value) {
    this._isRequired = value;
  }

  public boolean getIsActive () {
    return _isActive;
  }

  public void setIsActive (boolean value) {
    this._isActive = value;
  }

  public boolean getIsFieldTest () {
    return _isFieldTest;
  }

  public void setIsFieldTest (boolean value) {
    this._isFieldTest = value;
  }

  public boolean getisPrintable () {
    return _isPrintable;
  }

  public void setIsPrintable (boolean value) {
    this._isPrintable = value;
  }
}
