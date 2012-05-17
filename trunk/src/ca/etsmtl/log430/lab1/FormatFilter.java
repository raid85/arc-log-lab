package ca.etsmtl.log430.lab1;

import java.util.regex.*;

/**************************************************************************************
 ** Class name: FormatFilter
 ** Author: 
 ** Date: 
 ** Version 1.0
 ** Last modification: 
 **
 ***************************************************************************************
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle. This assignment is 
 ** designed to illustrate a pipe and filter architecture.  For the instructions, refer
 ** to the assignment write-up.
 **
 ** Abstract:  
 ** 
 ** Pseudo Code:
 **
 ** 	connect to input pipe
 ** 	connect to output pipe
 **
 **	while not end of line

 **
 ** Modification Log
 **************************************************************************************
 **
 **************************************************************************************/

import java.io.*;

public class FormatFilter extends Thread {

	// Declarations

	boolean Done;

	PipedReader InputPipe = new PipedReader();
	PipedWriter OutputPipe1 = new PipedWriter();

	public FormatFilter(PipedWriter InputPipe, PipedWriter OutputPipe1, PipedWriter OutputPipe2) {

		try {

			// Connect inputPipe

			this.InputPipe.connect(InputPipe);
			System.out.println("FormattageFilter:: connected to upstream filter.");

			// Connect OutputPipes

			this.OutputPipe1 = OutputPipe1;
			System.out.println("FormattageFilter:: connected to downstream filters.");

		} catch (Exception Error) {

			System.out.println("FormattageFilter:: Error connecting to other filters.");

		} // try/catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main

	public void run() {

		// Declarations
		
		Pattern patternStatus    = Pattern.compile("[A-Z]{3}\\s"); // Take the 3rd Match
		Pattern patternSeverity  = Pattern.compile("[A-Z]{3}\\s"); // Take the 2nd Match
		Pattern patternType      = Pattern.compile("[A-Z]{3}\\s"); // Take the 1st Match
		Pattern patternNumBillet = Pattern.compile("[0-9]{4}");

		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string
		String LineOfText = "";
		// string is required to look for the language code
		int IntegerCharacter; // the integer value read from the pipe

		try {

			Done = false;

			while (!Done) {

				IntegerCharacter = InputPipe.read();
				CharacterValue[0] = (char) IntegerCharacter;

				if (IntegerCharacter == -1) { // pipe is closed

					Done = true;

				} else {

					if (IntegerCharacter == '\n') { // end of line

						System.out.println("FormattageFilter:: received: " + LineOfText + ".");

						if (LineOfText.indexOf(" DEF ") != -1) {

							System.out.println("FormattageFilter:: sending: "
									+ LineOfText + " to output pipe 1 (DEF).");
							LineOfText += new String(CharacterValue);
							OutputPipe1
									.write(LineOfText, 0, LineOfText.length());
							OutputPipe1.flush();
						}
					} else {

						LineOfText += new String(CharacterValue);

					} // if //

				} // if

			} // while

		} catch (Exception Error) {

			System.out.println("FormattageFilter:: Interrupted.");

		} // try/catch

		try {

			InputPipe.close();
			System.out.println("FormattageFilter:: input pipe closed.");

			OutputPipe1.close();
			System.out.println("FormattageFilter:: output pipes closed.");

		} catch (Exception Error) {

			System.out.println("FormattageFilter:: Error closing pipes.");

		} // try/catch

	} // run

} // class