package com.solidmatrix.regxmaker.ui;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;

import com.solidmatrix.regxmaker.util.shared.*;
import com.solidmatrix.regxmaker.util.shared.Constants;
import com.solidmatrix.regxmaker.util.shared.Monitor;
import com.solidmatrix.regxmaker.util.superdiff.RegExCompiler;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.ui.RegExCompilerCmd
 * Project: RegXmaker Command Line utility
 * Version: 1.1
 * Tier   : 1 (User Interface)
 * Author : Konstantin Drondin
 * Purpose: User interface to the regular expression compiler
 *
 * Copyright (C) 2001, 2004 SolidMatrix Technologies, Inc.
 * This file is part of RegXmaker Command Line utility.
 *
 * RegXmaker Command Line Utility is is free software; you can redistribute it and/or modify
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
 * Comments: Full
 *
 * Modification History
 *
 * 05-21-2001  KD Created
 *
 * 05-23-2001  GS Adjusted for packages. Improved memory usage.
 *                Added accuracy and encoding options.
 *                NOTE: File in pre-release format, secure before 
 *                production version is compiled.
 *
 * 07-05-2001  GS Added ability to set timeout to the processing
 *                and to load settings from file, both are untested
 *                in this program.
 *
 * 07-05-2004  YS Added licensing information
 * </PRE>
 */

public class RegXMakerCmd implements RegXMakerConstants {
  public static final float VERSION = 1.1f;
  public static final String VER_STRING = "RegXMaker Command Line Utility v1.1";


	public static void main (String [] args)
	{
        System.out.println("REGXMAKER Command Line Utility Version " + VERSION);
        System.out.println("Copyright © 2001, 2004 SolidMatrix Technologies, Inc.");
		System.out.println("Command line and GUI utilities licensed under the GPL, library licensed under the LGPL.");
        System.out.println();
        
    	options o = new options(args);//create an instance of options
//        o.whatsWrong();//use this to see what's inside

        if (o.loadSettings()) {
          Properties p = new Properties();
    
          try  {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(SETTING_FILE_NAME));
      
            p.load(bis);
      
            bis.close();
          } catch(IOException e) {
            System.out.println("Error has occured while loaded settings from file.");
            System.out.println("Using given settings.");
          }//try..catch
    
          try {
            int compileLevel = Integer.parseInt(p.getProperty(CMP_LEVEL_PROPERTY));
            int ignoreWeight = Integer.parseInt(p.getProperty(ACC_LEVEL_PROPERTY));
            String encodingScheme = p.getProperty(FORMAT_PROPERTY);
            long timeout = Long.parseLong(p.getProperty(TIMEOUT_PROPERTY));

            //compile level
            switch (compileLevel) {
              case RegExCompiler.DEEP_COMPILE:
              case RegExCompiler.SEMI_DEEP_COMPILE:
              case RegExCompiler.SURFACE_COMPILE:
              case RegExCompiler.NO_COMPILE:
                break;
        
              default:
                compileLevel = RegExCompiler.SURFACE_COMPILE;
            }//switch
        
            if(encodingScheme == null || encodingScheme.length() <= 0)
              encodingScheme = Constants.OPTION_1_SCHEME;
              
            o.setCompileLevel(compileLevel);
            o.setIgnoreWeight(ignoreWeight);
            o.setEncodingScheme(encodingScheme);
            o.setTimeout(timeout);

            System.out.println("Restored settings from file");
          } catch(Exception e)  {
            System.out.println("Error has reading setting file, it may be corrupt or from a newer version.");
            System.out.println("Using given settings.");
          }//try..catch
        }//if
        
