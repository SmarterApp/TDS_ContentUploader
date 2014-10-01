/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;
public class Tuple<S1, S2>
{
  private S1 _first;
  private S2 _second;

  public Tuple (S1 first, S2 second) {
    this.setFirst (first);
    this.setSecond (second);
  }

  private S1 getFirst () {
    return _first;
  }

  private void setFirst (S1 value) {
    this._first = value;
  }

  private S2 getSecond () {
    return _second;
  }

  private void setSecond (S2 value) {
    this._second = value;
  }
}
