package ca.etsmtl.log430.lab1;

import java.io.PipedWriter;

import ca.etsmtl.log430.lab1b.TriFilter;

/**************************************************************************************
 ** Class name: Main Original author: A.J. Lattanze, CMU Date: 12/3/99
 ** Version 1.2
 ** 
 ** Adapted by R. Champagne, Ecole de technologie superieure 2002-May-08,
 ** 2011-Jan-12, 2012-Jan-11.
 ** 
 *************************************************************************************** 
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle. This assignment is
 ** designed to illustrate a pipe and filter architecture. For the instructions,
 ** refer to the assignment write-up.
 ** 
 ** Abstract: This class contains the main method for assignment 1. The
 ** assignment 1 program consists of these files:
 ** 
 ** 1) Main: instantiates all filters and pipes, starts all filters.
 ** 2) FileReaderFilter: reads input file and sends each line to its output pipe.
 ** 3) TypeFilter: separates the input stream in two languages (FRA, EN) and writes
 **    lines to the appropriate output pipe.
 ** 4) SeverityFilter: determines if an entry contains a particular severity specified
 **    at instantiation. If so, sends the whole line to its output pipe.
 ** 5) MergeFilter: accepts inputs from 2 input pipes and writes them to its output pipe.
 ** 6) FileWriterFilter: sends its input stream to a text file.
 ** 7) FormatFilter : Receives an input stream and retains only the required fields
 ** 8) SeverityLineRemover : Receives an input and retransmits all the severities except the one received in parameter
 ** 9) TriFilter : Receives an input and sorts then retransmits
 **
 ** 
 ** Modification Log
 ************************************************************************************** 
 ** @author ah45290 chebli.r@gmail.com for the purposes of implementing
 ** assignement 1.
 **************************************************************************************/

public class Main {

        public static void main(String argv[]) {
                // Lets make sure that input and output files are provided on the
                // command line
                String severityLineRemover = "";
                String fileWriterOutputName = "";
                if (argv.length != 2 && argv.length !=3) {      

                                System.out
                                                .println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
                                System.out
                                                .println("\njava Main <fichier d'entree> <fichier de sortie>");
                                
                } else {
                        if (argv.length == 3){
                                severityLineRemover = argv[2];
                        }
                        // These are the declarations for the pipes.
                        PipedWriter pipe01 = new PipedWriter();
                        PipedWriter pipe02 = new PipedWriter();
                        PipedWriter pipe03 = new PipedWriter();
                        PipedWriter pipe04 = new PipedWriter();
                        PipedWriter pipe05 = new PipedWriter();
                        PipedWriter pipe06 = new PipedWriter();
                        PipedWriter pipe07 = new PipedWriter();
                        PipedWriter pipe08 = new PipedWriter();
                        PipedWriter pipe09 = new PipedWriter();
                        PipedWriter pipe10 = new PipedWriter();
                        PipedWriter pipe11 = new PipedWriter();
                        PipedWriter pipe12 = new PipedWriter();
                        
        
                        


                        // Instantiate the Program Filter Thread
                        Thread FileReaderFilter1 = new FileReaderFilter(argv[0], pipe01);

                        // Instantiate the TypeFilter Thread
                        Thread LanguageFilter1 = new TypeFilter(pipe01, pipe02, pipe03,pipe08);

                        // Instantiate the Course Filter Threads
                        Thread KeywordFilter1 = new SeverityFilter("CRI", pipe02, pipe04);
                        Thread KeywordFilter2 = new SeverityFilter("MAJ", pipe03, pipe05);
                        
                        // Instantiate the Merge Filter Thread
                        Thread MergeFilter1 = new MergeFilter(pipe04, pipe05, pipe06);
                        
                        //Filter to remove selected severity for transmitted result
                        Thread SeverityLineRemover1 = new SeverityLineRemover(severityLineRemover,pipe08, pipe09);
                          
                        //Format filter to retain only selected fields
                        Thread FormatFilter = new FormatFilter(pipe06, pipe07);
                        Thread FormatFilter2 = new FormatFilter(pipe09, pipe10);
                        
                        // Instantiate the sorting filters
                        Thread TriFilter = new TriFilter(pipe10,pipe11) ;
                        Thread TriFilter2 = new TriFilter(pipe07,pipe12) ;
                                             
                        // Instantiate the FileWriter Filter Thread
                        Thread FileWriterFilter1 = new FileWriterFilter(argv[1], pipe12);
                        Thread FileWriterFilter2 = new FileWriterFilter("dataout2.txt", pipe11);

                        // Start the threads (these are the filters)
                        FileReaderFilter1.start();
                        LanguageFilter1.start();
                        KeywordFilter1.start();
                        KeywordFilter2.start();
                        MergeFilter1.start();
                        SeverityLineRemover1.start();
                        FormatFilter2.start();
                        TriFilter.start();
                        TriFilter2.start();
                        FormatFilter.start();
                        FileWriterFilter1.start();
                        FileWriterFilter2.start();
                }  // if
                
        } // main
        
} // Main
