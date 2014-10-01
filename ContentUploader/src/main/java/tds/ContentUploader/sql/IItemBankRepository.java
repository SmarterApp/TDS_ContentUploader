/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.sql;

import java.util.List;
import java.util.Map;

import tds.ContentUploader.ItemBankFile;
import tds.ContentUploader.Tuple;
import tds.ContentUploader.Model.ItemGroups;
import tds.ContentUploader.Model.TestProperties;

public interface IItemBankRepository
{
  public void getContentFiles (Map<String, ItemBankFile> items, Map<String, ItemBankFile> stimuli); //throws ReturnStatusException;
  public List<TestProperties> GetTestProperties (String testKey); //throws ReturnStatusException;
  public List<Tuple<String, String>> GetTests (); //throws ReturnStatusException;
  public ItemGroups GetItemGroups (String testKey, String language); //throws ReturnStatusException;
}
