package com.solidmatrix.regxmaker.ui;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.StringP
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: GUI component
 *
 * Copyright (C) 2001, 2004 SolidMatrix Technologies, Inc.
 * This file is part of RegXmaker GUI utility.
 *
 * RegXmaker GUI Utility is is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RegXmaker GUI Utility; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Modification History
 *
 * 07-05-2004	YS Added header and licensing information
 * </PRE>
 */

public class stringP
{
  //this object accepts input, either from the file or the user
  //the input has to be read, and its source has to be specified (file/usr inpt)
 
  //the string that contains text read from file or user
  private String contents;
  
  //this will show up in the list, either file name, or 1st N characters
  private String preview;
  
  //# of chars in that show up in the list; makes sence only for user input... for now
  private static final int N=25;
  
  //is this a file?
  private boolean thisIsfile;
  
  //is this a url?
  private boolean thisIsUrl;
 
  public stringP (String input, String name)
  { 
    if (name.length()<N)
      preview = name;
    else 
      preview = name.substring(0, N);
      
    contents = input;
    thisIsfile = true;
    thisIsUrl = false;
  }//stringP() - from file
  
  public stringP (String input, String name, boolean dummy) {
    this(input, name);
    
    thisIsfile = false;
    thisIsUrl = true;
  }//stringP - from url
  
  public stringP (String input) {
    if (input.length()<N)
      preview = input;
    else 
      preview = input.substring(0, N);
      
    contents= input;
    thisIsfile = false;
  }//stringP() - from String
    
  public boolean isFile() { return thisIsfile; }
  public boolean isUrl() { return thisIsUrl; }
  public String getContents() { return contents; }
  
  //get the string that was passed in
  public void setContents(String con)
  {
    contents=con;
    newStringPreview();
  }//setContents()
    
  private void newStringPreview()//get the string that was passed in
  {
    if (!isFile())//if not a file, refresh the preview
      preview = trimStringToPreview(contents);
  }//newStringPreview()
  
  public static String trimStringToPreview(String a) {
    if (a.length()<N)
      return a;
    else 
      return a.substring(0, N);
  }//trimStringToPreview()
    
  public String toString() { return preview; }
    
  public String toolTipPrev()//gets text that will appear in the tooltip
  {
    if (contents.length()<40)
      return contents;
    else 
      return (contents.substring(0, 40).concat("..."));
  }//toolTipPrev()
}//stringP