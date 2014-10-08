/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import AIR.Common.Utilities.Path;
import tds.itemrenderer.ITSDocumentFactory;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSContent;
import tds.itemrenderer.data.ITSOption;

public class ITSDocumentInfo
{
  private IITSDocument _document;
  private long                      _contentSize      = -1;

  private List<String> _resourcesFound   = new ArrayList<String> ();
  private List<String> _resourcesMissing = new ArrayList<String> ();

  public IITSDocument getDocument ()
  {
    return _document;
  }

  public String getBasePath ()
  {
    return Path.getDirectoryName (_document.getBaseUri ());
  }

  public String getBaseFile ()
  {
    return Path.getFileName (_document.getBaseUri ());
  }

  // / <summary>
  // / Get the content size of the rendered English content.
  // / </summary>
  public long getContentSize ()
  {
    return _contentSize;
  }

  // / <summary>
  // / List of unique resources that we use when rendering English content.
  // / </summary>
  public List<String> getResources ()
  {
    return _resourcesFound;
  }

  // / <summary>
  // / List of resources in the English content where the files were not found.
  // / </summary>
  public List<String> getResourcesMissing ()
  {
    return _resourcesMissing;
  }

  public ITSDocumentInfo (IITSDocument document)
  {
    _document = document;
  }

  public ITSDocumentInfo (String fileName)
  {
    _document = ITSDocumentFactory.load (fileName, "ENU", false);
  }

  public boolean process ()
  {
    _contentSize = 0;

    // calculate content size
    CalculateContentSize ();

    // calculate resource size
    List<String> resources = new ArrayList<String> ();
    resources.addAll (GetGridImages ());
    resources.addAll (GetContentResources ());

    // filter for unique file names
    Set<String> distinctResources = new HashSet<String> (resources);

    for (String resource : distinctResources)
    {
      processResource (resource);
    }

    return true;
  }

  // / <summary>
  // / Calculate the size of the HTML content in the XML that is used to render
  // the content with.
  // / </summary>
  private void CalculateContentSize ()
  {
    // get grid xml size
    if (!StringUtils.isEmpty (_document.getGridAnswerSpace ()))
    {
      _contentSize += _document.getGridAnswerSpace ().length ();
    }

    ITSContent content = _document.getContentDefault ();
    if (content == null)
      return;

    // get illustration size
    if (content.getIllustration () != null)
    {
      _contentSize += content.getIllustration ().length ();
    }

    // get stem size
    // JF: Some items (like EBSR) do not have a STEM node in the content xml, so
    // in this case it would be null
    if (content.getStem () != null)
    {
      _contentSize += content.getStem ().length ();
    }

    // get MC options size
    if (content.getOptions () != null)
    {
      for (ITSOption option : content.getOptions ())
      {
        _contentSize += option.getValue ().length ();
      }
    }
  }

  // / <summary>
  // / Used to check if the resource exists and get its file size
  // / </summary>
  private void processResource (String fileName)
  {
    String filePath = Path.combine (getBasePath (), fileName);
    // TODO Shajib: FileInfo used in .net
    File fileInfo = new File (filePath);

    if (fileInfo.exists ())
    {
      _resourcesFound.add (fileName);

      // calculate file size
      _contentSize += fileInfo.length ();
    }
    else
    {
      _resourcesMissing.add (fileName);
    }
  }

  private List<String> GetGridImages ()
  {
    // TODO Shajib: HtmlParser class has this method, we don't have this class
    // in itemrenderer.web
    return HtmlParser.scrapeGridResources (_document.getGridAnswerSpace ());
  }

  private List<String> GetContentResources (String language)
  {
    List<String> resources = new ArrayList<String> ();

    ITSContent content = _document.getContent (language);
    if (content == null)
      return resources;

    // get illustration images
    if (content.getIllustration () != null)
    {
      resources.addAll (HtmlParser.scrapeHtmlResources (content.getIllustration ()));
    }

    // get stem images/audio
    resources.addAll (HtmlParser.scrapeHtmlResources (content.getStem ()));

    // get MC options images/audio
    if (content.getOptions () != null)
    {
      for (ITSOption option : content.getOptions ())
      {
        resources.addAll (HtmlParser.scrapeHtmlResources (option.getValue ()));
      }
    }

    return resources;
  }

  private List<String> GetContentResources ()
  {
    return GetContentResources ("ENU");
  }

  @Override
  public String toString ()
  {
    if (_document != null)
      return getBaseFile ();
    return super.toString ();
  }

}
