package com.solidmatrix.regxmaker.util.shared.splicing;

import java.util.LinkedList;
import com.solidmatrix.regxmaker.util.shared.FailSafe;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.splicing.Splicer
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A (Archtype)
 * Author : Gennadiy Shafranovich
 * Purpose: Archtype for other splicers running the multi-match technique
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
 * 06-29-2001 GS Created
 * 07-05-2001 GS Added FailSafe features
 *
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public abstract class Splicer extends FailSafe {

	/**  
     * Number of elements being spliced
     */
  protected int elements;
  
	/**  
     * true is the splice method was called
     */
  protected boolean spliced;
  
	/**  
     * Holds results of splicing
     */
  protected LinkedList result;
  
	/**  
     * Minimum match to be recorded
     */
  protected int ignoreWeight;
  

	/**
	 * Constructs a new Splicer that will verify the given SecureObject
     * before proceding. Also the number of elements being spliced and
     * the minimum match size to record is given.
	 *
	 * @param   o  SecureObject to be verified
	 * @param   e  number of elements that will be spliced
	 * @param   iw  minimum match size to record
     * @throws SecurityException if the given SecureObject has not been verified
	 */
  public Splicer(int e, int iw) throws SecurityException {
    if(e < 0)
      throw new IllegalArgumentException("Element count for splicer can not be negative.");
      
    elements = e;
    ignoreWeight = iw;
    result = new LinkedList();
    spliced = false;
  }//Splicer()
  
    
	/**
	 * Splices the structures given during the intantiation of the
     * Splicer using the multi-match technique. Each sub class
     * of Splicer is responsible for maintananing the data and
     * implementing this method.
	 *
	 * @return A LinkedList containing the static sections of the
     * structures that were being spliced. Data type of sections
     * depends on implemintation of the particular sub class.
	 */
  public abstract LinkedList splice();
  

	/**
	 * Returns the result of splicing as a LinkedList containing 
     * sections that match (static) in the structures that were spliced.
     * The data type of the entries in the LinkedList depends of the
     * implemintation of the sub class of Splicer whose splice()
     * method will be called.
	 */
  public LinkedList getResult() { return result; }
  
  
    /**
     * This is a method to easily create an a boolean array of
     * a certain length with all elements equal to false. Can be
     * used to create a delimiter list which contains no delimiters.
     *
     * @param   len length of resulting array
     * @return  a boolean array with all elements = false
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
	 * Returns the number of elements being spliced
	 */
  public int getElementCount()  { return elements; }


	/**
	 * Returns true if the splice() method of this Splicer
     * has been called and false otherwise.<BR>
     * @see smti.util.shared.Splicer#splice();
	 */
  public boolean hasSpliced()  { return spliced; }
  
  
    /**
     * @see smti.util.shared.FailSafe#stopProcess()
     */
  public boolean stopProcess() {
    processing = false;
    
    triggerAllFailSafes();
    
    return true;
  }//stopProcess()  
}//Splicer()