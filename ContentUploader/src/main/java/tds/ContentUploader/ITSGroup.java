/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import AIR.Common.Utilities.Path;
import tds.itemrenderer.data.IITSDocument;

public class ITSGroup
{
  final String               _textFormat = "%s - %s %s (%s)";

  private String             _basePath;
  private String             _groupID;
  private IITSDocument       _passage;
  private List<IITSDocument> _items;

  public ITSGroup (String basePath, String groupID)
  {
    setBasePath (basePath);
    setGroupID (groupID);
  }

  public String getLabel ()
  {
    String groupIdFormat = "%s-%s-%s";
    String groupLabel;

    if (getPassage () != null)
    {
      groupLabel = String.format (groupIdFormat, "G-", _passage.getBankKey (), _passage.getItemKey ());
    }
    else if (getItems ().size () > 0)
    {
      groupLabel = String.format (groupIdFormat, "I-", _items.get (0).getBankKey (), _items.get (0).getItemKey ());
    }
    else
    {
      groupLabel = "NO CONTENT";
    }

    IITSDocument item = _items.get (0);
    return String.format (_textFormat, groupLabel, item.getLayout (), item.getResponseType (), item.getFormat ());

  }

  public String getGroupingText ()
  {
    String groupingText = _items.get (0).getBaseUri ().replace (_basePath, "");
    groupingText = Path.getDirectoryName (groupingText);

    if (StringUtils.isEmpty (groupingText))
    {
      // groupingText = "Samples";
    }

    return groupingText.trim ();
  }

  public String getBasePath () {
    return _basePath;
  }

  public void setBasePath (String value) {
    this._basePath = value;
  }

  public String getGroupID () {
    return _groupID;
  }

  public void setGroupID (String value) {
    this._groupID = value;
  }

  public IITSDocument getPassage () {
    return _passage;
  }

  public void setPassage (IITSDocument value) {
    this._passage = value;
  }

  public List<IITSDocument> getItems () {
    return _items;
  }

  public void setItems (List<IITSDocument> value) {
    this._items = value;
  }
}
