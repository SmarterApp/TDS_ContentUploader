/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSizeFormatProvider
{

  private static final String     FILE_SIZE_FORMAT = "fs";
  private static final BigDecimal ONE_KILO_BYTE    = new BigDecimal (1024);
  private static final BigDecimal ONE_MEGA_BYTE    = new BigDecimal (1024 * 1024);
  private static final BigDecimal ONE_GIGA_BYTE    = new BigDecimal (1024 * 1024 * 1024);

  /**
   * Items in the string to be substituted are in the format {<index>:fs<precision>} where :fs<precision> is optional and index starts from zero
   * if <precision>  is not provided then a default precision of 2 is used
   * see unit tests for examples
   * 
   * @param formatPattern
   * @param values
   * @return
   */
  public static String format (String formatPattern, Object... values) {
    if (formatPattern == null) {
      return null;
    }
    if (values == null || values.length == 0) {
      return formatPattern;
    }

    for (int index = 0; index < values.length; ++index) {
      if (values[index] != null) {
        String patternString = "\\{" + index + "(:" + FILE_SIZE_FORMAT + "(\\d+)?)?\\}";
        Pattern p = Pattern.compile (patternString);
        Matcher matcher = p.matcher (formatPattern);
        String value = values[index].toString ();
        StringBuffer sb = new StringBuffer ();
        while (matcher.find ()) {
          String fs = matcher.group (1);
          if (fs != null) {
            int precision = 2;
            if (fs.length () > 3) {
              precision = Integer.parseInt (fs.substring (3));
            }
            matcher.appendReplacement (sb, getBytesformat (value, precision));
          } else {
            matcher.appendReplacement (sb, value);
          }
        }
        matcher.appendTail (sb);
        formatPattern = sb.toString();
      }
    }
    return formatPattern;
  }
  

  private static String getBytesformat (String filesize, int precision) {

    BigDecimal size = new BigDecimal (filesize);
    String suffix;
    
    if (size.compareTo (ONE_GIGA_BYTE) > 0) {
      size = size.divide (ONE_GIGA_BYTE);
      suffix = "GB";
    } else if (size.compareTo (ONE_MEGA_BYTE) > 0) {
      size = size.divide (ONE_MEGA_BYTE);
      suffix = "MB";
    } else if (size.compareTo (ONE_KILO_BYTE) > 0) {
      size = size.divide (ONE_KILO_BYTE);
      suffix = "kB";
    } else {
      suffix = " B";
    }
    return size.setScale (precision, RoundingMode.HALF_UP) + suffix;
  }


}
