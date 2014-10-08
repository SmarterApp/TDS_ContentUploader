/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentestsystem.shared.test.LifecycleManagingTestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tds.ContentUploader.ItemBankFile;
import tds.ContentUploader.sql.IItemBankRepository;
import AIR.Common.DB.AbstractConnectionManager;
import AIR.Common.DB.SQLConnection;

@RunWith (LifecycleManagingTestRunner.class)
@ContextConfiguration ("/test-context.xml")
public class TestItemBankRepository
{

  @Autowired
  private AbstractConnectionManager _connectionManager = null;
  
  @Autowired 
  private IItemBankRepository        _itemBankRepository            = null;

  @Test
  public void testRepgetContentFiles() throws Exception {
    try (SQLConnection connection = _connectionManager.getConnection ()) {
      
      Map<String, ItemBankFile> items = new HashMap<String, ItemBankFile> ();
      Map<String, ItemBankFile> stimuli = new HashMap<String, ItemBankFile> ();
      
      _itemBankRepository.getContentFiles (items, stimuli);
      System.out.println ("here we are");
    } catch (Exception e) {
      System.out.println ("Exception: " + e.getMessage ());
      throw e;
    }
  }
  
}
