package ca.etsmtl.log430.lab1b;

import java.io.PipedReader;
import java.io.PipedWriter;

/**************************************************************************************
 ** Class name: SeverityFilter
 ** Original author: A.J. Lattanze, CMU
 ** Date: 12/3/99
 ** Version 1.0
 **
 ** Adapted by R. Champagne, Ecole de technologie superieure
 ** 2002-May-08, 2012-Jan-13, 2012-May-10
 **
 ***************************************************************************************
 ** Purpose: Assignment 1 for LOG430, Architectures logicielle. This
 ** assignment is designed to illustrate a pipe and filter architecture.  For the 
 ** instructions, refer to the assignment write-up.
 **
 ** Abstract: This class is intended to be a filter that will key on a particular severity
 **	     provided at instantiation.  Note that the stream has to be buffered so that
 **      it can be checked to see if the specified severity appears on the stream.
 **      If this string appears in the stream from Main, to the output stream.
 ** 
 ** Pseudo Code:
 **
 ** 	connect to input pipe
 ** 	connect to output pipe
 **
 **	while not end of line
 **
 **		read input pipe
 **
 **		if specified severity1 or severity2 appears on line of text
 **			write line of text to output pipe
 **			flush pipe
 **		end if
 **
 **	end while
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

public class SeverityFilter extends Thread {

	// Declarations

	boolean Done;

	String severity1,
		   severity2;
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipe = new PipedWriter();

	public SeverityFilter(String severity1, String severity2, PipedWriter inputPipe,
			PipedWriter outputPipe) {

		this.severity1 = severity1;
		this.severity2 = severity2;

		try {

			// Connect inputPipe to Main

			this.inputPipe.connect(inputPipe);
			System.out.println("SeverityFilters " + severity1 + " and " +
						severity2 + ":: connected to upstream filter.");

			// Connect outputPipe to Merge

			this.outputPipe = outputPipe;
			System.out.println("SeverityFilter " + severity1 + " and " +
					severity2 + ":: connected to downstream filter.");

		} catch (Exception Error) {

			System.out.println("SeverityFilter " + severity1 + " and " +
					severity2 + ":: Error connecting to other filters.");

		} // try/catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main
	public void run() {

		// Declarations

		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string
		String LineOfText = "";
		// string is required to look for the keyword
		int IntegerCharacter; // the integer value read from the pipe

		try {

			Done = false;

			while (!Done) {

				IntegerCharacter = inputPipe.read();
				CharacterValue[0] = (char) IntegerCharacter;

				if (IntegerCharacter == -1) { // pipe is closed

					Done = true;

				} else {

					if (IntegerCharacter == '\n') { // end of line

						System.out.println("SeverityFilter " + severity1 + " and " +
								severity2 + ":: received: " + LineOfText + ".");

						if (LineOfText.indexOf(severity1) != -1 || LineOfText.indexOf(severity2) != -1) {

							System.out.println("SeverityFilter "
									+ severity1 + " and " +
									severity2 + ":: sending: "
									+ LineOfText + " to output pipe.");
							LineOfText += new String(CharacterValue);
							outputPipe
									.write(LineOfText, 0, LineOfText.length());
							outputPipe.flush();

						} // if

						LineOfText = "";

					} else {

						LineOfText += new String(CharacterValue);

					} // if //

				} // if

			} // while

		} catch (Exception Error) {

			System.out.println("SeverityFilter::" + severity1 + " and " +
					severity2 + " Interrupted.");

		} // try/catch

		try {

			inputPipe.close();
			System.out.println("SeverityFilter " + severity1 + " and " +
					severity2 + ":: input pipe closed.");

			outputPipe.close();
			System.out.println("SeverityFilter " + severity1 + " and " +
					severity2 + ":: output pipe closed.");

		} catch (Exception Error) {

			System.out.println("SeverityFilter " + severity1 + " and " +
					severity2 + ":: Error closing pipes.");

		} // try/catch

	} // run

} // class