package com.solidmatrix.regxmaker.util.shared.matching;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.matching.Counter
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A (Data Holder)
 * Author : Gennadiy Shafranovich
 * Purpose: Changing counter to use with various data structures
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
 * 09-20-2001 GS Created
 * 
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public class Counter {
  private int count;
  
  public Counter() { count = 0; }
  
  public void increment() { count++; }
  public void decrement() { count--; }
  
  public void setValue(int c) { count = c; }
  public int getValue() { return count; }
  
  public void reset() { count = 0; }
  
  public boolean equals(Object o) {
    if(!(o instanceof Counter))
      return false;
      
    return (((Counter) o).getValue() == count);
  }//equals();
  
  public int error(Counter c) { return Math.abs(c.getValue() - count); }
}//Counter