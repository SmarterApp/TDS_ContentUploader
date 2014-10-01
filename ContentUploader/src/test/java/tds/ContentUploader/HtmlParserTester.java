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

import java.util.List;

import org.junit.Test;

/**
 * @author jmambo
 *
 */
public class HtmlParserTester
{

  @Test
  public void testScrapeGridResources () {
    String gridAnswerSpace = "<Question id=\"540\" ITSVer=\"10\" ScoreEngineVer=\"1\" version=\"2.0\"><Description></Description><QuestionPart id=\"1\"><Options><ShowButtons></ShowButtons><GridColor>None</GridColor><GridSpacing>10,N</GridSpacing><CenterImage>true</CenterImage><ScaleImage>false</ScaleImage></Options><Text></Text><ObjectMenuIcons><IconSpec><FileSpec>item_540_v22_1_png16malpha.png</FileSpec><Label>1</Label></IconSpec><IconSpec><FileSpec>item_540_v22_2_png16malpha.png</FileSpec><Label>2</Label></IconSpec><IconSpec><FileSpec>item_540_v22_3_png16malpha.png</FileSpec><Label>3</Label></IconSpec><IconSpec><FileSpec>item_540_v22_4_png16malpha.png</FileSpec><Label>4</Label></IconSpec></ObjectMenuIcons><ImageSpec><FileSpec>item_540_v22_Background_png16malpha.png</FileSpec><Position>0,0</Position></ImageSpec></QuestionPart></Question>";

    List<String> gridResources = HtmlParser.scrapeGridResources (gridAnswerSpace);
    assertEquals (5, gridResources.size ());
    for (int i = 1; i < 5; i++) {
      assertEquals ("item_540_v22_" + i + "_png16malpha.png", gridResources.get (i - 1));
    }
    assertEquals ("item_540_v22_Background_png16malpha.png",  gridResources.get (4));
  }

}
