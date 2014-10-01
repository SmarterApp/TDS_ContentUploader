/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Test;

import java.util.Iterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentestsystem.shared.test.LifecycleManagingTestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tds.ContentUploader.DLL.ContentUploaderDLL;
import tds.ContentUploader.sql.IItemBankRepository;
import AIR.Common.DB.AbstractConnectionManager;
import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.MultiDataResultSet;
import AIR.Common.DB.results.SingleDataResultSet;


@RunWith (LifecycleManagingTestRunner.class)
@ContextConfiguration ("/test-context.xml")
public class TestContentUploaderDLL 
{
  @Autowired
  private AbstractConnectionManager _connectionManager = null;

  @Autowired
  private ContentUploaderDLL        _dll               = null;
 
  
  @Test
  public void testgetContentFiles_DLL () throws Exception {

    try (SQLConnection connection = _connectionManager.getConnection ()) {
      
      MultiDataResultSet results = _dll.getContentFiles_DLL (connection, "SBAC_PT");
      //MultiDataResultSet results = _dll.getContentFiles_DLL (connection, "");
      if (results.getCount () == 2) {
        SingleDataResultSet rs1 = results.get (0);
        Iterator<DbResultRecord> records = rs1.getRecords ();
        while (records.hasNext ()) {
          DbResultRecord rcd = records.next ();
          String _key = rcd.<String> get ("_key");
          Long bankKey = rcd.<Long> get ("bankkey");
          Long itemKey = rcd.<Long> get ("itemkey");
          String filePath = rcd.<String> get ("filepath");
          System.out.println (String.format ("%s; %d; %d; %s", _key, bankKey, itemKey, filePath));
        }
        SingleDataResultSet rs2 = results.get (1);
        records = rs2.getRecords ();
        while (records.hasNext ()) {
          DbResultRecord rcd = records.next ();
          // dump set 2
          String _key = rcd.<String> get ("_key");
          Long bankKey = rcd.<Long> get ("bankkey");
          Long itemKey = rcd.<Long> get ("itemkey");
          String filePath = rcd.<String> get ("filepath");
          System.out.println (String.format ("%s; %d; %d; %s", _key, bankKey, itemKey, filePath));
        }

      }
    } catch (Exception e) {
      System.out.println ("Exception: " + e.getMessage ());
      throw e;
    }
  }

  @Test
  public void testT_ListClientTests_2011_S () throws Exception {

    try (SQLConnection connection = _connectionManager.getConnection ()) {

      SingleDataResultSet rs = _dll.T_ListClientTests_2011_SP (connection, "Minnesota_PT");
      Iterator<DbResultRecord> records = rs.getRecords ();
      while (records.hasNext ()) {
        DbResultRecord rcd = records.next ();
        String key = rcd.<String> get ("_key");
        String language = rcd.<String> get ("language");
        System.out.println (String.format ("%s; %s", key, language));
      }
    } catch (Exception e) {
      System.out.println ("Exception: " + e.getMessage ());
      throw e;
    }
  }

