/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web.backing;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import tds.ContentUploader.Web.BasePage;

@ManagedBean (name = "loginBacking")
public class LoginBacking extends BasePage
{

  String textUser     = null;
  String textPassword = null;

  public void setTextUser (String value)
  {
    textUser = value;
  }

  public String getTextUser ()
  {
    return textUser;
  }

  public String getTextPassword ()
  {
    return textPassword;
  }

  public void setTextPassword (String value)
  {
    textPassword = value;
  }

  public LoginBacking () {
    super ();
  }

  public void processLogin (ActionEvent e)
  {
    String s="";
  }
}
