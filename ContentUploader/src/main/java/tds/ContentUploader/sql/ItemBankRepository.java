/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tds.ContentUploader.ItemBankFile;
import tds.ContentUploader.Tuple;
import tds.ContentUploader.DLL.ContentUploaderDLL;
import tds.ContentUploader.Model.ItemGroup;
import tds.ContentUploader.Model.ItemGroups;
import tds.ContentUploader.Model.TestItem;
import tds.ContentUploader.Model.TestProperties;
import tds.ContentUploader.db.AbstractDAO;
import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.SqlParametersMaps;
import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.MultiDataResultSet;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Exceptions.ReturnStatusException;

/**
 * @author mpatel
 *
 */
public class ItemBankRepository  extends AbstractDAO implements IItemBankRepository
{
  @Autowired
  private ContentUploaderDLL  _dll       = null;

  private static final Logger _logger    = LoggerFactory.getLogger (ItemBankRepository.class);

  private String              clientName = null;

  public ItemBankRepository () {
    super ();
  }

  public String getClientName () {
    return clientName;
  }

  public void setClientName (String clientName) {
    this.clientName = clientName;
  }

  // public ItemBankRepository (String connectionString, String clientName)
  // {
  // _connectionString = connectionString;
  // _clientName = clientName;
  // }
  //
  // public SQLConnection getConnection ()
  // {
  // if (_connection == null)
  // {
  // try
  // {
  // Class.forName ("com.microsoft.sqlserver.jdbc.SQLServerDriver");
  // } catch (ClassNotFoundException e) {
  // }
  // try
  // {
  // _connection = new SQLConnection (DriverManager.getConnection
  // ("jdbc:sqlserver://38.118.82.146;DatabaseName=TDSCore_Test_Itembank_2012",
  // "dbtds", "KOJ89238876234rUHJ"));
  // } catch (SQLException e) {
  // }
  // }
  // return _connection;
  // }

  private ItemBankFile parser (DbResultRecord reader)
  {
    if (!reader.hasColumn ("FilePath"))
      return null;

    ItemBankFile itemBankFile = new ItemBankFile ();
    itemBankFile.setKey (reader.<String> get ("_Key").trim ());
    itemBankFile.setBankKey (reader.<Long> get ("BankKey"));
    itemBankFile.setItemKey (reader.<Long> get ("ItemKey"));
    itemBankFile.setFilePath (reader.<String> get ("FilePath").trim ());
    return itemBankFile;
  }

