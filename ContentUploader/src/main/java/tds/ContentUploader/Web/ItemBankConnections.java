/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mpatel
 *
 */
public class ItemBankConnections
{
  
  /**
   * @param connectionStrings
   */
  public ItemBankConnections (Map<String, String> connectionStrings) {
    this.connectionStrings = connectionStrings;
  }

  private Map<String, String> connectionStrings;

  public Map<String, String> getConnectionStrings () {
    return connectionStrings;
  }
  
  public String getConnectionStrings (String connectionName) {
    return connectionStrings.get (connectionName);
  }
  
  public int getSize(){
    return connectionStrings.size ();
  }
  
  public List<String> getConnectionNames() {
    return new ArrayList<String> (connectionStrings.keySet ());
  }
  
}