        if (o.isValid()) {
          String [] strings = o.getInput();//checking if the strings are right
        
          RegXMakerCmd mc = new RegXMakerCmd(o);
          mc.begin();
        }
	}    
    
    private static void writeFile(String file, String s, String enc)
    {
	    try
	    {
	    	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), enc)));
	        out.print(s);
            out.flush();
	        out.close();
	    } catch (IOException e) 
	    {
            e.printStackTrace();
	    	System.out.println("Error writing to output file");//tell me what to write?
	    }
    	
    }
    
    private boolean done;
    private options o;
    private Process p;
    
    public RegXMakerCmd(options oi) {
      o = oi;
      done = true;
    }//RegXMakerCmd()
    
    
    private void begin() {
      //FIXME: Add actualy security 
        
      done = false;
   
      p = new Process();
      ProcessTimeout pt = new ProcessTimeout();
    
      pt.start();
      p.start();      
    }//begin()
    
    
    private void done(String answer) {
      if(done)
        return;
      
      done = true;
    
      boolean userWant2C = false;
        
      if (o.getOutputFile()!=null) {
        writeFile(o.getOutputFile(), answer, o.getEncodingScheme());
            
        System.out.println();
        String ua = ConsoleUtils.prompt("Result written to output file. Its size is " + answer.length() + " characters.\nDisplay result to screen (Y/N)? ");
            
        if(ua.charAt(0) == 'Y' || ua.charAt(0) == 'y')
          userWant2C = true;
      }//if
          
      if (o.getOutputFile() == null || userWant2C) {
        System.out.println("Resulting regular expression is:");
        System.out.println(answer);
      }//if
    }//done()
  
  private void error(Throwable e) {
    if(done)
      return;
  
    System.out.println("An Error Has Occured While Processing");
    //e.printStackTrace();
    
    done = true;
  }//error()
  
  private void timeout() {
    if(done)
      return;
      
    System.out.println("Operation timed out.");
    
    p.stopProcessing();
    p = null;
    
    done = true;
  }//timeout()  
  
  private void cancel() {
    if(done)
      return;
  
    System.out.println("Operation canceled.");
    
    p.stopProcessing();
    p = null;
    
    done = true;
  }//cancel()
  
  
  private class Process extends Thread {
    private RegExCompiler rec;
    
    public void run() {
      try  {
        rec = new RegExCompiler(o.getIgnoreWeight(), new MyMonitor());
        String answer = rec.compile(o.getInput(), o.getCompileLevel());

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
      
      timeout = o.getTimeout();
    
      while (!done && (System.currentTimeMillis() - start) <= timeout) {
        Thread.yield();
      
        try  {
          Thread.sleep(INCREMENT);
        } catch(InterruptedException e)  {  }
      }//while
      
      timeout();
    }//run()
  }//ProcessTimeout()
}


class MyMonitor implements Monitor {
  public void updateStatus(String s) {
        System.out.println(s);
  }//updateStatus()
}//MyMonitor


class options
{
	private String [] inputFilesNames = new String [10];//strores file names
	private String [] inputFiles;//strores file output
    private String [] inputKey;//stores input from the user
    private String [] processUs;//strings from files/user
    private String [] args;//strores the arguments
    private String outputFile;
    private boolean canAddOutFiles=true;
    private int inputKeyNum=0;
    private int ignoreWeight = 0;
    private long timeout;
    private boolean load;
    private int compileLevel = RegExCompiler.SURFACE_COMPILE;
    private String encodingScheme = Constants.OPTION_1_SCHEME;
    private int j=0;//pointer for the inputFilesNames[]
    private boolean valid;
    
