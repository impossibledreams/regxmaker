package com.solidmatrix.regxmaker.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.basic.BasicToolBarUI;

import com.solidmatrix.regxmaker.util.shared.*;
import com.solidmatrix.regxmaker.util.shared.Constants;
import com.solidmatrix.regxmaker.util.superdiff.RegExCompiler;
 
/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.RegXMakerGui
 * Project: RegularExpression compiler
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Konstantin Drondin
 * Purpose: GUI to the regular expression compiler
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
 * Comments: Some, no javadoc
 *
 * Modification History
 *
 * 06-20-2001	KD Created
 *
 * 06-21-2001   GS Fixed many bugs and issues. Refer to What to fix doc
 *                 file for more information.
 *                 NOTE: Indentation and visibility modifiers need 
 *                       be finalized.
 *
 * 06-22-2001   GS Fixed indent and visibility modifiers.
 *
 * 07-03-2001   GS Added menues that are standard in windows programs
 *
 * 07-05-2001   GS Fixed CTRL-F4 keystroke closing a window no matter
 *                 what the user's response is to a prompt. The work
 *                 around use is to overide the Action in the ActionMap
 *                 of the program and point the action representative
 *                 of "close" to a custom action class which calls
 *                 processClose() method of the selected window. The
 *                 workaround is inherintly unsafe for future swing
 *                 releases because it depends on the format of
 *                 key stroke listings in the ActionMap.
 *
 * 07-05-2004  YS Added licensing information
 * </PRE>            
 */ 

public class RegXMakerGui extends JFrame implements RegXMakerConstants
{

  /***********************
   *     CONSTANTS       *
   ***********************/


  //globals
  public static final float VERSION = 1.1f;
  public static final String VER_STRING = "RegXMaker GUI 1.1";
  public static final String TITLE_STRING = "RegXMaker v1.1";
  
  //screen margins
  public static final int MARGIN = 40;
  
  //max elements in windows menu
  public static final int MAX_WIN_ITEMS = 10;
  
  
  /*********************
   *   MAIN VARIABLES  *
   *********************/
  
  
  //these vectors hold the objects (strings)
  //used to identify strings being veiewed/edited
  private Vector prevStrings = new Vector(6);//change this!!!
  private Vector editStrings = new Vector(3);//change this!!!
  
  //these variables store all options for the program   
  private int ignoreWeight = Constants.LETTER_ACCURACY;//accuracy level
  private int compileLevel = RegExCompiler.SURFACE_COMPILE;
  private String encodingScheme = Constants.OPTION_1_SCHEME;//text format
  private String outputFile = null;
  private boolean isWordWrap=false;
  private boolean autosave = true;
  private boolean autoload = true;
  private int windowCount = 0;
  private long timeout = DEFAULT_TIMEOUT;
  
  //size of top window
  private int sizeX;
  private int sizeY;
  
  //error stream
  public PrintStream err;
  public FileOutputStream file;
  
 
  /***********************
   *   MAIN COMPONENTS   *
   ***********************/
 
 
  //The desktop
  private JDesktopPane desktop = new JDesktopPane();
    
  //toolbars
  private JToolBar buttonFrameCont = new JToolBar(JToolBar.HORIZONTAL);//main buttons
  private JToolBar listButtons = new JToolBar(JToolBar.VERTICAL);//list buttons
  
  //file open and save dialog boxes
  private FileDialog fileOpenBox = new FileDialog(this,"Input File", FileDialog.LOAD);
  private FileDialog fileSaveBox = new FileDialog(this,"Output File", FileDialog.SAVE);

  //list of attached strings, and model that contains the list
  private DefaultListModel fileListModel=new DefaultListModel();
  private JList fileList;
  
  
  /***********************
   *  GLOBAL LISTENERS   *
   ***********************/
  
   
  private actions action = new actions(); 
  private RightClickListener rightClick = new RightClickListener(); 
  private ListListener myListListener = new ListListener();
  
  
  /***********************
   *   TOOLBAR BUTTONS   *
   ***********************/
  
  private JButton previewButton = new JButton();
  private JButton editButton = new JButton();
  private JButton addFileButton = new JButton();
  private JButton addUrlButton = new JButton();
  private JButton addStringButton = new JButton();
  private JButton removeButton = new JButton();
  private JButton removeAllButton = new JButton();
  private JButton outputFileButton = new JButton();
  private JButton outputFileClearButton = new JButton();
  private JButton processButton = new JButton();
  private JButton optionsButton = new JButton();
  private JButton cutButton = new JButton();
  private JButton copyButton = new JButton();
  private JButton pasteButton = new JButton();   


  /***********************
   *     MENU SETUP      *
   ***********************/
  
  
  //menu
  private JMenuBar mBar = new JMenuBar();//main menu bar
  
  //file->
  private JMenu fileMenu = new JMenu("File");
  private JMenuItem fileOpen = new JMenuItem("Add File");
  private JMenuItem fileUrl = new JMenuItem("Add URL");
  private JMenuItem fileString = new JMenuItem("Add String");
  private JMenuItem fileOut = new JMenuItem("Output File");
  private JMenuItem fileOutClear = new JMenuItem("Clear Output File");
  private JMenuItem fileProcess = new JMenuItem("Process");
  private JMenuItem fileExit = new JMenuItem("Exit"); 
 
  //List->
  private JMenu listMenu = new JMenu("List");
  private JPopupMenu listPop = new JPopupMenu();
  private JMenuItem listPreview = new JMenuItem("Preview");
  private JMenuItem listEdit = new JMenuItem("Edit"); 
  private JMenuItem listRemove = new JMenuItem("Remove");
  private JMenuItem listClear = new JMenuItem("Clear"); 
 
  
  //edit->
  private JMenu editMenu = new JMenu("Edit");
  private JPopupMenu editPop = new JPopupMenu();
  private JMenuItem editCut = new JMenuItem("Cut");
  private JMenuItem editCopy= new JMenuItem("Copy");
  private JMenuItem editPaste = new JMenuItem("Paste");
  private JMenuItem editSelectAll = new JMenuItem("Select All");
  private JCheckBoxMenuItem wordWrap = new JCheckBoxMenuItem();
 
  
  //options->
  private JMenu optionsMenu = new JMenu("Options");
  private JPopupMenu optionsPop = new JPopupMenu();
  private JMenuItem[] lfItems;
  
  
  //options->Compile level
  private JMenu cmpLvlMenu = new JMenu("Compile level");
  private ButtonGroup cmpLblGrp = new ButtonGroup();
  private JRadioButtonMenuItem deepCompile = new JRadioButtonMenuItem("Deep Compile");
  private JRadioButtonMenuItem deepSemiCompile = new JRadioButtonMenuItem("Semi-Deep Compile");
  private JRadioButtonMenuItem surfaceCompile = new JRadioButtonMenuItem("Surface Compile");
  private JRadioButtonMenuItem noCompile = new JRadioButtonMenuItem("No Compile");
  
  //options->Accuracy level
  private JMenu accLvlMenu = new JMenu("Accuracy level");
  private ButtonGroup accLblGrp = new ButtonGroup();
  private JRadioButtonMenuItem ltrAccuracy = new JRadioButtonMenuItem("Letter Accuracy");
  private JRadioButtonMenuItem wrdAccuracy = new JRadioButtonMenuItem("Word Accuracy");
  private JRadioButtonMenuItem lneAccuracy = new JRadioButtonMenuItem("Line Accuracy");
  
  
  //options->Text format
  private JMenu txtFrmMenu = new JMenu("Text format");
  private ButtonGroup txtFrmGrp = new ButtonGroup();
  private JRadioButtonMenuItem t1Frmt = new JRadioButtonMenuItem("US ASCII");
  private JRadioButtonMenuItem t2Frmt = new JRadioButtonMenuItem("ISO-8859-1");
  private JRadioButtonMenuItem t3Frmt = new JRadioButtonMenuItem("UTF-8");
  private JRadioButtonMenuItem t4Frmt = new JRadioButtonMenuItem("UTF-16BE");
  private JRadioButtonMenuItem t5Frmt = new JRadioButtonMenuItem("UTF-16LE");
  private JRadioButtonMenuItem t6Frmt = new JRadioButtonMenuItem("UTF-16");
 
 
  //customize->
  private JMenu custMenu = new JMenu("Customize");
  private JCheckBoxMenuItem toolBarEnable = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem listToolBarEnable = new JCheckBoxMenuItem();  
  private JCheckBoxMenuItem outlineDrag = new JCheckBoxMenuItem();  
  private JMenuItem setTimeout = new JMenuItem("Set Process Timeout");  
  private JCheckBoxMenuItem autosaveBox = new JCheckBoxMenuItem();  
  private JCheckBoxMenuItem autoloadBox = new JCheckBoxMenuItem();  
  private JMenuItem saveItem = new JMenuItem("Save Settings");  
  private JMenuItem loadItem = new JMenuItem("Load Settings");
    
  
  //window->
  private JMenu windowMenu = new JMenu("Window");
  private JPopupMenu windowPop = new JPopupMenu();
  private JMenuItem windowTileV = new JMenuItem("Tile Vertical");
  private JMenuItem windowTileH = new JMenuItem("Tile Horizontal");
  private JMenuItem windowCascade = new JMenuItem("Cascade");
  private JMenuItem windowCloseAll = new JMenuItem("Close All");
  private JMenuItem windowMore = new JMenuItem("More Windows...");
  private LinkedList windowWait = new LinkedList();
  
  
  //Help->
  private JMenu HelpMenu = new JMenu("Help");
  private JMenuItem HelpUserGuide = new JMenuItem("User Guide");
  private JMenuItem HelpLicense = new JMenuItem("License");
  private JMenuItem HelpAbout = new JMenuItem("About...");

  
  /******************************
   * END CLASS VARIABLE SECTION *
   ******************************/
     
  
  /*******************
   *  INIT SECTION   *
   *******************/
  

