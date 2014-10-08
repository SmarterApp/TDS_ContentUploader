/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Web.backing;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tds.ContentUploader.ITSDocumentInfo;
import tds.ContentUploader.ItemBankFile;
import tds.ContentUploader.ItemBankManager;
import tds.ContentUploader.Model.ITSZip;
import tds.ContentUploader.Web.BasePage;
import tds.ContentUploader.Web.ConnectionInfo;
import tds.ContentUploader.sql.IItemBankRepository;
import AIR.Common.Utilities.Path;
import AIR.Common.Utilities.SpringApplicationContext;

/**
 * @author mpatel
 * 
 */
public class ContentUploadBacking extends BasePage
{
  private static final Logger _logger = LoggerFactory.getLogger (ContentUploadBacking.class);
  private String lblExtractPath;
  private String lblUploadPath;
  private String lblIBServer;
  private String lblIBDB;
  private String lblContentSize;
  private UploadedFile uploadedFile;
  private List<ITSZip> fileList = new ArrayList<ITSZip> ();
  private String connectionName;
  private String sortColumn;
  private boolean sortAscending;
  
  

  public String getConnectionName () {
    return connectionName;
  }

  public void setConnectionName (String connectionName) {
    this.connectionName = connectionName;
  }

  public String getSortColumn () {
    return sortColumn;
  }

  public boolean isSortAscending () {
    return sortAscending;
  }

  public void setSortColumn (String sortColumn) {
    this.sortColumn = sortColumn;
  }

  public void setSortAscending (boolean sortAscending) {
    this.sortAscending = sortAscending;
  }

  public ContentUploadBacking () {
    super ();
    _itemBankManager = SpringApplicationContext.getBean (ItemBankManager.class);
    lblExtractPath = getTdsSettings ().getAppSettings ().get ("extractPath");
    lblUploadPath  = getTdsSettings ().getAppSettings ().get ("uploadPath");
  }

  private ItemBankManager _itemBankManager;

  
  public String getLblExtractPath () {
    return lblExtractPath;
  }

  public String getLblUploadPath () {
    return lblUploadPath;
  }

  public String getLblIBServer () {
    return lblIBServer;
  }

  public String getLblIBDB () {
    return lblIBDB;
  }

  public String getLblContentSize () {
    return lblContentSize;
  }
  
  public UploadedFile getUploadedFile() {
    return uploadedFile;
  }
  
  public void setUploadedFile(UploadedFile uploadedFile) {
      this.uploadedFile = uploadedFile;
  }

  
  public List<ITSZip> getFileList () {
    return fileList;
  }

  public void setFileList (List<ITSZip> fileList) {
    this.fileList = fileList;
  }

  public void uploadFile() throws Exception {
    try {
        if(uploadedFile==null) {
          writeMessage ("Click on Browse to select the File", MessageType.ERROR );
          return ;
        }
        String fileName = FilenameUtils.getName(uploadedFile.getName());
        String fileExtension = FilenameUtils.getExtension (uploadedFile.getName());
        String contentType = uploadedFile.getContentType();
        if(!contentType.toLowerCase ().contains ("zip")) {
          writeMessage ("Only files with .zip extension allowed. "+fileExtension+" is not a Valid file extension.", MessageType.ERROR );
          return ;
        }
        File uploadDir = new File(lblUploadPath);
        String newFilePath = uploadDir+"/"+fileName;
        File file = new File(newFilePath);
        try(InputStream input = uploadedFile.getInputStream();OutputStream output = new FileOutputStream(file)) {
          int read = 0;
          byte[] bytes = new byte[1024];
          while ((read = input.read(bytes)) != -1) {
            output.write(bytes, 0, read);
          }
          output.flush ();
        } catch (Exception e) {
          throw e;
        }
        updateFileList ();
        writeMessage (String.format("File '%s' is successfully uploaded!", fileName), MessageType.SUCCESS );
    } catch (Exception e) {
      _logger.error (e.getMessage (),e);
      writeMessage ("Error while uploading File : "+e.getMessage (), MessageType.ERROR );
    }
  }

  public void onClick_Delete(String fileName)
  {
      try {
        String filePath = Path.combine(lblUploadPath, fileName);
        File file = new File(filePath);
        file.delete ();
        updateFileList ();
        writeMessage (fileName+" is deleted Successfully!", MessageType.SUCCESS );
      } catch (Exception e) {
        _logger.error ("Error while deleting file "+fileName,e);
        writeMessage ("Error while deleting file "+fileName + ". Error: "+e.getMessage (), MessageType.ERROR );
      }
  }
  
