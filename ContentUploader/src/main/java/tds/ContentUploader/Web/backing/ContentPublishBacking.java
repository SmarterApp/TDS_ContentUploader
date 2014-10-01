/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web.backing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tds.ContentUploader.ITSDocumentInfo;
import tds.ContentUploader.ItemBankFile;
import tds.ContentUploader.ItemBankManager;
import tds.ContentUploader.Web.BasePage;
import tds.ContentUploader.sql.IItemBankRepository;
import tds.itemrenderer.ITSDocumentFactory;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import AIR.Common.Utilities.Path;
import AIR.Common.Utilities.SpringApplicationContext;

public class ContentPublishBacking extends BasePage
{

  protected static String             uploadPath;
  protected static String             extractPathParent;
  ItemBankManager _itemBankManager;
  protected StringBuilder dynamicResponse = new StringBuilder ();
  private String htmlResponse = "";
  private static final Logger _logger = LoggerFactory.getLogger (ContentPublishBacking.class);
  private boolean imgProcessAndRendered = false;
  
  
  public boolean isImgProcessAndRendered () {
    return imgProcessAndRendered;
  }

  public void setImgProcessAndRendered (boolean imgProcessAndRendered) {
    this.imgProcessAndRendered = imgProcessAndRendered;
  }

  public String getHtmlResponse () {
    return htmlResponse;
  }

  public void setHtmlResponse (String htmlResponse) {
    this.htmlResponse = htmlResponse;
  }

  public ContentPublishBacking () {
    super ();
    extractPathParent = getTdsSettings ().getAppSettings ().get ("extractPath");
    uploadPath  = getTdsSettings ().getAppSettings ().get ("uploadPath");
    _itemBankManager = SpringApplicationContext.getBean (ItemBankManager.class);
  }

  /**
   *  The current zip file we are going to publish.
   */
  private String                      _zipFileName;

  public String getZipFileName ()
  {
    _zipFileName = FacesContext.getCurrentInstance ().getExternalContext ().getRequestParameterMap ().get ("file");
    if (_zipFileName == null)
      _zipFileName = "";
    return _zipFileName;
  }

  /**
   * When someone clicks on the publish HTML button then this function is called.
   */
  public void btnPublish_OnClick ()
  {
    StringBuilder rw = null;
    try {
      rw = getResponseappendr();
      // ProcessXmlFiles(extractedContentPath);

      // get the items/stimuli from the item bank
      HashMap<String, ItemBankFile> items = new HashMap<String, ItemBankFile> ();
      HashMap<String, ItemBankFile> stimuli = new HashMap<String, ItemBankFile> ();

      IItemBankRepository itemPreviewRepository = _itemBankManager.CreateItemPreviewRepository ();
      
      rw.append ("<p>Reading item bank... </p>");
      //rw.flush ();
      itemPreviewRepository.getContentFiles (items, stimuli); // .GetContentFiles(out
                                                              // items, out
                                                              // stimuli);

      rw.append ("<p>Extracting zip... </p>");
      //rw.flush ();
      String extractedContentPath = extractContent ();

      rw.append ("<p>Uploading items... </p>");
      //rw.flush ();

      loadAndProcessFiles (items, "Item", "item", extractedContentPath);

      rw.append ("<p>Uploading passages... </p>");
      //rw.flush ();

      loadAndProcessFiles (stimuli, "Passage", "stim", extractedContentPath);

//      rw.flush ();
    } catch (Exception e) {
      _logger.error ("Error during action : btnPublish_OnClick ",e);
      writeMessage ("Error while publishing zip file : "+e.getMessage (), MessageType.ERROR);
      if(rw!=null) {
        rw.append ("Error: "+e.getMessage ());
        rw.append ("<br/>");
      }
    } finally {
      findComponent ("contentPublishResponse").setRendered (true);
      setHtmlResponse (StringEscapeUtils.unescapeXml(dynamicResponse.toString ()));
      dynamicResponse = new StringBuilder ();
      findComponent ("pnlActions").setRendered (false);
      findComponent ("pnlReview").setRendered (true);
    }
  }
  
  @PostConstruct
  public void init() {
    htmlResponse = "";
  }
  
  
  private StringBuilder getResponseappendr() throws IOException {
    return dynamicResponse;
  }

