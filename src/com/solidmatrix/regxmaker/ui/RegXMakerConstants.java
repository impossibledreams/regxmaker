package com.solidmatrix.regxmaker.ui;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.RegXMakerConstants
 * Project: RegularExpression compiler
 * Version: 1.1
 * Tier   : N/A (Constants)
 * Author : Gennadiy Shafranovich
 * Purpose: Holds common constants to the RegXMaker interfaces.
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
 * Comments: Full, with javadoc
 *
 * Modification History
 *
 * 07-05-2001  GS Created
 *
 * 07-05-2004  YS Added licensing information
 * </PRE>
 */

public interface RegXMakerConstants  {

  /**************************
   * Identification section *
   **************************/

   
  short MAJOR_VERSION = 1;
  short MINOR_VERSION = 0;
  short REVISION_VERSION = 0;
  String ID_STRING = "RegXMaker.";
  
  /*********************
   * RegXMaker section *
   *********************/
  

	/**
     * Hold the filename of the RegXMaker settings file
     */
  String SETTING_FILE_NAME = "RegXMaker.properties";
  
  	/**
     * Default value of timeout for processing
     */
  long DEFAULT_TIMEOUT = 600000; //10 minutes
  
  	/**
     * Delay between status updates
     */
  long INCREMENT = 1000; //1 second

  	/**
     * Name of property in settins file
     */
  String AUTOSAVE_PROPERTY = "autosave";
  
  	/**
     *
     */
  String AUTOLOAD_PROPERTY = "autoload";
  
  	/**
     * Name of property in settins file
     */
  String CMP_LEVEL_PROPERTY = "compileLevel";
  
  	/**
     * Name of property in settins file
     */
  String ACC_LEVEL_PROPERTY = "accuracyLevel";

  	/**
     * Name of property in settins file
     */
  String FORMAT_PROPERTY = "textFormat";
  
  	/**
     * Name of property in settins file
     */
  String TOOLBAR_PROPERTY = "toolbarEnabled";
  
  	/**
     * Name of property in settins file
     */
  String L_TOOLBAR_PROPERTY = "listToolbarEnabled";
  
  	/**
     * Name of property in settins file
     */
  String DRAG_MODE = "dragMode";  
  
  	/**
     * Name of property in settins file
     */
  String TIMEOUT_PROPERTY = "timeout";
 
    /**
     * Directory in which prog is executed
     */
  String USER_DIR = System.getProperty("user.dir");
  
    /**
     * System char for file seperator
     */
  String FILE_SEPERATOR = System.getProperty("file.separator");
  
  String ICON_DIR = "icons/";
}//RegXMakerConstants  