/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;

import AIR.Common.Utilities.Path;

public class ItemBankFile
{
  private String _key;
  private long   _bankKey;
  private long   _itemKey;

  private String _filePath;

  public String getDirectory ()
  {
    return Path.getDirectoryName (_filePath);
  }

  public boolean equals (ItemBankFile other)
  {
    if (other == null)
      return false;
    if (equals (other))
      return true;
    return other.getKey ().equals (getKey ());
  }

  @Override
  public boolean equals (Object obj)
  {
    if (obj == null)
      return false;
    if (equals (obj))
      return true;
    if (!(obj instanceof ItemBankFile))
      return false;
    return equals ((ItemBankFile) obj);
  }

  @Override
  public int hashCode ()
  {
    return (_key != null ? _key.hashCode () : 0);
  }

  @Override
  public String toString ()
  {
    return _key;
  }

  public String getKey () {
    return _key;
  }

  public void setKey (String value) {
    this._key = value;
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

  public String getFilePath () {
    return _filePath;
  }

  public void setFilePath (String value) {
    this._filePath = value;
  }
}
