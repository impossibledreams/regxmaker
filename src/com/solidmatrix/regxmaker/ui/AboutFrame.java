package com.solidmatrix.regxmaker.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.AboutFrame
 * Project: RegXmaker GUI Utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Various
 * Purpose: About form for the GUI Utility
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

public class AboutFrame extends MyFrame
{
  public AboutFrame (RegXMakerGui g)
  {
    super("About", g);
         
    int frmWidth = 450;
    int frmHeight = 420;
    int xOffset= (int) ((parent.getWidth()/2)-(frmWidth/2));
    int yOffset= (int) ((parent.getHeight()/2)-(frmHeight/2));
        
    setSize(frmWidth,frmHeight);
    setLocation(xOffset, yOffset);       
    setResizable(false);
    setMaximizable(false);
    setIconifiable(false);
    
    Container aboutPane = getContentPane();
     
    JTextArea textLabel = new JTextArea("RegXmaker v1.1 Open Source Edition\n"+
      "Copyright © 2001, 2004 SolidMatrix Technologies, Inc.\n"+
      "GUI and command line utilities are licensed under the GPL.\n" +      "RegXmaker Library is licensed under the LGPL.\n"+
	  "Documentation is licensed under the GNU FDL.\n"+
      "\n"+
      "This is a project of the SolidMatrix Research\n" +
      "Contact: research@solidmatrix.com\n" +
      "Website: www.solidmatrix.com\n"+
      "\n"+
      "Created by Gennadiy Shafranovich and Konstantin Drondin."      
      );
      
    textLabel.setEnabled(false);
    textLabel.setEditable(false);

    Icon icn = new ImageIcon(RegXMakerGui.ICON_DIR + "logo.jpg");
    JLabel logoLabel = new JLabel(icn);
     
    Icon solidlogo = new ImageIcon(RegXMakerGui.ICON_DIR + "blogo.jpg");
    JLabel solidlogoLabel = new JLabel(solidlogo);
    
    JButton close = new JButton("Close");
    close.addActionListener(new ActionListener() 
      {
        public void actionPerformed(ActionEvent e) {processClose(); }
      });
     
    aboutPane.setLayout(new FlowLayout());
    aboutPane.setBackground(Color.white);
    aboutPane.add(logoLabel);
    aboutPane.add(textLabel);
    aboutPane.add(solidlogoLabel);
    aboutPane.add(close);
    
    myWindow = gui.addFrameToList(getTitle());
  }//HelpFrame()
  
  protected void processClose()  { stdClose(); }
}//HelpFrame
