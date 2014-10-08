/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web.backing;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import tds.ContentUploader.Web.BasePage;
import tds.ContentUploader.Web.ConnectionInfo;
import tds.ContentUploader.Web.ItemBankConnections;
import AIR.Common.Utilities.SpringApplicationContext;

/**
 * @author mpatel
 *
 */
@ManagedBean(name="defaultPageBacking")
public class DefaultPageBacking extends BasePage
{
  
  
  public DefaultPageBacking () {
    super ();
    itemBankConnections = SpringApplicationContext.getBean (ItemBankConnections.class);
    init();
  }

  private ItemBankConnections itemBankConnections;
  
  public List<ConnectionInfo> itemBanks = new ArrayList <ConnectionInfo>();

  private  void init()
  {
      for(String connectionName: itemBankConnections.getConnectionStrings ().keySet ()) 
      {
          if (connectionName.toUpperCase ().startsWith ("ITEMBANK"))
          {
              ConnectionInfo connectionInfo = new ConnectionInfo(connectionName,itemBankConnections.getConnectionStrings (connectionName));
              itemBanks.add(connectionInfo);
          }
      }
  }

  public List<ConnectionInfo> getItemBanks () {
    return itemBanks;
  }
  
  
}
