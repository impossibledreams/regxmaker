package com.solidmatrix.regxmaker.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.MainListener
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: GUI component
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
 * Modification History
 *
 * 07-05-2004	YS Added header and licensing information
 * </PRE>
 */

public abstract class MainListener extends KeyAdapter implements DocumentListener {
  public void keyPressed(KeyEvent e)  { 
    int m = e.getModifiers() & KeyEvent.CTRL_MASK;
    int k = e.getKeyCode();
       
    if (m == KeyEvent.CTRL_MASK && k == KeyEvent.VK_S) {
      processCtrlS();
    }//if
  }
      
  public void changedUpdate(DocumentEvent e)  { processTextChange(); }
  public void insertUpdate(DocumentEvent e)  { processTextChange(); }
  public void removeUpdate(DocumentEvent e)  { processTextChange(); } 
 
  protected abstract void processCtrlS();
  protected abstract void processTextChange();
}//MainListener