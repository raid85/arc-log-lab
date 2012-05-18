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
		
		Pattern patternC   = Pattern.compile("[A-Z]{3}\\s"); // Take the 3rd Match
		Pattern patternNB = Pattern.compile("[0-9]{4}");
		Matcher matcherC ;
		Matcher matcherNB ;
		String unsdata[] = new String [4];
		String sdata[] = new String [4];

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

						System.out.println("FormattageFilter:: received: " + LineOfText);
						
						//--------DEBUT DU TRAITEMENT--------//
						//Association des matcher a la ligne de texte recue en parametre
						
						matcherC = patternC.matcher(LineOfText);
						matcherNB = patternNB.matcher(LineOfText);
						
						//Boucle permettant de remplir le tableau de char intialise anterieurement						
						
						for(int i = 0 ; matcherC.find() ;i++){
							
							unsdata[i] = matcherC.group();
							System.out.println(unsdata[i]);
						}
						
							matcherNB.find();
							unsdata[3] = matcherNB.group();
							System.out.println(unsdata[3]);
							
						// Placer les infos en ordre
							sdata[0] = unsdata[2] ;
							sdata[1] = unsdata[1] ;
							sdata[2] = unsdata [0] ;
							sdata [3] = unsdata [3] ;
							
							String result = (sdata[0].toString()+sdata[1].toString()+sdata[2].toString()+sdata[3].toString());
							
							LineOfText = result ;

						

							System.out.println("FormattageFilter:: sending: " + LineOfText) ;						
							OutputPipe1.write(LineOfText, 0, LineOfText.length());
							OutputPipe1.flush();
							
					}
				

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