package com.solidmatrix.regxmaker.ui;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.MyFrame
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

public abstract class MyFrame extends JInternalFrame
{
  //public static final double PHI = 1.61803398875;
  public static final double PHI = 1.3;
  
  protected JDesktopPane parent;
  protected RegXMakerGui gui;
  protected JMenuItem myWindow;
   
  public MyFrame(String title, RegXMakerGui gui)
  {
    super(title, true, true, true, true);
    
    parent = gui.getDesktopPane();
    this.gui = gui;
    
	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e) 
        { processClose(); }
      }
    ); 
	
    int openFrameCount = parent.getAllFrames().length-1;	
	int wndWidth = parent.getWidth();
	int wndHeight = parent.getHeight();
	int frmWidth =  (int) (wndWidth/PHI);
	int frmHeight = (int) (frmWidth/PHI)-44;
	int xOffset= (int) ((wndWidth/2)-(frmWidth/2));
	int yOffset= (int) ((wndHeight/2)-(frmHeight/2));
    int posX = xOffset + openFrameCount * 30;
    int posY = yOffset + openFrameCount * 30;
        
    while (posX + frmWidth > wndWidth)
      posX -= wndWidth - frmWidth;
          
    while (posY + frmHeight > wndHeight)
      posY -= wndHeight - frmHeight;
        
    setSize(frmWidth,frmHeight);
    setLocation(posX, posY);       
  }//MyFrame()
  
  public JMenuItem getMyWindowItem() {return myWindow;}

  protected void stdClose() {
    if(myWindow != null)
      gui.removeFrameFromList(myWindow);
    
    gui.disableButtons();
      
    dispose();
  }//stdClose()
  
  protected abstract void processClose();
}//MyFrame()
