package com.solidmatrix.regxmaker.util.shared;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.Monitor
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : N/A (archtype)
 * Author : Gennadiy Shafranovich
 * Purpose: Provides a way to pass status information between two
 *          processes.
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
 * Comments: 
 *
 * Modification History
 *
 * 07-01-2001 GS Creation
 *
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */

public interface Monitor {

    /**
	 * Updates a monitoring thread of the status of the process
     * it is monitoring. This method is synchronized so only one 
     * thread can update the status message at any given time.
	 *
	 * @param   s  String containing the new status message
	 */
  public void updateStatus(String s);
}//Monitor