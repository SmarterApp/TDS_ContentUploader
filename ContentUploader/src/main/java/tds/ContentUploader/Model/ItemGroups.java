/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Model;

import java.util.HashMap;
import java.util.List;

public class ItemGroups extends HashMap<String, ItemGroup>
{
  public List<ItemGroup> toList ()
  {
    return this.toList ();
  }

  public ItemGroup GetGroup (String groupID)
  {
    ItemGroup itemGroup = this.get (groupID);
    return itemGroup;
  }
}
