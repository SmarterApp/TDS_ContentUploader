/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Model;

import java.util.List;
// / <summary>
// / All the information for a test.
// / </summary>




import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.MultiDataResultSet;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Data.ColumnResultSet;

public class TestProperties
{
  private String       _key;
  private String       _id;
  private String       _schoolYear;
  private String       _season;
  private String       _subject;
  private String       _accFamily;
  private String       _gradeText;
  private String       _gradeCode;
  private String       _gradeSpan;

  // / <summary>
  // / The client name for this test.
  // / </summary>
  private String       _clientName;

  // / <summary>
  // / Use this for displaying the test name to the student (e.x., opportunity
  // page).
  // / </summary>
  private String       _displayName;

  // / <summary>
  // / The max # of opportunities the student is allowed to take for this test.
  // / </summary>
  private int          _maxOpportunities;

  // / <summary>
  // / If this is false then this test cannot be selected on the opportunity
  // page
  // / and should not be passed into the SP T_CanOpenTestOpportunities.
  // / </summary>
  // / <remarks>This was added for shadow tests.</remarks>
  private boolean      _isSelectable;

  private int          _minItems;

  private int          _maxItems;

  // / <summary>
  // / The # of items to prefetch when calling the adaptive algorithm.
  // / </summary>
  private int          _prefetch;

  // / <summary>
  // / If this is true then student application will attempt to score this test
  // if it is possible.
  // / </summary>
  private boolean      _scoreByTDS;

  // / <summary>
  // / If this is true then we need to check a SP to see if we have permission
  // to complete the test.
  // / </summary>
  private boolean      _validateCompleteness;

  // / <summary>
  // / Subject/grade sorting
  // / </summary>
  private int          _sortOrder;

  // / <summary>
  // / Test sorting
  // / </summary>
  private int          _testSortOrder;

  // / <summary>
  // / Does this test contain student help items (tutorial, GTR, etc)
  // / </summary>
  private boolean      _isStudentHelp;

  // / <summary>
  // / A list of all the grades available for this test
  // / </summary>
  private List<String> _grades;

  // / <summary>
  // / Parses test properties from a reader.
  // / </summary>
  public static TestProperties parse (MultiDataResultSet resultSet)
  {
    TestProperties testProperties = new TestProperties ();
    
    SingleDataResultSet singleSet=resultSet.getResultSets ().next ();
    singleSet.setFixNulls (false);
    DbResultRecord reader=singleSet.getRecords ().next ();
  
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

    // get ScoreByTDS (TODO: Can this still sometimes be an int?)
    Object scoreByTDS = reader.get ("ScoreByTDS");

    if (scoreByTDS instanceof Integer)
    {
      testProperties.setScoreByTDS ((reader.<Integer>get ("ScoreByTDS") == 1));
    }
    else if (scoreByTDS instanceof Boolean)
    {
      testProperties.setScoreByTDS (reader.<Boolean> get ("ScoreByTDS"));
    }

    // this means you can't pass this into the SP CanOpenOpportunities
    testProperties.setIsSelectable (reader.hasColumn ("IsSelectable") ? reader.<Boolean> get ("IsSelectable") : true);

    // get sorting info
    if (reader.hasColumn ("SortOrder") && reader.get ("SortOrder")!=null)
    {
      testProperties.setSortOrder (reader.<Integer>get ("SortOrder"));
    }

    if (reader.hasColumn ("TestSortOrder") && reader.get ("TestSortOrder")!=null)
    {
      testProperties.setTestSortOrder (reader.<Integer> get ("TestSortOrder"));
    }

    if (reader.hasColumn ("validateCompleteness") && reader.get ("validateCompleteness")!=null)
    {
      testProperties.setValidateCompleteness (reader.<Boolean> get("validateCompleteness"));
    }

    // below columns could be NULL
    singleSet.setFixNulls(true);

    // accommodations family (subject)
    testProperties.setAccFamily (reader.<String>get("AccommodationFamily"));

    testProperties.setMaxOpportunities (reader.<Integer>get ("MaxOpportunities"));
    testProperties.setMinItems (reader.<Integer>get ("MinItems"));
    testProperties.setMaxItems (reader.<Integer>get ("MaxItems"));
    testProperties.setPrefetch (reader.<Integer>get ("Prefetch"));

    return testProperties;
  }

  @Override
  public String toString ()
  {
    return _key;
  }

  public String getKey () {
    return _key;
  }

  public void setKey (String value) {
    this._key = value;
  }

  public String getID () {
    return _id;
  }

  public void setID (String value) {
    this._id = value;
  }

  public String getSchoolYear () {
    return _schoolYear;
  }

  public void setSchoolYear (String value) {
    this._schoolYear = value;
  }

  public String getSeason () {
    return _season;
  }

  public void setSeason (String value) {
    this._season = value;
  }

  public String getSubject () {
    return _subject;
  }

  public void setSubject (String value) {
    this._subject = value;
  }

  public String getAccFamily () {
    return _accFamily;
  }

  public void setAccFamily (String value) {
    this._accFamily = value;
  }

  public String getGradeText () {
    return _gradeText;
  }

  public void setGradeText (String value) {
    this._gradeText = value;
  }

  public String getGradeCode () {
    return _gradeCode;
  }

  public void setGradeCode (String value) {
    this._gradeCode = value;
  }

  public String getGradeSpan () {
    return _gradeSpan;
  }

  public void setGradeSpan (String value) {
    this._gradeSpan = value;
  }

  public String getClientName () {
    return _clientName;
  }

  public void setClientName (String value) {
    this._clientName = value;
  }

  public String getDisplayName () {
    return _displayName;
  }

  public void setDisplayName (String value) {
    this._displayName = value;
  }

  public int getMaxOpportunities () {
    return _maxOpportunities;
  }

  public void setMaxOpportunities (int value) {
    this._maxOpportunities = value;
  }

  public boolean getIsSelectable () {
    return _isSelectable;
  }

  public void setIsSelectable (boolean value) {
    this._isSelectable = value;
  }

  public int getMinItems () {
    return _minItems;
  }

  public void setMinItems (int value) {
    this._minItems = value;
  }

  public int getMaxItems () {
    return _maxItems;
  }

  public void setMaxItems (int value) {
    this._maxItems = value;
  }

  public int getPrefetch () {
    return _prefetch;
  }

  public void setPrefetch (int value) {
    this._prefetch = value;
  }

  public boolean getScoreByTDS () {
    return _scoreByTDS;
  }

  public void setScoreByTDS (boolean value) {
    this._scoreByTDS = value;
  }

  public boolean getValidateCompleteness () {
    return _validateCompleteness;
  }

  public void setValidateCompleteness (boolean value) {
    this._validateCompleteness = value;
  }

  public int getSortOrder () {
    return _sortOrder;
  }

  public void setSortOrder (int value) {
    this._sortOrder = value;
  }

  public int getTestSortOrder () {
    return _testSortOrder;
  }

  public void setTestSortOrder (int value) {
    this._testSortOrder = value;
  }

  public boolean getIsStudentHelp () {
    return _isStudentHelp;
  }

  public void setIsStudentHelp (boolean value) {
    this._isStudentHelp = value;
  }

  public List<String> getGrades () {
    return _grades;
  }

  public void setGrades (List<String> value) {
    this._grades = value;
  }

}
