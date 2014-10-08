/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Utilities;

/**
 * Structure containing results of search   (keyword and position in original text)

 * @author jmambo
 *
 */
public class StringSearchResult
{

  private int    _index;
  private String _keyword;


  /**
   *  Initialize string search result
   * @param index Index in text
   * @param keyword Found keyword
   */
  public StringSearchResult (int index, String keyword) {
    _index = index;
    _keyword = keyword;
  }


  /**
   * Gets index of found keyword in original text
   * 
   * @return index of found keyword
   */
  public int getIndex () {
    return _index;
  }


  /**
   * Gets keyword found by this result
   * @return keyword found by this result
   */
  public String getKeyword ()  {
    return _keyword;
  }


  /**
   * Creates a new empty search result
   * 
   * @return empty search result
   */
  public static StringSearchResult empty ()  {
    return new StringSearchResult (-1, "");
  }

}
