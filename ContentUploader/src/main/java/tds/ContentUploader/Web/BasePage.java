/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.visit.FullVisitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import AIR.Common.DB.SQLConnection;
import AIR.Common.Utilities.SpringApplicationContext;
import AIR.Common.Web.WebHelper;
import AIR.Common.Web.Session.Server;


public class BasePage extends TDS.Shared.Web.BasePage
{
  private static final Logger _logger = LoggerFactory.getLogger (BasePage.class);
  private ContentApplication _contentApplication;
  public BasePage () {
    super ();
    _contentApplication = SpringApplicationContext.getBean (ContentApplication.class);
  }

  public String getTestKey ()
  {
    return WebHelper.getQueryString ("testKey");
  }

  public String getLanguage ()
  {
    return WebHelper.getQueryString ("language");
  }

  // TODO Shajib: this method was virtual
  public String getUrl (String path)
  {
    String url = Server.resolveUrl (path);

    String connectionName = _contentApplication.getItemBankConnectionName ();

    if (connectionName != null)
    {
      url += "?name=" + connectionName;
    }

    return url;
  }
  
  protected void executeBatch (List<String> batchQueryList) throws SQLException{
    try(Connection connection = getSQLConnection ()) {
      Statement statement = connection.createStatement ();
      for(String query:batchQueryList) {
        statement.addBatch (query);
      }
      statement.executeBatch ();
      statement.close ();
    } catch (SQLException e) {
      _logger.error (e.getMessage (),e);
      throw new SQLException ("Error during executeBatch .. "+e.getMessage (),e);
    } 
  }
  
  public SQLConnection getSQLConnection () throws SQLException {
    try {
      String itemBankConnName = _contentApplication.getItemBankConnectionName ();
      ConnectionInfo connectionInfo = new ConnectionInfo(itemBankConnName,_contentApplication.getItemBankConnectionString ());
      try
      {
        Class.forName (connectionInfo.getDatabaseClass ());
      } catch (ClassNotFoundException e) {
        _logger.error (e.getMessage (),e);
        throw new SQLException("JDBC Driver Class not found !! -- "+connectionInfo.getDatabaseClass (),e);
      }
      Connection connection = DriverManager.getConnection (connectionInfo.getConnectionURL (), connectionInfo.getUserId (), connectionInfo.getPassword ());
      return new SQLConnection (connection);
    } catch (Exception e) {
      _logger.error (e.getMessage (),e);
      throw new SQLException ("Error Creating SQLConnection.. "+e.getMessage (),e);
    }
  }
  
  protected String readableFileSize(long size) {
    if(size <= 0) return "0";
    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }
  
  protected UIComponent findComponent(final String id) {
    FacesContext context = FacesContext.getCurrentInstance(); 
    UIViewRoot root = context.getViewRoot();
    final UIComponent[] found = new UIComponent[1];
    root.visitTree(new FullVisitContext(context), new VisitCallback() {     
        @Override
        public VisitResult visit(VisitContext context, UIComponent component) {
            if(component.getId().equals(id)){
                found[0] = component;
                return VisitResult.COMPLETE;
            }
            return VisitResult.ACCEPT;              
        }
    });
    return found[0];
  }
  
  protected void writeMessage(String message,MessageType messageType ) {
    FacesContext.getCurrentInstance().addMessage(null, 
        new FacesMessage(message));
    javax.faces.component.html.HtmlMessages messageComp = (javax.faces.component.html.HtmlMessages)findComponent ("displayMessage");
    if(MessageType.SUCCESS == messageType) {
      messageComp.setStyle ("color:blue;margin:8px;");
    } else {
      messageComp.setStyle ("color:red;margin:8px;");
    }
  }
  
  protected static enum MessageType{
    ERROR,
    SUCCESS;
  }
}

