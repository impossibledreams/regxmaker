package com.solidmatrix.regxmaker.util.shared;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.FailSafe
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A (archtype)
 * Author : Gennadiy Shafranovich
 * Purpose: Provides for a method to stop the execution of a process
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
 * Comments: Full, with javadoc
 *
 * Modification History
 *
 * 07-04-2001 GS Creation
 *
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public abstract class FailSafe {

    /**
     * Flag that tells the implementing class wether it can continue
     * processing
     */
  protected boolean processing;

    /**
     * List of FailSafe instances which were instantiated and whose
     * methods the implementing class has called but is still waiting
     * for a return from
     */
  private LinkedList failSafeList = new LinkedList();


    /**
     * Adds a FailSafe instance to the list of just instances which
     * were instantiated and whose methods are currently processing
     *
     * @param fail FailSafe instance to be added
     */  
  protected final void addFailSafe(FailSafe fail) { failSafeList.addLast(fail); }    
  
  
    /**
     * Removes a FailSafe instance from the list
     *
     * @param fail FailSafe instance to be removed
     */  
  protected final void removeFailSafe(FailSafe fail) { failSafeList.remove(fail); }

  
    /**
     * Calls the stopProcess() method of all FailSafe instances currently
     * in the  fail safe list of this class. <BR><BR>
     *
     * The given SecureObject is passed to the FailSafe instances 
     * on the stopProcess() call. <BR><BR>
     *
     * This method returns an array of boolean values containing
     * the return values of each of the FailSafe instances whose
     * stopProcess() method was called by this method.<BR><BR>
     *
     * @param o SecureObject to be passed to stopProcess() methods of the FailSafe instances
     */  
  public boolean[] triggerAllFailSafes () {
    boolean[] returns = new boolean[failSafeList.size()];
    
    Iterator failSafeIterator = failSafeList.iterator();
    for(int i = 0; i < returns.length && failSafeIterator.hasNext(); i++)
      returns[i] = ((FailSafe) failSafeIterator.next()).stopProcess();
    
    return returns;
  }//triggerAllFailSafes()
  

    /**
	 * Attempts to stop the currently running process. The fail safe
     * is triggered only if the given secure object's certificate is
     * equal to the certificate of the SecureObject which was passed
     * to the process during instantiation (if required). <BR><BR>
     *
     * The implemening class does not have to respond to a stopProcess()
     * call but that defines the purpose of implementing the method.
     * <BR><BR>
     *
     * Idealy the implementing class should have a flag which is
     * checked as part of all loops and breaks the loop if the
     * flag is of a certain value. The stopProcess call (if
     * all security verification is done and is valid) should set
     * this flag to the value that would stop the processing of the
     * implemening class. Also the implementing class should
     * keep a list of FaileSafe instances that it instantiates and
     * makes calls to. It should also propogate the stopProcess() 
     * to those FailSafe intances. (This includes any threads created). 
     * The addFailSafe(), removeFailSafe(), and triggerAllFailSafes() 
     * methods of this class should be used for this purpose. 
     * If a FailSafe process calls on another FailSafe process which
     * it did not instantiate, it should not attempt to stop that
     * process on a stopProcess() call since it will not contain
     * the proper SecureObject to stop the process. BR><BR>
     *
     *
     * The method returns true only if the implementing class has
     * made sure that the running process would stop and false
     * otherwise <BR><BR>
     *
     * @param o SecureObject to be verified before stopProcess() proceeds
	 */
  public abstract boolean stopProcess();
}//Monitor