/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;
import tds.itemrenderer.data.IITSDocument;

public/* static */class ITSDocumentExtensions
{
  public static String getGroupID (/* this */IITSDocument document)
  {
    boolean hasPassage = (document.getStimulusKey () > 0);
    String groupID = (hasPassage ? "G-" : "I-") + document.getBankKey () + "-" + (hasPassage ? document.getStimulusKey () : document.getItemKey ());
    return groupID;
  }
}
