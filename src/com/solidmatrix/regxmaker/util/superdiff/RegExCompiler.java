package com.solidmatrix.regxmaker.util.superdiff;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import com.solidmatrix.regxmaker.util.shared.ArrayUtils;
import com.solidmatrix.regxmaker.util.shared.FailSafe;
import com.solidmatrix.regxmaker.util.shared.Monitor;
import com.solidmatrix.regxmaker.util.shared.splicing.CharSplicer;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.superdiff.RegExCompiler
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : 2 (Utility class)
 * Author : Gennadiy Shafranovich
 * Purpose: Provide methods to matching string or byte arrays
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
 * Comments: Full, with javadoc.
 *
 * Modification History
 *
 * 05-17-2001 GS Creation
 *
 * 05-19-2001 GS First version complete. Minimum testing done.
 *               Although the regex compiler looks like it works
 *               correctly i recommend more testing before the ALPHA
 *               version is put out.
 *
 * 06-18-2001 GS Adjusted methods to work with primitive splicer
 *               methods in MiniSplicer. Should become faster.
 *
 * 07-05-2001 GS Added FailSafe features
 *
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public final class RegExCompiler extends FailSafe {

	/**
     * The resulting regular expression will contain example cases
     * between the static sections of the given strings. ([abc])
     */
  public static final int DEEP_COMPILE = 0;
  
    /**
     * The resulting regular expresion will contain range of highest
     * to lowest example casesbetween the static sections of the 
     * given strings.
     * (for strings "a0" and "f4", regex: [a-f][0-4])
     */  
  public static final int SEMI_DEEP_COMPILE = 1;
  
	/**
     * The resulting regular expresion will contain only types of
     * cases between the static sections of the given strings.
     * ([a-z0-9_])
     */  
  public static final int SURFACE_COMPILE = 2;
  
	/**
     * The resulting regular expression will contain no representation
     * of dynamic sections in the given strings, rather it will
     * only contain the static sections. (.*)
     */  
  public static final int NO_COMPILE = 3;

  
  //object flags for non deep scans
  private static final Byte UNDERSCORE = new Byte((byte) 0);
  private static final Byte WHITESPACE = new Byte((byte) 1);
  private static final Byte NEWLINE = new Byte((byte) 2);
  private static final Byte LOWERCASE = new Byte((byte) 3);
  private static final Byte UPPERCASE = new Byte((byte) 4);
  private static final Byte NUMBERS = new Byte((byte) 5);
  private static final Byte UNKNOWN = new Byte((byte) 6);
  
  
  //minisplicer section
  private int ignoreWeight; 
  
  
  //last compiled reg ex
  private String regEx;
  
  
  //status monitoring
  private boolean monitoring;
  private Monitor status;
  
  
	/**
	 * Makes a new instance of RegExCompiler
	 *
     * @param   o SecureObject to be verified  
     * @param   ignoreWeight  minimum size of a match
     * @throws SecurityException if the given SecureObject has not been verified
	 */
  public RegExCompiler(int ignoreWeight) throws SecurityException {
    this.ignoreWeight = ignoreWeight;
    monitoring = false;
  }//RegExCompiler()
  
  
    /**
     * Makes a new instance of RegExCompiler with a monitoring object
     *
     * @param   o SecureObject to be verified  
     * @param   ignoreWeight  minimum size of a match
     * @param m Monitor object to which status updates will be posted
     * @throws SecurityException if the given SecureObject has not been verified
     */
  public RegExCompiler(int ignoreWeight, Monitor m) throws SecurityException {
    this(ignoreWeight);
    
    if(m == null)
      throw new IllegalArgumentException("Monitor object is null");
    
    monitoring = true;
    status = m;
  }//RegExCompiler()
 
 
	/**
	 * Compiles a regular expression out of an array of strings. 
     * First the CharMatcher class will be used to find all 
     * sections in the strings which match. Then the compileSection
     * method will be used to turn the non matching sections 
     * (those pieces of the input strings that are between matching 
     * sections) into regular expressions. The resulting regular 
     * expression is made up of sections which match across all input 
     * strings and sub expressions derived from non matching sections.
	 *
	 * @param   strings  input strings to compile
	 * @param   level  level of compile
	 * @return     a String containing the regular expression
	 * @exception   NullPointerException  if any of the input strings are
     * null or the input string array is null
     *
	 * @exception   IllegalArgumentException  if the input string array
     * contains a 0 length string
     *
     * @exception SecurityException if the SecurityObject given during
     * instantiation has changed and can not be verified
     *
     * @see smti.util.shared.CharMatcher
     * @see smti.util.superdiff.RegExCompiler#DEEP_COMPILE
     * @see smti.util.superdiff.RegExCompiler#SEMI_DEEP_COMPILE
     * @see smti.util.superdiff.RegExCompiler#SURFACE_COMPILE
     * @see smti.util.superdiff.RegExCompiler#NO_COMPILE
	 */
  public String compile(String[] strings, int level) throws NullPointerException, IllegalArgumentException, SecurityException {
    //verify String array
    if(strings == null)
      throw new NullPointerException("String structures cannot be null");
      
    int elements = strings.length;
      
    for(int i = 0; i < elements; i++) {
      if(strings[i] == null)
        throw new NullPointerException("A String structure cannot be null");
      
      if(strings[i].length() <= 0)
        throw new IllegalArgumentException("A String structure cannot be of length 0, this will cause a total mismatch");
    }//for
    
    //status update
    if(monitoring)
      status.updateStatus("Parameters Valid.\nStarting Compile.");
      
    processing = true;
    
    //prep result
    StringBuffer sb = new StringBuffer(32);
    sb.append("/");
  
    //convert to char arrays
    char[][] data = new char[strings.length][];
    for(int x = 0; x < data.length && processing; x++)
      data[x] = strings[x].toCharArray();
  
    //status update
    if(monitoring && processing)
      status.updateStatus("Looking for matching sections (this may take several minutes).");
  
    //get static sections
    CharSplicer splicer = new CharSplicer(data, ignoreWeight);
    
    addFailSafe(splicer);
    LinkedList sections = splicer.splice();
    removeFailSafe(splicer);
    
    //status update
    if(monitoring && processing)
      status.updateStatus("Found all matching sections.");
    
    //prep temp arrays
    int[] starts = new int[elements];
    int[] ends = new int[elements];
    char[][] mismatches = new char[elements][];
   
    int sectionCount = sections.size();
    int processed = 0;
    int opercent = 0;
          
    //get iterator for static sections
    Iterator sectionIterator = sections.iterator();
    while (sectionIterator.hasNext() && processing) {
    
      //get next static section
      char[] section = (char[]) sectionIterator.next();
      
      //System.out.println(new String(section));
      
      //empty mismatch flag
      boolean emptyMis = true;
      
      //for each string record the ending of the previous mismatch
      //and the actually mismatch data
      for(int i = 0; i < elements && processing; i++) {
        ends[i] = ArrayUtils.indexOf(data[i], section, starts[i]);
        mismatches[i] = ArrayUtils.subarray(data[i], starts[i], ends[i] - starts[i]);
   
        //check for non empty mismatch
        if(emptyMis && ends[i] != starts[i] && processing)
          emptyMis = false;
      }//for
      
      //record dynamic only if non empty mismatch
      if(!emptyMis && processing)
        sb.append(compileSection(mismatches, level));
        
      //record static section
      if(processing)
        sb.append(processStatic(section));
     
      //prepare starts for next mismatch
      for(int j = 0; j < elements && processing; j++)
        starts[j] = ends[j] + section.length;
        
      processed++;
      int percent = (int) ((processed / (float) sectionCount) * 100);
      
      //status update
      if (monitoring && percent != opercent && processing) {
        status.updateStatus("Converting to regular expression format: " + percent + "% done.");
        opercent = percent;
      }//if
    }//while
    
    //check if we need to record the end of the strings as a mismatch
    boolean recordLast = false;
    for(int k = 0; k < elements && !recordLast && processing; k++) {
      if(starts[k] < data[k].length && processing)
        recordLast = true;
    }//for
    
    //record end of strings as mismatch if needed
    if (recordLast && processing) {
      //for each string record the ending of the previous mismatch
      //and the actually mismatch data
      for(int i = 0; i < elements && processing; i++) {
        ends[i] = data[i].length;
        
        if(starts[i] < ends[i])
          mismatches[i] = ArrayUtils.subarray(data[i], starts[i], ends[i] - starts[i]);
        else
          mismatches[i] = new char[0];
      }//for
      
      //record data
      if(processing)
        sb.append(compileSection(mismatches, level));
    }//if
    
    sb.append("/");
    
    regEx = sb.toString();
    
    //status update
    if(monitoring && processing)
      status.updateStatus("Regular expression compile done. Result size: " + regEx.length());
    
    processing = false;
    
    return regEx;
  }//compile()
  
  
	/**
	 * Compiles char arrays into regular expressions. For use by the compile() method
     * in order to detect all matching sections before this method is called.
	 *
	 * @param   data char arrays tp be compiled
	 * @param   level  level of compile
	 * @return     a String containing the resulting regular expression 
     * @see smti.util.superdiff.RegExCompiler#DEEP_COMPILE
     * @see smti.util.superdiff.RegExCompiler#SEMI_DEEP_COMPILE
     * @see smti.util.superdiff.RegExCompiler#SURFACE_COMPILE
     * @see smti.util.superdiff.RegExCompiler#NO_COMPILE
	 */
  private String compileSection(char[][] data, int level) {
    if(level == NO_COMPILE)
      return ".*";
  
    //prepare data for processing
    
    StringBuffer sb = new StringBuffer(32); //output buffer
    int elements = data.length; //count
    boolean[] active = new boolean[elements]; //active string flags
    
    //all active
    LinkedList types = new LinkedList(); //char types or examples (TreeSets)
    LinkedList counts = new LinkedList(); //counts for types or examples
    
    int activec = 0;//active string counter
    int point = 0;//current position pointer
    int apoint = -1; //at which position is activec < elements

    
    //set defaults
    for(int i = 0; i < elements && processing; i++) {
      if (data[i].length > 0) {
      
        //if string length is not 0 turn string on
        active[i] = true;
        activec++;
      } else {
      
        //otherwise turn string off
        active[i] = false;
        
        //check for "not all" flag pointer setting
        if(apoint == -1)
          apoint = 0;
      }//if..else
    }//for
    
    //cycle until there are no more active strings
    while (activec > 0 && processing) {
      
      //make set with characters from all active strings at current position
      char[] temp = new char[activec];
      for(int j = 0, k = 0; j < elements && processing; j++) {
      
        //take char only if active string
        if (active[j]) {
          temp[k] = data[j][point];
          k++;
        }//if
      }//for
      
      //make a TreeSet of current characters or types
      TreeSet cur = new TreeSet();
      for(int l = 0; l < activec && processing; l++) {
        switch (level) {
          case DEEP_COMPILE:
          case SEMI_DEEP_COMPILE:
            cur.add(new Character(temp[l])); //record example
            
            break;
            
          case SURFACE_COMPILE:
        
            //if surface scan, record type with constants
            if(temp[l] == '_')
              cur.add(UNDERSCORE);
            else if(temp[l] == ' ')
              cur.add(WHITESPACE);
            else if(temp[l] == '\n')
              cur.add(NEWLINE);
            else if (temp[l] >= 'a' && temp[l] <= 'z')
              cur.add(LOWERCASE);
            else if (temp[l] >= 'A' && temp[l] <= 'Z')
              cur.add(UPPERCASE);
            else if (temp[l] >= '0' && temp[l] <= '9')
              cur.add(NUMBERS);
            else 
              cur.add(UNKNOWN);
          
            break;
        }//switch
      }//for
      
      
      //if an unknown char exists and surface scan is on, record only unknown
      if (level == SURFACE_COMPILE && cur.contains(UNKNOWN)) {
        boolean withNewLine = cur.contains(NEWLINE);
      
        cur.clear();
        cur.add(UNKNOWN);
        
        if(withNewLine)
          cur.add(NEWLINE);
      }//if
      
      //FIXME: make more efficient way to keep counts
        
      if (point == 0 || !cur.equals((TreeSet) types.getLast())) {
        
        //if these are the first, record
        types.addLast(cur);
        counts.addLast(new Integer(1));
      } else {
        
        //if sets equal, add to previous counter
        Integer ccur = (Integer) counts.removeLast();
        counts.addLast(new Integer(ccur.intValue() + 1));
      }//if..else
      
      point++; //increment position
      
      for(int m = 0; m < elements && processing; m++) {
      
        //deactivate string if char taken was the last one
        if (active[m] && point >= data[m].length) {
          active[m] = false;
          activec--;
          
          //check for "not all" flag pointer setting
          if(apoint == -1)
            apoint = point;
        }//if
      }//for
    }//while
    
    //record findings into regular expression
    Iterator ti = types.iterator();
    Iterator ci = counts.iterator();
    int ecount = 0;
    
    //cycle through all findings
    while (ti.hasNext() && processing) {
    
      //get record, counter and TreeSet
      int ccount = ((Integer) ci.next()).intValue();
      TreeSet cur = (TreeSet) ti.next();
      
      //head bracket
      sb.append("[");
      
      if(level == DEEP_COMPILE) {
        
        //write example chars
        Iterator charit = cur.iterator();
        while (charit.hasNext() && processing) {
          char t = ((Character) charit.next()).charValue();
          sb.append(chooseRegExForChar(t));
        }//while
      } else if(level == SEMI_DEEP_COMPILE) {
        
        //write example char ranges
        char max = 0;
        char min = Character.MAX_VALUE;
        boolean hasExamples = (cur.size() > 0);
        
        Iterator charit = cur.iterator();
        while (charit.hasNext() && processing) {
          char t = ((Character) charit.next()).charValue();
          
          if(t < min)
            min = t;
          
          if(t > max)
            max = t;
        }//while
        
        if (hasExamples) {
          sb.append(chooseRegExForChar(min));
          
          if (min != max) {
            sb.append("-");
            sb.append(chooseRegExForChar(max));
          }//if
        }//if
      } else if(level == SURFACE_COMPILE) {
        
        //write types
        
        if (cur.contains(UNKNOWN)) {
        
          //if unknown type is present, write anychar
          sb.append(".");
          
          if(cur.contains(NEWLINE))
            sb.append("\\n");
        } else {
        
          //cycle through recorded types and write to reg ex
          Iterator ttit = cur.iterator();
          while (ttit.hasNext()) {
            Byte type = (Byte) ttit.next();
            
            if(type == UNDERSCORE)
              sb.append("_");
            else if(type == WHITESPACE)
              sb.append(" ");
            else if(type == NEWLINE)
              sb.append("\\n");
            else if (type == LOWERCASE)
              sb.append("a-z");
            else if (type == UPPERCASE)
              sb.append("A-Z");
            else if (type == NUMBERS)
              sb.append("0-9");
          }//while
        }//if..else
      }//if..else
      
      //tail bracket
      sb.append("]");
      
      if (ecount < apoint && ecount + ccount > apoint && ccount > 1)
      
        //check if "not all" pointer cuts this finding in half, if so, write
        //min and max counter
        sb.append("{").append(apoint - ecount).append(",").append(ccount).append("}");
      else if (ecount < apoint) {
      
        //if ve are in front of the "not all" pointer, record count if above 1
        if(ccount > 1)
          sb.append("{").append(ccount).append("}");
      } else {
      
        //we are behind "not all" pointer, means not all string where active
        //for this record
      
        if(ccount > 1)
        
          //record count if counter above 1
          sb.append("{0,").append(ccount).append("}");
        else
        
          //record "0 or 1" if counter is 1
          sb.append("?");
      }//if..else
        
      //increment writing counter
      ecount += ccount;
    }//while
    
    return sb.toString();
  }//compileSection()
  
  

	/**
	 * Converts a char into a regular sub expression. Removes
     * repeating characters and adds counters.
	 *
	 * @param   stat  char array to be converted
	 * @return     a string representation of the generated regular expression
	 */
  private String processStatic(char[] stat) {
  
    //make sure we need to process this string
    if(stat == null || stat.length <= 0)
      return new String(stat);
  
    StringBuffer sb = new StringBuffer(stat.length);
    
    //get first char
    char last = stat[0];
    int c = 1;
    
    //cycle through chars, remove repetitions
    for(int i = 1; i < stat.length && processing; i++) {
      char cur = stat[i];
      
      if (last != cur) {
      
        //if new char is found, record last and counter and reset
        sb.append(chooseRegExForChar(last));
        if(c > 1)
          sb.append("{").append(c).append("}");
          
        last = cur;
        c = 1;
      } else
      
        //if same char just increment counter
        c++;
    }//for
    
    //make sure we record the last one
    sb.append(chooseRegExForChar(last));
    if(c > 1)
      sb.append("{").append(c).append("}");
  
    return sb.toString();
  }//processStatic()
  

    /**
     * Returns a string representation of a character as converted
     * to the version 8 regular expression format. If character is
     * alphanumeric it is returned as is. If it was a metacharacter
     * it is converted to the quoted metacharacter representation.
     * If the character was is outside these cases a wide hexadecimal
     * escape is used to represent this character.
     *
     * @param t character to be converted
     * @returns String the regular expression representation of the character
     */
  private String chooseRegExForChar(char t) {
    if((t >= 'a' && t <= 'z') || (t >= 'A' && t <= 'Z') || (t >= '0' && t <= '9') || t == '_' || t == ' ')
          
      //if alphanumeric, write the char
      return String.valueOf(t);
    else {
      
      //convert metachars and unicode
      switch (t) {
        case '\t':
          return ("\\t");
          
        case '\n':
          return ("\\n");
          
        case '\r':
          return ("\\r");
          
        case '\f':
          return ("\\f");
          
        case '\\':
          return ("\\\\");
          
        case '^':
          return ("\\^");
          
        case '.':
          return ("\\.");
          
        case '$':
          return ("\\$");
          
        case '|':
          return ("\\|");
          
        case '(':
          return ("\\(");
          
        case ')':
          return ("\\)");
          
        case '[':
          return ("\\[");
          
        case ']':
          return ("\\]");
          
        case '*':
          return ("\\*");
          
        case '+':
          return ("\\+");
          
        case '?':
          return ("\\?");
          
        case '{':
          return ("\\{");
          
        case '}':
          return ("\\}");
          
        case '\"':
          return ("\\\"");
          
        case '\'':
          return ("\\\'");
          
        case '`':
          return ("\\`");
          
        //special cases to be writen as is
        case '!':
        case '@':
        case '#':
        case '%':
        case '&':
        case '-':
        case ':':
        case ';':
        case '<':
        case '>':
        case '/':
        case ',':
        case '=':
          return String.valueOf(t);
          
        default:
        
          //if special unicode, write unicode escape
          String tt = Integer.toString((int) t, 16).toUpperCase();
          return ("\\x{" + tt + "}");
      }//switch
    }//if..else
  }//chooseRegExForChar()
  
  
  	/**
	 * Returns the regular expression that was the result of the last compileXXX call.
	 *
	 * @return a regular expression as a String
	 */
  public String getLastRegEx() { return regEx; }
  

    /**
     * @see smti.util.shared.FailSafe#stopProcess(SecureObject o)
     */
  public boolean stopProcess() {
    processing = false;
  
    triggerAllFailSafes();
  
    return true;
  }//stopProcess()  
}//RegExCompiler