package com.solidmatrix.regxmaker.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.Windows
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

public class Windows extends JDialog {
  public static final double PARENT_RATIO = 1.8;

  private JDesktopPane parent;
  private JList win;
  private JInternalFrame[] frames;
  
  private JButton switchTo = new JButton("Switch To");
  private JButton closeWindow = new JButton("Close Window");
  private JButton back = new JButton("Back");
  
  public Windows(RegXMakerGui gui) {
    super(gui, "Currently Open Windows:", true);
  
    parent = gui.getDesktopPane();
    
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
    int wndWidth = gui.getWidth();
	int wndHeight = gui.getHeight();
	int frmWidth =  (int) (wndWidth / PARENT_RATIO);
	int frmHeight = (int) (wndHeight / PARENT_RATIO);
	int xOffset= (int) ((wndWidth/2)-(frmWidth/2));
	int yOffset= (int) ((wndHeight/2)-(frmHeight/2));
            
    setSize(frmWidth,frmHeight);
    setLocation(gui.getX() + xOffset, gui.getY() + yOffset);
    
    MListener action = new MListener();
    
    win = new JList();
    JScrollPane listScroller = new JScrollPane(win);
    win.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    win.addMouseListener(new MouseAdapter()  
      {
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) { switchToCall(); }
        }
      });//double-click to switch
    
    resetListData();
    
    Container wcont = getContentPane();
    wcont.setLayout(new BorderLayout());
    
    JToolBar buttons = new JToolBar(JToolBar.HORIZONTAL);
    buttons.setFloatable(false);
    
    switchTo.setToolTipText("Switch to selected window");
    switchTo.addActionListener(action);
    buttons.add(switchTo);
    
    closeWindow.setToolTipText("Close selected window");
    closeWindow.addActionListener(action);
    buttons.add(closeWindow);
    
    back.setToolTipText("Back to program");
    back.addActionListener(action);
    buttons.add(back);
    
    wcont.add(buttons, BorderLayout.NORTH);
    wcont.add(listScroller, BorderLayout.CENTER);
  }//Windows()
  
  
  private void switchToCall() {
    JInternalFrame selFrame = frames[win.getSelectedIndex()];
    
    if(selFrame == null)
      return;
    
    dispose();
    
    try  { selFrame.setIcon(false); } catch(Exception e)  {  }
    selFrame.show();
    selFrame.requestFocus();
  }//switchToCall()
  
  
  public void closeWindowCall() {
    JInternalFrame selFrame = frames[win.getSelectedIndex()];
    
    if(selFrame == null)
      return;
    
    if(selFrame instanceof MyFrame)
      ((MyFrame) selFrame).processClose();
    else
      selFrame.doDefaultCloseAction();
    
    resetListData();
  }//closeWindowCall()
  
  
  private void resetListData() {
    frames = parent.getAllFrames();
    Vector openw = new Vector(frames.length);
        
    for(int i = 0; i < frames.length; i++) {
      if (frames[i] instanceof MyFrame) {
        MyFrame mf = (MyFrame) frames[i];
        
        openw.add(mf.getTitle());
      }//if
    }//for
    
    win.setListData(openw);
  }//resetListData()
  
  private class MListener implements ActionListener {
    public void actionPerformed(ActionEvent event)
    {
        
      //find which component called it
      Object object = event.getSource();
      
      //react to button clicks
      if(object == back)
        dispose();
      else if (object == switchTo)
        switchToCall();
      else if (object == closeWindow)
        closeWindowCall();
        
    }//actionPerformed()
  }//MListener
}//Windows