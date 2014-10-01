/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Utilities;

/**
 * Currently not used
 * 
 * @author jmambo
 *
 */
public class ControlHelper
{

  /*
   * TODO: implement when required. Get Java equivalent of C# control
  public static Control getPostBackControl(Page page)
  {
      Control control = null;
      String ctrlname = page.Request.Params["__EVENTTARGET"];
      
      if (!StringUtils.IsEmpty(ctrlname))
      {
          control = page.FindControl(ctrlname);
      }
      // if __EVENTTARGET is null, the control is a button type and we need to 
      // iterate over the form collection to find it
      else
      {
          string ctrlStr = String.Empty;
          Control c = null;
          foreach (string ctl in page.Request.Form)
          {
              if (ctl == null) continue;

              // handle ImageButton controls ...
              if (ctl.EndsWith(".x") || ctl.EndsWith(".y"))
              {
                  ctrlStr = ctl.Substring(0, ctl.Length - 2);
                  c = page.FindControl(ctrlStr);
              }
              else
              {
                  c = page.FindControl(ctl);
              }
              if (c is System.Web.UI.WebControls.Button ||
                       c is System.Web.UI.WebControls.ImageButton)
              {
                  control = c;
                  break;
              }
          }
      }
      return control;
  }
  */

}