  public static void main (String[] args)
  {
    JFrame s = new RegXMakerGui();//create an instance
    s.show();
  }//main()
  
    
  public RegXMakerGui()
  {
 
    /**************** Error Stream Init  *******************/
    
    try  {
      file = new FileOutputStream("RegXMakerErrors.log");
      err = new PrintStream(file);
      
      DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
      
      err.println(VER_STRING);
      err.println("Error Log File: " + format.format(new Date()));
      
      err.println();
      err.println("Initialization time errors:");
      err.println();
    } catch(Exception  e) { e.printStackTrace(); }
 
    /**************** Global Init  *******************/
    
    
    //basicly, get the screensize, and put the frame in 
    //the middle of the screen, w/ a border all around
    
    setJMenuBar(mBar);//set the menubar
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    sizeX = screenSize.width-MARGIN*2;
    sizeY = screenSize.height-MARGIN*2;
    
    setBounds(MARGIN, MARGIN, sizeX, sizeY);//set size
    setTitle(TITLE_STRING);//set title
    
    //exit (the X-thing)                
    addWindowListener(new WindowAdapter() 
      {
        public void windowClosing(WindowEvent e) { 
          if(autosave)
            saveSettings(false);
            
          System.exit(0);
        }
      });
    
    desktop.addMouseListener(rightClick);
    desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
   
    //get container of the jframe, on which everything is drawn
    Container mainCont = getContentPane();
    
    //need this for the toolbar
    mainCont.setLayout(new BorderLayout());
    
    //add the toolbar to the north part of the frame
    mainCont.add(buttonFrameCont, BorderLayout.NORTH);
        
    //add the desktop on which all internal windows will be drawn
    //mainCont.add(desktop, BorderLayout.CENTER);
    
    
    /**************** List Init  *******************/
    
    
    //set the fileList's model to fileListModel
    fileList = new JList(fileListModel); 
    fileList.addFocusListener(new ButtonListener(false,  false,  false, true, false));
    fileList.addMouseListener(rightClick);
    fileList.addMouseListener(myListListener);
    fileList.addKeyListener(myListListener);
    
    //create a scrollpane for the list
    JScrollPane listScroller = new JScrollPane(fileList);
    listScroller.setPreferredSize(new Dimension(sizeX / 4, listScroller.getHeight()));

    //set custom rendered for italic
    ListCellRenderer renderer = new ToolTipRenderer();
    fileList.setCellRenderer(renderer);
    
    JPanel p = new JPanel(new BorderLayout());
    
    p.add(listButtons, BorderLayout.WEST);
    p.add(listScroller, BorderLayout.CENTER);
    
    //mainCont.add(p, BorderLayout.WEST);
    
    JSplitPane spane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p, desktop);
    
    mainCont.add(spane, BorderLayout.CENTER);
    
    
    /**************** Buttons Init  *******************/
    
    //buttons:
    addFileButton.setIcon(new ImageIcon (ICON_DIR + "add-file.gif"));
    addFileButton.setToolTipText("Add file to the list");
    addFileButton.addActionListener(action);
    
    addUrlButton.setIcon(new ImageIcon (ICON_DIR +  "add-url.gif"));
    addUrlButton.setToolTipText("Add contents of url to the list");
    addUrlButton.addActionListener(action);
    
    previewButton.setIcon(new ImageIcon (ICON_DIR + "preview.gif"));
    previewButton.setToolTipText("View contents of a file");
    previewButton.setEnabled(false);
    listButtons.add(previewButton);
    previewButton.addActionListener(action);
    
    editButton.setIcon(new ImageIcon (ICON_DIR + "edit.gif"));
    editButton.setToolTipText("Edit contents");
    editButton.setEnabled(false);
    listButtons.add(editButton);
    editButton.addActionListener(action);
    
    removeButton.setIcon(new ImageIcon (ICON_DIR + "remove.gif"));
    removeButton.setToolTipText("Remove selected items from the list");
    removeButton.setEnabled(false);
    listButtons.add(removeButton);
    removeButton.addActionListener(action);
    
    removeAllButton.setIcon(new ImageIcon (ICON_DIR + "remove-all.gif"));
    removeAllButton.setToolTipText("Clear the list");
    removeAllButton.setEnabled(false);
    listButtons.add(removeAllButton);
    removeAllButton.addActionListener(action);
    
    addStringButton.setIcon(new ImageIcon (ICON_DIR + "add-string.gif"));
    addStringButton.setToolTipText("Add user input to the list");
    addStringButton.addActionListener(action);
    
    outputFileButton.setIcon(new ImageIcon (ICON_DIR + "outputfile.gif"));
    outputFileButton.setToolTipText("Select output file");
    outputFileButton.addActionListener(action);
    
    outputFileClearButton.setIcon(new ImageIcon (ICON_DIR + "outputfilec.gif"));
    outputFileClearButton.setToolTipText("Clear output file setting.");
    outputFileClearButton.addActionListener(action);
    
    processButton.setIcon(new ImageIcon (ICON_DIR + "process.gif"));
    processButton.setToolTipText("Process the items on the list");
    processButton.addActionListener(action);
    
    cutButton.setIcon(new ImageIcon (ICON_DIR + "cut24.gif"));
    cutButton.setToolTipText("Cut");
    cutButton.addActionListener(action);
    
    copyButton.setIcon(new ImageIcon (ICON_DIR + "copy24.gif"));
    copyButton.setToolTipText("Copy");
    copyButton.addActionListener(action);
    
    pasteButton.setIcon(new ImageIcon (ICON_DIR + "paste24.gif"));
    pasteButton.setToolTipText("Paste");
    pasteButton.addActionListener(action);
    
    
    //ADD ALL THE BUTTONS, CHANGE THE LAYOUT IF ANYTHING
    buttonFrameCont.add(addFileButton);
    buttonFrameCont.add(addUrlButton);
    buttonFrameCont.add(addStringButton);
    buttonFrameCont.addSeparator();
    buttonFrameCont.add(optionsButton);
    buttonFrameCont.addSeparator();
    buttonFrameCont.add(cutButton);
    buttonFrameCont.add(copyButton);
    buttonFrameCont.add(pasteButton);
    buttonFrameCont.addSeparator();
    buttonFrameCont.add(outputFileButton);
    buttonFrameCont.add(outputFileClearButton);
    buttonFrameCont.addSeparator();
    buttonFrameCont.add(processButton);
    
    disableButtons();
    
        
    /**************** Menu Init  *******************/
    
    
    //Menu
    mBar.add(fileMenu);
    mBar.add(listMenu);
    mBar.add(editMenu);
    mBar.add(optionsMenu);
    mBar.add(custMenu);
    mBar.add(windowMenu);
    mBar.add(HelpMenu);
    
    //File Menu:
    fileMenu.setMnemonic(KeyEvent.VK_F);
    
    //add FileMenu Items
    
