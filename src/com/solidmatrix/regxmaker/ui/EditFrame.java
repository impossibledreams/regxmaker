package com.solidmatrix.regxmaker.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.EditFrame
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: Edit frame
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

public class EditFrame extends MyFrame
{
  private JTextArea preview;
  private JButton saveButton;
  private DefaultListModel listmodel;
  private stringP s;
 
  public EditFrame(stringP d, RegXMakerGui g)
  {
    super("Editing: " + d.toString(), g);
    
	s=d;
    listmodel= gui.getListModel();
    
    MyListener my = new MyListener();
    
    saveButton = new JButton();
    saveButton.setIcon(new ImageIcon (RegXMakerGui.ICON_DIR + "save-disk.gif"));
    saveButton.setToolTipText("Save Updated Item");
    
    saveButton.addActionListener(my);
                
	preview = new JTextArea();
    preview.addKeyListener(my);
    preview.getDocument().addDocumentListener(my);
    preview.setText(s.getContents());//set the text
    preview.setCaretPosition(0);
    preview.setWrapStyleWord(true);
       
	JScrollPane previewScroller = new JScrollPane(preview);
	getContentPane().add(previewScroller);
           
    JToolBar buttonFrameCont = new JToolBar();
	buttonFrameCont.add(saveButton);
	getContentPane().add(buttonFrameCont, BorderLayout.NORTH);
    
    gui.getEditStrings().add(s);
    
    myWindow = gui.addFrameToList(getTitle());
  }//editFrame()
 
  protected void processClose()
  {
    if (preview.getText().length() > 0) {
    
      //i'm showing a non-internal box bc i don't want user to save multiple copies
      //(clicking on the X-several times), the dialog has to be modal
      int x = JOptionPane.showConfirmDialog(this, "Save String?", "Confirm Close:", JOptionPane.YES_NO_CANCEL_OPTION);
      
      if (x==JOptionPane.YES_OPTION)
        saveString();
      else if (x == JOptionPane.CANCEL_OPTION || x == JOptionPane.CLOSED_OPTION)
        return;
	}//if
    
    gui.getEditStrings().remove(s);
    stdClose();//close the internal frame
  }//usrString()
    
  private void  saveString() {
    if (preview.getText().length() > 0)
      s.setContents(preview.getText());
    
    gui.getEditStrings().remove(s);
    gui.getFileList().requestFocus();
  }//saveString()
      
  public stringP getStringP() { return s; }
  public JTextArea getTextArea() { return preview; }
  
  private class MyListener extends MainListener implements ActionListener {
    protected void processCtrlS() {
      saveString();
      stdClose();
    }//processCtrlS()
    
    protected void processTextChange() {
      saveButton.setEnabled(preview.getText().length() > 0);
    }//processDocumentChange()
    
    public void actionPerformed(ActionEvent e)  { processCtrlS(); }
  }//MyListener
}//EditFrame
