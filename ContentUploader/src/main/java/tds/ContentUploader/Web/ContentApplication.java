/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ContentApplication
{
  // / <summary>
  // / This is fired when the ASP.NET app first starts.
  // / </summary>
   
  @Autowired
  private ItemBankConnections itemBankConnections;

  public String getItemBankConnectionName ()
  {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap ().get ("name");
  }

  public String getItemBankConnectionString ()
  {
    String connectionName = getItemBankConnectionName ();

    // try and get connection String from url
    if (StringUtils.isEmpty (connectionName))
    {
      try {
        throw new Exception ("Could not get the item bank connection String. Please select an existing connection name.");
      } catch (Exception e) {
      }
    }

    String connectionString = itemBankConnections.getConnectionStrings (connectionName);

    if (connectionString == null)
    {
      try {
        throw new Exception (String.format ("Could not get the item bank connection String. The connection name \"%s\" could not be found.", connectionName));
      } catch (Exception e) {
      }
    }

    return connectionString;
  }
  
  /*protected void Application_Start ( object sender, EventArgs e )
  {
    Start ();
  }*/

 /* private static void Start ()
  {
    RegisterRepositories ();
  }

  public static void RegisterRepositories ()
  {
    TDSSettings _tdsSettings = FacesContextHelper.getBean ("tdsSettings", TDSSettings.class);
    // setup direct item bank repository
     ServiceLocator.Register<ItemBankRepository>(() => 
    _itemBankRepository = new ItemBankRepository (getItemBankConnectionString (), _tdsSettings.getClientName ());
  }*/
}
