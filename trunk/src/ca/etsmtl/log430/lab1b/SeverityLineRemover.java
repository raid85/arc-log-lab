package ca.etsmtl.log430.lab1b;

import java.io.PipedReader;
import java.io.PipedWriter;

public class SeverityLineRemover extends Thread {
	// Declarations

	String Severity;

	// Maximum number of lines of text to be sorted
	int MaxBufferSize = 100;

	// Create local pipe that will connect to upstream filter
	PipedReader InputPipe1 = new PipedReader();
	
	// Create local pipes to that will connect to downstream filter
	PipedWriter OutputPipe2 = new PipedWriter();

	public SeverityLineRemover(String Severity, PipedWriter InputPipe1, PipedWriter OutputPipe1) {
		this.Severity = Severity;
		
		try {
			// Connect inputPipe to upstream filter
			this.InputPipe1.connect(InputPipe1);
			System.out.println("SeverityLineRemover:: connected to upstream filter.");
		} catch (Exception Error) {
			System.out.println("SeverityLineRemover:: Error connecting input pipe.");
		} // catch
		try {
			// Connect outputPipe1 to downstream filter
			this.OutputPipe2 = OutputPipe1;
			System.out.println("MergeFilter:: connected to downstream filter.");
		} catch (Exception Error) {
			System.out.println("MergeFilter:: Error connecting output pipe.");
		} // catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main

	public void run() {
		// Declarations

		boolean Done1; // Flags for reading from pipe
		// Begin process data from the input pipes
		try {
			// Declarations

			// Needs to be an array for easy conversion to string
			char[] CharacterValue1 = new char[1];

			Done1 = false; // Indicates when you are done
			// reading on pipe #1
			int IntegerCharacter1; // integer value read from pipe
			String LineOfText1 = ""; // line of text from inputpipe #1
			boolean Write1 = false; // line of text to write to output
			// pipe #1

			// Main loop for reading data

			while (!Done1) {
				// Read pipe #1
				if (!Done1) {
					IntegerCharacter1 = InputPipe1.read();
					CharacterValue1[0] = (char) IntegerCharacter1;
					if (IntegerCharacter1 == -1) // pipe #1 is closed
					{
						Done1 = true;
						System.out.println("SeverityLineRemover:: input pipe closed.");
						try {
							InputPipe1.close();
						} catch (Exception Error) {
							System.out
									.println("SeverityLineRemover:: Error closing input pipe.");
						} // try/catch
					} else {
						if (IntegerCharacter1 == '\n') // end of line
						{
							System.out.println("SeverityLineRemover:: Received: "
									+ LineOfText1 + " on input pipe.");
							Write1 = true;
						} else {
							LineOfText1 += new String(CharacterValue1);
						} // if
					} // if ( IntegerCharacter1 == -1 )
				} // if (!Done1)
				
				if (Write1) {
					Write1 = false;
					if(LineOfText1.substring(22,25).compareTo(Severity)!=0){
						try {					
							System.out.println("SeverityLineRemover:: Sending "
									+ LineOfText1 + " to output pipe.");
							LineOfText1 += "\n";
							OutputPipe2.write(LineOfText1, 0, LineOfText1.length());
							OutputPipe2.flush();
							}
						 catch (Exception IOError) {
							
							System.out.println("SeverityLineRemover:: Write Error."+ IOError+LineOfText1);
						} // try/catch
					}
					LineOfText1 = "";

				} // if ( Write1 )

			} // while (!Done1)

		} // try
		
		
		

		catch (Exception Error) {
			System.out.println("SeverityLineRemover:: Interrupted.");
		} // catch
		try {
			System.out.println("SeverityLineRemover:: output pipe closed.");
			OutputPipe2.close();
		} catch (Exception Error) {
			System.out.println("SeverityLineRemover:: Error closing pipes");
		} // try/catch
		

	} // run

} // class
