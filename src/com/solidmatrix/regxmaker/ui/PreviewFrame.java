package com.solidmatrix.regxmaker.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.PreviewFrame
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

public class PreviewFrame extends MyFrame
{
  private JTextArea preview;
  private JButton editButton;
  private stringP s;
 
  public PreviewFrame(stringP d, RegXMakerGui g)
  {
    super("Viewing " + d.toString(), g);
    
    s=d;
    
    preview = new JTextArea();
    preview.setEditable(false);//don't want user to edit the preview
    preview.setText(s.getContents());//set the text
    preview.setCaretPosition(0);
    preview.setWrapStyleWord(true);
    
    editButton = new JButton();
    editButton.setIcon(new ImageIcon (RegXMakerGui.ICON_DIR + "edit.gif"));
    editButton.setToolTipText("Edit Current Item");
    editButton.addActionListener(new ActionListener() 
      {
        public void actionPerformed(ActionEvent e)  {
          processClose();
          gui.createEditFrame(s);
        }
      });

    JScrollPane previewScroller = new JScrollPane(preview);
    getContentPane().add(previewScroller, BorderLayout.CENTER);
    
    JToolBar buttonFrameCont = new JToolBar();
    buttonFrameCont.add(editButton);
    getContentPane().add(buttonFrameCont, BorderLayout.NORTH);
    
    gui.getPrevStrings().add(s);
    
    myWindow = gui.addFrameToList(getTitle());
  }//PreviewFrame()
  
  protected void processClose() {
    gui.getPrevStrings().remove(s);
	stdClose();//close the internal frame
  }//processClose()
    
  public stringP getStringP() { return s; }
  public JTextArea getTextArea() { return preview; } 
}//PreviewFrame
