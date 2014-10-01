/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web;

/**
 * @author mpatel
 *
 */
public class ConnectionInfo
{
  private String name;
  private String label;
  private String connectionString;
  private String initCatalog;
  private String userId;
  private String password;
  private String datasource;
  private String connectTimeout;
  private String connectionURL;
  private String databaseClass;
  
  /**
   * @param name
   * @param label
   */
  public ConnectionInfo (String name,String connectionString) {
    super ();
    this.name = name;
    this.connectionString = connectionString;
    parseConnectionString ();
  }
  
  private void parseConnectionString() {
    String[] connInfo = connectionString.split (";");
    for(String params:connInfo) {
      String[] param = params.split("=");
      String paramName = param[0].toLowerCase ();
      switch(paramName) {
        case "user id" :
          this.userId = param[1];
          break;
        case "password" :
          this.password = param[1];
          break;
        case "initial catalog" :
          this.initCatalog = param[1];
          break;
        case "data source" :
          this.datasource = param[1];
          break;
        case "connect timeout":
          this.connectTimeout = param[1];
          break;
        case "url":
          this.connectionURL = param[1];
          break;
        default:
          throw new IllegalArgumentException("Invalid Connection String Value : "+paramName);
      }
    }
    this.label = String.format ("%s (%s)", this.initCatalog,this.datasource);
    if(this.connectionURL!=null) {
      if(this.connectionURL.toLowerCase ().contains ("mysql")) {
        this.databaseClass = "com.mysql.jdbc.Driver";
      } else if (this.connectionURL.toLowerCase ().contains ("sqlserver")) {
        this.databaseClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
      }
    }
  }
  
  public static void main (String[] args) {
    String connectionString = "User ID=dbtds;password=KOJ89238876234rUHJ;Initial Catalog=TDSCore_Dev_ItemBank_2012;Data Source=38.118.82.146;Connect Timeout=60" ;
    System.out.println (new ConnectionInfo ("ITEMBANK_TDSCore_Dev_2012", connectionString));
  }
  
  public String getName () {
    return name;
  }
  public String getLabel () {
    return label;
  }

  public void setLabel (String label) {
    this.label = label;
  }

  public String getConnectionString () {
    return connectionString;
  }

  public void setConnectionString (String connectionString) {
    this.connectionString = connectionString;
  }

  public String getInitCatalog () {
    return initCatalog;
  }

  public String getUserId () {
    return userId;
  }

  public String getPassword () {
    return password;
  }

  public String getDatasource () {
    return datasource;
  }

  public String getConnectTimeout () {
    return connectTimeout;
  }

  
  public String getConnectionURL () {
    return connectionURL;
  }
  
  

  public String getDatabaseClass () {
    return databaseClass;
  }

  @Override
  public String toString () {
    return "ConnectionInfo [name=" + name + ", label=" + label + ", connectionString=" + connectionString + ", initCatalog=" + initCatalog + ", userId=" + userId + ", password=" + password
        + ", datasource=" + datasource + ", connectTimeout=" + connectTimeout + "]";
  }
  
  
  
  
}
