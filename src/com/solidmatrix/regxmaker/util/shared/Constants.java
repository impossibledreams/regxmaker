package com.solidmatrix.regxmaker.util.shared;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.Constants
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A 
 * Author : Gennadiy Shafranovich
 * Purpose: Constants and related functions for file patching and 
 *          comparison
 * 
 * Copyright (C) 2001, 2004 SolidMatrix Technologies, Inc.
 * This file is part of RegXmaker Library.
 *
 * RegXmaker Library is is free software; you can redistribute it and/or modify
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * RegXmaker library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Comments: Full, no javadoc.
 *
 * Modification History
 *
 * 01-15-2001 GS Creation
 * 03-11-2001 GS Revised for new format
 * 05-22-2001 GS Converted to class from interface, included 
 *               byte[] to int signature convertion
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */
 
public final class Constants {
  
  //make sure you cannot be instantiated
  private Constants() { }

  
  /**************************
   * Identification section *
   **************************/
   
  
  public static final short MAJOR_VERSION = 1;
  public static final short MINOR_VERSION = 0;
  public static final short REVISION_VERSION = 0;
  public static final String ID_STRING = "SuperDiff/SuperPatch/RegXMaker Global Testing library. GAMMA VERSION.";
  
  
  /************************
   * Comparison Constants *
   ************************/
  
  
  //context display constants
  public static final int MAX_CONTEXT_SIZE = 240;
  public static final int CONTEXT_FORWARD_SIZE = 30;
  
  
  //splicer accuracy options
  public static final int LETTER_ACCURACY = 1;
  public static final int WORD_ACCURACY = 5;
  public static final int LINE_ACCURACY = 80;
  
  
  //encoding scheme options for text file loading
  public static final String OPTION_1_SCHEME = "ISO646-US";
  public static final String OPTION_2_SCHEME = "ISO-8859-1";
  public static final String OPTION_3_SCHEME = "UTF-8";
  public static final String OPTION_4_SCHEME = "UTF-16BE";
  public static final String OPTION_5_SCHEME = "UTF-16LE";
  public static final String OPTION_6_SCHEME = "UTF-16";

  
}//Constants