    fileMenu.add(fileOpen);
    fileOpen.setIcon(new ImageIcon (ICON_DIR + "add-file-16.gif"));
    KeyStroke ctrlOKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK);
    fileOpen.setAccelerator(ctrlOKeyStroke);    
    fileOpen.setMnemonic(KeyEvent.VK_A);
    fileOpen.addActionListener(action);
    
    fileMenu.add(fileUrl);
    fileUrl.setIcon(new ImageIcon (ICON_DIR + "add-url-16.gif"));
    KeyStroke ctrlUKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK);
    fileUrl.setAccelerator(ctrlUKeyStroke);    
    fileUrl.setMnemonic(KeyEvent.VK_U);
    fileUrl.addActionListener(action);
    
    fileMenu.add(fileString);
    fileString.setIcon(new ImageIcon (ICON_DIR + "add-string-16.gif"));
    KeyStroke ctrlGKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK);
    fileString.setAccelerator(ctrlGKeyStroke);    
    fileString.setMnemonic(KeyEvent.VK_S);
    fileString.addActionListener(action);
    
    fileMenu.addSeparator();
    
    fileMenu.add(fileOut);
    fileOut.setIcon(new ImageIcon (ICON_DIR + "outputfile-16.gif"));
    KeyStroke ctrlTKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK);
    fileOut.setAccelerator(ctrlTKeyStroke);    
    fileOut.setMnemonic(KeyEvent.VK_T);
    fileOut.addActionListener(action);
    
    fileMenu.add(fileOutClear);
    fileOutClear.setIcon(new ImageIcon (ICON_DIR + "outputfilec-16.gif"));
    KeyStroke ctrlLKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK);
    fileOutClear.setAccelerator(ctrlLKeyStroke);    
    fileOutClear.setMnemonic(KeyEvent.VK_C);
    fileOutClear.addActionListener(action);
    
    fileMenu.add(fileProcess);
    fileProcess.setIcon(new ImageIcon (ICON_DIR + "process-16.gif"));
    KeyStroke ctrlRKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK);
    fileProcess.setAccelerator(ctrlRKeyStroke);    
    fileProcess.setMnemonic(KeyEvent.VK_R);
    fileProcess.addActionListener(action);
    
    fileMenu.addSeparator();
    
    fileMenu.add(fileExit);
    fileExit.setIcon(new ImageIcon (ICON_DIR + "exit.gif"));
    fileExit.setMnemonic(KeyEvent.VK_X);
    fileExit.addActionListener(action);
    
    //List Menu
    listMenu.setMnemonic(KeyEvent.VK_L);
    listMenu.addMenuListener(action);
    listPop = listMenu.getPopupMenu();
    
    listMenu.add(listPreview);
    listPreview.setIcon(new ImageIcon (ICON_DIR + "preview-16.gif"));
    KeyStroke ctrlPKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
    listPreview.setAccelerator(ctrlPKeyStroke);    
    listPreview.setMnemonic(KeyEvent.VK_P);
    listPreview.setEnabled(false);
    listPreview.addActionListener(action);
    
    listMenu.add(listEdit);
    listEdit.setIcon(new ImageIcon (ICON_DIR + "edit-16.gif"));
    KeyStroke ctrlEKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK);
    listEdit.setAccelerator(ctrlEKeyStroke);    
    listEdit.setMnemonic(KeyEvent.VK_D);
    listEdit.setEnabled(false);
    listEdit.addActionListener(action);
    
    listMenu.add(listRemove);
    listRemove.setIcon(new ImageIcon (ICON_DIR + "remove-16.gif"));
    KeyStroke DelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
    listRemove.setAccelerator(DelKeyStroke);    
    listRemove.setMnemonic(KeyEvent.VK_R);
    listRemove.setEnabled(false);
    listRemove.addActionListener(action);
    
    listMenu.add(listClear);
    listClear.setIcon(new ImageIcon (ICON_DIR + "remove-all-16.gif"));
    listClear.setMnemonic(KeyEvent.VK_C);
    listClear.setEnabled(false);
    listClear.addActionListener(action);
    
    
    //Edit Menu
    editMenu.setMnemonic(KeyEvent.VK_E);
    editMenu.addMenuListener(action);
    editPop = editMenu.getPopupMenu();
    
    editMenu.add(editCut);
    editCut.setIcon(new ImageIcon (ICON_DIR + "cut16.gif"));
    KeyStroke ctrlXKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
    editCut.setAccelerator(ctrlXKeyStroke);    
    editCut.setMnemonic(KeyEvent.VK_T);
    editCut.addActionListener(action);
    
    editMenu.add(editCopy);
    editCopy.setIcon(new ImageIcon (ICON_DIR + "copy16.gif"));
    KeyStroke ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
    editCopy.setAccelerator(ctrlCKeyStroke);    
    editCopy.setMnemonic(KeyEvent.VK_C);
    editCopy.addActionListener(action);
    
    editMenu.add(editPaste);
    editPaste.setIcon(new ImageIcon (ICON_DIR + "paste16.gif"));
    KeyStroke ctrlVKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
    editPaste.setAccelerator(ctrlVKeyStroke);    
    editPaste.setMnemonic(KeyEvent.VK_P);
    editPaste.addActionListener(action);
    
    editMenu.addSeparator();
    
    editMenu.add(editSelectAll);
    KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK);
    editSelectAll.setAccelerator(ctrlAKeyStroke);    
    editSelectAll.setMnemonic(KeyEvent.VK_A);
    editSelectAll.addActionListener(action);
    
    editMenu.addSeparator();
    
    wordWrap = new JCheckBoxMenuItem("Word Wrap", isWordWrap); 
    wordWrap.setMnemonic(KeyEvent.VK_R);
    wordWrap.addActionListener(action);
    editMenu.add(wordWrap);
        
    
    //options menu
    optionsMenu.setMnemonic(KeyEvent.VK_O);
    optionsMenu.addMenuListener(action);
    
    deepCompile.setMnemonic(KeyEvent.VK_D);
    deepCompile.addActionListener(action);
    cmpLblGrp.add(deepCompile);
    cmpLvlMenu.add(deepCompile);
    
    deepSemiCompile.setMnemonic(KeyEvent.VK_M);
    deepSemiCompile.addActionListener(action);
    cmpLblGrp.add(deepSemiCompile);
    cmpLvlMenu.add(deepSemiCompile);
    
    surfaceCompile.setSelected(true);
    surfaceCompile.setMnemonic(KeyEvent.VK_S);
    surfaceCompile.addActionListener(action);
    cmpLblGrp.add(surfaceCompile);
    cmpLvlMenu.add(surfaceCompile);
    
    noCompile.setMnemonic(KeyEvent.VK_N);
    noCompile.addActionListener(action);
    cmpLblGrp.add(noCompile);
    cmpLvlMenu.add(noCompile);
    
    ltrAccuracy.setSelected(true);
    ltrAccuracy.setMnemonic(KeyEvent.VK_T);
    ltrAccuracy.addActionListener(action);
    accLblGrp.add(ltrAccuracy);
    accLvlMenu.add(ltrAccuracy);
    
    wrdAccuracy.setMnemonic(KeyEvent.VK_R);
    wrdAccuracy.addActionListener(action);
    accLblGrp.add(wrdAccuracy);
    accLvlMenu.add(wrdAccuracy);
    
    lneAccuracy.setMnemonic(KeyEvent.VK_N);
    lneAccuracy.addActionListener(action);
    accLblGrp.add(lneAccuracy);
    accLvlMenu.add(lneAccuracy);
        
    t1Frmt.setSelected(true);
    t1Frmt.setMnemonic(KeyEvent.VK_A);
    t1Frmt.addActionListener(action);
    txtFrmGrp.add(t1Frmt);
    txtFrmMenu.add(t1Frmt);
    
    t2Frmt.addActionListener(action);
    t2Frmt.setMnemonic(KeyEvent.VK_S);
    txtFrmGrp.add(t2Frmt);
    txtFrmMenu.add(t2Frmt);
    
    t3Frmt.addActionListener(action);
    t3Frmt.setMnemonic(KeyEvent.VK_8);
    txtFrmGrp.add(t3Frmt);
    txtFrmMenu.add(t3Frmt);
    
    t4Frmt.addActionListener(action);
    t4Frmt.setMnemonic(KeyEvent.VK_B);
    txtFrmGrp.add(t4Frmt);
    txtFrmMenu.add(t4Frmt);
    
    t5Frmt.addActionListener(action);
    t5Frmt.setMnemonic(KeyEvent.VK_E);
    txtFrmGrp.add(t5Frmt);
    txtFrmMenu.add(t5Frmt);
    
    t6Frmt.addActionListener(action);
    t6Frmt.setMnemonic(KeyEvent.VK_6);
    txtFrmGrp.add(t6Frmt);
    txtFrmMenu.add(t6Frmt);
    
    
    //add everything to options
    cmpLvlMenu.setMnemonic(KeyEvent.VK_M);
    accLvlMenu.setMnemonic(KeyEvent.VK_C);
    txtFrmMenu.setMnemonic(KeyEvent.VK_F);
    optionsMenu.add(cmpLvlMenu);
    optionsMenu.add(accLvlMenu);
    optionsMenu.add(txtFrmMenu);
    
    
    //pop up options..same as above menu
	optionsButton.setIcon(new ImageIcon (ICON_DIR + "options.gif"));
    optionsButton.addActionListener(action);
    optionsPop = optionsMenu.getPopupMenu();
    
    //customize menu
    custMenu.setMnemonic(KeyEvent.VK_C);
    
    toolBarEnable = new JCheckBoxMenuItem("Tool Bar", true); 
    toolBarEnable.setMnemonic(KeyEvent.VK_B);
    toolBarEnable.addActionListener(action);
    custMenu.add(toolBarEnable);
    
    listToolBarEnable = new JCheckBoxMenuItem("List Tool Bar", true); 
    listToolBarEnable.setMnemonic(KeyEvent.VK_T);
    listToolBarEnable.addActionListener(action);
    custMenu.add(listToolBarEnable);
    
    outlineDrag = new JCheckBoxMenuItem("\"Outline\" Frame Dragging", true); 
    outlineDrag.setMnemonic(KeyEvent.VK_D);
    outlineDrag.addActionListener(action);
    custMenu.add(outlineDrag);
    
    custMenu.addSeparator();
    
    custMenu.add(setTimeout);
    setTimeout.setMnemonic(KeyEvent.VK_P);
    setTimeout.addActionListener(action);
    
    custMenu.addSeparator();
    
    autoloadBox = new JCheckBoxMenuItem("Load Settings On Startup", true); 
    autoloadBox.setMnemonic(KeyEvent.VK_S);
    autoloadBox.addActionListener(action);
    custMenu.add(autoloadBox);
    
    autosaveBox = new JCheckBoxMenuItem("Save Settings On Exit", true); 
    autosaveBox.setMnemonic(KeyEvent.VK_E);
    autosaveBox.addActionListener(action);
    custMenu.add(autosaveBox);
    
    custMenu.addSeparator();
    
    custMenu.add(saveItem);
    saveItem.setMnemonic(KeyEvent.VK_V);
    saveItem.addActionListener(action);
    
    custMenu.add(loadItem);
    loadItem.setMnemonic(KeyEvent.VK_A);
    loadItem.addActionListener(action);
    
    
    //window menu
    windowMenu.setMnemonic(KeyEvent.VK_W);
    windowMenu.addMenuListener(action);
    windowPop = windowMenu.getPopupMenu();
    
    windowMenu.add(windowCascade);
    windowCascade.setIcon(new ImageIcon (ICON_DIR + "cascade.gif"));
    windowCascade.setMnemonic(KeyEvent.VK_D);
    windowCascade.addActionListener(action);
    
    windowMenu.add(windowTileV);
    windowTileV.setIcon(new ImageIcon (ICON_DIR + "vertical.gif"));
    windowTileV.setMnemonic(KeyEvent.VK_V);
    windowTileV.addActionListener(action);
    
    windowMenu.add(windowTileH);
    windowTileH.setIcon(new ImageIcon (ICON_DIR + "horizontal.gif"));
    windowTileH.setMnemonic(KeyEvent.VK_H);
    windowTileH.addActionListener(action);
                   
    windowMenu.addSeparator();
    
    windowMenu.add(windowCloseAll);
    windowCloseAll.setMnemonic(KeyEvent.VK_A);
    windowCloseAll.addActionListener(action);
    
    windowMenu.addSeparator();
    windowMenu.addSeparator();
        
    windowMenu.add(windowMore);
    windowMore.setMnemonic(KeyEvent.VK_M);
    windowMore.addActionListener(action);
    
    
    //help menu
    HelpMenu.setMnemonic(KeyEvent.VK_H);
    
    HelpMenu.add(HelpUserGuide);
    KeyStroke F1KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
    HelpUserGuide.setAccelerator(F1KeyStroke);    
    HelpUserGuide.setMnemonic(KeyEvent.VK_G);
    HelpUserGuide.addActionListener(action);

    HelpMenu.add(HelpLicense);
    HelpLicense.setMnemonic(KeyEvent.VK_L);
    HelpLicense.addActionListener(action);
   
    HelpMenu.addSeparator();

    HelpMenu.add(HelpAbout);
    HelpAbout.setMnemonic(KeyEvent.VK_L);
    HelpAbout.addActionListener(action);
    
    
    /**************** Settings Init  *******************/
    
          
    Properties pd = new Properties();
    
    try  {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(SETTING_FILE_NAME));
      pd.load(bis);
      bis.close();
      boolean load = Boolean.valueOf(pd.getProperty(AUTOLOAD_PROPERTY)).booleanValue();
     
      if(load)
       loadSettings(false);
    } catch(IOException e) { }//try..catch
    
    
    /**************** CTRL+F4 Work Around *******************/
    
   
    //replace CTRL+F4 action with out own to override the process
    ActionMap am = desktop.getActionMap();
    if (am != null) {
      Object[] o = am.keys();
      while (o == null) {
        am = am.getParent();
        o = am.keys();
      }//while
            
      for(int i = 0; i < o.length; i++) {
        if(o[i].toString().equals("close"))
          am.put(o[i], new MyCloseAction());
      }//for
    }//if
  
 }//RegXMakerGui()
 
 
  //make sure error stream is closed on close
  protected void finalize() throws Throwable {
    err.flush();
    err.close();
    file.flush();
    file.close();
  }//finalize()
  
  
  /*************************************
   *  BUTTON ENABLE HANDLING SECTION   *
   *************************************/
  
  
  //copy/cut/paste enable-disable
  protected void disableButtons() {
    editCut.setEnabled(false);
    cutButton.setEnabled(false);
      
    editCopy.setEnabled(false);
    copyButton.setEnabled(false);
      
    editPaste.setEnabled(false);
    pasteButton.setEnabled(false);
     
    editSelectAll.setEnabled(false);
      
    wordWrap.setEnabled(false);
  }//disableButtons()
  
  
  //list toolbar buttons and menu enable/disable
  private void updateListButtons() {
    boolean on = !fileList.isSelectionEmpty();
      
    previewButton.setEnabled(on);
    editButton.setEnabled(on);
    removeButton.setEnabled(on);
    removeAllButton.setEnabled(on);
      
    listPreview.setEnabled(on);
    listEdit.setEnabled(on);
    listRemove.setEnabled(on);
    listClear.setEnabled(on);
  }//updateListButtons()
  
  
  /***********************************
   *  INNER FRAME CREATION SECTION   *
   ***********************************/

  
  protected void createEditFrame(stringP s) {
    EditFrame editFrame = new EditFrame(s,this);
    editFrame.getTextArea().addFocusListener(new ButtonListener(true,  true,  true, true, true));
    editFrame.getTextArea().addMouseListener(rightClick);
        
    desktop.add(editFrame);

    editFrame.getTextArea().setLineWrap(isWordWrap);
    editFrame.show();
    editFrame.getTextArea().requestFocus();
  }//createEditFrame()
  
  
  protected void createInputFrame() {
    InputFrame inptFrame = new InputFrame(this);//new frame
    inptFrame.getTextArea().addFocusListener(new ButtonListener(true,  true,  true, true, true));
    inptFrame.getTextArea().addMouseListener(rightClick);
      
    desktop.add(inptFrame);//add the frame
     
    inptFrame.getTextArea().setLineWrap(isWordWrap);
    inptFrame.show();//make visible
    inptFrame.getTextArea().requestFocus();
  }//createInputFrame()
  
  
  protected void createOutputFrame(String answer) {
    JDialog wait = notifyWait();
  
    OutputFrame otptFrame = new OutputFrame(answer, this);
    otptFrame.getTextArea().addFocusListener(new ButtonListener(false,  true,  false, true, true));
    otptFrame.getTextArea().addMouseListener(rightClick);
          
    desktop.add(otptFrame);
    
    otptFrame.getTextArea().setLineWrap(isWordWrap);     
    otptFrame.show();
    otptFrame.getTextArea().requestFocus();
    
    wait.dispose();
  }//makeOutputFrame()
  
  
  protected void createPreviewFrame(stringP s) {
    PreviewFrame prevFrame = new PreviewFrame(s,this);
    prevFrame.getTextArea().addFocusListener(new ButtonListener(false,  true,  false, true, true));
    prevFrame.getTextArea().addMouseListener(rightClick);
        
    desktop.add(prevFrame);
        
    prevFrame.getTextArea().setLineWrap(isWordWrap);
    prevFrame.show();
    prevFrame.getTextArea().requestFocus();
  }//createPreviewFrame()
  
  
  protected void createProcessFrame() {
    ProcessFrame proc = new ProcessFrame(this, this.getInput(), this.getIgnoreWeight(), this.getCompileLevel());
    proc.getTextArea().addFocusListener(new ButtonListener(false,  true,  false, true, true));
    proc.getTextArea().addMouseListener(rightClick);
    
    desktop.add(proc);
      
    proc.getTextArea().setLineWrap(isWordWrap);
    proc.show();
    proc.getTextArea().requestFocus();
      
    proc.begin();
  }//createProcessFrame()
  
  
  private void loadHelp(String html) {
    HelpFrame userGuide = new HelpFrame(this, html);
    userGuide.getEditorPane().addFocusListener(new ButtonListener(false,  true,  false, true, false));
    
 	desktop.add(userGuide);
    
    userGuide.show();
    
    try  { userGuide.setMaximum(true); }
    catch (PropertyVetoException e)  { }
        
    userGuide.requestFocus();
  }//loadHelp()

  
  private void helpAbout()
  {
    AboutFrame about = new AboutFrame(this);
    about.addFocusListener(new ButtonListener(false,  false,  false, false, false));
    
    desktop.add(about);
  
    about.show();
    about.requestFocus();
  }//helpAbout()  
    
  
  /******************************
   *  WINDOW HANDLING SECTION   *
   ******************************/
  

  protected JMenuItem addFrameToList(String f) {
    JMenuItem ni = new JMenuItem(f);
    ni.addActionListener(action);
    
    if(windowCount < MAX_WIN_ITEMS)
      windowMenu.insert(ni, windowMenu.getMenuComponentCount() - 2);
    else 
      windowWait.add(ni);
    
    windowCount++;
    
    return ni;
  }//addFrameToList()
  
  
  protected void removeFrameFromList(JMenuItem i) {
    if(windowWait.contains(i))
      windowWait.remove(i);
    else {
      windowMenu.remove(i);
      
      if (windowWait.size() > 0) {
        JMenuItem addon = (JMenuItem) windowWait.removeFirst();
        windowMenu.insert(addon, windowMenu.getMenuComponentCount() - 2);
      }//if
    }//if..else
    
    windowCount--;
  }//removeFrameFromList();
  
  
  private void openMoreWindow() {
    Windows w = new Windows(this);
    w.show();
    w.requestFocus();
  }//openMoreWindow()
  
  
  private void tileWindows(boolean horizontal) {
    JInternalFrame[] frames = desktop.getAllFrames();
    
    if(frames.length <= 0)
      return;
    
    int wndWidth = desktop.getWidth();
    int wndHeight = desktop.getHeight();
    int cols = (int) (Math.ceil(Math.sqrt(frames.length)));
    int rows = (int) (Math.round(Math.sqrt(frames.length)));
    
    if (horizontal) {
      int t = cols;
      cols = rows;
      rows = t;
    }//if
    
    int frmWidth = wndWidth / cols;
    int frmHeight = wndHeight / rows;
    int proc = 0;
    
    for(int i = 0; i < frames.length; i++) {
      if (frames[i] instanceof MyFrame) {
        int cCol = proc % cols;
        int cRow = proc / cols;
      
        int posX = frmWidth * cCol;
        int posY = frmHeight * cRow;
        
        frames[i].setSize(frmWidth,frmHeight);
        frames[i].setLocation(posX, posY);
        
        try  {
          frames[i].setIcon(false);
        } catch(Exception e)  {  }
        
        frames[i].show();
        
        proc++;
      }//if
    }//for
  }//tileWindows()
  
  
  private void cascadeWindows() {
    JInternalFrame[] frames = desktop.getAllFrames();
    
    int wndWidth = desktop.getWidth();
    int wndHeight = desktop.getHeight();
    int frmWidth =  (int) (wndWidth/MyFrame.PHI);
    int frmHeight = (int) (frmWidth/MyFrame.PHI)-44;
    int xOffset= (int) ((wndWidth/2)-(frmWidth/2));
    int yOffset= (int) ((wndHeight/2)-(frmHeight/2));
        
    for(int i = 0; i < frames.length; i++) {
      if (frames[i] instanceof MyFrame) {
        int posX = xOffset + i * 30;
        int posY = yOffset + i * 30;
        
        while (posX + frmWidth > wndWidth)
          posX -= wndWidth - frmWidth;
              
        while (posY + frmHeight > wndHeight)
          posY -= wndHeight - frmHeight;
            
        frames[i].setSize(frmWidth,frmHeight);
        frames[i].setLocation(posX, posY);
        
        try  {
          frames[i].setIcon(false);
        } catch(Exception e)  {  }
        
        frames[i].show();
      }//if
    }//for
  }//cascadeWindows()
  
  
  private void closeAllWindows() {
    JInternalFrame[] frames = desktop.getAllFrames();
    
    for(int i = 0; i < frames.length; i++) {
      if (frames[i] instanceof MyFrame) {
        try  {
          frames[i].setIcon(false);
        } catch(Exception e)  {  }
        
        frames[i].show();
              
        ((MyFrame) frames[i]).processClose();
      }//if
    }//for
  }//closeAllWindows()
  
  
  private JTextArea getTopTextArea()
  {
    JInternalFrame s = desktop.getSelectedFrame();
    
    if(s == null)
      return null;
    
    if(s instanceof EditFrame)
      return ((EditFrame) s).getTextArea();
    else if(s instanceof InputFrame)
      return ((InputFrame) s).getTextArea();
    else if(s instanceof OutputFrame)
      return ((OutputFrame) s).getTextArea();
    else if(s instanceof PreviewFrame)
      return ((PreviewFrame) s).getTextArea();
    else if(s instanceof ProcessFrame)
      return ((ProcessFrame) s).getTextArea();
    else
      return null;
  }//getTopTextArea()
  
  
  private void closeOtherPreviews(stringP s)
  {
    JInternalFrame[] frames = desktop.getAllFrames();
    for (int i=0; i<frames.length; i++)
    {
      try
      {   
        PreviewFrame closeMe = (PreviewFrame) frames[i];
        if (s.equals(closeMe.getStringP())) {
          prevStrings.remove(s);
          closeMe.stdClose();
        }//if
       
	  } catch (Exception c){}
    }//for
  }//closeOtherPreviews()
  
  
  /**************************
   *  MISC UPDATE SECTION   *
   **************************/  
  
  
  private void updateWordWrapFlag() {
    JTextArea textarea = getTopTextArea();
    
    if (textarea!=null) {
      isWordWrap = textarea.getLineWrap();
      wordWrap.setState(isWordWrap);
    }//if
  }//updateWordWrapFlag()
  
  
  private void updateToolTips() {
    if (outputFile == null)  {
      outputFileButton.setToolTipText("Select output file");
      outputFileClearButton.setToolTipText("Clear output file setting");
      processButton.setToolTipText("Process the items on the list");
    } else {
      outputFileButton.setToolTipText("Select new output file. Current file: " + outputFile);
      outputFileClearButton.setToolTipText("Clear output file setting. Current file: " + outputFile);
      processButton.setToolTipText("Process the items on the list and record to " + outputFile);
    }//if..else
  }//updateToolTips()
  
  
  private JDialog notifyWait() {
    JDialog wait = new JDialog(this, "Processing Data... Please Wait", false);
    wait.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    wait.setResizable(false);
    
	int frmWidth = 250;
	int frmHeight = 0;
	int xOffset= (int) ((getWidth() /  2) - (frmWidth / 2));
	int yOffset= (int) ((getHeight() / 2) - (frmHeight / 2));
            
    wait.setSize(frmWidth,frmHeight);
    wait.setLocation(getX() + xOffset, getY() + yOffset);
    
    wait.show();
    
    return wait;
  }//notifyWait()
  
  
  /**********************************
   *  MAIN BUTTON ACTIONS SECTION   *
   **********************************/  
  
  
  private void edit()
  {
    if(fileList.isSelectionEmpty())
      return;
  
    //get the indeces
    int sel [] = fileList.getSelectedIndices();
 	
    for (int i=0; i<sel.length; i++)
    {
      stringP s= (stringP) fileListModel.elementAt(sel[i]);//retrieve object from the vector
      closeOtherPreviews(s);       

      if (!getEditStrings().contains(s))
        createEditFrame(s);
      else
      {
        JInternalFrame[] frames = desktop.getAllFrames();
       
        for (int j=0; j<frames.length; j++)
        {
          try
          { 
            EditFrame closeMe = (EditFrame) frames[j];
            if (s.equals(closeMe.getStringP())) {
              closeMe.setIcon(false);
              closeMe.show();
              closeMe.getTextArea().requestFocus();
            }//if
          } catch (Exception c){}
        }//for
      }//if..else
    }//for
  }//edit()
  
  
  //this is the really really important method... does all the work
  private void processButtonAction()
  {
  
    String [] strings = this.getInput();
    if (strings.length > 0)//if there's input...
    {
      JInternalFrame[] frames = desktop.getAllFrames();
      JInternalFrame found = null;
      
      for(int i = 0; i < frames.length && found == null; i++) {
        if (frames[i] instanceof ProcessFrame)
          found = frames[i];
      }//for
      
      if (found != null) {
        try  {
          found.setIcon(false);
        } catch(Exception e)  { }
        found.show();
        
        ((ProcessFrame) found).getTextArea().requestFocus();
      } else
        createProcessFrame();
    }
    else //if there's no input, give an error
    	JOptionPane.showMessageDialog(desktop, "Nothing to process\n Please use \"Add File\", or \"Add String\"", 
    	"Error", JOptionPane.ERROR_MESSAGE);
  }//processButtonAction()
  
  
  //add the user string   
  private void addString()
  {
    JInternalFrame[] frames = desktop.getAllFrames();
    JInternalFrame found = null;
      
    for(int i = 0; i < frames.length && found == null; i++) {
      if (frames[i] instanceof InputFrame) {
        if(((InputFrame) frames[i]).getTextArea().getText().length() == 0)
          found = frames[i];
      }//if
    }//for
      
    if (found != null) {
      try  {
        found.setIcon(false);
      } catch(Exception e)  { }
      found.show();
        
      ((InputFrame) found).getTextArea().requestFocus();
    } else
     createInputFrame();
  	      
  }//addStringButtonAction()
  
  
  private void preview()
  {
    if(fileList.isSelectionEmpty())
      return;
  
    //get the indeces
    int sel [] = fileList.getSelectedIndices();
 	
    for (int i=0; i<sel.length; i++)
    {
      stringP s = (stringP) fileListModel.elementAt(sel[i]);//retrieve object from the vector
       
      if (!getEditStrings().contains(s) && !getPrevStrings().contains(s))
        createPreviewFrame(s);
      else
      {
        JInternalFrame[] frames = desktop.getAllFrames();
        for (int j=0; j<frames.length; j++)
        {
          try
          { 
            if (getEditStrings().contains(s)) {
              EditFrame closeMe = (EditFrame) frames[j];
               
              if (s.equals(closeMe.getStringP())) {
                JOptionPane.showMessageDialog(closeMe, "Please close the edit window", "Cannot display preview", JOptionPane.ERROR_MESSAGE);
        
                closeMe.setIcon(false);
                closeMe.show();;
                closeMe.getTextArea().requestFocus();
                
              }//if
            }
            else
            {
               PreviewFrame closeMe = (PreviewFrame) frames[j];
               if (s.equals(closeMe.getStringP())) {
                 closeMe.setIcon(false);
                 closeMe.show();
                 closeMe.getTextArea().requestFocus();
               }//if
            }//if..else
          } catch (Exception c){}
        }//for
      }//if..else
    }//for
  }//preview()

  
  private void openUrl()
  {
    String url = "";
    JDialog wait = null;
      
    try
    {
      url = JOptionPane.showInputDialog(this, "Please enter a valid url (e.g. http://www.solidmatrix.com):", "Add Contents of URL", JOptionPane.QUESTION_MESSAGE);
        
      if (url != null) {
        URL u = new URL(url);
          
        int r = JOptionPane.showConfirmDialog(this, "You are about to read a file from a URL.\nThis may take several minutes depending on the\nsize of the file and your connection speed.\nThis process may not work if you are not connected\nto the web or if you are behind a firewall.\nDo you want to proceed?", "Read from URL", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
          wait = notifyWait();
          
          String content = ConsoleUtils.readFull(u.openStream(), encodingScheme);
          
	   //create and instance of the stringP and add it to the preview list
	   //since this is the file, put "true"
          fileListModel.addElement(new stringP(content, url, true));
        }//if
      }//if
    } catch(MalformedInputException e)  {
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Input is not of the format specified: " + encodingScheme, "Error", JOptionPane.ERROR_MESSAGE);
    } catch (MalformedURLException e) {
      JOptionPane.showMessageDialog(desktop, "The URL you entered is not valid", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(desktop, "The URL you specified was not found.\nPlease make sure you entered it correctly and try again.\n(" + url + ")", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Throwable e)//this really shouldn't happen
	{
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Error Reading From URL", "Error", JOptionPane.ERROR_MESSAGE);
    }//catch
    
    if(wait != null)
      wait.dispose();
  }//openUrl()
  
  
  private void openFile() {
    JDialog wait = null;
  
    try
    {
      fileOpenBox.setVisible(true);//show me the box
      if (fileOpenBox.getFile()!=null)//if user cancels, the file will be null
      {
        
        //fileNL stores name and location of the file that user selected
        String fileNL = fileOpenBox.getDirectory()+fileOpenBox.getFile();
          
        wait = notifyWait();
          
        String content = ConsoleUtils.readFullFile(fileNL, encodingScheme);
         
        //create and instance of the stringP and add it to the preview list
	    //since this is the file, put "true"
          
        fileListModel.addElement(new stringP(content, fileNL));
    }//if
    } catch(MalformedInputException e)  {
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Input is not of the format specified: " + encodingScheme, "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Throwable e)//this really shouldn't happen
	{
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Error Opening File", "Error", JOptionPane.ERROR_MESSAGE);
    }//catch
        
    if(wait != null)
      wait.dispose();
  }//openFile()
  
  
  //remove an item from a list   
  private void removeButtonAction()
  {
    if(fileList.isSelectionEmpty())
      return;
  
    int selected[] = fileList.getSelectedIndices();
	 
    //this is a vector, so we delete frim the back not to mess up the indeces
    for (int i=selected.length-1; i >= 0; --i) {
      stringP s= (stringP) fileListModel.elementAt(selected[i]);
      closeOtherPreviews(s);
                   
      fileListModel.removeElementAt(selected[i]);
            
      if (fileListModel.size() > 0) {
        if(selected[i] == fileListModel.size())
          fileList.setSelectedIndex(selected[i] - 1);
        else
          fileList.setSelectedIndex(selected[i]);
      }//if

      if (editStrings.contains(s))
      {
        editStrings.remove(s);
              
        JInternalFrame[] frames = desktop.getAllFrames();
        for (int j=0; j<frames.length; j++)
        {
          try   
          { 
            EditFrame closeMe = (EditFrame) frames[j];
            if (s.equals(closeMe.getStringP()))
              closeMe.stdClose();
          } catch (Exception c){}
        }//for
      }//if
    }//for
    
    updateListButtons();
    
    fileList.requestFocus();
  }//removeButtonAction()

 
  //clear list
  private void removeAllButtonAction() 
  {
    fileList.clearSelection();
    fileList.setSelectedIndices(getAllListIndeces());
   
    removeButtonAction();
  }//removeAllButtonAction()
  
  
  //get the output file name/location from the user 
  private void outputFileButtonAction()
  {
    fileSaveBox.setVisible(true);
    
    //if user cancels, the file will be null
    if (fileSaveBox.getFile()!=null)
    	outputFile = fileSaveBox.getDirectory()+fileSaveBox.getFile();
    
    updateToolTips();
  }//outputFileButtonAction()
  
  
  private void outputFileClearButtonAction() {
    outputFile = null;
    updateToolTips();
    
    JOptionPane.showMessageDialog(desktop, "Output file setting has been cleared.", "Cleared", JOptionPane.INFORMATION_MESSAGE);
  }//outputFileClearButtonAction()
  
  
  /********************************
   *  EDIT MENU ACTIONS SECTION   *
   ********************************/  
  
  
  private void wordWrapAction()
  {
    isWordWrap=wordWrap.getState();
    JTextArea textarea = getTopTextArea();
    
    if (textarea!=null)
    {
      JDialog wait = notifyWait();
    
      textarea.setLineWrap(isWordWrap);
      textarea.requestFocus();
      
      wait.dispose();
    }//if
  }//wordWrapAction()

   
  private void editCut()
  {
    JTextArea textarea = getTopTextArea();
    
    if (textarea!=null) {
      if(textarea.isEditable())
        textarea.cut();
    }//if
  }//editCutAction()
 

  private void editCopy()
  {
    JTextArea textarea = getTopTextArea();
    if (textarea!=null) {
      textarea.copy();
      textarea.requestFocus();
    }//if
  }//editCopyAction()

  
  private void editPaste()
  {
    JTextArea textarea = getTopTextArea();
    
    if (textarea!=null) {
      if (textarea.isEditable()) {
       	textarea.paste();
        textarea.requestFocus();
      }//if
    }//if
  }//editPasteAction
  

  private void editSelectAllAction()
  {
 	//the logic is:
    //if it's a text-containing frame, there should be a textarea
    //if the top window doens't contain a textarea, there will be an error,
    //and null is returned... which means it's a list
 	
    JTextArea textarea = getTopTextArea();
 	if (textarea!=null) {
 		textarea.selectAll();
        textarea.requestFocus();
 	} else {
    	fileList.setSelectedIndices(getAllListIndeces());
        fileList.requestFocus();
 	}//if..else
  }//editSelectAllAction()
  
    
  /*************************************
   *  CUSTOMIZE MENU ACTIONS SECTION   *
   *************************************/  
  

  private void toolBarAction() {  
    buttonFrameCont.setVisible(toolBarEnable.getState());
        
    try  {
      BasicToolBarUI ui = (BasicToolBarUI) buttonFrameCont.getUI();
          
      if(ui.isFloating())
        ui.setFloating(false, null);
    }catch(Exception e)  { e.printStackTrace(err); }
  }//toolBarAction()
  
  
  private void listToolBarAction() {
    listButtons.setVisible(listToolBarEnable.getState());
      
    try  {
      BasicToolBarUI ui = (BasicToolBarUI) listButtons.getUI();
          
      if(ui.isFloating())
        ui.setFloating(false, null);
    }catch(Exception e)  { e.printStackTrace(err); }
  }//listToolBarAction()
  
  
  private void outlineDragAction() {
    if(outlineDrag.getState())
      desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    else
      desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
  }//outlineDragAction()
  
  
  private void setTimeoutValue() {
    String now = "";
    if(timeout != DEFAULT_TIMEOUT)
      now = ("\nCurrent setting is " +  (timeout / 1000) + " seconds.");
  
    String n = JOptionPane.showInputDialog(desktop, "Please enter the number of seconds that can pass\nbefore processing times out." + now + "\n(default is 600 seconds or 10 minutes)", "Timeout Setting", JOptionPane.QUESTION_MESSAGE);
    
    if(n == null || n.length() <= 0)
      return;
    
    try {
      long nto = Long.parseLong(n);
      timeout = 1000 * nto;
      
      JOptionPane.showMessageDialog(desktop, "Process timout set to " + nto + " seconds.", "Timeout Setting", JOptionPane.INFORMATION_MESSAGE);
    } catch(NumberFormatException e) {
      JOptionPane.showMessageDialog(desktop, "The value you entered is not valid,\nplease try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }//try..catch
  }//setTimeoutValue()
  
  
  /******************************
   *  MISC PROCESSING SECTION   *
   ******************************/  
  
    
  private int[] getAllListIndeces()
  {
    int listsize = fileListModel.size();
    int [] indeces = new int [listsize];
    for (int i=0; i<listsize; i++) 
    	indeces[i]=i;
    
    return indeces;
  }//getAllListIndeces()
  
  
  public String fourChar(String in) {
    while(in.length() < 32)
      in = "0" + in;
  
    StringBuffer out = new StringBuffer(in.length() + (in.length() / 4));
    
    char[] chars = in.toCharArray();
    out.append(chars[0]);
    for(int i = 1; i < chars.length; i++) {
      if(i % 4 == 0)
        out.append("-");
        
      out.append(chars[i]);
    }//for
    
    return out.toString();
  }//fourChar()
  
  
  //these set the options... no point in having them though   
  public int getIgnoreWeight(){return ignoreWeight;}
  public long getTimeoutPeriod()  { return timeout;  }
  public int getCompileLevel() {return  compileLevel; }
  public String outputFile(){return outputFile;}
  public String getEncodingScheme(){return encodingScheme;}
  public DefaultListModel getListModel(){return fileListModel;}
  public JDesktopPane getDesktopPane(){return desktop;}
  public JList getFileList(){return fileList;}
  public Vector getPrevStrings(){return prevStrings;}
  public Vector getEditStrings(){return editStrings;}
  public PrintStream getErrorWriter() { return err; }
   
  
  public String [] getInput()//this, on the other hand, is useful
  {
    //the ans array contains all items that are in the list
    //get the size of the list, and set that to be the list's size
    String [] ans = new String [fileListModel.getSize()];
    
    //copy all elements
    for (int i=0; i<ans.length; i++)
    {
      //get an object from the list, cast it into stringP
      stringP temp = (stringP) fileListModel.get(i);
      
      //get contents of that object and save it to array
      ans[i]= temp.getContents();
    }//for
    
    return ans;//return the answer
  }//getInput()
	
 
  protected void writeFile(String file, String s, String enc)
  {
    JDialog wait = null;
    
    try
    {
      wait = notifyWait();
    
      PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), enc)));
      out.print(s);
      out.flush();
      out.close();
    } catch (Throwable e) {
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Error Writing Output File: " + file , "Error", JOptionPane.ERROR_MESSAGE);
    }//try/catch
    
    if(wait != null)
      wait.dispose();
  }//writeFile method
  
  
  /***********************************
   *  SETTINGS MAINTANANCE SECTION   *
   ***********************************/  
  
  
  private void saveSettings(boolean message) {
    Properties p = new Properties();
    
    p.setProperty(AUTOSAVE_PROPERTY, String.valueOf(autosave));
    p.setProperty(AUTOLOAD_PROPERTY, String.valueOf(autoload));
    p.setProperty(CMP_LEVEL_PROPERTY, String.valueOf(compileLevel));
    p.setProperty(ACC_LEVEL_PROPERTY, String.valueOf(ignoreWeight));
    p.setProperty(FORMAT_PROPERTY, encodingScheme);
    p.setProperty(TOOLBAR_PROPERTY, String.valueOf(toolBarEnable.getState()));
    p.setProperty(L_TOOLBAR_PROPERTY, String.valueOf(listToolBarEnable.getState()));
    p.setProperty(DRAG_MODE, String.valueOf(outlineDrag.getState()));
    p.setProperty(TIMEOUT_PROPERTY, String.valueOf(timeout));
    
    try  {
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(SETTING_FILE_NAME));
      
      p.store(bos, VER_STRING + " Settings File");
      
      bos.flush();
      bos.close();
      
      if (message)
        JOptionPane.showMessageDialog(desktop, "Saved settings to file" , "Saved", JOptionPane.INFORMATION_MESSAGE);
        
    } catch(IOException e) {
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Error has occured while saving settings" , "Error", JOptionPane.ERROR_MESSAGE);
    }//try..catch
  }//saveSettings()
  
  
  private void loadSettings(boolean message) {
    if (message) {
      int x = JOptionPane.showConfirmDialog(desktop, "Loading settings from file will overide current\nsettings of the program.\nDo you want to proceed?", "Load Settings?", JOptionPane.YES_NO_OPTION);
    
      if(x != JOptionPane.YES_OPTION)
        return;
    }//if
  
    Properties p = new Properties();
    
    try  {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(SETTING_FILE_NAME));
      
      p.load(bis);
      
      bis.close();
    } catch(IOException e) {
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Error has occured while loaded settings" , "Error", JOptionPane.ERROR_MESSAGE);
    }//try..catch
  
    try {
      autosave = Boolean.valueOf(p.getProperty(AUTOSAVE_PROPERTY)).booleanValue();
      autoload = Boolean.valueOf(p.getProperty(AUTOLOAD_PROPERTY)).booleanValue();
      compileLevel = Integer.parseInt(p.getProperty(CMP_LEVEL_PROPERTY));
      ignoreWeight = Integer.parseInt(p.getProperty(ACC_LEVEL_PROPERTY));
      encodingScheme = p.getProperty(FORMAT_PROPERTY);
      boolean toolBarE = Boolean.valueOf(p.getProperty(TOOLBAR_PROPERTY)).booleanValue();
      boolean listToolBarE = Boolean.valueOf(p.getProperty(L_TOOLBAR_PROPERTY)).booleanValue();
      boolean outlineDragE = Boolean.valueOf(p.getProperty(DRAG_MODE)).booleanValue();
      timeout = Long.parseLong(p.getProperty(TIMEOUT_PROPERTY));

      //save/load options
      autosaveBox.setState(autosave);
      autoloadBox.setState(autoload);
    
    
      //compile level
      switch (compileLevel) {
        case RegExCompiler.DEEP_COMPILE:
          deepCompile.setSelected(true);
          break;
        
        case RegExCompiler.SEMI_DEEP_COMPILE:
          deepSemiCompile.setSelected(true);
          break;
        
        case RegExCompiler.NO_COMPILE:
          noCompile.setSelected(true);
          break;
        
        default:
          surfaceCompile.setSelected(true);
      }//switch
        
    
      //accuracy level options
      switch (ignoreWeight) {
        case Constants.WORD_ACCURACY:
          wrdAccuracy.setSelected(true);
          break;
        
        case Constants.LINE_ACCURACY:
          lneAccuracy.setSelected(true);
          break;
        
       default:
         ltrAccuracy.setSelected(true);
      }//switch
    
      //encoding scheme option    
      if(encodingScheme == null || encodingScheme.length() <= 0)
        encodingScheme = Constants.OPTION_1_SCHEME;
      
      if (encodingScheme.equals(Constants.OPTION_2_SCHEME))
        t2Frmt.setSelected(true);
      else if (encodingScheme.equals(Constants.OPTION_3_SCHEME))
        t3Frmt.setSelected(true);
      else if (encodingScheme.equals(Constants.OPTION_4_SCHEME))
        t4Frmt.setSelected(true);
      else if (encodingScheme.equals(Constants.OPTION_5_SCHEME))
        t5Frmt.setSelected(true);
      else if (encodingScheme.equals(Constants.OPTION_6_SCHEME))
        t6Frmt.setSelected(true);
      else
        t1Frmt.setSelected(true);
      
      //dragging
      if(outlineDragE)
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
      else
        desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
      
      //misc options
      toolBarEnable.setState(toolBarE);
      listToolBarEnable.setState(listToolBarE);
      outlineDrag.setState(outlineDragE);
    
      toolBarAction();
      listToolBarAction();
    
      if(message)
        JOptionPane.showMessageDialog(desktop, "Restored settings from file" , "Restore", JOptionPane.INFORMATION_MESSAGE);
    } catch(Exception e)  {
      e.printStackTrace(err);
      JOptionPane.showMessageDialog(desktop, "Error has reading setting file, \nit may be corrupt or from a newer version." , "Error", JOptionPane.ERROR_MESSAGE);
    }//try..catch
  }//loadSettings()
  
  
  /***********************************
   *  INNER CLASS LISTENER SECTION   *
   ***********************************/
  
  
  //main action delegator 
  private class actions implements ActionListener, MenuListener
  {
    public void menuDeselected(MenuEvent e) { }
    public void menuCanceled(MenuEvent e) { }
    public void menuSelected(MenuEvent e) {
      //find which component called it
      Object object = e.getSource();
    
      if (object == optionsMenu)
        optionsPop.show(optionsMenu,0,optionsMenu.getHeight());
      else if(object == editMenu && editPop.getInvoker() != editMenu) {
        editPop.show(editMenu, 0, editMenu.getHeight());
        editPop.setInvoker(getTopTextArea());
      } else if(object == listMenu && listPop.getInvoker() != listMenu) {
        listPop.show(listMenu, 0, listMenu.getHeight());
        listPop.setInvoker(fileList);
      } else if(object == windowMenu && windowPop.getInvoker() != windowMenu) {
        windowPop.show(windowMenu, 0, windowMenu.getHeight());
        windowPop.setInvoker(desktop);
      }//if..else
    }
  
    public void actionPerformed(ActionEvent event)
    {
        
      //find which component called it
      Object object = event.getSource();
       

      //menu
      if (object == fileExit) {
        if(autosave)
            saveSettings(false);
            
        System.exit(0); // terminate execution
      } else if (object == fileOpen || object == addFileButton)
        openFile();   
      else if (object == fileUrl || object == addUrlButton)
        openUrl();   
      else if (object == fileString || object == addStringButton)
        addString();
      else if (object == fileOut || object == outputFileButton)
        outputFileButtonAction();
      else if (object == fileOutClear || object == outputFileClearButton)
        outputFileClearButtonAction();
      else if (object == fileProcess || object == processButton)
        processButtonAction();
                 

      //list
      else if (object == listPreview || object == previewButton)
        preview();
      else if (object == listEdit || object == editButton)
        edit();
      else if (object == listRemove || object == removeButton)
        removeButtonAction();       
      else if (object == listClear || object == removeAllButton)
        removeAllButtonAction();


      //copy/cut/paste
      else if (object == editCut || object == cutButton)
        editCut();
      else if (object == editCopy || object == copyButton)
        editCopy();
      else if (object == editPaste || object == pasteButton)
        editPaste();       
      else if (object == editSelectAll)
        editSelectAllAction();
      else if (object == wordWrap)
        wordWrapAction();
        
      
      //buttons          
      else if (object == optionsButton)
        optionsPop.show(optionsButton,0,optionsButton.getHeight());

      
      //options
      
      else if (object == deepCompile)
        compileLevel = RegExCompiler.DEEP_COMPILE;
      else if (object == deepSemiCompile)
        compileLevel = RegExCompiler.SEMI_DEEP_COMPILE;
      else if (object == surfaceCompile)
        compileLevel = RegExCompiler.SURFACE_COMPILE;
      else if (object == noCompile)
        compileLevel = RegExCompiler.NO_COMPILE;
      else if (object == ltrAccuracy)
        ignoreWeight = Constants.LETTER_ACCURACY;
      else if (object == wrdAccuracy)
        ignoreWeight = Constants.WORD_ACCURACY;
      else if (object == lneAccuracy)
        ignoreWeight = Constants.LINE_ACCURACY;
      else if (object == t1Frmt) 
        encodingScheme = Constants.OPTION_1_SCHEME;
      else if (object == t2Frmt)
        encodingScheme = Constants.OPTION_2_SCHEME;
      else if (object == t3Frmt)
        encodingScheme = Constants.OPTION_3_SCHEME;
      else if (object == t4Frmt)
        encodingScheme = Constants.OPTION_4_SCHEME;
      else if (object == t5Frmt)
        encodingScheme = Constants.OPTION_5_SCHEME;
      else if (object == t6Frmt)
        encodingScheme = Constants.OPTION_6_SCHEME;

      //end of options
       
      
      //window
      
      else if (object == windowTileV)
        tileWindows(false);
      else if (object == windowTileH)
        tileWindows(true);
      else if (object == windowCascade)
        cascadeWindows();
      else if (object == windowCloseAll)
        closeAllWindows();
      else if (object == windowMore)
        openMoreWindow();
        
      //end of window
      
      
      //help
       
      else if (object == HelpUserGuide)
        loadHelp("userguide.html");
      else if (object == HelpLicense)
        loadHelp("license.html");
      else if (object == HelpAbout)
        helpAbout();

      //end of help
      
      
      //customize
      
      else if (object == toolBarEnable)
        toolBarAction();
      else if (object == listToolBarEnable)
        listToolBarAction();
      else if (object == outlineDrag)
        outlineDragAction();
      else if (object == setTimeout)
        setTimeoutValue();
      else if (object == autosaveBox)
        autosave = autosaveBox.getState();
      else if (object == autoloadBox)
        autoload = autoloadBox.getState();
      else if (object == saveItem)
        saveSettings(true);
      else if (object == loadItem)
        loadSettings(true);
        
      //end of help
      
      //if nothing else it must be open window selections
      else {
        JInternalFrame[] frames = desktop.getAllFrames();
    
        for(int i = 0; i < frames.length; i++) {
          if (frames[i] instanceof MyFrame) {
            if (object == ((MyFrame) frames[i]).getMyWindowItem()) {
              try  { frames[i].setIcon(false); } catch(Exception e){}
        
              frames[i].show();
            }//if
          }//if
        }//for
      }//if..else if..else
      
    }//actionPerformed()
  }//actions
  
  
    
  private class ButtonListener extends FocusAdapter {
    private boolean  enableCut, enableCopy, enablePaste,  
                     enableSelect, enableWrap;
  
  
    public ButtonListener(boolean ec, boolean eco, boolean ep, boolean es, boolean ew) {
      enableCut = ec;
      enableCopy = eco;
      enablePaste = ep;
      enableSelect = es;
      enableWrap = ew;
    }//ButtonListener()
  
  
    public void focusGained(FocusEvent f)  {
      editCut.setEnabled(enableCut);
      cutButton.setEnabled(enableCut);
      
      editCopy.setEnabled(enableCopy);
      copyButton.setEnabled(enableCopy);
      
      editPaste.setEnabled(enablePaste);
      pasteButton.setEnabled(enablePaste);
      
      editSelectAll.setEnabled(enableSelect);
      
      wordWrap.setEnabled(enableWrap);
      
      updateWordWrapFlag();
    }//focusGained()
    
    public void focusLost(FocusEvent e) {
      Object source = e.getComponent();
      
      if (source instanceof JTextArea) {
        JTextArea cur = (JTextArea) source;
        cur.getCaret().setSelectionVisible(true);
      } else if (source instanceof JEditorPane) {
        JEditorPane cur = (JEditorPane) source;
        cur.getCaret().setSelectionVisible(true);
      }//if..else if
    }//focusLost()
  }//ButtonListener()
  
  
  //listener for various list actions
  private class ListListener implements MouseListener, KeyListener{
    public void mouseEntered(MouseEvent e) {  }
    public void mouseExited(MouseEvent e) {  }
    public void mousePressed(MouseEvent e) {  }
    public void mouseReleased(MouseEvent e) {  }
  
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) 
        preview();
        
      updateListButtons();
    }//mouse double click
        
    public void keyPressed(KeyEvent e) {  }
    public void keyReleased(KeyEvent e) {  }
        
    public void keyTyped(KeyEvent e) {
      if(e.getKeyChar() == KeyEvent.VK_ENTER)
        preview();
    }//enter pressed
  }//ListListener
  
  
  //listener to listen for event when to pop up menus
  private class RightClickListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      Object source = e.getSource();
      
      if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        return;
      
      if(source instanceof JTextArea) {
        editPop.show((JTextArea) source, e.getX(), e.getY());
        editPop.setInvoker(editMenu);
      } else if (source == fileList) {
        listPop.show(fileList, e.getX(), e.getY());
        listPop.setInvoker(listMenu);
      } else if (source == desktop) {
        windowPop.show(desktop, e.getX(), e.getY());
        windowPop.setInvoker(windowMenu);
      }//if..else if
    }//mousePressed()
  }//RightClickListener
  

  //class to use for closing internal frames via CTRL+F4 key stroke
  private class MyCloseAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
	  JInternalFrame frame = desktop.getSelectedFrame();
      if(frame instanceof MyFrame)
        ((MyFrame) frame).processClose();
	}//actionPerformed()
    
	public boolean isEnabled() { return true; }
  }//MyCloseAction()
}//class RegXMakerGui