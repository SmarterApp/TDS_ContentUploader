/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Model;

import java.util.Date;

public class ITSZip
{
  private String _name;
  private String _fileName;
  private String _fileSize;
  private long _fileSizeBytes;
  // TODO Shajib: DateTime used in .net
  private Date   _dateUploaded;
  private String _dateDisplayed;
  
  public String getName () {
    return _name;
  }

  public void setName (String value) {
    this._name = value;
  }

  public String getFileName () {
    return _fileName;
  }

  public void setFileName (String value) {
    this._fileName = value;
  }

  public String getFileSize () {
    return _fileSize;
  }

  public void setFileSize (String value) {
    this._fileSize = value;
  }

  public Date getDateUploaded () {
    return _dateUploaded;
  }

  public void setDateUploaded (Date value) {
    this._dateUploaded = value;
  }

  public String getDateDisplayed () {
    return _dateDisplayed;
  }

  public void setDateDisplayed (String dateDisplayed) {
    this._dateDisplayed = dateDisplayed;
  }
  
  

  public long getFileSizeBytes () {
    return _fileSizeBytes;
  }

  public void setFileSizeBytes (long fileSizeBytes) {
    this._fileSizeBytes = fileSizeBytes;
  }

  @Override
  public String toString () {
    return "ITSZip [_name=" + _name + ", _fileName=" + _fileName + ", _fileSize=" + _fileSize + ", _dateUploaded=" + _dateUploaded + ", _dateDisplayed=" + _dateDisplayed + "]";
  }
  
  
}
