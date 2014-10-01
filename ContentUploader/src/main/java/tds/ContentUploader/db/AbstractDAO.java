/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tds.ContentUploader.Web.ConnectionInfo;
import tds.ContentUploader.Web.ContentApplication;
import AIR.Common.DB.AbstractDLL;
import AIR.Common.DB.SQLConnection;

/**
 * @author mpatel
 *
 */
public class AbstractDAO extends AbstractDLL
{
  private static final Logger _logger = LoggerFactory.getLogger (AbstractDAO.class);
  @Autowired
  private ContentApplication _contentapplication;
  
  public SQLConnection getSQLConnection () throws SQLException {
    try {
      String itemBankConnName = _contentapplication.getItemBankConnectionName ();
      ConnectionInfo connectionInfo = new ConnectionInfo(itemBankConnName,_contentapplication.getItemBankConnectionString ());
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
      throw new SQLException ("Error Creating SQLConnection.. "+e.getMessage (),e);
    }
  }
  
  
  
}
