/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.DLL;

import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.results.MultiDataResultSet;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Exceptions.ReturnStatusException;

public interface IContentUploaderDLL
{
  public MultiDataResultSet getContentFiles_DLL (SQLConnection connection, String clientName) throws ReturnStatusException;

  public SingleDataResultSet getTestProperties_DLL (SQLConnection connection, String clientName,String testKey) throws ReturnStatusException;

  public SingleDataResultSet T_ListClientTests_2011_SP(SQLConnection connection, String clientName) throws ReturnStatusException;

  public MultiDataResultSet T_GetItempool_2011_SP (SQLConnection connection, String testKey, String language) throws ReturnStatusException;

}
