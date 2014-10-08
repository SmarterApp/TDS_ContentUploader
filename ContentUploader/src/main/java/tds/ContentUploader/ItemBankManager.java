/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tds.ContentUploader.Model.ItemGroups;
import tds.ContentUploader.Model.TestProperties;
import tds.ContentUploader.Web.ContentApplication;
import tds.ContentUploader.sql.IItemBankRepository;

@Component
public class ItemBankManager
{
  private static Object                  _syncRoot  = new Object ();                     // for
                                                                                                       // locking
  private static Map<String, ItemGroups> _itemPools = new HashMap<String, ItemGroups> ();

  private boolean                                     _allowCaching;
  @Autowired
  private IItemBankRepository                                  _itemBankRepository;
  @Autowired
  private ContentApplication _contentApplication;


  public ItemBankManager ()
  {
    _allowCaching = false;
  }
  
  
  public ContentApplication getContentApplication () {
    return _contentApplication;
  }


  public List<TestProperties> listTests ()
  {
    return _itemBankRepository.GetTestProperties (null);
  }

  public TestProperties getTest (String testKey)
  {
    List<TestProperties> testProperties = _itemBankRepository.GetTestProperties (testKey);
    return (testProperties.size () > 0) ? testProperties.get (0) : null;
  }

  public ItemGroups GetItemGroups (String testKey, String language)
  {
    // get item pool key
    String id = Integer.toString (getItemBankConnectionString ().hashCode ());
    String key = id + "-" + testKey + "-" + language;

    // try and get existing item pool
    ItemGroups itemGroups = _itemPools.get (key);

    if (itemGroups == null)
    {
      if (_allowCaching)
      {
        synchronized (_syncRoot)
        {
          itemGroups = _itemPools.get (key);

          if (itemGroups == null)
          {
            // load item pool data
            itemGroups = LoadItemGroups (testKey, language);

            // cache item pool
            _itemPools.put (key, itemGroups);
          }
        }
      }
      else
      {
        // load item pool data
        itemGroups = LoadItemGroups (testKey, language);
      }
    }

    return itemGroups;
  }
  
  public IItemBankRepository CreateItemPreviewRepository()
  {
      return _itemBankRepository;
  }

  private ItemGroups LoadItemGroups (String testKey, String language)
  {
    // get item pool from item bank DB
    return _itemBankRepository.GetItemGroups (testKey, language);
  }

  public boolean isAllowCaching () {
    return _allowCaching;
  }

  public void setAllowCaching (boolean value) {
    this._allowCaching = value;
  }
  
  public String getItemBankConnectionString()
  {
          return _contentApplication.getItemBankConnectionString();
  }


}
