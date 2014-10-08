/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import AIR.Common.Utilities.TDSStringUtils;

public class HtmlParser
{
  // / <summary>
  // / Return the images links in a given URL as an array of Strings.
  // / </summary>
  // / <returns>String array of all images on a page</returns>
  public static List<String> scrapeHtml (String html, String tag, String attribute)
  {
    List<String> matchingValues = new ArrayList<String> ();
    if (StringUtils.isEmpty (html))
      return matchingValues;

    // set up the regex for finding tag/attribute
    StringBuilder htmlPattern = new StringBuilder ();
    htmlPattern.append (String.format ("<%s[^>]+", tag)); // start tag (e.x.,
                                                          // "img")
    htmlPattern.append (String.format ("%s\\s*=\\s*", attribute)); // start
                                                                   // attribute
    // property (e.x.,
    // "src")
    // three possibilities for what attribute property --
    // (1) enclosed in double quotes
    // (2) enclosed in single quotes
    // (3) enclosed in spaces
    htmlPattern.append (TDSStringUtils.format ("(?<{0}>\"([^\"]*)\"|'([^']*)'|([^\"'>\\s]+))", attribute));
    htmlPattern.append ("[^>]*>"); // end of tag

    Pattern pattern = Pattern.compile (htmlPattern.toString (), Pattern.CASE_INSENSITIVE);

    // look for matches
    Matcher tagCheck = pattern.matcher (html);

    while (tagCheck.find ())
    {
      String matchingValue = tagCheck.group (attribute);
      if(matchingValue.startsWith ("\"")) {
        matchingValue = matchingValue.substring (1, matchingValue.length ()-1);
      } 
      matchingValues.add (matchingValue);
    }

    return matchingValues;
  }

  public static List<String> scrapeGridResources (String gridAnswerSpace)
  {
    List<String> matchingValues = new ArrayList<String> ();
    if (StringUtils.isEmpty (gridAnswerSpace))
      return matchingValues;

    // set up the regex for finding images
    StringBuilder filePattern = new StringBuilder ();
    filePattern.append ("<FileSpec>"); // start tag
    filePattern.append ("(?<file>[^<]*)"); // named matching pattern
    filePattern.append ("</FileSpec>"); // end tag

    Pattern pattern = Pattern.compile (filePattern.toString (), Pattern.CASE_INSENSITIVE);
    // look for matches
    Matcher tagCheck = pattern.matcher (gridAnswerSpace);

    while (tagCheck.find ())
    {
      String matchingValue = tagCheck.group ("file");
      matchingValues.add (matchingValue);
    }

    return matchingValues;
  }

  public static List<String> scrapeHtmlImages (String html)
  {
    return scrapeHtml (html, "img", "src");
  }

  public static List<String> scrapeHtmlLinks (String html)
  {
    return scrapeHtml (html, "a", "href");
  }

  public static List<String> scrapeHtmlResources (String html)
  {
    List<String> resources = new ArrayList<String> ();
    resources.addAll (scrapeHtmlImages (html));
    resources.addAll (scrapeHtmlLinks (html));
    return resources;
  }
}
