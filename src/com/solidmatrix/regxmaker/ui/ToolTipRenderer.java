package com.solidmatrix.regxmaker.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ImageIcon;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.ToolTipRenderer
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: GUI component
 *
 * Copyright (C) 2000 - 2001, 2004 SolidMatrix Technologies, Inc.
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

public class ToolTipRenderer extends DefaultListCellRenderer
{

  public final static ImageIcon file = new ImageIcon(RegXMakerGui.ICON_DIR + "stringP-FILE.gif");
  public final static ImageIcon string = new ImageIcon(RegXMakerGui.ICON_DIR + "stringP-STRING.gif");
  public final static ImageIcon url = new ImageIcon(RegXMakerGui.ICON_DIR + "stringP-URL.gif");

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    stringP obj = (stringP) value;
    
    Component t = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    
    setText((value == null) ? "" : obj.toString());
    
    if (value != null) {
      if(obj.isFile())
        setIcon(file);
      else if (obj.isUrl())
        setIcon(url);
      else
        setIcon(string);
    }//if
    
    return t;
  }//getListCellRendererComponent()
}//ToolTipRenderer 

