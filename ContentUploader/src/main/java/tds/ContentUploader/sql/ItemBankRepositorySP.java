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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tds.ContentUploader.ItemBankFile;
import tds.ContentUploader.Tuple;
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

@Component
@Scope ("prototype")
public class ItemBankRepositorySP extends AbstractDAO  implements IItemBankRepository // extends
// tds.student.sql.repository.ItemBankRepository//
// :
// RepositoryBase
{
  private static final Logger _logger = LoggerFactory.getLogger (ItemBankRepository.class);
  
  private String clientName = null;
  //
  // private String _connectionString;
  //
  // private SQLConnection _connection;

  public ItemBankRepositorySP () {
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

  public void getContentFiles ( Map<String, ItemBankFile> items, Map<String, ItemBankFile> stimuli) //throws ReturnStatusException
  {
    items = new HashMap<String, ItemBankFile> ();
    stimuli = new HashMap<String, ItemBankFile> ();
    String sql = null;

    try (SQLConnection connection = getSQLConnection ()) {

      //String clientName = getTdsSettings ().getClientName ();
      if (StringUtils.isEmpty (clientName))
      {
        sql = "SELECT I._Key, I._efk_ItemBank as BankKey, I._efk_Item as ItemKey, dbo.ItemFile(I._efk_ItemBank, I._efk_Item) as FilePath"
            + "FROM tblItem I (NOLOCK)"
            + "SELECT S._Key, S._efk_ItemBank as BankKey, S._efk_ITSKey as ItemKey, dbo.StimulusFile(S._efk_ItemBank, S._efk_ITSKey) as FilePath"
            + "FROM tblStimulus S (NOLOCK)";
      }
      else
      {
        sql = "SELECT I._Key, I._efk_ItemBank as BankKey, I._efk_Item as ItemKey, dbo.ItemFile(I._efk_ItemBank, I._efk_Item) as FilePath"
            + "FROM tblclient C"
            + " INNER JOIN tblitembank IB on C._Key = IB._fk_Client"
            + "    INNER JOIN tblitem I on IB._efk_ItemBank = I._efk_ItemBank"
            + "WHERE C.Name = ${ClientName}"

            + "SELECT S._Key, S._efk_ItemBank as BankKey, S._efk_ITSKey as ItemKey, dbo.StimulusFile(S._efk_ItemBank, S._efk_ITSKey) as FilePath"
            + "FROM tblclient C "
            + "    INNER JOIN tblitembank IB on C._Key = IB._fk_Client"
            + "    INNER JOIN tblStimulus S on IB._efk_ItemBank = S._efk_ItemBank"
            + "WHERE C.Name = ${ClientName}";
      }

      SqlParametersMaps parameters = new SqlParametersMaps ();
      if (!StringUtils.isEmpty (clientName))
      {
        parameters.put ("clientname", clientName);
      }
      MultiDataResultSet results = executeStatement (connection, sql, parameters, false);

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
      _logger.error (se.getMessage ());
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage ());
    }
  }

  public List<TestProperties> GetTestProperties (String testKey) //throws ReturnStatusException
  {
    List<TestProperties> listOfTestProperties = new ArrayList<TestProperties> ();

    final String sql = "SELECT * from TDSGradeSubjects (NOLOCK)"
        + "WHERE ClientName = ISNULL(${ClientName}, ClientName) and _Key = ISNULL(${TestKey}, _Key)"
        + "ORDER BY ClientName, SortOrder";

    try (SQLConnection connection = getSQLConnection ()) {
      //String clientName = getTdsSettings ().getClientName ();

      SqlParametersMaps sqlParameter = new SqlParametersMaps ();
      sqlParameter.put ("clientname", clientName);
      sqlParameter.put ("TestKey", testKey);

      SingleDataResultSet singleSet = executeStatement (connection, sql, sqlParameter, false).getResultSets ().next ();
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
      _logger.error (se.getMessage ());
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage ());
    }
    return listOfTestProperties;
  }

  // create table #items ( fk_Item varchar(150), GID varchar(100), strand
  // varchar(200), isRequired bit, bVector varchar(max), a float, c float);

  public List<Tuple<String, String>> GetTests () //throws ReturnStatusException
  {
    //String clientName = getTdsSettings ().getClientName ();

    List<Tuple<String, String>> tests = new ArrayList<Tuple<String, String>> ();

    try (SQLConnection connection = getSQLConnection ()) {
      final String sql = "exec T_ListClientTests_2011 @clientName=${clientname}";
      SqlParametersMaps parameters = new SqlParametersMaps ();
      parameters.put ("clientname", clientName);

      SingleDataResultSet singleSet = executeStatement (connection, sql, parameters, false).getResultSets ().next ();
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
      _logger.error (se.getMessage ());
      //throw new ReturnStatusException (se);
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage ());
    }
    return tests;
  }

  public ItemGroups GetItemGroups (String testKey, String language) //throws ReturnStatusException
  {
    ItemGroups itemGroups = new ItemGroups ();
    
    String sql = "exec T_GetItempool_2011 @TestKey=${TestKey} , @Language=${Language}";
    
    try (SQLConnection connection = getSQLConnection ()) {
    SqlParametersMaps parameters = new SqlParametersMaps ();
    parameters.put ("TestKey", testKey);
    parameters.put ("Language", language);
   
    Iterator<SingleDataResultSet> results = executeStatement (connection, sql, parameters, false).getResultSets ();
    
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
      _logger.error (se.getMessage ());
      //throw new ReturnStatusException (se);     
    } catch (ReturnStatusException re) {
      _logger.error (re.getMessage ());
    }
    return itemGroups;
  }
}