  /**
   *  Extracts the current zip file and returns the path of the file
   * @return
   */
  protected String extractContent ()  throws Exception
  {
    File file = new File (extractPathParent);

    // if the zip parent folder does not exist then create it
    if (!file.exists ())
    {
      file.mkdir ();
    }

    // get the path of the zip file to extract
    String zipFilePath = uploadPath + getZipFileName ();

    // get the path of the folder to extract the zip to
    String zipName = Path.getFileNameWithoutExtension (getZipFileName ());

    Calendar cal = Calendar.getInstance ();
    SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd_hh-mm-ss-aa");
    String strDate = sdf.format (cal.getTime ());

    String zipExtactPath = String.format ("%s-%s", zipName, strDate);
    zipExtactPath = Path.combine (extractPathParent, zipExtactPath);

    unzipFile (zipFilePath, zipExtactPath);

    return zipExtactPath;
  }

  public static void unzipFile (String filePath, String extractPath) throws Exception {
            /*
             * STEP 1 : Create directory with the name of the zip file
             *
             * For e.g. if we are going to extract c:/demo.zip create c:/demo
             * directory where we can extract all the zip entries
             *
             */
            File extractDir = new File(extractPath);
            if(!extractDir.exists ()) {
              extractDir.mkdir ();
            }
           
            /*
             * STEP 2 : Extract entries while creating required
             * sub-directories
             *
             */
            try(ZipFile zipFile = new ZipFile(filePath)) {
                Enumeration<? extends ZipEntry> e = zipFile.entries ();
               
                while(e.hasMoreElements())
                {
                        ZipEntry entry = e.nextElement();
                        File destinationFilePath = new File(extractPath,entry.getName());
    
                        //create directories if required.
                        destinationFilePath.getParentFile().mkdirs();
                       
                        //if the entry is directory, leave it. Otherwise extract it.
                        if(entry.isDirectory())
                        {
                                continue;
                        }
                        else
                        {
                               
                                /*
                                 * Get the InputStream for current entry
                                 * of the zip file using
                                 *
                                 * InputStream getInputStream(Entry entry) method.
                                 */
                                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                                                                                                               
                                int b;
                                byte buffer[] = new byte[1024];
    
                                /*
                                 * read the current entry from the zip file, extract it
                                 * and append the extracted file.
                                 */
                                FileOutputStream fos = new FileOutputStream(destinationFilePath);
                                BufferedOutputStream bos = new BufferedOutputStream(fos,
                                                                1024);
    
                                while ((b = bis.read(buffer, 0, 1024)) != -1) {
                                                bos.write (buffer, 0, b);
                                }
                               
                                //flush the output stream and close it.
                                bos.flush();
                                bos.close();
                               
                                //close the input stream.
                                bis.close();
                        }
                }
            } catch(Exception e) {
              _logger.error ("Error during unzipFile ",e);
              throw e;
            }
   
}

