package com.solidmatrix.regxmaker.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.solidmatrix.regxmaker.util.shared.*;
import com.solidmatrix.regxmaker.util.superdiff.RegExCompiler;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.ProcessFrame
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


public class ProcessFrame extends MyFrame implements Monitor
{
  private JTextArea preview;
  private JButton cancel = new JButton("Cancel Process");
  private JButton clip = new JButton("To Clipboard");
    
  private String[] strings;
  private int ignoreWeight;
  private int compileLevel;
  
  private boolean done;
  private Process p;  
 
  public ProcessFrame(RegXMakerGui g, String[] s, int iw, int cl)
  {
    super("Processing...", g);
    
    Container cont = getContentPane();
    cont.setLayout(new BorderLayout());
        
    preview = new JTextArea();
    preview.setEditable(false);//don't want user to edit the preview
    preview.setText("Compiling Regular Expression.");//set the text
    preview.setCaretPosition(0);
    preview.setWrapStyleWord(true);
    JScrollPane previewScroller = new JScrollPane(preview);
    
    MListener action = new MListener();
    
    JToolBar buts = new JToolBar();
    buts.setFloatable(false);
    
    cancel.setToolTipText("Cancel current process");
    cancel.addActionListener(action);
    buts.add(cancel);
    
    clip.setToolTipText("Save contents to clipboard");
    clip.addActionListener(action);
    buts.add(clip);
    
    cont.add(buts, BorderLayout.NORTH);
    cont.add(previewScroller, BorderLayout.CENTER);
    
    strings = s;
    ignoreWeight = iw;
    compileLevel = cl;
    done = true;
    
    myWindow = gui.addFrameToList(getTitle());
  }//ProcessFrame()
  
  public JTextArea getTextArea() { return preview; }
  
  private void toClip() {
    int oselStart = preview.getSelectionStart();
    int oselEnd = preview.getSelectionEnd();
    
    preview.setSelectionStart(0);
    preview.setSelectionEnd(preview.getText().length());
    
    preview.copy();
    
    preview.setSelectionStart(oselStart);
    preview.setSelectionEnd(oselEnd);
    
    preview.requestFocus();
  }//toClip()
    
  public void begin() {
    done = false;
    
    p = new Process(this);
    ProcessTimeout pt = new ProcessTimeout();
    
    pt.start();
    p.start();
  }//begin()
  
  private void done(String a) {
    if(done)
      return;
  
    done = true;
  
    boolean userWants2c=false;
  
    //if there's a file, first write, then show confirmations...
    //here's an idea... show the file size to make the decision easier
    //cancel button == no... makes sence?
        
    if (gui.outputFile() !=null)
    {
      gui.writeFile(gui.outputFile(), a, gui.getEncodingScheme());
      int userChoice = JOptionPane.showInternalConfirmDialog(this, "The output was written to " + gui.outputFile() + ". \nIt is of size " + a.length() + " bytes. \nWould you like to view it?", "Done", JOptionPane.YES_NO_OPTION);
      userWants2c = (userChoice == JOptionPane.YES_OPTION);
    }//if
        
    //... and then display the output
    if (gui.outputFile() == null || userWants2c)
      gui.createOutputFrame(a);  
  }//done()
  
  private void error(Throwable e) {
    if(done)
      return;
  
    JOptionPane.showInternalMessageDialog(this, "An Error Has Occured While Processing" , "Error", JOptionPane.ERROR_MESSAGE);
    e.printStackTrace(gui.getErrorWriter());
    
    done = true;
  }//error()
  
  private void timeout() {
    if(done)
      return;
      
    updateStatus("Operation timed out.");
    
    p.stopProcessing();
    p = null;
    
    done = true;
  }//timeout()  
  
  private void cancel() {
    if(done)
      return;
  
    updateStatus("Operation canceled.");
    
    p.stopProcessing();
    p = null;
    
    done = true;
  }//cancel()
  
  public void updateStatus(String s)  {
    if (!done && s  != null && s.length() > 0) {
      preview.append("\n");
      preview.append(s);
      preview.setCaretPosition(preview.getText().length() - 1);
    }//if
    
    repaint();
  }//updateStatus()
  
  protected void processClose() {
    if (!isClosed()) {
      if (!done)
        JOptionPane.showInternalMessageDialog(this, "Cannot close process window while processing." , "Error", JOptionPane.ERROR_MESSAGE);
      else
        stdClose();//close the internal frame
    }//if
  }//processClose()
  
  private class MListener implements ActionListener {
    public void actionPerformed(ActionEvent event)
    {
        
      //find which component called it
      Object object = event.getSource();
      
      //react to button clicks
      if(object == cancel)
        cancel();
      else if (object == clip)
        toClip();
    }//actionPerformed()
  }//MListener
  
  private class Process extends Thread {
    private ProcessFrame parent;
    private RegExCompiler rec;
    
    public Process(ProcessFrame p)  { parent = p; }
  
    public void run() {
      try  {
        rec = new RegExCompiler(ignoreWeight, parent);
        String answer = rec.compile(strings, compileLevel);

        done(answer);
      } catch (Throwable e)  {
        error(e);
      }//try..catch  
    }//run()
    
    public void stopProcessing() {
      rec.stopProcess();
    }//stopProcessing()
  }//Process
  
  private class ProcessTimeout extends Thread {
    private long timeout;
  
    public void run() {
      long start = System.currentTimeMillis();
      
      timeout = gui.getTimeoutPeriod();
    
      while (!done && (System.currentTimeMillis() - start) <= timeout) {
        long time = System.currentTimeMillis() - start;
        long second = time / 1000;
        long minute = second / 60;
        long hour = minute / 60;
       
        String s = String.valueOf(second % 60);
        if(s.length() == 1)
          s = "0" + s;
        
        String m = String.valueOf(minute  % 60);
        if(m.length() == 1)
          m = "0" + m;
      
        setTitle("Processing (" + hour +  ":" + m + ":" + s + ")");
      
        Thread.yield();
      
        try  {
          Thread.sleep(RegXMakerGui.INCREMENT);
        } catch(InterruptedException e)  {  }
      }//while
      
      timeout();
    }//run()
  }//ProcessTimeout()
}//ProcessFrame