  @Test
  public void testT_GetItempool_2011_SP () throws Exception {

    try (SQLConnection connection = _connectionManager.getConnection ()) {
      // MultiDataResultSet results = _dll.T_GetItempool_2011_SP(connection, "(Minnesota_PT)GRAD-Mathematics-11-Fall-2011-2012", "ENU");
      MultiDataResultSet results = _dll.T_GetItempool_2011_SP (connection, "(SBAC_PT)SBAC-Perf-ELA-11-Spring-2013-2015", "ENU");
      if (results.getCount () == 2) {
        SingleDataResultSet rs1 = results.get (0);
        Iterator<DbResultRecord> records = rs1.getRecords ();
        while (records.hasNext ()) {
          DbResultRecord rcd = records.next ();
          String id = rcd.<String> get ("groupid");
          String filePath = rcd.<String> get ("stimulusfile");
          Long bankKey = rcd.<Long> get ("_efk_itembank");
          Long itemKey = rcd.<Long> get ("_efk_itskey");
          Integer groupItemsRequired = rcd.<Integer> get ("GroupItemsRequired");

          System.out.println (String.format ("%s; %s; %d; %d; %d", id, filePath, bankKey, itemKey, groupItemsRequired));
        }
        SingleDataResultSet rs2 = results.get (1);
        records = rs2.getRecords ();
        while (records.hasNext ()) {
          DbResultRecord rcd = records.next ();
          // dump set 2
          String itemID = rcd.<String> get ("ItemID");
          String groupID = rcd.<String> get ("GroupID");
          String blockID = rcd.<String> get ("BlockID");
          String itemType = rcd.<String> get ("ItemType");
          String answer = rcd.<String> get ("Answer");
          String contentLevel = rcd.<String> get ("ContentLevel");
          String filePath = rcd.<String> get ("ItemFile");
          Long bankKey = rcd.<Long> get ("_efk_Itembank");
          Long itemKey = rcd.<Long> get ("_efk_Item");
          Integer scorePoint = rcd.<Integer> get ("ScorePoint");
          Integer itemPosition = rcd.<Integer> get ("ItemPosition");
          Boolean isRequired = rcd.<Boolean> get ("itemRequired");
          Boolean isActive = rcd.<Boolean> get ("IsActive");
          Boolean isFieldTest = rcd.<Boolean> get ("IsFieldTest");
          Boolean isPrintable = rcd.<Boolean> get ("IsPrintable");
          String IRT_b = rcd.<String> get ("IRT_b");
          System.out.println (String.format ("%s; %s; %s; %s; %s; %s; %s; %d; %d; %d; %d; %s; %s; %s; %s; %s",
              itemID, groupID, blockID, itemType, answer, contentLevel, filePath,
              bankKey, itemKey, scorePoint, itemPosition,
              isRequired, isActive, isFieldTest, isPrintable, IRT_b));
        }
      }
    } catch (Exception e) {
      System.out.println ("Exception: " + e.getMessage ());
      throw e;
    }
  }

  @Test
  public void testgetTestProperties_DLL () throws Exception {

    try (SQLConnection connection = _connectionManager.getConnection ()) {
      SingleDataResultSet rs = _dll.getTestProperties_DLL (connection, "Minnesota_PT", "(Minnesota_PT)GRAD-Reading-10-Fall-2011-2012");
      Iterator<DbResultRecord> records = rs.getRecords ();
      while (records.hasNext ()) {
        DbResultRecord rcd = records.next ();
        rs.setFixNulls (false);
        String key = rcd.<String> get ("_key");
        String subject = rcd.<String> get ("subject").trim();
        String schoolYear = rcd.<String> get ("schoolYear").trim();
        String season = rcd.<String> get ("season").trim();
        String gradeText = rcd.<String> get ("gradeText").trim();
        String gradeCode = rcd.<String> get ("gradeCode").trim();
        String gradeSpan = rcd.<String> get ("gradeSpan").trim();
        String id = rcd.<String> get ("testid").trim();
        String displayName = rcd.<String> get ("displayname").trim();
        String clientName = rcd.<String> get ("clientname").trim();
        Boolean scorebyTds = rcd.<Boolean> get ("scoreByTds");
        Boolean isSelectable = rcd.<Boolean> get ("isSelectable");
    
        System.out.println (String.format ("%s; %s; %s; %s; %s; %s; %s; %s; %s; %s; %s;  %s;", 
            key,subject, schoolYear, season, gradeText, gradeCode, gradeSpan, id, displayName, clientName, scorebyTds, isSelectable));
        
        rs.setFixNulls (true);
        Integer maxOpportunities = rcd.<Integer>get("MaxOpportunities");
        Integer minItems = rcd.<Integer>get("MinItems");
        Integer maxItems = rcd.<Integer>get("MaxItems");
        Integer prefetch = rcd.<Integer>get("Prefetch");
        System.out.println (String.format ("%d; %d; %d; %d;", maxOpportunities, minItems, maxItems, prefetch));
      }
    } catch (Exception e) {
      System.out.println ("Exception: " + e.getMessage ());
      throw e;
    }
  }
}