  public void getContentFiles (Map<String, ItemBankFile> items, Map<String, ItemBankFile> stimuli) //throws ReturnStatusException
  {

    try (SQLConnection connection = getSQLConnection ()) {

      MultiDataResultSet results = _dll.getContentFiles_DLL (connection, this.clientName);

      Iterator<SingleDataResultSet> singleSets = results.getResultSets ();
      SingleDataResultSet itemData = singleSets.next ();
      ReturnStatusException.getInstanceIfAvailable (itemData);

      Iterator<DbResultRecord> records = itemData.getRecords ();
      // read items
      while (records.hasNext ())
      {
        ItemBankFile itemFile = parser (records.next ());
        if (itemFile != null)
          items.put (itemFile.getKey (), itemFile);
      }

      // read stimuli
      if (singleSets.hasNext ())
      {
        SingleDataResultSet stimuliData = singleSets.next ();
        records = stimuliData.getRecords ();
        while (records.hasNext ())
        {
          ItemBankFile stimulusFile = parser (records.next ());
          if (stimulusFile != null)
            stimuli.put (stimulusFile.getKey (), stimulusFile);
        }
      }
    } catch (SQLException se) {
      _logger.error (se.getMessage (),se);
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage (),re);
    }
  }

  public List<TestProperties> GetTestProperties (String testKey) //throws ReturnStatusException
  {
    List<TestProperties> listOfTestProperties = new ArrayList<TestProperties> ();

    try (SQLConnection connection = getSQLConnection ()) {

      SingleDataResultSet singleSet = _dll.getTestProperties_DLL (connection, this.clientName, testKey);
      ReturnStatusException.getInstanceIfAvailable (singleSet);

      Iterator<DbResultRecord> records = singleSet.getRecords ();

      while (records.hasNext ())
      {
        singleSet.setFixNulls (false);

        DbResultRecord reader = records.next ();
        TestProperties testProperties = new TestProperties ();

        testProperties.setKey (reader.<String> get ("_Key"));
        testProperties.setSubject (reader.<String> get ("Subject").trim ());
        testProperties.setSchoolYear (reader.<String> get ("SchoolYear").trim ());
        testProperties.setSeason (reader.<String> get ("Season").trim ());
        testProperties.setGradeText (reader.<String> get ("GradeText").trim ());
        testProperties.setGradeCode (reader.<String> get ("GradeCode").trim ());
        testProperties.setGradeSpan (reader.<String> get ("GradeSpan").trim ());
        testProperties.setID (reader.<String> get ("TestID").trim ());
        testProperties.setDisplayName (reader.<String> get ("DisplayName").trim ());
        testProperties.setClientName (reader.<String> get ("ClientName").trim ());
        testProperties.setScoreByTDS (reader.<Boolean> get ("ScoreByTDS"));

        // this means you can't pass this into the SP CanOpenOpportunities
        testProperties.setIsSelectable (reader.hasColumn ("IsSelectable") ? reader.<Boolean> get ("IsSelectable") : true);

        singleSet.setFixNulls (true);
        testProperties.setMaxOpportunities (reader.<Integer> get ("MaxOpportunities"));
        testProperties.setMinItems (reader.<Integer> get ("MinItems"));
        testProperties.setMaxItems (reader.<Integer> get ("MaxItems"));
        testProperties.setPrefetch (reader.<Integer> get ("Prefetch"));

        // HACK: Hard code java/flash requirements
        // testProperties.CheckTestRequirements();

        listOfTestProperties.add (testProperties);
      }
      // below columns could be NULL (e.x., Student Help)

    } catch (SQLException se) {
      _logger.error (se.getMessage (),se);
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage (),re);
    }
    return listOfTestProperties;
  }

  public List<Tuple<String, String>> GetTests () //throws ReturnStatusException
  {
    // String clientName = getTdsSettings ().getClientName ();

    List<Tuple<String, String>> tests = new ArrayList<Tuple<String, String>> ();

    try (SQLConnection connection = getSQLConnection ()) {

      SingleDataResultSet singleSet = _dll.T_ListClientTests_2011_SP (connection, this.clientName);

      ReturnStatusException.getInstanceIfAvailable (singleSet);

      Iterator<DbResultRecord> results = singleSet.getRecords ();

      while (results.hasNext ())
      {
        DbResultRecord reader = results.next ();
        String testKey = reader.<String> get ("_Key");
        String language = reader.<String> get ("Language");

        Tuple<String, String> test = new Tuple<String, String> (testKey, language);
        tests.add (test);
      }
    } catch (SQLException se) {
      _logger.error (se.getMessage (),se);
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage (),re);
    }
    return tests;
  }

  public ItemGroups GetItemGroups (String testKey, String language) //throws ReturnStatusException
  {
    ItemGroups itemGroups = new ItemGroups ();

    try (SQLConnection connection = getSQLConnection ()) {
      SqlParametersMaps parameters = new SqlParametersMaps ();
      parameters.put ("TestKey", testKey);
      parameters.put ("Language", language);

      MultiDataResultSet sets = _dll.T_GetItempool_2011_SP (connection, testKey, language);
      Iterator<SingleDataResultSet> results = sets.getResultSets ();

      SingleDataResultSet passage = results.next ();
      ReturnStatusException.getInstanceIfAvailable (passage);

      Iterator<DbResultRecord> passageRecord = passage.getRecords ();

      DbResultRecord reader;
      while (passageRecord.hasNext ())
      {
        reader = passageRecord.next ();
        // passage
        ItemGroup itemGroup = new ItemGroup ();
        itemGroup.setID (reader.<String> get ("GroupID"));
        itemGroup.setFilePath (reader.<String> get ("StimulusFile"));
        itemGroup.setBankKey (reader.<Integer> get ("_efk_ItemBank"));
        itemGroup.setItemKey (reader.<Integer> get ("_efk_ITSKey"));
        itemGroup.setGroupItemsRequired (reader.<Integer> get ("GroupItemsRequired"));

        itemGroups.put (itemGroup.getID (), itemGroup);
      }

      // item
      if (results.hasNext ())
      {
        SingleDataResultSet itemData = results.next ();

        Iterator<DbResultRecord> itemDBRecords = itemData.getRecords ();

        while (itemDBRecords.hasNext ())
        {
          reader = itemDBRecords.next ();
          TestItem testItem = new TestItem ();

          testItem.setItemID (reader.<String> get ("ItemID"));
          testItem.setGroupID (reader.<String> get ("GroupID"));
          testItem.setBlockID (reader.<String> get ("BlockID"));
          testItem.setItemType (reader.<String> get ("ItemType"));
          testItem.setAnswer (reader.<String> get ("Answer"));
          testItem.setContentLevel (reader.<String> get ("ContentLevel"));
          testItem.setFilePath (reader.<String> get ("ItemFile"));
          testItem.setBankKey (reader.<Integer> get ("_efk_Itembank"));
          testItem.setItemKey (reader.<Integer> get ("_efk_Item"));
          testItem.setScorePoint (reader.<Integer> get ("ScorePoint"));
          testItem.setItemPosition (reader.<Integer> get ("ItemPosition"));
          testItem.setIsRequired (reader.<Boolean> get ("itemRequired"));
          testItem.setIsActive (reader.<Boolean> get ("IsActive"));
          testItem.setIsFieldTest (reader.<Boolean> get ("IsFieldTest"));
          testItem.setIsPrintable (reader.<Boolean> get ("IsPrintable"));

          itemGroups.GetGroup (testItem.getGroupID ()).addItem (testItem);
        }
      }
    } catch (SQLException se) {
      _logger.error (se.getMessage (),se);
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage (),re);
    }
    return itemGroups;
  }
}
