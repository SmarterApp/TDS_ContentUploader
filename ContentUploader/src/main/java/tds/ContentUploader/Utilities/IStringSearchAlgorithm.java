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
 * Interface containing all methods to be implemented by string search algorithm
 *
 * @author jmambo
 *
 */
public interface IStringSearchAlgorithm
{

  /**
   * Gets List of keywords to search for
   * 
   * @return String[]  List of keywords to search for
   */
  String[] getKeywords();
  
  /**
   * Sets keywords to search for
   * 
   * @param newKeywords
   */
  void setKeywords(String[] newKeywords);

  /**
   * Searches passed text and returns all occurrences of any keyword
   *  
   * @param text Text to search
   * @return Array of occurrences
   */
  StringSearchResult[] findAll(String text);

  /**
   * Searches passed text and returns first occurrence of any keyword
   * 
   * @param text Text to search
   * @return First occurrence of any keyword (or StringSearchResult.Empty if text doesn't contain any keyword)
   */
  StringSearchResult findFirst(String text);

  /**
   * Searches passed text and returns true if text contains any keyword
   * 
   * @param text Text to search
   * @return True when text contains any keyword
   */
  boolean containsAny(String text);

}
