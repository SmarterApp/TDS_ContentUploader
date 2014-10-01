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
import java.util.Iterator;

import AIR.Common.Utilities.Path;
import tds.itemrenderer.data.IITSDocument;

// / <summary>
// / This represents a xml file that came from an ITS zip.
// / </summary>
// / <remarks>
// / We are not using this right now. But there is some commented out
// / code at the bottom of ContentPublish.cs that at some point will
// / make use of this.
// / </remarks>
public class ITSXmlFile
{
  private/* readonly */IITSDocument _itsDocument;

  public IITSDocument getDocument ()
  {
    return _itsDocument;
  }

  public ITSXmlFile (IITSDocument itsDocument)
  {
    _itsDocument = itsDocument;
  }

  public String getDirectoryName ()
  {
    return Path.getDirectoryName (_itsDocument.getBaseUri ());
  }

  public String getFileName ()
  {
    return Path.getFileName (_itsDocument.getBaseUri ());
  }

  public String getName ()
  {
    return Path.getFileNameWithoutExtension (_itsDocument.getBaseUri ());
  }

  public Iterator<String> getResourceFiles ()
  {
    ArrayList<String> files = new ArrayList<String> ();
    File folder = new File (getDirectoryName ());
    for (File file : folder.listFiles ())
    {
      if (file.getName ().matches (getName () + "_\\w*.*"))
        files.add (file.getName ());
    }
    return files.iterator ();
  }
}
