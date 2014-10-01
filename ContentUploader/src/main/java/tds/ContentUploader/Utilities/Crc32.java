/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Utilities;

import java.util.zip.CRC32;
import java.util.zip.Checksum;


/**
 * 
 * Creates CRC32 value
 * 
 * Class currently not used
 * 
 * @author jmambo
 *
 */
public class Crc32
{

  /**
   * Gets CRC32 value of the input parameter
   * 
   * @param input
   * @return long CRC32 value
   */
  public long getValue(String input) {
    
      // get bytes from string
     byte bytes[] = input.getBytes();
      
     Checksum checksum = new CRC32();
    
     // update the current checksum with the specified array of bytes
     checksum.update(bytes, 0, bytes.length);
     
     return checksum.getValue();
   }
  
}
