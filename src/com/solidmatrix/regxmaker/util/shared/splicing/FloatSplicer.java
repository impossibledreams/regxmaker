package com.solidmatrix.regxmaker.util.shared.splicing;

import java.util.LinkedList;
import com.solidmatrix.regxmaker.util.shared.matching.FloatMatcher;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.splicing.FloatSplicer
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : 3 (Function Class)
 * Author : Gennadiy Shafranovich
 * Purpose: Finds all static (same) sections in multiple float arrays
 *          Based on the multi-match technique.
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
 *               NOTE: implemintaion has to be tested
 *
 * 07-05-2001 GS Added FailSafe features
 *
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public final class FloatSplicer extends Splicer {

	/**
	 * the data to be spliced
     */
  private float[][] data;
  
	/**
	 * Delimiters flags for the multi-match splice
     */
  private boolean[][] delims;
         

	/**
	 * Constructs a new Splicer to splice arrays of the float 
     * primitive type. 
	 *
	 * @param   o  SecureObject to be verified
	 * @param   in  the data to be spliced
	 * @param   iw  minimum match size to record
     * @throws SecurityException if the given SecureObject has not been verified
	 */
  public FloatSplicer(float[][] in, int iw) throws SecurityException {
    super(in.length, iw);
    
    //we need make copies of the input arrays that we will use
    //for splicing.
    
    processing = true;
    
    data = new float[elements][]; //main array
    delims = new boolean[elements][];//delimiter array
    for(int i = 0; i < elements && processing; i++) {
      delims[i] = makeEmptyDelimArray(in[i].length);
      data[i] = new float[in[i].length];
      
      for(int j = 0; j < in[i].length && processing; j++)
        data[i][j] = in[i][j];
    }//for
    
    processing = false;
  }//FloatSplicer()
    
  

	/**
	 * Implements the multi-match splice technique for
     * arrays of the primitive float data type. The resulting
     * LinkedList will contain float arrays as the entry data type.
	 *
	 * @see smti.util.shared.Splicer#splice()
	 */
  public LinkedList splice() {
    if(spliced)
      return result;
      
    processing = true;
    
    int limit = elements; //limit of array placement
    while(limit > 1 && processing) { //loop until only one array remain
    
      int place = 0; //where in the array to put the next spliced array

      //match arrays two at a time
      for(int o = 0; o < limit - 1 && processing; o += 2) {
        
        //temporary sequence
        LinkedList tempseq = new LinkedList();
        int tempsum = 0; //sum of all matches
        
        //get static starts
        FloatMatcher matcher = new FloatMatcher(data[o], data[o + 1], delims[o], delims[o + 1], ignoreWeight);
        
        addFailSafe(matcher);
        int[][] matches = matcher.match();
        removeFailSafe(matcher);
        
        if(matches.length == 3 && matches[0].length > 0 && processing) {
          int mpos = 1; 
          
          int fcur = matches[0][0];      //match start in array 1
          int weight = matches[2][0];    //match weight
          tempsum += weight;             //increment total weight
          float[] temp = new float[weight];//create new temp array
          
          boolean done = false;
          while(!done && processing) {
            //record whole match to temp array
            for(int t = 0; t < weight && processing; t++, fcur++)
              temp[t] = data[o][fcur];
              
            tempseq.add(temp);          //add it to the temp sequence

            if(mpos < matches[0].length && processing) {
              fcur = matches[0][mpos];  //get next match 
              weight = matches[2][mpos];//match weight
              tempsum += weight;        //increment total weight
              temp = new float[weight];  //create new temp array
              mpos++;                   //increment match counter
            } else                      //else
              done = true;              //we're done
          }//while
        } else {
          //if two pieces are unmatched, result is empty
          spliced = true;
          return result;
        }//if..else
        
        //we now need to put the portion of the temp array
        //that was used into the sequence list
        int asize = tempsum; //new array size
        int cur = 0; //position in new array
        float[] newB = new float[asize];
        boolean[] newD = makeEmptyDelimArray(asize);
        while (tempseq.size() > 0 && processing) {
          float[] t = (float[]) tempseq.removeFirst();
          
          for(int p = 0; p < t.length && processing; p++, cur++)
            newB[cur] = t[p];
            
          //if this is not the last match, add delimiter
          if (tempseq.size() > 0)
            newD[cur] = true;
        }//while
          
        //put matched array in place
        data[place] = newB;
        delims[place] = newD;
        place++;
        
        //free up references
        newB = null;
        newD = null;
        matches = null;
        matcher = null;
        tempseq = null;
      }//for
      
      //now there are twice less arrays, adjust limit
      limit = (limit + 1) / 2;
      
      //in case we had an odd number of arrays, preserve the last one
      if (place < limit && processing) {
        data[place] = data[elements - 1];
        delims[place] = delims[elements - 1];
      }//if
    }//while

    if (limit == 1 && processing) {
      int last = 0;
      int nextDelim = 0;
      while (nextDelim < data[0].length && processing) {
        while(nextDelim < data[0].length && !delims[0][nextDelim] && processing)
          nextDelim++;
          
        float[] t = new float[nextDelim - last];
        for(int i = 0; i < t.length && processing; i++)
          t[i] = data[0][last + i];
          
        result.add(t);
        last = nextDelim;
        nextDelim++;
      }//while
      
      if (last < data[0].length) {
        float[] t = new float[data[0].length - last];
        for(int i = 0; i < t.length && processing; i++)
          t[i] = data[0][last + i];
        
        result.add(t);
      }//if
    }//if
       
    spliced = true;
    
    processing = false;
    
    return result;
  }//splice()
}//FloatSplicer()