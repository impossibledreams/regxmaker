package com.solidmatrix.regxmaker.ui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.OutputFrame
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

public class OutputFrame extends MyFrame
{ 
  private JTextArea preview;

  public OutputFrame(String answer, RegXMakerGui gui)
  {
    super("Results: " + stringP.trimStringToPreview(answer), gui);
    
    preview = new JTextArea(answer);
    preview.setEditable(false);//don't want user to edit the preview
    preview.setText(answer);//set the text
    preview.setCaretPosition(0);
    preview.setWrapStyleWord(true);
    
    JScrollPane previewScroller = new JScrollPane(preview);
    getContentPane().add(previewScroller);
    
    myWindow = gui.addFrameToList(getTitle());
  }//OutputFrame()
 
  public JTextArea getTextArea() { return preview; } 
  
  protected void processClose() {
    stdClose();
  }//processClose()
}//OutputFrame