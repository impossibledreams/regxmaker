package com.solidmatrix.regxmaker.util.shared;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.ArrayUtils
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A (Function Class)
 * Author : Gennadiy Shafranovich
 * Purpose: General utilities for array searching and matching
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
 * 02-19-2001 GS Created
 *
 * 02-19-2001 GS Ready for testing
 *
 * 02-23-2001 GS added byte to boolean convertion methods for patch library
 *
 * 06-18-2001 GS Fixed possible error in indexOf() methods.
 *
 * 06-21-2001 GS Fixed bug in indexOf() method. Values seemed to be
 *               offset by one so -1 was inserted in many conditional
 *               statements.
 *
 * 07-01-2001 GS Fixed bug in indexOf() method that cause an infinite
 *               loop while searching.
 *
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public class ArrayUtils {



  /**********************************************************
   *                      indexOf section                   *
   *                                                        *
   * This section contains methods implemented for the 7    *
   * primitive types that allow the finding of an occurance *
   * of an array inside another array.                      *
   *                                                        *
   **********************************************************/


   
	/**
	 * NOTE: Method implemented for byte arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(byte[] main, byte[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for byte arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */
  public static int indexOf(byte[] main, byte[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int max = (main.length - sub.length);
    while (true) {

      while (start <= max && main[start] != sub[0])
		start++;

      if (start > max)
        return -1;
      
      boolean found = true;
      for(int i = start + 1, j = 1; j < sub.length & found; i++, j++) {
        if(main[i] != sub[j])
          found = false;
      }//for
      
      if(found)
        return start;
      else
        start++;
        
    }//while
        
	    
    /*
    int next = -1;      //next occurance of sub array start
    int max = (main.length - sub.length); //maximum reach
    int mstart = start; //start of search
    while (mstart < max) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart < max) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then move on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart < max) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;     //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
    */
  }//indexOf() - with start


	/**
	 * NOTE: Method implemented for short arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(short[] main, short[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for short arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */
  public static int indexOf(short[] main, short[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int next = -1;      //next occurance of sub array start
    int mstart = start; //start of search
    while (mstart + sub.length - 1 < main.length) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart + sub.length - 1 < main.length) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then more on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart + sub.length - 1 < main.length) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;     //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
  }//indexOf() - with start


	/**
	 * NOTE: Method implemented for int arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(int[] main, int[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for int arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */
  public static int indexOf(int[] main, int[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int next = -1;      //next occurance of sub array start
    int mstart = start; //start of search
    while (mstart + sub.length - 1 < main.length) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart + sub.length - 1 < main.length) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then more on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart + sub.length - 1 < main.length) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;     //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
  }//indexOf() - with start


	/**
	 * NOTE: Method implemented for long arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(long[] main, long[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for long arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */
  public static int indexOf(long[] main, long[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int next = -1;      //next occurance of sub array start
    int mstart = start; //start of search
    while (mstart + sub.length - 1 < main.length) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart + sub.length - 1 < main.length) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then more on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart + sub.length - 1 < main.length) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;     //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
  }//indexOf() - with start


	/**
	 * NOTE: Method implemented for float arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(float[] main, float[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for float arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */
  public static int indexOf(float[] main, float[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int next = -1;      //next occurance of sub array start
    int mstart = start; //start of search
    while (mstart + sub.length - 1 < main.length) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart + sub.length - 1 < main.length) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then more on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart + sub.length - 1 < main.length) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;     //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
  }//indexOf() - with start
  

	/**
	 * NOTE: Method implemented for double arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(double[] main, double[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for double arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */
  public static int indexOf(double[] main, double[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int next = -1;      //next occurance of sub array start
    int mstart = start; //start of search
    while (mstart + sub.length - 1 < main.length) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart + sub.length - 1 < main.length) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then more on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart + sub.length - 1 < main.length) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;     //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
  }//indexOf() - with start

  
	/**
	 * NOTE: Method implemented for char arrays.
     *
     * Index of method with the starting position equal to 0
	 */
  public static int indexOf(char[] main, char[] sub) {
    return indexOf(main, sub, 0);
  }//indexOf() - no start


	/**
     * NOTE: Method implemented for char arrays.
     *
	 * Finds the first occurance of the sub array in the main array starting for a given
     * position in the main array. The method validates the arrays and the start positions
     * and throws exceptions in the following cases:
     *
     * NullPointerException - if either main or sub array is null
     *
     * ArrayIndexOutOfBoundsException - if start position is negative or start is more than main array length
     *
     * IllegalArgumentException - if the sub array is larger than the main array or if the length of the sub array plus the start position if bigger than the size of the main array
	 *
     *
	 * @param   main array to search in
	 * @param   sub  array to search for
	 * @param   start start position of search
	 * @return an int containing the position of the first occurance of sub in main after start or -1 is sub is not found in main
	 */  
  public static int indexOf(char[] main, char[] sub, int start) {
    validateMainSubStart(main, sub, main.length, sub.length, start);
    
    int next = -1;      //next occurance of sub array start
    int mstart = start; //start of search
    while (mstart + sub.length - 1< main.length) {
    
      //loop while we did not find the next occurance of sub[0] and
      //while its possible to find sub in main
      while (next == -1 && mstart + sub.length - 1 < main.length) {
        if(main[mstart] == sub[0])
          //if this value in main is equal to sub[0] then this is our next
          next = mstart;
        else
          //if not then more on to next value
          mstart++;
      }//while
      
      //loop while we know the next occurance of sub[0] and while
      //it is possible to find sub in main
      while (next != -1 && mstart + sub.length - 1 < main.length) {
        int rfind = next;
        mstart = next + 1; //make next our start
        next = -1;         //reset value of next
        
        //loop through entire sub array and make sure it matches this section
        //in main or while a mismatch is found
        boolean falsefind = false;
        for(int i = 1; i < sub.length && mstart < main.length && !falsefind; mstart++, i++) {
          if(main[mstart] != sub[i])
            falsefind = true;
            
          //if we have no next occurance and this value is the next
          //occurance, record it
          if(next == -1 && main[mstart] == sub[0])
            next = mstart;
        }//for
        
        //if this was not a false find, return the start of the found match
        if(!falsefind)
          return rfind;
      }//while
    }//while
    
    //sub is not found, return -1
    return -1;
  }//indexOf() - with start
  

	/**
	 * Validate arrays, their lengths, and a search start position for indexOf searching.
     * This method does not return any values, rather it will throw an exception if the parameters
     * are found to be invalid for indexOf searching.
	 *
	 * @param   main  main array 
	 * @param   sub  sub array
	 * @param   mainlength  length of main array as int
	 * @param   sublength  length of sub array as int
	 * @param   start  start position of search as int
	 */
  private static void validateMainSubStart(Object main, Object sub, int mainlength, int sublength, int start) {
    if(main == null)
      throw new NullPointerException("Main array may not be null");
      
    if(sub == null)
      throw new NullPointerException("Sub array may not be null");
      
    if(sublength < 0)
      throw new IllegalArgumentException("Length of sub array cannot be less than 0");
    
    if(mainlength < 0)
      throw new IllegalArgumentException("Length of main array cannot be less than 0");
      
    if(start < 0)
      throw new ArrayIndexOutOfBoundsException("Start position in main array may not be negative");
  
    if(sublength > mainlength)
      throw new IllegalArgumentException("Sub array may not be longer than main array");    
    
    if(start >= mainlength)
      throw new ArrayIndexOutOfBoundsException("Start position may not be outside main array");
      
    if(start + sublength > mainlength)
      throw new IllegalArgumentException("Placing sub array at start position would put it outside main array");
  }//validateMainSubStart()
  
  
  
  /**********************************************************
   *                     subarray section                   *
   *                                                        *
   * This section contains methods implemented for the 7    *
   * primitive types that allow the extraction of a portion *
   * of an array.                                           *
   *                                                        *
   **********************************************************/
  
  
  
	/**
	 * NOTE: subarray method implemented for arrays of type byte
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static byte[] subarrayStart(byte[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type byte
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static byte[] subarrayEnd(byte[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type byte
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static byte[] subarray(byte[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    byte[] sub = new byte[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()


	/**
	 * NOTE: subarray method implemented for arrays of type short
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static short[] subarrayStart(short[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type short
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static short[] subarrayEnd(short[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type short
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static short[] subarray(short[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    short[] sub = new short[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()    


	/**
	 * NOTE: subarray method implemented for arrays of type int
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static int[] subarrayStart(int[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type int
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static int[] subarrayEnd(int[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type int
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static int[] subarray(int[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    int[] sub = new int[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()


	/**
	 * NOTE: subarray method implemented for arrays of type long
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static long[] subarrayStart(long[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type long
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static long[] subarrayEnd(long[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type long
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static long[] subarray(long[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    long[] sub = new long[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()  


	/**
	 * NOTE: subarray method implemented for arrays of type float
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static float[] subarrayStart(float[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type float
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static float[] subarrayEnd(float[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type float
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static float[] subarray(float[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    float[] sub = new float[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()


	/**
	 * NOTE: subarray method implemented for arrays of type double
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static double[] subarrayStart(double[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type double
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static double[] subarrayEnd(double[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type double
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static double[] subarray(double[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    double[] sub = new double[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()


	/**
	 * NOTE: subarray method implemented for arrays of type char
     *
     * subarray method implemented with offset equal to 0. Making the resulting subarray start from the beggining of the original array.
	 */  
  public static char[] subarrayStart(char[] array, int length) {
    return subarray(array, 0, length);
  }//subarray()
  

	/**
	 * NOTE: subarray method implemented for arrays of type char
     *
     * subarray method that makes an array copy of the original array starting at offset and ending at the end of the original array. Similar to the System.arrayCopy method.
	 */  
  public static char[] subarrayEnd(char[] array, int offset) {
    return subarray(array, offset, array.length - offset);
  }//subarray()
  

	/**
     * NOTE: subarray method implemented for arrays of type char
     *
	 * Extracts a sub array from an array of a primitive type.
     * Throws the following exceptions:
     *
     * NullPointerException - if input array is null
     *
     * IllegalArgumentException - if the length of the new array is negative
     *
     * ArrayIndexOutOfBoundsException - If the offset is negative or is larger than array size. OR If offset + length is larger than the array size, making extraction impossible.
	 *
	 * @param   array array to extract from 
	 * @param   offset  offset in input array to start from
	 * @param   length  length of the new sub array
	 * @return  the sub array
	 */
  public static char[] subarray(char[] array, int offset, int length) {
    validateOffsetLength(array, array.length, offset, length);
    
    char[] sub = new char[length];
    for(int i = 0; i < length; i++)
      sub[i] = array[offset + i];
    
    return sub;
  }//subarray()

  
	/**
	 * Validates an array, offset, and length values to make sure they are
     * valid for extraction of subarray.
	 *
	 * @param   array  input array
	 * @param   arrayLength length of input array
	 * @param   offset  offset in input array
	 * @param   length  length of the new subaray
	 */
  private static void validateOffsetLength(Object array, int arrayLength, int offset, int length) {
    if(array == null)
      throw new NullPointerException("Input array may not be null");
      
    if(arrayLength < 0)
      throw new IllegalArgumentException("Array length may not be negative");
      
    if(offset < 0)
      throw new ArrayIndexOutOfBoundsException("Offset may not be negative");
      
    if(offset >= arrayLength)
      throw new ArrayIndexOutOfBoundsException("Offset may not be larger than input array size");
      
    if(length < 0)
      throw new IllegalArgumentException("Length of subarray may not be negative");
      
    if(offset + length > arrayLength)
      throw new ArrayIndexOutOfBoundsException("Extracted subarray may not go over the size of the main array");
  }//validateOffsetLength()
  
  
  
  /**********************************************************
   *                  Boolean-Byte section                  *
   *                                                        *
   * This section contains methods responsible for handling *
   * the convertion of flag arrays (boolean) into bytes     *
   * that may be writen to outside files or other streams   *
   *                                                        *
   **********************************************************/
  
  
  
  public static final byte[] BIT_FLAGS = { (byte) 1, (byte) 2, 
                                           (byte) 4, (byte) 8, 
                                           (byte) 16,(byte) 32,
                                           (byte) 64,(byte) 128 };
  

	/**
	 * This method can be used to convert an array of boolean values
     * into a byte. If the input boolean array is of length less than
     * 8 then the values from the length of the array to 8 will be assumed
     * as false. If the length of the input array is more than 8 then
     * all the values above 8 will be ignored. For converting large boolean
     * arrays into bytes it is recommended to use the booleanArrayToBytes() 
     * method. The values will be converted in left to right order. The first
     * value in the input array will be the least significant bit of the resulting
     * byte.
	 *
	 * @param   input  an array of boolean values
	 */
  public static byte booleanArrayToByte(boolean[] input) {
    byte out = 0;
  
    for(int i = 0; i < 8 && i < input.length; i++) {
      if(input[i])
        out = (byte) (out | BIT_FLAGS[i]);
    }//for
    
    return out;
  }//booleanArrayToByte()
  

	/**
	 * This method can be used to convert a byte into an array of
     * boolean values. The resulting boolean array will be of size 8.
     * For converting byte arrays into boolean arrays it is recommended 
     * to use the bytesToBooleanArray() method. The values will be 
     * converted in left to right order. The first value in the output 
     * array will be the least significant bit of the input byte.
	 *
	 * @param   input a single byte to be converted 
	 * @return  array of booleans of size 8
	 */
  public static boolean[] byteToBooleanArray(byte input) {
    boolean[] out = new boolean[8];
    
    for(int i = 0; i < 8; i++) {
      if((input & BIT_FLAGS[i]) == BIT_FLAGS[i])
        out[i] = true;
      else
        out[i] = false;
    }//for
    
    return out;
  }//byteToBooleanArray()
  

	/**
	 * A method that performs the same function as the booleanArrayToByte()
     * method but is not limited by input and output size. This method
     * can take an ultimited length boolean array and will convert it to 
     * an array of bytes equal in length to the function 
     * ceiling(size of input array / 8.0)
	 *
	 */
  public static byte[] booleanArrayToBytes(boolean[] input) {
    int count = input.length / 8;
    if(input.length % 8 != 0)
      count++;
      
    byte[] out = new byte[count];
    
    int pos = 0;
    int bpos = 0;
    while (bpos < count) {
      out[bpos] = 0;
      
      for(int temp = 0; temp < 8 && pos < input.length; temp++, pos++) {
        if(input[pos])
          out[bpos] = (byte) (out[bpos] | BIT_FLAGS[temp]);
      }//for
      
      bpos++;
    }//while
  
    return out;
  }//booleanArrayToBytes()

  
	/**
	 * A method that performs the same function as the bytesToBooleanArray()
     * method but is not limited by input and output size. This method
     * can take an ultimited length byte array and will converted to a 
     * boolean array equal in size to (8 * byte array size)
	 */
  public static boolean[] bytesToBooleanArray(byte[] input) {
    boolean[] out = new boolean[input.length * 8];
    
    int pos = 0;
    for(int i = 0; i < input.length; i++) {
      for(int j = 0; j < 8; j++, pos++) {
        if ((input[i] & BIT_FLAGS[j]) == BIT_FLAGS[j])
          out[pos] = true;
        else
          out[pos] = false;
      }//for
    }//for
    
    return out;
  }//bytesToBooleanArray()
}//ArrayUtils