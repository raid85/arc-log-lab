package ca.etsmtl.log430.lab1;

/**************************************************************************************
 ** Class name: TrieFilter
 ** Author: R. Champagne, Ecole de technologie superieure
 ** Date: 2012-May-10
 ** Version 1.0
 ** Last modification: R. Champagne, 2012-May-10
 **
 ***************************************************************************************
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle. This assignment is 
 ** designed to illustrate a pipe and filter architecture.  For the instructions, refer
 ** to the assignment write-up.
 **
 ** Abstract: This class is intended to be a filter that will send the information on
 **           an output pipe specific to each ticket type. In other terms, this filter splits
 **           the data by language. 
 ** 
 ** Pseudo Code:
 **
 ** 	connect to input pipe
 ** 	connect to output pipe
 **
 **	while not end of line
 **    if " DEF " appears on line of text
 **        write line of text to DEF output pipe
 **        flush pipe
 **    else if " AME " appears on line of text
 **        write line of text to AME output pipe
 **        flush pipe
 **    end if
 **	end while
 **
 **	close pipes
 **
 ** Running the program
 **
 ** 	See Main.java
 **
 ** Modification Log
 **************************************************************************************
 **
 **************************************************************************************/

import java.io.*;

public class TriFilter extends Thread {

	// Declarations

	boolean Done;

	PipedReader InputPipe = new PipedReader();
	PipedWriter OutputPipe = new PipedWriter();

	public TriFilter(PipedWriter InputPipe, PipedWriter OutputPipe1) {

		try {

			// Connect inputPipe

			this.InputPipe.connect(InputPipe);
			System.out.println("TrieFilter:: connected to upstream filter.");

			// Connect OutputPipes

			this.OutputPipe = OutputPipe;
			System.out.println("TrieFilter:: connected to downstream filters.");

		} catch (Exception Error) {

			System.out.println("TrieFilter:: Error connecting to other filters.");

		} // try/catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main

	public void run() {

		// Declarations
		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string
		String LineOfText = "",
			   LineASS    = "",
			   LineNOU    = "",
			   LineRES    = "",
			   LineROU    = "";
		// string is required to look for the language code
		int IntegerCharacter = 0; // the integer value read from the pipe

		try {

			Done = false;

			while (!Done) {

				IntegerCharacter = InputPipe.read();
				CharacterValue[0] = (char) IntegerCharacter;

				if (IntegerCharacter == -1) { // pipe is closed

					Done = true;

				} else {

					if (IntegerCharacter == '\n') { // end of line

						System.out.println("TrieFilter:: received: " + LineOfText + ".");
						
						if (LineOfText.indexOf("ASS") != -1) {
							LineASS += LineOfText + "\n";
							System.out.println("TrieFilter:: sending: "
									+ LineASS + " to output pipe Trie (ASS).");
						} else if(LineOfText.indexOf("NOU") != -1) {
							LineNOU += LineOfText + "\n";
							System.out.println("TrieFilter:: sending: "
									+ LineNOU + " to output Trie (NOU).");
 						} else if(LineOfText.indexOf("RES") != -1) {
							LineRES += LineOfText + "\n";
							System.out.println("TrieFilter:: sending: "
									+ LineRES + " to output Trie (RES).");

 						} else if(LineOfText.indexOf("ROU") != -1) {
							LineROU += LineOfText + "\n";
							System.out.println("TrieFilter:: sending: "
									+ LineROU + " to output Trie (ROU).");
						}// if

						LineOfText = "";

					} else {

						LineOfText += new String(CharacterValue);

					} // if //
					

				} // if


				
			} // while
			
			Done = false;
			
			LineOfText = LineASS + LineNOU + LineRES + LineROU;
			
			System.out.println(LineOfText.substring(0, 17));
			
			String test = "";
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			for(int i = 0; i < LineOfText.length(); i+= (LineOfText.indexOf("\n")+1)){
				
				test = LineOfText.substring(i, i+LineOfText.indexOf("\n"));
				System.out.println(test);
				System.out.println(LineOfText.indexOf("\n"));
				//OutputPipe.write(test, 0, test.length());
				//OutputPipe.flush();
				
			}
			System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
				
			
			
			System.out.println("TrieFilter:: sending: "
					+ LineOfText + " to output pipe in order.");
			OutputPipe.write(LineOfText, 0, LineOfText.length());
			OutputPipe.flush();

			
			
			

		} catch (Exception Error) {
			Error.printStackTrace();
			System.out.println("TrieFilter:: Interrupted.");

		} // try/catch

		try {

			InputPipe.close();
			System.out.println("TrieFilter:: input pipe closed.");

			OutputPipe.close();
			System.out.println("TrieFilter:: output pipes closed.");

		} catch (Exception Error) {

			System.out.println("TrieFilter:: Error closing pipes.");

		} // try/catch

	} // run

} // class