  private void loadAndProcessFiles (HashMap<String, ItemBankFile> itemBankFiles, String resourceFilePrefix, String contentFilePrefix, String extractDir) throws Exception
  {
    // get file names
    HashMap<Long, String> xmlFilePaths = new HashMap<Long, String> ();
    HashMap<Long, Set<String>> resourceFilePaths = new HashMap<Long, Set<String>> ();
    Set<String> zipXMLFileNames = new HashSet<String> ();
    getExtractedFileNames (resourceFilePrefix,contentFilePrefix, extractDir, xmlFilePaths, resourceFilePaths, zipXMLFileNames);
    //TODO if validation required for zip file content.
    /*List<String> missingXMLFilesInsideZip = new ArrayList<String> ();
    if(!isItemBankFilesExistInsideZip (zipXMLFileNames, itemBankFiles,missingXMLFilesInsideZip)){
      throw new Exception();
    }*/
    // a collection of xml documents that has missing resources
    List<ITSDocumentInfo> allMissingResources = new ArrayList<ITSDocumentInfo> ();

    List<String> sqlList = new ArrayList<String>();
    StringBuilder sw = getResponseappendr();

    sw.append ("<table border='1' >");
    sw.append ("<thead>");
    sw.append ("<tr>");

    // headers
    sw.append ("<th>File</th>");
    sw.append ("<th>Bank Key</th>");
    sw.append ("<th>Item Key</th>");
    sw.append ("<th>Resources</th>");

    sw.append ("<th>Content Size</th>");
    sw.append ("<th>IB File</th>");
    sw.append ("<th>Errors</th>");

    sw.append ("</tr>");
    sw.append ("</thead>");

    sw.append ("<tbody>");

    for (Entry<Long, String> zipXmlFilePath:xmlFilePaths.entrySet ())
    {
      sw.append ("<tr>");
      // load xml for this file
      IITSDocument document = ITSDocumentFactory.load (zipXmlFilePath.getValue (), AccLookup.getNone (), false);

      // get the xml file name
      String zipXmlFile = Path.getFileName (zipXmlFilePath.getValue ());

      // get all the xml resource file paths
      Set<String> zipResourceFilePaths;
      zipResourceFilePaths = resourceFilePaths.get (zipXmlFilePath.getKey ());

      boolean hasResources = false;
      if (zipResourceFilePaths != null)
        hasResources = true;

      sw.append (String.format ("<td>%s</td>", zipXmlFile));
      sw.append (String.format ("<td>%s</td>", document.getBankKey ()));
      sw.append (String.format ("<td>%s</td>", document.getItemKey ()));
      //sw.flush ();

      // get key
      String key = document.getBankKey () + "-" + document.getItemKey ();

   // check if the item bank has this files info
      ItemBankFile itemBankFile = itemBankFiles.get (key);
      if (itemBankFile==null)  {
        appendError ("Not found in Item Bank.");
        continue; // go to the next file
      }
      
      
      // create xml content destination directory if it doesn't exist
      File itemBankFileDirectory = new File(itemBankFile.getDirectory ());
      if (!itemBankFileDirectory.exists ())
      {
        itemBankFileDirectory.mkdir ();
      }

      // get the path of the destination item/passage xml file
      String itemBankXmlFileName = Path.combine (itemBankFile.getDirectory (), zipXmlFile);
      
      // move xml content from extract folder to the destination and make a copy
      // of original file name
      
      FileUtils.copyFile  (new File(zipXmlFilePath.getValue ()), new File (itemBankFile.getFilePath ()));
//      FileUtils.copyFile  (new File(itemBankFile.getFilePath ()), new File (itemBankXmlFileName));

      // move resources from extract folder to the destination
      if (hasResources)
      {
        for (String resourceFile : zipResourceFilePaths)
        {
          FileUtils.copyFile  (new File(resourceFile), new File (Path.combine (itemBankFile.getDirectory (), Path.getFileName (resourceFile))));
        }
      }

      // check for missing resources
      ITSDocumentInfo documentInfo = new ITSDocumentInfo (itemBankXmlFileName);
      documentInfo.process ();

      // track any missing resources
      if (documentInfo.getResourcesMissing ().size () > 0)
      {
        allMissingResources.add (documentInfo);
      }

      // update the database with the timestamp
      String sql = "UPDATE " + (contentFilePrefix.equalsIgnoreCase  ("Item") ? "tblitem" : "tblstimulus") +
          " SET version = '" + Path.getFileName (zipXmlFilePath.getValue ()) +
          "', datelastupdated = now(), contentsize = " + documentInfo.getContentSize () +
          " WHERE _key = '" + itemBankFile.getKey () + "'";

      sqlList.add (sql);

      // append out table
      sw.append (String.format ("<td>%s</td>", documentInfo.getResources ().size ())); // "Resources"
      sw.append (String.format ("<td>%s <br/>(%s)</td>",readableFileSize (documentInfo.getContentSize ()), documentInfo.getContentSize ())); // "Size"
      sw.append (String.format ("<td>%s</td>", itemBankXmlFileName)); // "IB File"
      sw.append ("<td></td>"); // "Errors"
      sw.append ("</tr>");
      //sw.flush ();
    }

    // for (KeyValuePair<long, String> zipXmlFilePath in xmlFilePaths)

    sw.append ("</tbody>");
    sw.append ("</table>");

    sw.append ("<br/><br/>");

    // report missing resources
    if (allMissingResources.size () > 0)
    {
      sw.append ("<br/>");
      sw.append (wrapColor ("red", "Missing Resources... <br/>"));
      //sw.flush ();

      for (ITSDocumentInfo missingResource : allMissingResources)
      {
        // get key
        String key = missingResource.getDocument ().getBankKey () + "-" + missingResource.getDocument ().getItemKey ();
        sw.append (String.format ("<b>%s %s</b> ", resourceFilePrefix, key));
        sw.append (StringUtils.join (missingResource.getResourcesMissing (), ", "));
        sw.append ("<br/>");
      }

      sw.append ("<br/>");
      //sw.flush ();
    }

    // update version in item bank
    if (!sqlList.isEmpty ())
    {
      sw.append ("<p>Updating item bank... </p>");
      //sw.flush ();

      executeBatch (sqlList);
    }

  }
  