	public options (String [] arguments)
	{
    	args=arguments;
        valid = true;
        int t;//temporarily stores the -k value
    
    	for (int i=0; i<args.length; i++)
    	{
           
        	if (args[i].charAt(0)=='-')//if this is an option
            {
            	//check if this if this is load option
            	if (args[i].charAt(1)=='l' || args[i].charAt(1)=='L')
                	load = true;
                
                //help message
                else if (args[i].charAt(1)=='?' || args[i].charAt(1)=='h') {
                	this.printHelp();
                    valid = false;
                    return;
                }
                    
                
                //is this a output file?
                else if (args[i].charAt(1)=='o' || args[i].charAt(1)=='O')
                {
                	try
                	{
                    	i++;
                        
                        if(args[i].charAt(0) != '-')
                    	  outputFile = args[i];
                        else
                          System.out.println("The output file has not been specified; using screen output only");
                	} 
                    catch(ArrayIndexOutOfBoundsException e) 
                    {
                    	System.out.println("The output file has not been specified; using screen output only");
                    }
                }
                
                //does the user want use the keyboard
                else if (args[i].charAt(1)=='k' || args[i].charAt(1)=='K')
                {
                	try//let's if the user said -k#
                	{
                    	t=Integer.parseInt(args[i].substring(2, args[i].length()));
                        if (t>=0)
                        	inputKeyNum += t;
                	}
                    catch(Exception e)
                    {
                    	try//guess not, let's see if the number is in the next arg
                    	{
                        	i++;//go to next arg
                        	t = Integer.parseInt(args[i]);
                            if (t>=0)
                        		inputKeyNum += t;
                    	}
                        catch(Exception f)//okayy... then let's leave it as one string
                        {
                        	i--;//incremented in the previous try, let's decrement now
                            inputKeyNum++;
                        } 
                    }
                        
                }//keyboard input
                
                
                //does the user want to adjust timeout
                else if (args[i].charAt(1)=='u' || args[i].charAt(1)=='U')
                {
                	try//let's if the user said -k#
                	{
                    	int t1 = Integer.parseInt(args[i].substring(2, args[i].length()));
                        if (t1 >= 1)
                        	timeout = t1 * 1000;
                	}
                    catch(Exception e) { 
                      try//guess not, let's see if the number is in the next arg
                      {
                        i++;//go to next arg
                        
                        int t1 = Integer.parseInt(args[i]);
                        if (t1 >= 1)
                        	timeout = t1 * 1000;
                      }
                      catch(Exception f)//error in  setting
                      {
                        System.out.println("Invalid input for timeout value: " + args[i] + ". Ignoring setting.");
                      }//try..catch
                    }//try..catch
                }//adjust timeout
                
                
                //does user want to adjust compile level
                else if (args[i].charAt(1)=='c' || args[i].charAt(1)=='C') 
                {
                  if(args[i].length() == 3) {
                    //argument just right, read third char
                    char tt = args[i].charAt(2);
                    
                    switch (tt) {
                      case '1':
                        compileLevel = RegExCompiler.NO_COMPILE;
                        break;
                        
                      case '2':
                        compileLevel = RegExCompiler.SURFACE_COMPILE;
                        break;
                        
                      case '3':
                        compileLevel = RegExCompiler.SEMI_DEEP_COMPILE;
                        break;
                        
                      case '4':
                        compileLevel = RegExCompiler.DEEP_COMPILE;
                        break;
                        
                      default:
                        System.out.println("Could not read compile level option, using default");
                    }//switch
                  } else
                    System.out.println("Could not read compile level option, using default");   
                }//compile level adjust
                
                
                //does user want to adjust accuracy
                else if (args[i].charAt(1)=='a' || args[i].charAt(1)=='A') 
                {
                  if(args[i].length() == 3) {
                    //argument just right, read third char
                    char tt = args[i].charAt(2);
                    if(tt == 'm' || tt == 'M')
                      ignoreWeight = Constants.LETTER_ACCURACY;
                    else if (tt == 'w' || tt == 'W')
                      ignoreWeight = Constants.WORD_ACCURACY;
                    else if (tt == 'n' || tt == 'N')
                      ignoreWeight= Constants.LINE_ACCURACY;
                  } else {
                    int msize = -1;
                    try {
                      msize = Integer.parseInt(args[i].substring(2, args[i].length()));
                    } catch (NumberFormatException e) { }
                  
                    if(msize != -1 && msize > 0)
                      ignoreWeight = msize;
                  }//if..else
                }//accuracy adjust
                
                
                //does the user adjust encoding
                else if (args[i].charAt(1)=='t' || args[i].charAt(1)=='T') 
                {
                  if(args[i].length() == 3) {
                    //argument just right, read third char
                    char tt = args[i].charAt(2);
                    if(tt == '1')
                      encodingScheme = Constants.OPTION_1_SCHEME;
                    else if (tt == '2')
                      encodingScheme = Constants.OPTION_2_SCHEME;
                    else if (tt == '3')
                      encodingScheme = Constants.OPTION_3_SCHEME;
                    else if (tt == '4')
                      encodingScheme = Constants.OPTION_4_SCHEME;
                    else if (tt == '5')
                      encodingScheme = Constants.OPTION_5_SCHEME;
                    else if (tt == '6')
                      encodingScheme = Constants.OPTION_6_SCHEME;
                  } else
                    encodingScheme = args[i].substring(2, args[i].length());
                }//scheme adjust
                
                else {
                    System.out.println("Unknown option: " + args[i]);
                    valid = false;
                	this.printHelp();
                }//if..else if..else
                
            }//if
            
            
            else
            {
            	inputFilesNames[j]=args[i];
                j++;
            }
            
            //check if output files can be added
            if (j+inputKeyNum>inputFilesNames.length)
            {
            	System.out.println("Current version is limited to ten strings");
                this.printHelp();
                valid = false;
            	return;
            }
                
    	}//for
        
        inputKey = this.getUserStrings();
        inputFiles= this.getFiles();
        processUs= this.combineInputs();
        
        //clear memory
        inputFiles = null;
        inputKey = null;
        inputFilesNames = null;
        args = null;
	}//constructor
    
