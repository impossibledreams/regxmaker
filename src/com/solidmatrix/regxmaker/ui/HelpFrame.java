package com.solidmatrix.regxmaker.ui;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.HelpFrame
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: Help frame
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

public class HelpFrame extends MyFrame
{
  private JEditorPane preview;
 
  public HelpFrame(RegXMakerGui g, String htmlFile)
  {
    super("Help Window - " + htmlFile, g);
         
    try
    {
      preview = new JEditorPane("file:" + RegXMakerGui.USER_DIR + RegXMakerGui.FILE_SEPERATOR + "help" + RegXMakerGui.FILE_SEPERATOR + htmlFile);
      preview.setEditable(false);
      preview.addHyperlinkListener(new LinkListener());
    } catch (java.io.IOException u)
    {
      u.printStackTrace(gui.getErrorWriter());
      JOptionPane.showMessageDialog(gui, "Error Reading Help File\n"+u.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }//try..catch

    JScrollPane previewScroller = new JScrollPane(preview);
    getContentPane().add(previewScroller);
    
    myWindow = gui.addFrameToList(getTitle());
  }//HelpFrame()
  
  public JEditorPane getEditorPane() { return preview; }
  
  protected void processClose()  { stdClose(); }
  
  private class LinkListener implements HyperlinkListener {
    public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        JEditorPane pane = (JEditorPane) e.getSource();
        try  { pane.setPage(e.getURL()); } catch (Throwable t) { }
      }//if
 	}//hyperlinkUpdate()
  }//LinkedListener
}//HelpFrame
