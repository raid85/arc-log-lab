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
	PipedWriter OutputPipe = new PipedWriter();

	public FormatFilter(PipedWriter InputPipe, PipedWriter OutputPipe) {

		try {

			// Connect inputPipe

			this.InputPipe.connect(InputPipe);
			System.out.println("FormatFilter:: connected to upstream filter.");

			// Connect OutputPipes

			this.OutputPipe = OutputPipe;
			System.out.println("FormatFilter:: connected to downstream filters.");

		} catch (Exception Error) {

			System.out.println("FormatFilter:: Error connecting to other filters.");

		} // try/catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main

	public void run() {

		// Déclaration des "patterns" et des " matchers" nécéssaires à la décomposition du string entrant		
		Pattern patternC   = Pattern.compile("[A-Z]{3}\\s"); 
		Pattern patternNB = Pattern.compile("[0-9]{4}");
		Matcher matcherC ;
		Matcher matcherNB ;

		// Déclaration des deux tableau servant à placer les champs en ordre
		String unsdata[] = new String [4];
		String sdata[] = new String [4];
		boolean isComplete = false ;

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

						System.out.println("FormatFilter received: " + LineOfText);	

						isComplete = true ;

					} else {
						LineOfText += new String(CharacterValue);
					}
				} // if

				if (isComplete){
					isComplete =false ;
					//--------DEBUT DU TRAITEMENT--------//
					//Association des "matchers" à la ligne de texte reçue dans le but d'en faire la décomposition

					matcherC = patternC.matcher(LineOfText);
					matcherNB = patternNB.matcher(LineOfText);

					//Boucle permettant de remplir le tableau de tri intialisé anterieurement						

					for(int i = 0 ; matcherC.find() && i<3 ;i++){						
						unsdata[i] = matcherC.group();

					}

					matcherNB.find();
					unsdata[3] = matcherNB.group();


					// Placer les champs dans l'ordre demandé
					sdata[0] = unsdata[2] ;
					sdata[1] = unsdata[1] ;
					sdata[2] = unsdata [0] ;
					sdata [3] = unsdata [3] ;

					//Cocanténation de la ligne finale à envoyer		
					String result = (sdata[0].toString()+sdata[1].toString()+sdata[2].toString()+sdata[3].toString());

					LineOfText = result+"\n" ;						

					try {
						System.out.println("FormatFilter:: sending: " + LineOfText) ;							

						OutputPipe.write(LineOfText, 0, LineOfText.length());
						OutputPipe.flush();
					}
					catch (Exception IOError) {

						System.out.println("FormatFilter : Write Error."+ IOError+LineOfText);
					} // try/catch
					LineOfText = "";
				}

			} // while

		} catch (Exception Error) {

			System.out.println("FormatFilter:: Interrupted."+Error);

		} // try/catch

		try {

			InputPipe.close();
			System.out.println("FormatFilter:: input pipe closed.");

			OutputPipe.close();
			System.out.println("FormatFilter:: output pipe closed.");

		} catch (Exception Error) {

			System.out.println("FormatFilter:: Error closing pipe.");

		} // try/catch

	} // run

} // class