    private void printHelp()
    {
        System.out.println("Usage: REGXMAKE [options] [filenames]");
        System.out.println("  -o [filename]    Output to specified filename");
        System.out.println("  -l               Load settings from file (file  generated by GUI version)");
        System.out.println("  -c[1-4]          Compile Level");
        System.out.println("                     1 - No compile");
        System.out.println("                     2 - Surface compile (Default)");
        System.out.println("                     3 - Semi-Deep compile");
        System.out.println("                     4 - Deep compile");
        System.out.println("  -k [integer]     Number of inputs to be taken from keyboard,");
        System.out.println("                   1 if no number is specified");
        System.out.println("  -u [integer]     Number of seconds to wait before a process times out");
        System.out.println("                   (Advanced option, no timeout by default");
        System.out.println("  -a[m|w|n]        Accuracy level (advanced option):");
        System.out.println("                   m - letter accuracy (" + Constants.LETTER_ACCURACY + " byte(s), default)");
        System.out.println("                   w - word accuracy (" + Constants.WORD_ACCURACY + " byte(s))");
        System.out.println("                   n - line accuracy (" + Constants.LINE_ACCURACY + " byte(s))");
        System.out.println("  -t[1-6]          Read and write data in text format (default):");
        System.out.println("                     1 - use US_ASCII encoding scheme");
        System.out.println("                     2 - use ISO-8859-1 encoding scheme");
        System.out.println("                     3 - use UTF-8 encoding scheme");
        System.out.println("                     4 - use UTF-16BE encoding scheme");
        System.out.println("                     5 - use UTF-16LE encoding scheme");
        System.out.println("                     6 - use UTF-16 encoding scheme");
        System.out.println("  [filenames]      Files or urls to be processed by the program as input");
        System.out.println("NOTE: For more information about encoding shcemes ");
        System.out.println("      visit http://ietf.org/rfc/rfc2278.txt");
    }
    
    private String[] getUserStrings()
    {
    	String [] usrInpt = new String [inputKeyNum];
        
        for (int i=0; i<usrInpt.length; i++)
        {
        	usrInpt[i]=ConsoleUtils.prompt("User input #" + (i+1) + ": ");
        }
        return usrInpt;
    }
    
