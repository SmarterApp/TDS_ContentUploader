/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.DLL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import TDS.Shared.Exceptions.ReturnStatusException;
import AIR.Common.DB.AbstractDLL;
import AIR.Common.DB.DataBaseTable;
import AIR.Common.DB.DbComparator;
import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.SQL_TYPE_To_JAVA_TYPE;
import AIR.Common.DB.SqlParametersMaps;
import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.MultiDataResultSet;
import AIR.Common.DB.results.SingleDataResultSet;
import AIR.Common.Helpers.CaseInsensitiveMap;
import AIR.Common.Helpers._Ref;

public class ContentUploaderDLL extends AbstractDLL implements IContentUploaderDLL
{
  public MultiDataResultSet getContentFiles_DLL (SQLConnection connection, String clientName) throws ReturnStatusException {
    List<SingleDataResultSet> resultsets = new ArrayList<SingleDataResultSet> ();
    DbResultRecord record = null;
    Iterator<DbResultRecord> records = null;
    String filePath = null;
    
    if (clientName == null || "".equals (clientName)) {
      final String cmd1 = "SELECT I._Key, I._efk_ItemBank as BankKey, I._efk_Item as ItemKey,"
          // + "dbo.ItemFile(I._efk_ItemBank, I._efk_Item) as FilePath "
          + " (select concat(C.Homepath, B.HomePath, B.ItemPath, I.FilePath, I.FileName) "
          + "   from tblitembank B, tblclient C, tblitem I"
          + "   where B._efk_Itembank = BankKey and B._fk_Client = C._Key "
         // + "         and I._Key = concat(BankKey, '-', ItemKey) limit 1) as FilePath "
          + "         and  I._efk_ItemBank = BankKey and I._efk_Item = ItemKey limit 1) as FilePathTemp "
          + "  FROM tblitem I";
      SingleDataResultSet rs1 = executeStatement (connection, cmd1, null, false).getResultSets ().next ();
      rs1.addColumn ("FilePath", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
      records = rs1.getRecords ();
      while (records.hasNext ()) {
        record = records.next ();
        filePath =  replaceSeparatorChar (record.<String> get ("FilePathTemp"));
        record.addColumnValue ("FilePath", filePath);
      }  
      resultsets.add (rs1);

      final String cmd2 = "SELECT S._Key, S._efk_ItemBank as BankKey, S._efk_ITSKey as ItemKey, "
          // + "dbo.StimulusFile(S._efk_ItemBank, S._efk_ITSKey) as FilePath "

          + " (select concat(C.Homepath, B.HomePath, B.stimuliPath, S.FilePath, S.FileName) "
          + " from tblitembank B, tblclient C, tblstimulus S "
          + " where B._efk_Itembank = BankKey and B._fk_Client = C._Key "
          + " and S._efk_ItemBank = BankKey and S._efk_ITSKey = ItemKey limit 1) as FilePathTemp "
         // + " and S._Key = concat(BankKey, '-', ItemKey) limit 1) as FilePath "
          + "   FROM tblstimulus S ";
      SingleDataResultSet rs2 = executeStatement (connection, cmd2, null, false).getResultSets ().next ();
      rs2.addColumn ("FilePath", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
      records = rs2.getRecords ();
      while (records.hasNext ()) {
        record = records.next ();
        filePath =  replaceSeparatorChar (record.<String> get ("FilePathTemp"));
        record.addColumnValue ("FilePath", filePath);
      } 
      resultsets.add (rs2);
    } else {
      final String cmd1 =
          "SELECT I._Key, I._efk_ItemBank as BankKey, I._efk_Item as ItemKey,"
              // + "dbo.ItemFile(I._efk_ItemBank, I._efk_Item) as FilePath "
              + " (select concat(C.Homepath, B.HomePath, B.ItemPath, I.FilePath, I.FileName) "
              + "   from tblitembank B, tblclient C, tblitem I"
              + "   where B._efk_Itembank = BankKey and B._fk_Client = C._Key "
            //  + "         and I._Key = concat(BankKey, '-', ItemKey) limit 1) as FilePath "
              + "         and  I._efk_ItemBank = BankKey and I._efk_Item = ItemKey limit 1) as FilePathTemp "
              + " FROM tblclient C "
              + "    INNER JOIN tblitembank IB on C._Key = IB._fk_Client "
              + "    INNER JOIN tblitem I on IB._efk_ItemBank = I._efk_ItemBank "
              + " WHERE C.Name = ${clientname} ";
      SqlParametersMaps parameters = (new SqlParametersMaps ()).put ("clientname", clientName);
      SingleDataResultSet rs1 = executeStatement (connection, cmd1, parameters, false).getResultSets ().next ();
      rs1.addColumn ("FilePath", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
      records = rs1.getRecords ();
      while (records.hasNext ()) {
        record = records.next ();
        filePath =  replaceSeparatorChar (record.<String> get ("FilePathTemp"));
        record.addColumnValue ("FilePath", filePath);
      } 
      resultsets.add (rs1);

      final String cmd2 =
          "SELECT S._Key, S._efk_ItemBank as BankKey, S._efk_ITSKey as ItemKey, "
              // +
              // "dbo.StimulusFile(S._efk_ItemBank, S._efk_ITSKey) as FilePath "
              + " (select concat(C.Homepath, B.HomePath, B.stimuliPath, S.FilePath, S.FileName) "
              + " from tblitembank B, tblclient C, tblstimulus S "
              + " where B._efk_Itembank = BankKey and B._fk_Client = C._Key "
              + "   and S._efk_ItemBank = BankKey and S._efk_ITSKey = ItemKey limit 1) as FilePathTemp "
          //    + " and S._Key = concat(BankKey, '-', ItemKey) limit 1) as FilePath "
              + " FROM tblclient C "
              + "    INNER JOIN tblitembank IB on C._Key = IB._fk_Client "
              + "    INNER JOIN tblstimulus S on IB._efk_ItemBank = S._efk_ItemBank "
              + " WHERE C.Name = ${clientname}";
      SingleDataResultSet rs2 = executeStatement (connection, cmd2, parameters, false).getResultSets ().next ();
      rs2.addColumn ("FilePath", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
      records = rs2.getRecords ();
      while (records.hasNext ()) {
        record = records.next ();
        filePath =  replaceSeparatorChar (record.<String> get ("FilePathTemp"));
        record.addColumnValue ("FilePath", filePath);
      } 
      resultsets.add (rs2);
    }
    return new MultiDataResultSet (resultsets);

  }

  // EF: tdsgradesubjects is an extensive view. I ported it and functions it
  // calls into java
  public SingleDataResultSet getTestProperties_DLL (SQLConnection connection, String clientName, String testKey) throws ReturnStatusException {

    // final String cmd1 =
    // "SELECT * from tdsgradesubjects WHERE clientname = ifnull(${clientname}, clientName) "
    // + " and _key = ifnull(${testkey}, _key) ORDER BY clientname, sortorder";

    final String cmd1 =
        "select A._key as _key, S.Name as `subject`, T.schoolyear, T.season, G.isselectable, "
            + "   A.Testid as testid, G.MaxOpportunities, A.MinItems, A.MaxItems, G.Prefetch, "
            + "   C.Name as ClientName, S.grade as sgrade, G.label glabel, "
            + "   case when G.ScoreByTDS is null or G.ScoreByTDS = 1 then 1 else 0 end as scorebytdsN, "
            + "   (select min(cast(G.grade as signed))  from setoftestgrades G "
            + "       where G.grade REGEXP '^[0-9]+$' and G._fk_AdminSubject = A._Key) as sortorder "
            // , dbo._MakeTestGradeLabel(C.name, A.TestID) AS [GradeText]
            // , case when coalesce(S.grade, '') = '' then
            // dbo._MakeTestGradeSpan(C.name, A.TestID) else S.grade end as
            // GradeCode
            // , dbo._MakeTestGradeSpan(C.name, A.TestID) as GradeSpan
            // , case when G.Label is not null and len(G.Label) > 0 then G.Label
            // else dbo._MakeTestLabel(C.name, G.TestID) end as DisplayName
            + " from tblsubject  S,tblsetofadminsubjects A, tbltestadmin T, "
            + "      tblclient C, configs.client_testproperties G "
            + " where "
            + "     S._Key = A._fk_Subject and A._fk_TestAdmin = T._Key and T._fk_Client = C._Key and C.name = G.clientname "
            + "     and A.TestID = G.TestID and A.VirtualTest is null "
            + "     and Clientname = IFNULL(${clientname}, Clientname) and A._Key = IFNULL(${testkey}, A._Key) "
            + " ORDER BY Clientname, SortOrder ";
    SqlParametersMaps parameters = (new SqlParametersMaps ()).put ("clientname", clientName).put ("testkey", testKey);
    SingleDataResultSet rs1 = executeStatement (connection, cmd1, parameters, false).getResultSets ().next ();

    rs1.addColumn ("gradetext", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    rs1.addColumn ("gradeCode", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    rs1.addColumn ("gradeSpan", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    rs1.addColumn ("displayName", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    rs1.addColumn ("scorebytds", SQL_TYPE_To_JAVA_TYPE.BIT);

    Iterator<DbResultRecord> records = rs1.getRecords ();
    while (records.hasNext ()) {
      DbResultRecord record = records.next ();

      String cname = record.<String> get ("clientName");
      String testid = record.<String> get ("testid");
      _Ref<String> gradeLabelRef = new _Ref<String> ();
      _Ref<String> gradeSpanRef = new _Ref<String> ();
      makeTestGradeLabelAndSpan (connection, cname, testid, gradeLabelRef, gradeSpanRef);
      record.addColumnValue ("gradeText", gradeLabelRef.get ());

      String sgrade = record.<String> get ("sgrade");
      if (sgrade == null || sgrade.length () == 0)
        record.addColumnValue ("gradeCode", gradeSpanRef.get ());
      else
        record.addColumnValue ("gradeCode", sgrade);

      record.addColumnValue ("gradeSpan", gradeSpanRef.get ());

      String glabel = record.<String> get ("glabel");
      if (glabel != null && glabel.length () > 0)
        record.addColumnValue ("displayName", glabel);
      else
        record.addColumnValue ("displayName", makeTestLabel (connection, cname, testid, gradeLabelRef.get ()));

      Integer scorebytdsN = record.<Integer> get ("scorebytdsN");
      Boolean scorebytds = null;
      if (scorebytdsN == null || scorebytdsN == 1)
        scorebytds = true;
      else
        scorebytds = false;
      record.addColumnValue ("scorebytds", scorebytds);

    }
    return rs1;
  }

  public SingleDataResultSet T_ListClientTests_2011_SP (SQLConnection connection, String clientName) throws ReturnStatusException {
    String environment = "none";

    DataBaseTable act = GetActiveTests_FN (connection, clientName, environment);
    final String cmd = "SELECT DISTINCT(testkey) as _Key, T.TestID as TestID,  Language, IsSelectable,  0 as Window, SortOrder "
        + " from ${actTblName} T, tblsetofadminsubjects S where testkey = _Key order by IsSelectable desc";
    Map<String, String> unquotedParms = new HashMap<String, String> ();
    unquotedParms.put ("actTblName", act.getTableName ());
    SingleDataResultSet rs = executeStatement (connection, fixDataBaseNames (cmd, unquotedParms), null, false).getResultSets ().next ();

    connection.dropTemporaryTable (act);
    return rs;
  }

  public MultiDataResultSet T_GetItempool_2011_SP (SQLConnection connection, String testKey, String language) throws ReturnStatusException {

    List<SingleDataResultSet> resultsets = new ArrayList<SingleDataResultSet> ();

    String algorithm = null;
    String testId = null;
    String client = null;
    String printableItems = null;
    String adminSubject = testKey;

    final String cmd1 = "select testID,  S._fk_TestAdmin as testAdmin, selectionAlgorithm as algorithm, C.Name as client "
        + " from tblsetofadminsubjects S, tbltestadmin A, tblclient C "
        + " where S._Key = ${testkey} and S._fk_TestAdmin = A._key and A._fk_Client = C._Key ";
    SqlParametersMaps parameters1 = (new SqlParametersMaps ()).put ("testkey", testKey);
    SingleDataResultSet rs1 = executeStatement (connection, cmd1, parameters1, false).getResultSets ().next ();
    DbResultRecord record = rs1.getCount () > 0 ? rs1.getRecords ().next () : null;
    if (record != null) {
      algorithm = record.<String> get ("algorithm");
      testId = record.<String> get ("testID");
      client = record.<String> get ("client");
    }

    if (DbComparator.isEqual (algorithm, "Virtual")) {
      return _GetVirtualTestItempool_SP (connection, testKey, language);
    }

    final String cmd2 = "select concat(PrintItemTypes, '|') as printableItems from ${ConfigDB}.client_testproperties "
        + " where TestID = ${testId} and ClientName = ${client}";
    SqlParametersMaps parameters2 = (new SqlParametersMaps ()).put ("testId", testId).put ("client", client);
    SingleDataResultSet rs2 = executeStatement (connection, fixDataBaseNames (cmd2), parameters2, false).getResultSets ().next ();
    DbResultRecord record2 = rs2.getCount () > 0 ? rs2.getRecords ().next () : null;
    if (record2 != null) {
      printableItems = record2.<String> get ("printableItems");
    }

    // create table #items (bankkey bigint, fk_Item varchar(150), GID
    // varchar(100), strand varchar(200), isActive bit, isRequired bit, bVector
    // varchar(max), a float, c float, model varchar(10));
    DataBaseTable itemsTbl = getDataBaseTable ("itemsTbl").addColumn ("bankkey", SQL_TYPE_To_JAVA_TYPE.BIGINT).addColumn ("fk_item", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 150).
        addColumn ("gid", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 100).addColumn ("strand", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 200).addColumn ("isactive", SQL_TYPE_To_JAVA_TYPE.BIT).
        addColumn ("isrequired", SQL_TYPE_To_JAVA_TYPE.BIT).
        // addColumn ("bVector", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 4096).
        addColumn ("a", SQL_TYPE_To_JAVA_TYPE.FLOAT).addColumn ("c", SQL_TYPE_To_JAVA_TYPE.FLOAT).
        addColumn ("model", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 10);
    connection.createTemporaryTable (itemsTbl);

    // We will take care about bVector later on
    final String cmd3 = "insert into ${itemsTbl} (bankkey, fk_Item, GID, strand, isActive, isRequired,  a, c) "
        + " select M._efk_ItemBank, I._fk_Item, I.GroupID, I._fk_Strand, "
        + "    case when I.isActive = 1 and P.isActive = 1 then 1 else 0 end,  "
        + "    I.isRequired,  "
        + "    1.0, 0.0"
        + " from tblsetofadminitems I, tblitemprops P, tbladminstrand S, tblitem M "
        + "   where I._fk_AdminSubject = ${adminSubject} and S._fk_AdminSubject = ${adminSubject} and I._fk_Strand = S._fk_Strand "
        + "     and I._fk_Item = M._Key "
        + "     and I._fk_Item = P._fk_Item and P.Propname = 'Language' and P.Propvalue = ${language} "
        + "     and (P._fk_AdminSUbject is null or P._fk_AdminSUbject = ${adminSubject}) ";

    Map<String, String> unquotedParms = new HashMap<String, String> ();
    unquotedParms.put ("itemsTbl", itemsTbl.getTableName ());
    SqlParametersMaps parameters3 = (new SqlParametersMaps ()).put ("adminSubject", adminSubject).put ("language", language);
    int insertedCnt = executeStatement (connection, fixDataBaseNames (cmd3, unquotedParms), parameters3, false).getUpdateCount ();

    final String cmd4 = "update ${itemsTbl} I, itemscoredimension D, itemmeasurementparameter P, measurementparameter M "
        + " set I.a = parmvalue "
        + " where _fk_AdminSubject = ${adminSubject} and fk_Item = D._fk_Item and P._fk_ItemScoreDimension = D._Key "
        + "   and D._fk_MeasurementModel = M._fk_MeasurementModel and M.parmname = 'a' and P._fk_MeasurementParameter = M.parmnum";
    SqlParametersMaps parameters4 = (new SqlParametersMaps ()).put ("adminSubject", adminSubject);
    int updatedCnt = executeStatement (connection, fixDataBaseNames (cmd4, unquotedParms), parameters4, false).getUpdateCount ();

    final String cmd5 = "update ${itemsTbl} I, itemscoredimension D, itemmeasurementparameter P, measurementparameter M "
        + " set I.c = parmvalue "
        + " where _fk_AdminSubject = ${adminSubject} and fk_Item = D._fk_Item and P._fk_ItemScoreDimension = D._Key "
        + "    and D._fk_MeasurementModel = M._fk_MeasurementModel and M.parmname = 'c' and P._fk_MeasurementParameter = M.parmnum";
    SqlParametersMaps parameters5 = (new SqlParametersMaps ()).put ("adminSubject", adminSubject);
    updatedCnt = executeStatement (connection, fixDataBaseNames (cmd5, unquotedParms), parameters5, false).getUpdateCount ();

    final String cmd6 = "update ${itemsTbl} I, itemscoredimension D, measurementmodel M"
        + " set I.model = M.ModelName "
        + "  where D._fk_AdminSUbject = ${adminSubject} and D._fk_Item = fk_item and D._fk_MeasurementModel = M.ModelNumber";
    SqlParametersMaps parameters6 = (new SqlParametersMaps ()).put ("adminSubject", adminSubject);
    updatedCnt = executeStatement (connection, fixDataBaseNames (cmd6, unquotedParms), parameters6, false).getUpdateCount ();

    // First, select all the stimuli (Note: Item 'groups' that consist only of a
    // single item will not be in this table.
    // That is okay because the Adaptive Algorithm item load creates a group on
    // the fly for an item that comes in with an unknown group ID)
    final String cmd7 = "(select distinct(I.GID) as GroupID, "
        // +
        // "     dbo.StimulusFile(S._efk_Itembank, S._efk_ITSKey) as StimulusFile, "
        + "(select concat(C.Homepath, B.HomePath, B.stimuliPath, SS.FilePath, SS.FileName) "
        + " from tblitembank B, tblclient C, tblstimulus SS "
        + "  where B._efk_Itembank = S._efk_itembank and B._fk_Client = C._Key "
    //    + "    and SS._Key = concat(S._efk_itembank, '-', S._efk_ITSKey) limit 1) as StimulusFile, "
        + "    and SS._efk_itembank = S._efk_itembank and SS._efk_ITSKey = S._efk_ITSKey) limit 1) as StimulusFileTemp, "
        + "     S._efk_ItemBank, S._efk_ITSKey, A.numItemsRequired as GroupItemsRequired, A.MaxItems, A.bpweight "
        + "   from tblsetofitemstimuli IStims, tblstimulus S, ${itemsTbl} I, tbladminstimulus A "
        + "    where  IStims._fk_Item = I.fk_Item and IStims._fk_Stimulus = S._key and A._fk_Stimulus = S._Key"
        + "    and A._fk_AdminSubject = ${adminSubject}) ";

    // + "union "
    // +
    // "   (select GID as GroupID, '' as StimulusFile, bankkey as _efk_ItemBank, 0 as _efk_ITSKey, "
    // + "     bigtoint(0) as GroupItemsRequired, bigtoint(1), 1.0 "
    // + " from ${itemsTbl} where GID like 'I-%')";

    SqlParametersMaps parameters7 = (new SqlParametersMaps ()).put ("adminSubject", adminSubject);
    SingleDataResultSet rs7 = executeStatement (connection, fixDataBaseNames (cmd7, unquotedParms), parameters7, false).getResultSets ().next ();
    rs7.addColumn ("StimulusFile", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    Iterator<DbResultRecord>records = rs7.getRecords ();
    while (records.hasNext ()) {
      record = records.next ();
      String stimulusFile =  replaceSeparatorChar (record.<String> get ("StimulusFileTemp"));
      record.addColumnValue ("StimulusFile", stimulusFile);
    } 
    
    // --create item group for each standalone item. If the item is not
    // --required, then the groupItemsRequired = 0
    // We cannot use temp table in the same statement more than once,
    // That's why we do not use union in the statement above
    final String cmd9 =
        "   select GID as GroupID, bankkey as _efk_ItemBank"
            + " from ${itemsTbl} where GID like 'I-%'";
    SingleDataResultSet rs9 = executeStatement (connection, fixDataBaseNames (cmd9, unquotedParms), null, false).getResultSets ().next ();
    records = rs9.getRecords ();

    List<CaseInsensitiveMap<Object>> resultList = new ArrayList<CaseInsensitiveMap<Object>> ();
    while (records.hasNext ()) {
      record = records.next ();
      CaseInsensitiveMap<Object> rcd = new CaseInsensitiveMap<Object> ();

      String groupId = record.<String> get ("groupID");
      Long _efk_ItemBank = record.<Long> get ("_efk_ItemBank");

      rcd.put ("groupID", groupId);
      rcd.put ("stimulusFile", "");
      rcd.put ("stimulusFileTemp", "");
      rcd.put ("_efk_itembank", _efk_ItemBank);
      rcd.put ("_efk_ITSKey", (long) 0);
      rcd.put ("groupItemsRequired", 0);
      rcd.put ("maxItems", 1);
      rcd.put ("bpweight", (float) 1.0);
      resultList.add (rcd);
    }
    rs7.addRecords (resultList);
    resultsets.add (rs7);

    // now select all the test items with all their attributes
    // NOTE: The join with tblSetofITemStrands may seem redundant, but its
    // purpose is to exclude
    // pulling an item twice that may be cross-classified on two tests.
    final String cmd8 = "select"
        // --item data
        + " M.fk_Item as ItemID, A.GroupID as GroupID, A.BlockID as BlockID, I.ItemType as ItemType, I.Answer, M.Strand as ContentLevel,"
        // " dbo.ItemFile(I._efk_Itembank, I._efk_Item) as ItemFile, "
        + " (select concat(C.Homepath, B.HomePath, B.ItemPath, II.FilePath, II.FileName) "
        + "   from tblitembank B, tblclient C, tblitem II"
        + "   where B._efk_Itembank = BankKey and B._fk_Client = C._Key "
     //   + "         and II._Key = concat(I._efk_Itembank, '-', I._efk_Item) limit 1) as ItemFile, "
        + "         and II._efk_itembank = I._efk_Itembank and II._efk_item =  I._efk_Item limit 1) as ItemFileTemp, "
        + " I._efk_Itembank, I._efk_Item, I.ScorePoint, "
        // --item admin data
        + " A.ItemPosition, A.IsRequired as itemRequired, M.IsActive, A.IsFieldTest, "
        // + " coalesce(bVector, A.IRT_b)  as IRT_b, "
        + " A.IRT_b as IRT_bTmp,"
        + " M.a, M.c, M.model, "
        // " cast (case when IsPrintable = 1 or charindex(I.ItemType + '|', @printableItems, 1) > 0 then 1 else 0 end as bit) as IsPrintable "
        + " IsPrintable as IsPrintableTmp"
        + "  from ${itemsTbl} M, tblitem I, tblsetofadminitems A "
        + "    where  A._fk_Item = M.fk_Item and A._fk_AdminSubject = ${adminSubject} "
        + "      and I._Key = A._fk_Item "
        + " order by A.GroupID, ItemPosition ";
    SqlParametersMaps parameters8 = (new SqlParametersMaps ()).put ("adminSubject", adminSubject);
    SingleDataResultSet rs8 = executeStatement (connection, fixDataBaseNames (cmd8, unquotedParms), parameters8, false).getResultSets ().next ();

    rs8.addColumn ("isprintable", SQL_TYPE_To_JAVA_TYPE.BIT);
    rs8.addColumn ("IRT_b", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    rs8.addColumn ("ItemFile", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    records = rs8.getRecords ();
    while (records.hasNext ()) {
      DbResultRecord rcd = records.next ();
      
      String itemFile =  replaceSeparatorChar (rcd.<String> get ("ItemFileTemp"));
      rcd.addColumnValue ("ItemFile", itemFile);
      
      Boolean isPrintable = rcd.<Boolean> get ("isprintabletmp");
      if (DbComparator.isEqual (isPrintable, true))
        rcd.addColumnValue ("isprintable", true);
      else {
        String itemType = rcd.<String> get ("ItemType");
        if (itemType != null && printableItems != null && printableItems.indexOf (itemType.concat ("|")) > -1)
          rcd.addColumnValue ("isprintable", true);
        else
          rcd.addColumnValue ("isprintable", false);
      }

      String itemId = rcd.<String> get ("ItemID");
      String bVector = ItemBVector_FN (connection, adminSubject, itemId);
      String IRT_bTmp = rcd.<String> get ("IRT_bTmp");
      // coalesce(bVector, A.IRT_b) as IRT_b
      String IRT_b = (bVector == null ? IRT_bTmp : bVector);

      rcd.addColumnValue ("IRT_b", IRT_b);
    }
    resultsets.add (rs8);
    connection.dropTemporaryTable (itemsTbl);
    return new MultiDataResultSet (resultsets);
  }

  private DataBaseTable GetActiveTests_FN (SQLConnection connection, String clientName, String environment) throws ReturnStatusException {
    DataBaseTable activeTestsTbl = getDataBaseTable ("ActiveTestsTbl").addColumn ("testid", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 255).
        addColumn ("language", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 255).addColumn ("isselectable", SQL_TYPE_To_JAVA_TYPE.BIT).
        addColumn ("sortorder", SQL_TYPE_To_JAVA_TYPE.INT).addColumn ("testkey", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 250);
    connection.createTemporaryTable (activeTestsTbl);

    final String cmd = "insert into ${tblName} (testid, language, isselectable, sortorder, testkey) "
        + " (select distinct P.testID, TOOL.Code as language,  P.IsSelectable, P.sortOrder, M.testkey "
        + " from ${ConfigDB}.client_testwindow W, ${ConfigDB}.client_testmode M, ${ConfigDB}.client_testproperties P, "
        + "   ${ConfigDB}.client_testgrades G,  ${ConfigDB}.client_testtool TOOL, tblsetofadminsubjects BANK, tblitemprops LANG "
        + " where "
        + "    P.clientname = ${clientname} "
        + "    and G.clientname = ${clientname} and G.TestID = P.TestID "
        + "    and W.clientname = ${clientname} and W.testID = P.TestID "
        + "    and now() between W.startDate and W.endDate "
        + "    and M.clientname = ${clientname} and M.TestID = P.TestID "
        + "    and TOOL.Clientname = ${clientname} and TOOL.ContextType = 'TEST' and TOOL.Context = P.TestID and TOOL.type = 'Language'"
        + "    and BANK.IsSegmented = 0 and BANK.VirtualTest is null and BANK._Key = M.testkey and BANK.TestID = P.TestID "
        + "    and BANK._KEy = LANG._fk_AdminSubject and LANG.propname = 'Language' and TOOL.Code = LANG.Propvalue) "
        + "Union all "
        + " (select distinct P.testID,  TOOL.Code as language,  P.IsSelectable, P.sortOrder, M.testkey "
        + " from ${ConfigDB}.client_testwindow W, ${ConfigDB}.client_testmode M, ${ConfigDB}.client_testproperties P, "
        + "   ${ConfigDB}.client_testgrades G, ${ConfigDB}.client_testtool TOOL, tblsetofadminsubjects BANK, tblitemprops LANG "
        + "   ,tblsetofadminsubjects SEGMENT "
        + " where "
        + "    P.clientname = ${clientname} "
        + "    and G.clientname = ${clientname} and G.TestID = P.TestID "
        + "    and W.clientname = ${clientname} and W.testID = P.TestID and now() between W.startDate and W.endDate "
        + "    and M.clientname = ${clientname} and M.TestID = P.TestID "
        + "    and TOOL.Clientname = ${clientname} and TOOL.ContextType = 'TEST' and TOOL.Context = P.TestID and TOOL.type = 'Language'"
        + "    and BANK.IsSegmented = 1 and  BANK._Key = M.testkey and BANK.TestID = P.TestID "
        + "    and BANK._key = SEGMENT.VirtualTest "
        + "    and SEGMENT._KEy = LANG._fk_AdminSubject and LANG.propname = 'Language' and TOOL.Code = LANG.Propvalue)";

    String query = fixDataBaseNames (cmd);
    Map<String, String> unquotedParms = new HashMap<String, String> ();
    unquotedParms.put ("tblName", activeTestsTbl.getTableName ());
    SqlParametersMaps parameters = (new SqlParametersMaps ()).put ("clientname", clientName);
    int insertedCnt = executeStatement (connection, fixDataBaseNames (query, unquotedParms), parameters, false).getUpdateCount ();
    return activeTestsTbl;
  }

  private MultiDataResultSet _GetVirtualTestItempool_SP (SQLConnection connection, String testKey, String language) throws ReturnStatusException {
    List<SingleDataResultSet> resultsets = new ArrayList<SingleDataResultSet> ();

    String algorithm = null;
    String testId = null;
    String client = null;
    String printableItems = null;

    final String cmd1 = "select testID,  S._fk_TestAdmin as testAdmin, selectionAlgorithm as algorithm, C.Name as client "
        + " from tblsetofadminsubjects S, tbltestadmin A, tblclient C "
        + " where S._Key = ${testkey} and S._fk_TestAdmin = A._key and A._fk_Client = C._Key ";
    SqlParametersMaps parameters1 = (new SqlParametersMaps ()).put ("testkey", testKey);
    SingleDataResultSet rs1 = executeStatement (connection, cmd1, parameters1, false).getResultSets ().next ();
    DbResultRecord record = rs1.getCount () > 0 ? rs1.getRecords ().next () : null;
    if (record != null) {
      algorithm = record.<String> get ("algorithm");
      testId = record.<String> get ("testID");
      client = record.<String> get ("client");
    }

    final String cmd2 = "select concat(PrintItemTypes, '|') as printableItems from ${ConfigDB}.client_testproperties "
        + " where TestID = ${testId} and ClientName = ${client}";
    SqlParametersMaps parameters2 = (new SqlParametersMaps ()).put ("testId", testId).put ("client", client);
    SingleDataResultSet rs2 = executeStatement (connection, fixDataBaseNames (cmd2), parameters2, false).getResultSets ().next ();
    DbResultRecord record2 = rs2.getCount () > 0 ? rs2.getRecords ().next () : null;
    if (record2 != null) {
      printableItems = record2.<String> get ("printableItems");
    }

    DataBaseTable itemsTbl = getDataBaseTable ("itemsTbl").addColumn ("bankkey", SQL_TYPE_To_JAVA_TYPE.BIGINT).addColumn ("fk_item", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 150).
        addColumn ("gid", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 100).addColumn ("strand", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 200).
        addColumn ("isActiveN", SQL_TYPE_To_JAVA_TYPE.INT).
        addColumn ("isRequired", SQL_TYPE_To_JAVA_TYPE.BIT).
        // addColumn ("bVector", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 4096).
        addColumn ("a", SQL_TYPE_To_JAVA_TYPE.FLOAT).addColumn ("c", SQL_TYPE_To_JAVA_TYPE.FLOAT).
        addColumn ("model", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 10).addColumn ("adminSubjectKey", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 250);
    connection.createTemporaryTable (itemsTbl);

    // We will take care about isActive and bVector later on
    // and we added _key from tblSetOfAdminSubjects tbl, we need it later
    // too calculate dbo.ItemBVector(T._Key, I._fk_Item)
    final String cmd3 = "insert into ${itemsTbl} (bankkey, fk_Item, GID, strand, isActiveN, isRequired,  a, c, adminSubjectKey) "
        + " select M._efk_ItemBank, I._fk_Item, I.GroupID, I._fk_Strand, "
        // + " dbo.IsVtestItemActive(@testkey, I._fk_Item, @language)"
        + "( case when exists "
        + "  (select * "
        + "        from tblsetofadminitems II, tblitemprops P, tblsetofadminsubjects S "
        + "        where S.VirtualTest = ${testkey} and II._fk_AdminSubject = S._Key and P._fk_AdminSubject = S._key "
        + "            and II._fk_Item = I._fk_Item and P._fk_Item = I._fk_Item and II.isActive = 1 and P.isActive = 1 "
        + "            and P.propname = 'Language' and P.Propvalue = ${language})"
        + " then 1 else 0 end"
        + ")  as isActiveN, "
        + "    I.isRequired,  "
        + "    1.0, 0.0,  T._key"
        + " from tblsetofadminitems I, tblitemprops P, tbladminstrand S, tblitem M, tblsetofadminsubjects T "
        + " where T.VirtualTest = ${testkey} "
        + "   and I._fk_AdminSubject = T._Key and S._fk_AdminSubject = T._Key and I._fk_Strand = S._fk_Strand "
        + "   and I._fk_Item = M._Key "
        + "   and I._fk_Item = P._fk_Item and P.Propname = 'Language' and P.Propvalue = ${language} "
        + "   and (P._fk_AdminSUbject is null or P._fk_AdminSUbject = T._Key) ";

    Map<String, String> unquotedParms = new HashMap<String, String> ();
    unquotedParms.put ("itemsTbl", itemsTbl.getTableName ());
    SqlParametersMaps parameters3 = (new SqlParametersMaps ()).put ("testkey", testKey).put ("language", language);
    int insertedCnt = executeStatement (connection, fixDataBaseNames (cmd3, unquotedParms), parameters3, false).getUpdateCount ();

    final String cmd4 = "update ${itemsTbl} I, itemscoredimension D, itemmeasurementparameter P, measurementparameter M, tblsetofadminsubjects T "
        + " set I.a = parmvalue "
        + "   where T.VirtualTest = ${testkey} and D._fk_AdminSubject = T._Key "
        + "    and fk_Item = D._fk_Item and P._fk_ItemScoreDimension = D._Key "
        + "    and D._fk_MeasurementModel = M._fk_MeasurementModel and M.parmname = 'a' and P._fk_MeasurementParameter = M.parmnum";
    SqlParametersMaps parameters4 = (new SqlParametersMaps ()).put ("testkey", testKey);
    int updateCnt = executeStatement (connection, fixDataBaseNames (cmd4, unquotedParms), parameters4, false).getUpdateCount ();

    final String cmd5 = "update ${itemsTbl} I, itemscoredimension D, itemmeasurementparameter P, measurementparameter M, tblsetofadminsubjects T "
        + " set I.c = parmvalue  "
        + "   where T.VirtualTest = ${testkey} and D._fk_AdminSubject = T._Key "
        + "    and fk_Item = D._fk_Item and P._fk_ItemScoreDimension = D._Key "
        + "    and D._fk_MeasurementModel = M._fk_MeasurementModel and M.parmname = 'c' and P._fk_MeasurementParameter = M.parmnum";
    SqlParametersMaps parameters5 = (new SqlParametersMaps ()).put ("testkey", testKey);
    updateCnt = executeStatement (connection, fixDataBaseNames (cmd5, unquotedParms), parameters5, false).getUpdateCount ();

    final String cmd6 = "update ${itemsTbl} I,  itemscoredimension D, measurementmodel M, tblsetofadminsubjects T "
        + "  set I.model = M.ModelName "
        + "   where T.VirtualTest = ${testkey} and D._fk_AdminSubject = T._Key "
        + "     and D._fk_Item = fk_item and D._fk_MeasurementModel = M.ModelNumber";
    SqlParametersMaps parameters6 = (new SqlParametersMaps ()).put ("testkey", testKey);
    updateCnt = executeStatement (connection, fixDataBaseNames (cmd6, unquotedParms), parameters6, false).getUpdateCount ();

    // -- First, select all the stimuli (Note: Item 'groups' that consist only
    // of a single item will not be in this table.
    // -- That is okay because the Adaptive Algorithm item load creates a group
    // on the fly for an item that comes in with an unknown group ID)
    final String cmd7 = "(select distinct(I.GID) as GroupID, "
        // +"     dbo.StimulusFile(S._efk_Itembank, S._efk_ITSKey) as StimulusFile, "
        + "(select concat(C.Homepath, B.HomePath, B.stimuliPath, SS.FilePath, SS.FileName) "
        + " from tblitembank B, tblclient C, tblstimulus SS "
        + "  where B._efk_Itembank = S._efk_itembank and B._fk_Client = C._Key "
        //+ "    and SS._Key = concat(S._efk_itembank, '-', S._efk_ITSKey) limit 1) as StimulusFile, "
        + "    and SS._efk_itembank = S._efk_itembank and SS._efk_itskey = S._efk_ITSKey limit 1) as StimulusFileTemp, "

        + "     S._efk_ItemBank, S._efk_ITSKey, A.numItemsRequired as GroupItemsRequired, A.MaxItems, A.bpweight "
        + "   from tblsetofitemstimuli IStims, tblstimulus S, ${itemsTbl} I, tbladminstimulus A,tblSetofAdminSubjects T "
        + "     where  T.VirtualTest = ${testkey} "
        + "     and IStims._fk_Item = I.fk_Item and IStims._fk_Stimulus = S._key "
        + "     and A._fk_Stimulus = S._Key and A._fk_AdminSubject = T._Key) ";

    // + "union "
    // +
    // "   (select GID as GroupID, '' as StimulusFile, bankkey as _efk_ItemBank, 0 as _efk_ITSKey, "
    // + "     bigtoint(0) as GroupItemsRequired, bigtoint(1), 1.0 "
    // + " from ${itemsTbl} where GID like 'I-%')";
    SqlParametersMaps parameters7 = (new SqlParametersMaps ()).put ("testkey", testKey);
    SingleDataResultSet rs7 = executeStatement (connection, fixDataBaseNames (cmd7, unquotedParms), parameters7, false).getResultSets ().next ();

    rs7.addColumn ("StimulusFile", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    Iterator<DbResultRecord>records = rs7.getRecords ();
    while (records.hasNext ()) {
      record = records.next ();
      String stimulusFile =  replaceSeparatorChar (record.<String> get ("StimulusFileTemp"));
      record.addColumnValue ("StimulusFile", stimulusFile);
    } 
    // --create item group for each standalone item. If the item is not
    // --required, then the groupItemsRequired = 0
    // We cannot use temp table in the same statement more than once,
    // That's why we do not use union in the statement above
    final String cmd9 =
        "   select GID as GroupID, bankkey as _efk_ItemBank"
            + " from ${itemsTbl} where GID like 'I-%'";
    SingleDataResultSet rs9 = executeStatement (connection, fixDataBaseNames (cmd9, unquotedParms), null, false).getResultSets ().next ();
    records = rs9.getRecords ();

    List<CaseInsensitiveMap<Object>> resultList = new ArrayList<CaseInsensitiveMap<Object>> ();
    while (records.hasNext ()) {
      record = records.next ();
      CaseInsensitiveMap<Object> rcd = new CaseInsensitiveMap<Object> ();

      String groupId = record.<String> get ("groupID");
      Long _efk_ItemBank = record.<Long> get ("_efk_ItemBank");

      rcd.put ("groupID", groupId);
      rcd.put ("stimulusFile", "");
      rcd.put ("stimulusFileTemp", "");
      rcd.put ("_efk_itembank", _efk_ItemBank);
      rcd.put ("_efk_ITSKey", (long) 0);
      rcd.put ("groupItemsRequired", 0);
      rcd.put ("maxItems", 1);
      rcd.put ("bpweight", (float) 1.0);
      resultList.add (rcd);
    }
    rs7.addRecords (resultList);
    resultsets.add (rs7);

    final String cmd8 = "select"
        // --item data
        + " M.fk_Item as ItemID, A.GroupID as GroupID, A.BlockID as BlockID, I.ItemType as ItemType, I.Answer, M.Strand as ContentLevel,"
        // " dbo.ItemFile(I._efk_Itembank, I._efk_Item) as ItemFile, "
        + " (select concat(C.Homepath, B.HomePath, B.ItemPath, II.FilePath, II.FileName) "
        + "   from tblitembank B, tblclient C, tblitem II"
        + "   where B._efk_Itembank = BankKey and B._fk_Client = C._Key "
       // + "         and II._Key = concat(I._efk_Itembank, '-', I._efk_Item) limit 1) as ItemFile, "
        + "         and II._efk_itembank = I._efk_Itembank and II._efk_item =  I._efk_Item limit 1) as ItemFileTemp, "
        + " I._efk_Itembank, I._efk_Item, I.ScorePoint, "
        // --item admin data
        + " A.ItemPosition, A.IsRequired as itemRequired,  A.IsFieldTest, "
        // + " coalesce(bVector, A.IRT_b)  as IRT_b, "
        + " A.IRT_b as IRT_bTmp,"
        + " M.a, M.c, M.model, "
        // +
        // " cast (case when IsPrintable = 1 or charindex(I.ItemType + '|', @printableItems, 1) > 0 then 1 else 0 end as bit) as IsPrintable "
        + " IsPrintable as IsPrintableTmp,  M.adminSubjectKey as adminSubjectKey, M.isActiveN as isActiveN "
        + "  from ${itemsTbl} M, tblitem I, tblsetofadminitems A, tblsetofadminsubjects T "
        + "   where T.VirtualTest = ${testkey} and A._fk_Item = M.fk_Item and A._fk_AdminSubject = T._Key "
        + "    and I._Key = A._fk_Item "
        + " order by A.GroupID, ItemPosition ";
    SqlParametersMaps parameters8 = (new SqlParametersMaps ()).put ("testkey", testKey);
    SingleDataResultSet rs8 = executeStatement (connection, fixDataBaseNames (cmd8, unquotedParms), parameters8, false).getResultSets ().next ();

    rs8.addColumn ("isprintable", SQL_TYPE_To_JAVA_TYPE.BIT);
    rs8.addColumn ("IRT_b", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    rs8.addColumn ("isactive", SQL_TYPE_To_JAVA_TYPE.BIT);
    rs8.addColumn ("ItemFile", SQL_TYPE_To_JAVA_TYPE.VARCHAR);
    
    records = rs8.getRecords ();
    while (records.hasNext ()) {
      DbResultRecord rcd = records.next ();
      
      String itemFile =  replaceSeparatorChar (rcd.<String> get ("ItemFileTemp"));
      rcd.addColumnValue ("ItemFile", itemFile);
      
      Boolean isPrintable = rcd.<Boolean> get ("isprintabletmp");
      if (DbComparator.isEqual (isPrintable, true))
        rcd.addColumnValue ("isprintable", true);
      else {
        String itemType = rcd.<String> get ("ItemType");
        if (itemType != null && printableItems != null && printableItems.indexOf (itemType.concat ("|")) > -1)
          rcd.addColumnValue ("isprintable", true);
        else
          rcd.addColumnValue ("isprintable", false);
      }

      String itemId = rcd.<String> get ("ItemID");
      String adminSubjectKey = rcd.<String> get ("adminSubjectKey");
      String bVector = ItemBVector_FN (connection, adminSubjectKey, itemId);
      String IRT_bTmp = rcd.<String> get ("IRT_bTmp");
      // coalesce(bVector, A.IRT_b) as IRT_b
      String IRT_b = (bVector == null ? IRT_bTmp : bVector);

      rcd.addColumnValue ("IRT_b", IRT_b);

      // Boolean isActive = IsVtestItemActive_FN(connection, testKey, itemId,
      // language);
      Integer isAciveN = rcd.<Integer> get ("isActiveN");
      if (DbComparator.isEqual (isAciveN, 1))
        rcd.addColumnValue ("isActive", true);
      else
        rcd.addColumnValue ("isActive", false);
    }
    resultsets.add (rs8);
    connection.dropTemporaryTable (itemsTbl);
    return new MultiDataResultSet (resultsets);

  }

  private String ItemBVector_FN (SQLConnection connection, String adminSubject, String itemId) throws ReturnStatusException {
    String resultStr = null;
    final String cmd1 = "select parmvalue, parmnum "
        + " from itemmeasurementparameter P, itemscoredimension D, measurementparameter M "
        + "   where D._fk_AdminSubject = ${adminSubject} and D._fk_Item = ${itemId} "
        + "     and D._fk_MeasurementModel = M._fk_MeasurementModel and M.parmname like 'b%' "
        + "     and P._fk_ItemScoreDimension = D._Key and P._fk_MeasurementParameter = M.parmnum";
    SqlParametersMaps parameters = (new SqlParametersMaps ()).put ("adminSubject", adminSubject).put ("itemId", itemId);
    SingleDataResultSet rs = executeStatement (connection, cmd1, parameters, false).getResultSets ().next ();
    Iterator<DbResultRecord> records = rs.getRecords ();
    while (records.hasNext ()) {
      DbResultRecord rcd = records.next ();
      Float parmvalue = rcd.<Float> get ("parmvalue");
      if (resultStr == null) {
        resultStr = String.format ("%s", parmvalue.toString ());
      } else {
        resultStr = String.format ("%s;%s", resultStr, parmvalue.toString ());
      }
    }
    return resultStr;
  }

  // EF: implemented inline version
  private Boolean IsVtestItemActive_FN (SQLConnection connection, String testKey, String itemId, String language) throws ReturnStatusException {

    return null;
  }

  // EF: this method incorporates functionality of _MakeTestGradeLabel and
  // _MakeTestGradeSpan functions
  private void makeTestGradeLabelAndSpan (SQLConnection connection, String cname, String testid, _Ref<String> gradeLabelRef, _Ref<String> gradeSpanRef) throws ReturnStatusException {

    DataBaseTable gradesTbl = getDataBaseTable ("gradesTbl").addColumn ("grade", SQL_TYPE_To_JAVA_TYPE.VARCHAR, 25).addColumn ("g", SQL_TYPE_To_JAVA_TYPE.INT);
    connection.createTemporaryTable (gradesTbl);

    final String cmd1 =
        "insert into ${gradesTbl} (grade, g) "
            + "   select G.grade, "
            + "   case when G.grade REGEXP '^[0-9]+$' then cast(G.grade as signed) else null end "
            + " from SetofTestGrades G, tblSetofAdminSubjects S, tblClient C,   tblTestAdmin A "
            + "   where G._fk_AdminSubject = S._Key "
            + "   and C.Name = ${cname} and A._fk_Client = C._Key and S.testID = ${testID} and S._fk_TestAdmin = A._key ";

    Map<String, String> unquotedParms = new HashMap<String, String> ();
    unquotedParms.put ("gradesTbl", gradesTbl.getTableName ());
    SqlParametersMaps parameters = (new SqlParametersMaps ()).put ("cname", cname).put ("testid", testid);

    executeStatement (connection, fixDataBaseNames (cmd1, unquotedParms), parameters, true);

    Long numgrades = null;
    final String cmd2 = "select count(*) as numgrades from ${gradesTbl}";
    SingleDataResultSet rs2 = executeStatement (connection, fixDataBaseNames (cmd2, unquotedParms), null, false).getResultSets ().next ();
    DbResultRecord rcd2 = (rs2.getCount () > 0 ? rs2.getRecords ().next () : null);
    if (rcd2 != null) {
      numgrades = rcd2.<Long> get ("numgrades");
    }

    if (DbComparator.isEqual (numgrades, 0)) {
      gradeLabelRef.set ("");
      gradeSpanRef.set ("");
      return;
    }
    if (DbComparator.isEqual (numgrades, 1)) {
      final String cmd3 = "select grade, g as intgrade from ${gradesTbl}";
      SingleDataResultSet rs3 = executeStatement (connection, fixDataBaseNames (cmd3, unquotedParms), null, false).getResultSets ().next ();
      // should work because we know that tbl has one and only one records
      DbResultRecord rcd3 = rs3.getRecords ().next ();
      Integer intgrade = rcd3.<Integer> get ("intgrade");
      String grade = rcd3.<String> get ("grade");
      if (intgrade == null) {
        gradeLabelRef.set (grade);
        gradeSpanRef.set (grade);
      }
      else {
        gradeLabelRef.set (String.format ("Grade %s", grade));
        gradeSpanRef.set (grade);
      }
      return;
    }

    Long numintgrades = null;
    final String cmd4 = " select count(*)  as numintgrades from ${gradesTbl} where g is not null";
    SingleDataResultSet rs4 = executeStatement (connection, fixDataBaseNames (cmd4, unquotedParms), null, false).getResultSets ().next ();
    DbResultRecord rcd4 = (rs4.getCount () > 0 ? rs4.getRecords ().next () : null);
    if (rcd4 != null) {
      numintgrades = rcd4.<Long> get ("numintgrades");
    }

    if (DbComparator.isEqual (numintgrades, numgrades)) {
      final String cmd5 = "select min(g) as mingrade, max(g) as maxgrade  from ${gradesTbl}";
      SingleDataResultSet rs5 = executeStatement (connection, fixDataBaseNames (cmd5, unquotedParms), null, false).getResultSets ().next ();
      DbResultRecord rcd5 = rs5.getRecords ().next ();
      Integer mingrade = rcd5.<Integer> get ("mingrade");
      Integer maxgrade = rcd5.<Integer> get ("maxgrade");
      // if (@mingrade = 9 and @maxgrade = 12 and @numgrades = 4) return 'High
      // School' ;
      if (DbComparator.isEqual (mingrade, 9) && DbComparator.isEqual (maxgrade, 12) && DbComparator.isEqual (numgrades, 4)) {
        gradeLabelRef.set ("High School");
        gradeSpanRef.set ("HS");
        return;
      }
      // mingrade and maxgrade will not be null in this case because
      // numitemsgrades is equal to numgrades
      if (maxgrade - mingrade + 1 == numintgrades) {
        gradeLabelRef.set (String.format ("Grades %d - %d", mingrade, maxgrade));
        gradeSpanRef.set (String.format ("%d - %d", mingrade, maxgrade));
        return;
      }

      final String cmd6 = "select distinct(g) from ${gradesTbl} order by g";
      SingleDataResultSet rs6 = executeStatement (connection, fixDataBaseNames (cmd6, unquotedParms), null, false).getResultSets ().next ();

      String labelStr = null;
      String spanStr = null;
      Iterator<DbResultRecord> records = rs6.getRecords ();
      while (records.hasNext ()) {
        DbResultRecord rcd6 = records.next ();
        // Reminder: this is the case where all grades values are number.
        Integer g = rcd6.<Integer> get ("g");
        if (labelStr == null && spanStr == null) {
          labelStr = String.format ("Grades %d", g);
          spanStr = String.format ("%d", g);
        }
        else {
          labelStr = String.format ("%s, %d", labelStr, g);
          spanStr = String.format ("%s, %d", spanStr, g);
        }
      }
      gradeLabelRef.set (labelStr);
      gradeSpanRef.set (spanStr);
      return;
    }

    // case when grades list contains non-numneric values, like 'k1' or 'na'
    final String cmd7 = "select distinct(grade) from ${gradesTbl} order by grade";
    SingleDataResultSet rs7 = executeStatement (connection, fixDataBaseNames (cmd7, unquotedParms), null, false).getResultSets ().next ();

    String labelStr = null;
    String spanStr = null;
    Iterator<DbResultRecord> records = rs7.getRecords ();
    while (records.hasNext ()) {
      DbResultRecord rcd7 = records.next ();
      String grade = rcd7.<String> get ("grade");
      if (labelStr == null && spanStr == null) {
        labelStr = String.format ("Grades %s", grade);
        spanStr = String.format ("%s", grade);
      }
      else {
        labelStr = String.format ("%s, %s", labelStr, grade);
        spanStr = String.format ("%s, %s", spanStr, grade);
      }
    }
    gradeLabelRef.set (labelStr);
    gradeSpanRef.set (spanStr);
    return;

  }

  // EF: based on _MakeTestLabel function
  private String makeTestLabel (SQLConnection connection, String cname, String testid, String gradeLabel) throws ReturnStatusException {

    final String cmd1 = 
        "select SS.Name as subject "
      + " from tblSetofAdminSubjects AA, tblSubject SS, "
      + " tblSetofAdminSubjects S, tblClient C, tblTestAdmin A "
      + "  where " //-- A._key = dbo.ClientTestKey(@client, @testID)
      + " AA._key = S._key and  "
      + " C.Name = ${cname} and A._fk_Client = C._Key and S.testID = ${testid} and S._fk_TestAdmin = A._key "
      + " and AA._fk_Subject = SS._Key"; 
    SqlParametersMaps parameters1 = (new SqlParametersMaps ()).put ("cname", cname).put ("testid", testid);
    
    String subject = null;
    SingleDataResultSet rs1 = executeStatement (connection, cmd1, parameters1, true).getResultSets ().next ();
    DbResultRecord rcd1 = (rs1.getCount () > 0 ? rs1.getRecords ().next () : null);
    if (rcd1 != null)  {
      subject = rcd1.<String> get ("subject");
    }
    
    if (subject == null)
      subject = "";
    return String.format ("%s %s", gradeLabel, subject);
    
  }
  
  private String replaceSeparatorChar (String str) {
    return str.replace ('/', java.io.File.separatorChar).replace ('\\', java.io.File.separatorChar);
  }
}

// TODO EF: use this statement to check if 1.0 got converted my jdbc to float
// type
// select distinct '5' as GroupID, 'stimf' as StimulusFile, S._efk_ItemBank,
// S._efk_ITSKey
// , A.numItemsRequired as GroupItemsRequired, A.MaxItems, A.bpweight
// from tblSetofItemStimuli IStims, tblStimulus S, tblAdminStimulus A
// where IStims._fk_Stimulus = S._key and A._fk_Stimulus = S._Key and
// A._fk_AdminSubject = '(Minnesota_PT)GRAD-Mathematics-11-Fall-2011-2012'
// union
// select '6' as GroupID, '' as StimulusFile, '99' as _efk_ItemBank, 0 as
// _efk_ITSKey, 0 as GroupItemsRequired, 1, 1.0
