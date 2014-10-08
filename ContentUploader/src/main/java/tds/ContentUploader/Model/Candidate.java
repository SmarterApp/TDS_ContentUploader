/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Model;

public class Candidate
{
  private long   _testeeKey;
  private String _testID;
  private int    _opportunity;

  public Candidate ()
  {
  }

  public Candidate (long testeeKey, String testID, int opportunity)
  {
    setTesteeKey (testeeKey);
    setTestID (testID);
    setOpportunity (opportunity);
  }

  public long getTesteeKey () {
    return _testeeKey;
  }

  public void setTesteeKey (long value) {
    this._testeeKey = value;
  }

  public String getTestID () {
    return _testID;
  }

  public void setTestID (String value) {
    this._testID = value;
  }

  public int getOpportunity () {
    return _opportunity;
  }

  public void setOpportunity (int value) {
    this._opportunity = value;
  }
}
