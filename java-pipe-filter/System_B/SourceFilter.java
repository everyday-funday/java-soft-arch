/******************************************************************************************************************
* File:SourceFilterTemplate.java
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
*
* Description:
*
* This class serves as a template for creating source filters. The details of threading, connections writing output
* are contained in the FilterFramework super class. In order to use this template the program should rename the class.
* The template includes the run() method which is executed when the filter is started. The run() method is the guts
* of the filter and is where the programmer should put their filter specific code.The run() method is the main
* read-write loop for reading data from some source and writing to the output port of the filter. This template
* assumes that the filter is a source filter that reads data from a file, device (sensor),or generates the data
* interally, and then writes data to its output port. In this case, only the output port is used. In cases where the
* filter is a standard filter or a sink filter, you should use the FilterTemplate.java or SinkFilterTemplate.java as
* a starting point for creating standard or sink filters.
*
* Parameters: 		None
*
* Internal Methods:
*
*	public void run() - this method must be overridden by this class.
*
******************************************************************************************************************/

package System_B;
import java.io.*;

public class SourceFilter extends FilterFramework
{
	// Source filter takes filename as the input.
	public String filename;

	SourceFilter(){
		this.filename = "FlightData.dat";
	}
	SourceFilter(String filename){
		this.filename = filename;
	}

	public void run()
    {

		// Load the data from file, byte by byte.
		int bytes_in = 0;
		int bytes_out = 0;
		byte databyte = 0;
		DataInputStream in = null;


		// Now write out the message to the output port, until it reaches EOF
		try {
				// instantiate input byte stream
				in = new DataInputStream(new FileInputStream(filename));
				System.out.println("\n" + this.getName() + "::Source reading file..." );

				while(true) {

					// input
					databyte = in.readByte();
					bytes_in++;

					// output
					WriteFilterOutputPort(databyte);
					bytes_out++;

				} // while

			} catch (EOFException e) {
				try {
					in.close();
					ClosePorts();
					System.out.println(this.getName() + ": EOF stop reading: bytes read::" + bytes_in + " bytes written: " + bytes_out);
				} catch (IOException ioException) {
					System.out.println( this.getName() + ": Problem with closing the input pipe from file");
				}
		} catch (IOException e) {
				System.out.println( this.getName() + ": Problem with reading the file. Make sure the CSV file is not open in another program." + e );
			}

   } // run

} // SourceFilter