/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author jmambo
 *
 */
public class FileSizeFormatProviderTest
{

  @Test
  public void testFormatWithAndWithoutFormatSize () throws Exception {
    assertEquals ("345.00 B (345)", FileSizeFormatProvider.format ("{0:fs} ({0})", 345));
  }

  @Test
  public void testFormatMultipleNonFormatSize () throws Exception {
    assertEquals ("(2345) 2345 2345", FileSizeFormatProvider.format ("({0}) {0} {0}", 2345));
  }

  @Test
  public void testFormatWithDifferentValues () throws Exception {
    assertEquals ("234.00 B 84832", FileSizeFormatProvider.format ("{0:fs} {1}", 234, 84832));
  }

  @Test
  public void testFormatRoundingOff () throws Exception {
    assertEquals ("234.35 B", FileSizeFormatProvider.format ("{0:fs2}", 234.347));
    assertEquals ("234.35 B", FileSizeFormatProvider.format ("{0:fs2}", 234.345));
    assertEquals ("234.34 B", FileSizeFormatProvider.format ("{0:fs2}", 234.344));
  }

  @Test
  public void testFormatKB () throws Exception {
    assertEquals ("827.59kB", FileSizeFormatProvider.format ("{0:fs}", 847449));
  }

  @Test
  public void testFormatPrecision () throws Exception {
    assertEquals ("824.6572kB", FileSizeFormatProvider.format ("{0:fs4}", 844449));
  }

  @Test
  public void testFormatMB () throws Exception {
    assertEquals ("80.53MB", FileSizeFormatProvider.format ("{0:fs2}", 84444945));
  }

  @Test
  public void testFormatGB () throws Exception {
    assertEquals ("78.65GB", FileSizeFormatProvider.format ("{0:fs}", "84444945678"));
  }

  @Test
  public void testFormatWithMultipleFileSizeFormat () throws Exception {
    assertEquals ("824.6572kB 530.783kB", FileSizeFormatProvider.format ("{0:fs4} {1:fs3}", 844449, 543522));
  }

}