  /*private boolean isItemBankFilesExistInsideZip(Set<String> zipXMLFileNames, HashMap<String, ItemBankFile> itemBankFiles,List<String> missingXMLFilesInsideZip ) {
    for(ItemBankFile itemBankFile:itemBankFiles.values ()) {
      String filePath = itemBankFile.getFilePath ().substring (0, itemBankFile.getFilePath ().indexOf ("\\"));
      
    }
    return true;
  }*/

  public Collection<File> getResourceFiles (String directory, String resourceNamePattern)  throws Exception
  {
    File baseDir = new File(directory);
    IOFileFilter filter = new WildcardFileFilter (resourceNamePattern,IOCase.INSENSITIVE);
    Collection<File> fileLists = FileUtils.listFiles (baseDir,filter,TrueFileFilter.INSTANCE);
    return fileLists;
  }
  
  public Collection<File> getFilesListByPattern(String directory, String resourceNamePattern) throws Exception {
    File baseDir = new File(directory);
    IOFileFilter filter = new RegexFileFilter  (resourceNamePattern,IOCase.INSENSITIVE);
    Collection<File> fileLists = FileUtils.listFiles (baseDir,filter,TrueFileFilter.INSTANCE);
    return fileLists;
  }
  
  
 
  /**
   * Get a list of all the file and resources in a directory.
   * @param filePrefix - item or passage
   * @param directory
   * @param xmlFiles
   * @param resourceFiles
   * @throws Exception
   */
  private void getExtractedFileNames (String resourceFilePrefix,String contentFilePrefix, String directory, HashMap<Long, String> xmlFiles, HashMap<Long, Set<String>> resourceFiles,Set<String> xmlFileNames) throws Exception
  {
    
    for(File contentFileObj : getFilesListByPattern (directory, contentFilePrefix + "-[0-9]*-[0-9]*.xml")){
      
      String[] fileSplit = Path.getFileNameWithoutExtension (contentFileObj.getName ()).split ("-");
      // get ITS item key from file name
      long itsKey = tryParseLong (fileSplit[2]);
      xmlFiles.put (itsKey, contentFileObj.getAbsolutePath ());
      xmlFileNames.add (contentFileObj.getName ());
    }
    // loop through each item file
    for (File fileObj : getResourceFiles (directory, resourceFilePrefix + "_*.*"))
    {
      String file = fileObj.getName ();
      String[] fileSplit = Path.getFileNameWithoutExtension (file).split ("_");

      // get ITS item key from file name
      long itsKey = tryParseLong (fileSplit[1]);

      // check if the file is xml
      /*boolean isXml = Path.getExtension (file).equalsIgnoreCase ("xml");

      // check if file is content
      boolean isContent = false;

      if (isXml)
      {
        // e.x., "Item_97_v65.xml"
        if (fileSplit.length == 3) {
          isContent = true;
        }
      }

      // place file in right bucket
      if (isContent)
      {
        if (xmlFiles.containsKey (itsKey))
        {
          throw new Exception("The ITS key " + itsKey + " (" + Path.getFileName(file) + ") is a duplicate");
        }
        xmlFiles.put (itsKey, fileObj.getAbsolutePath ());
        xmlFileNames.add (fileObj.getName ());
      }
      else
      {*/
        // add to items dictionary
        Set<String> itemFiles = resourceFiles.get (itsKey);

        if (itemFiles == null)
        {
          itemFiles = new HashSet <String> ();
          resourceFiles.put (itsKey, itemFiles);
        }

        itemFiles.add (fileObj.getAbsolutePath ());
      }
//    }
  }
  
  private long tryParseLong(String value) {
    try {
      return Long.parseLong (value);
    } catch (NumberFormatException e) {
      _logger.error (e.getMessage ());
      return 0;
    }
  }

  private void appendError (String message) throws IOException
  {
    StringBuilder sw = getResponseappendr ();
    sw.append ("<td></td>");
    sw.append ("<td></td>");
    sw.append (String.format ("<td>%s</td>", message));
    sw.append ("</tr>");
  }

  protected String wrapColor (String color, String text)  throws Exception
  {
    return String.format ("<font color=\"%s\">%s</font>", color, text);
  }

}