  public void btnClearExtract_OnClick() {
    try {
      File file = new File(lblExtractPath);
      FileUtils.deleteDirectory (file);
      writeMessage ("Extraction folder is cleared. ", MessageType.SUCCESS );
    } catch (IOException e) {
      _logger.error (e.getMessage (),e);
      writeMessage ("Error while clearing extraction : "+e.getMessage (), MessageType.ERROR );
    }
  }
  
  
  public void btnUpdateContentSize_OnClick() {
    try {
      UpdateContentSize ();
    } catch (Exception e) {
      _logger.error (e.getMessage (),e);
      e.printStackTrace ();
      writeMessage ("Error while updating content size : "+e.getMessage (), MessageType.ERROR );
    }
      
  }
  private void updateFileList() throws Exception
  {
      fileList.clear ();
      for (File fileInfo : getFileNamesInDirectory(lblUploadPath, "*.zip"))
      {
          ITSZip itsZip = new ITSZip();
          itsZip.setName(_itemBankManager.getContentApplication ().getItemBankConnectionName());

          itsZip.setFileName(fileInfo.getName ());
          itsZip.setDateUploaded (new Date(fileInfo.lastModified ()));
          itsZip.setDateDisplayed (new SimpleDateFormat ("dd/MM/YYYY hh:mm:ss aa").format (itsZip.getDateUploaded ()));
          itsZip.setFileSize(readableFileSize( fileInfo.length ()));
          itsZip.setFileSizeBytes (fileInfo.length ());
          fileList.add (itsZip);
      }
  }
  
  
  private  File[] getFileNamesInDirectory(String path, String pattern)  throws Exception{
    File directory = new File (path);
    FileFilter filter = new WildcardFileFilter (pattern);
    File[] filteredFiles = directory.listFiles (filter);
    return filteredFiles;
  }
  
  @PostConstruct
  public void init () {
    pageInit ();
  }

  private void pageInit () {
    try {
      String itemBankConnName = FacesContext.getCurrentInstance ().getExternalContext ().getRequestParameterMap ().get ("name");
      setConnectionName (itemBankConnName);
      if(itemBankConnName!=null && !itemBankConnName.isEmpty ()) {
        // set item bank info
        ConnectionInfo connectionInfo = new ConnectionInfo (itemBankConnName, _itemBankManager.getItemBankConnectionString ());
        this.lblIBServer = connectionInfo.getDatasource ();
        this.lblIBDB = connectionInfo.getInitCatalog ();
        
        // setup directories
        File uploadDir = new File (lblUploadPath);
        if (!uploadDir.exists ())
        {
          uploadDir.mkdirs ();
        }
      }
      updateFileList ();
    } catch (Exception e) {
      _logger.error ("Error in pageInit : ",e);
      writeMessage ("Error : "+e.getMessage (), MessageType.ERROR );
    }

  }

  private void UpdateContentSize () throws Exception
  {
    IItemBankRepository itemPreviewRepository = _itemBankManager.CreateItemPreviewRepository ();

    Map<String, ItemBankFile> itemFiles = new HashMap<String, ItemBankFile> (), stimulusFiles = new HashMap<String, ItemBankFile> ();
    itemPreviewRepository.getContentFiles (itemFiles, stimulusFiles);
    
//    StringBuilder sqlBuilder = new StringBuilder ();
    List<String> batchQueryList = new ArrayList<String> ();

    for (Entry<String, ItemBankFile> itemFile : itemFiles.entrySet ())
    {
      ITSDocumentInfo docInfo = GetITSDocumentInfo (itemFile.getValue ().getFilePath ());

      // update the database content size
      final String sql = "UPDATE tblitem SET DateLastUpdated = now(), ContentSize = %s WHERE _Key = '%s'; ";
      batchQueryList.add (String.format (sql, docInfo.getContentSize (), itemFile.getValue ().getKey ()));
    }

    for (Entry<String, ItemBankFile> stimulusFile : stimulusFiles.entrySet ())
    {
      ITSDocumentInfo docInfo = GetITSDocumentInfo (stimulusFile.getValue ().getFilePath ());

      // update the database content size
      final String sql = "UPDATE tblstimulus SET DateLastUpdated = now(), ContentSize = %s WHERE _Key = '%s'; ";
      batchQueryList.add (String.format (sql, docInfo.getContentSize (), stimulusFile.getValue ().getKey ()));
    }
    
    // update SQL
    if(!batchQueryList.isEmpty ()) {
      executeBatch (batchQueryList);
    }
    lblContentSize = String.format ("Updated: Stimuli %s, Items %s", stimulusFiles.size (), itemFiles.size ());
  }

  private static ITSDocumentInfo GetITSDocumentInfo (String fileName)
  {
    ITSDocumentInfo docInfo = new ITSDocumentInfo (fileName);
    docInfo.process ();
    return docInfo;
  }
  
}
