package com.solidmatrix.regxmaker.util.shared.matching;

import java.util.Iterator;
import java.util.LinkedList;
import com.solidmatrix.regxmaker.util.shared.FailSafe;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.matching.Matcher
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A (Archtype)
 * Author : Gennadiy Shafranovich
 * Purpose: Archtype for other matchers running the matchTwo algorithm
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
 * 06-28-2001 GS Created
 * 07-05-2001 GS Added FailSafe features
 * 
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public abstract class Matcher extends FailSafe {

	/**
     * Cutoff value beyond which a fast algorithm has to be used
     */
  protected static final int FAST_ALGO_CUTOFF = 1024;
  
	/**  
     * Default minimum size of match to be recorded (0)
     */
  protected static final int DEFAULT_IGNORE_WEIGHT = 0;
  
  	/**  
     * Debug flag
     */
  protected boolean debug;
  
	/**  
     * Minimum match to be recorded
     */  
  protected int ignoreWeight;

  	/**  
     * LinkedList to hold location of matches in first matched 
     * structure as Integer instances
     */
  protected LinkedList firstMatch;
  
    /**  
     * LinkedList to hold location of matches in second matched 
     * structure as Integer instances
     */
  protected LinkedList secondMatch;
  
    /**  
     * LinkedList to hold the lengths of matches as Integer instances
     */  
  protected LinkedList weights;

    /**  
     * Two dimentional array of ints that holds a list of bins.
     * each bin contains the locations in the original structure.
     * The same index of a bin in the upper bin array and the lower
     * bin array represent that it is the same exact value located
     * in the original structures at the locations specified by that
     * bin.
     */    
  protected int[][] uBinArray;
  
    /**  
     * Two dimentional array of ints that holds a list of bins.
     * each bin contains the locations in the original structure.
     * The same index of a bin in the upper bin array and the lower
     * bin array represent that it is the same exact value located
     * in the original structures at the locations specified by that
     * bin.
     */      
  protected int[][] lBinArray;

    /**
     * Holds the result of the match operation.<BR>
     * @see smti.util.shared.Matcher#getResult()
     */  
  protected int[][] result;
  
    /**
     * If true, means that match operation was called and finished
     * succesfully
     */  
  protected boolean matched;
  

	/**
	 * A method which returns a two dimentional array as specified by
     * the getResult() method which contains the best set of matches
     * between given structures. <BR><BR>
     *
     * All subclasses of Matcher must provide the implemintaion of 
     * this method. All subclasses are also responsible for keeping
     * track and maintaining the structures that are being matched.
     *
     * @see smti.util.shared.Matcher#getResult()
	 */
  public abstract int[][] match();
  
         
	/**
	 * Constructs a new Matcher. This constructor is to be used by
     * sub classes of Matcher as a convinience.
	 *
	 * @param   o  A SecureObject to verify
	 * @param   iw  minimum size of match to be recorded
	 * @param   d  debug flag, if true sub classes may print debug messages
     * @throws SecurityException if the given SecureObject has not been verified
	 */
  public Matcher(int iw, boolean d) throws SecurityException {
    resetLists();

    ignoreWeight = iw;
    matched = false;
    debug = d;
  }//Matcher() - all params
  

	/**
	 * Resets the match and weight lists of this matcher
	 */
  protected void resetLists() {
    firstMatch = new LinkedList();
    secondMatch = new LinkedList();
    weights = new LinkedList();
  }//resetLists();
  

	/**
	 * Dumps match and weight lists into a two dimentional array
     * of ints as defined by the getResult() method;
	 *
	 * @see smti.util.shared.Matcher#getResult()
	 */
  protected int[][] outputLists() {
    int[][] out = new int[3][firstMatch.size()];
    
    for(int k = 0; firstMatch.size() > 0 && processing; k++) {
      out[0][k] = ((Integer) firstMatch.removeFirst()).intValue();
      out[1][k] = ((Integer) secondMatch.removeFirst()).intValue();
      out[2][k] = ((Integer) weights.removeFirst()).intValue();
    }//for
    
    return out;
  }//outputLists()
  
  
    /**
	 * Method used as shortcut for sub classes of Matcher that
     * compare two structures. Converts contents of a LinkedList
     * which contains arrays of ints into a two dimentional bin
     * list. Used to convert upper bins.
	 *
	 * @param   upper  LinkedList containing int arrays as its elements
	 */  
  protected void prepareUpperBins(LinkedList upper) {
    //convert bin lists to arrays
    uBinArray = new int[upper.size()][];
    Iterator ubi = upper.iterator();
    for(int i = 0; ubi.hasNext() && processing; i++)
      uBinArray[i] = (int[]) ubi.next();
      
  }//prepareUpperBins()
  
          
	/**
	 * Method used as shortcut for sub classes of Matcher that
     * compare two structures. Converts contents of a LinkedList
     * which contains arrays of ints into a two dimentional bin
     * list. Used to convert lower bins.
	 *
	 * @param   lower  LinkedList containing int arrays as its elements
	 */
  protected void prepareLowerBins(LinkedList lower) {
    //convert bin lists to arrays
    lBinArray = new int[lower.size()][];
    Iterator lbi = lower.iterator();
    for(int i = 0; lbi.hasNext() && processing; i++)
      lBinArray[i] = (int[]) lbi.next();
      
  }//prepareLowerBins()
  
  
	/**
	 * A method that is usufull to subclasses of Matcher. It trims a 
     * given pointer array (as defined by the getDefaultPointerArray()
     * method) by splitting it into two point arrays. One points to 
     * the part of all bins which are included in the original point
     * array and are less that the start parameter. The second points
     * to the par of all bins which are included in the original point
     * array and are greater then or equal to the end parameter.<BR>
	 *
	 * @param   point  The original point array
	 * @param   pointFront  Where values for first output point array 
     * are recorded. Has to be the same size as the original point
     * array in both dimentions.
     *
	 * @param   pointBack  Where values for second output point array 
     * are recorded. Has to be the same size as the original point
     * array in both dimentions.
     *
	 * @param   binArray  The binArray containing the list of bins
     * to which the original point array coresponds.
     *
	 * @param   start  The cutoff (end) of the first output array
	 * @param   end  The curoff (start) of the second output array
	 */
  protected static void trimPointArray(int[][] point, int[][] pointFront, int[][] pointBack, int[][] binArray, int start, int end) {
    for(int i = 0; i < binArray.length; i++) {
      pointFront[i][0] = point[i][0];
      pointFront[i][1] = point[i][0];
      pointBack[i][0] = point[i][1] - 1;
      pointBack[i][1] = point[i][1];
        
      while(pointFront[i][1] < point[i][1] && binArray[i][pointFront[i][1]] < start)
        pointFront[i][1]++;
          
      while(pointBack[i][0] >= point[i][0] && binArray[i][pointBack[i][0]] > end)
        pointBack[i][0]--;
          
      if(pointBack[i][0] >= point[i][0] && binArray[i][pointBack[i][0]] < end)
        pointBack[i][0]++;
          
      if(pointFront[i][1] > point[i][1])
        pointFront[i][1] = point[i][1];
          
      if(pointBack[i][0] < point[i][0])
        pointBack[i][0] = point[i][0];
    }//for
  }//trimPointArray()
  

	/**
	 * Creates a two dimentional array with pointers to the start and
     * end of a bin in the specified bin array. By default the start
     * of a bin is 0 and its end is the size of the bin. This method
     * was created as a convinience for actual implemented matchers.
	 *
	 * @param   binArray  The array of bins for which the pointer 
     * array is created
     *
	 * @return a two dimentional array if int. The first 
     * dimention is the same size as the number of bins. The 
     * second dimention consists of two elements, the start 
     * and end of a bin.
	 */
  public int[][] getDefaultPointArray(int[][] binArray) {
    int[][] point = new int[binArray.length][2];
  
    for(int i = 0; i < point.length && processing; i++) {
      point[i][0] = 0;
      point[i][1] = binArray[i].length;
    }//for
    
    return point;
  }//getDefaultPointArray()
  
  
   	/**
	 * This is a method to easily create an a boolean array of
     * a certain length with all elements equal to false. Can be
     * used to create a delimiter list which contains no delimiters
     * and is the same size as a structure to be matched.
	 *
	 * @param   len length of resulting array
	 * @return  a boolean array with elements = false
	 */
  public static boolean[] makeEmptyDelimArray(int len) {
    if(len < 0)
      len = 0;
      
    boolean[] out = new boolean[len];
    for(int i = 0; i < len; i++)
      out[i] = false;
      
    return out;
  }//makeEmptyDelimArray()
  
        
	/**
	 * Method used to decide wether a fast algorithm is needed.
     * Decision is based on the maximum of the given lengths as 
     * compared to the FAST_ALGO_CUTOFF constant.
	 *
	 * @return     true is the biggest of the given lengths is more 
     * that FAST_ALGO_CUTOFF and false otherwise.
	 */
  public boolean needsFastAlgo(int length1, int length2) {
    if(length1 > length2 && length1 > FAST_ALGO_CUTOFF)
      return true;
    else if(length2 > FAST_ALGO_CUTOFF)
      return true;
      
    return false;
  }//needsFastAlgo()
  

	/**
	 * Sets the debug flag of this Matcher. Sub classes of Matcher
     * may print debug messages is debug flag is set.
	 *
	 * @param   d  new debug flag
	 */
  public void setDebug(boolean d) { debug = d; }

  
	/**
	 * @return     
	 */
  public boolean isInDebug() { return debug; }

  
	/**
	 * @return the minimum size of a match that will be recorder 
     * by this matcher and an int
	 */
  public int getIgnoreWeight() { return ignoreWeight; }

  
	/**
	 * This is an easy way to retrieve the results of a matcher. The 
     * output is a two dimentional array. The first dimention is 
     * the same size as the number of structure that are being 
     * matched + 1. At each second dimention index of the all 
     * first dimentions the location of the match in the coresponding
     * first dimention index structure is represented as an integer. 
     * That leaves one second dimention list, in  which at each 
     * index the length of the match is recorded.
	 */
  public int[][] getResult() { return result; }

  
	/**
	 * @return true is the match() method of this 
     * matcher has been called and results are ready, false overwise
	 */
  public boolean hasMatched() { return matched; }
  
  
    /**
     * @see smti.util.shared.FailSafe#stopProcess(SecureObject o)
     */
  public boolean stopProcess() {
    processing = false;
    
    triggerAllFailSafes();
    
    return true;
  }//stopProcess()
}//Matcher