    private String[] getFiles()
    {
    	String [] fileInpt = new String [j];
        
        for (int i=0; i<fileInpt.length; i++)
        {
          try  {
            URL u = new URL(inputFilesNames[i]);
            fileInpt[i]=ConsoleUtils.readFull(u.openStream(), encodingScheme);
                
          } catch(Exception e) {
            try  {
              fileInpt[i]=ConsoleUtils.readFullFile(inputFilesNames[i], encodingScheme);
            } catch(IOException e1)  { fileInpt[i] = ""; }
            
          } //try..catch
        }
        
        return fileInpt;
    }
    
    private String[] combineInputs()
    {
    	String [] comb = new String [inputFiles.length+inputKey.length];
        
        if (comb.length==0)//if user doesn't wanna do anything, exit
        {
        	System.out.println("Nothing to process.");
            this.printHelp();
            valid = false;
            return new String[0];
        }
        
        for (int i=0; i<inputFiles.length; i++)//copy the input files
        {
        	comb[i]=inputFiles[i];
        }
        
        for (int i=inputFiles.length, x=0; i<comb.length; i++, x++)//copy the strings
        {
        	comb[i]=inputKey[x];
        }
        
        return comb;
    }
    
    public void whatsWrong()
    {
    	System.out.println("***   ***   ***");
    	System.out.println("outputFile "+	outputFile );
        System.out.println("compileLevel "	+	compileLevel);
        System.out.println("inputKeyNum "+	inputKeyNum);
        System.out.println("load ini file " + load);
        System.out.println("timeout(millis) "+ timeout);
        System.out.println("j, numoffiles "+	j);
    	System.out.println("***   ***   ***");
    	
        System.out.println("***   ***   ***");
        System.out.println("String [] inputFilesNames ");
        for (int i=0; i<inputFilesNames.length; i++)
        	System.out.println("At " + i + " ... "+inputFilesNames[i]);
            
        System.out.println("***   ***   ***");
    	System.out.println("String [] inputKey ");
        try
        {
        	for (int i=0; i<inputKey.length; i++)
        		System.out.println("At " + i + " ... "+inputKey[i]);
        }
        catch (NullPointerException n)
        {
        	System.out.println("it's null");
        }
            
        System.out.println("***   ***   ***");
        System.out.println("String [] inputFiles ");
        try
        {
        	for (int i=0; i<inputFiles.length; i++)
        		System.out.println("At " + i + " ... "+inputFiles[i]);
        }
        catch (NullPointerException n)
        {
        	System.out.println("it's null");
        }
            
        
        System.out.println("***   ***   ***");
    	System.out.println("String [] args");
        for (int i=0; i<args.length; i++)
        	try 
        	{
            	System.out.println("At " + i + " ... "+args[i]);
        	}
            catch (NullPointerException n)
            {
            	System.out.println("it's null");
            }
            catch (ArrayIndexOutOfBoundsException b)
            {
            	System.out.println("outofbounds");
                break;
            }
            
        System.out.println("***   ***   ***");
    	System.out.println("String [] processUs");
        for (int i=0; i<processUs.length; i++)
        	try 
        	{
            	System.out.println("At " + i + " ... "+processUs[i]);
        	}
            catch (NullPointerException n)
            {
            	System.out.println("it's null");
            }
            catch (ArrayIndexOutOfBoundsException b)
            {
            	System.out.println("outofbounds");
                break;
            }
    }
    
    public String[] getInput() { return processUs; }
    public String getOutputFile() { return outputFile; }
    public int getIgnoreWeight() { return ignoreWeight; }
    public int getCompileLevel() { return compileLevel; }
    public String getEncodingScheme() { return encodingScheme; }
    public long getTimeout() { return timeout;}
    public boolean loadSettings() { return load; }
    public boolean isValid() { return valid; }
    
    public void setIgnoreWeight(int iw) { ignoreWeight = iw; }
    public void setCompileLevel(int cl) { compileLevel = cl; }
    public void setTimeout(long to) { timeout = to; }
    public void setEncodingScheme(String ec)  { encodingScheme = ec;}
}//class options