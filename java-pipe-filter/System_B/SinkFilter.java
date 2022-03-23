/******************************************************************************************************************
* File:SinkFilterTemplate.java
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
*
* Description:
*
* This class serves as a template for creating sink filters. The details of threading, connections writing output
* are contained in the FilterFramework super class. In order to use this template the program should rename the class.
* The template includes the run() method which is executed when the filter is started.
* The run() method is the guts of the filter and is where the programmer should put their filter specific code.
* In the template there is a main read-write loop for reading from the input port of the filter. The programmer is
* responsible for writing the data to a file, or device of some kind. This template assumes that the filter is a sink
* filter that reads data from the input file and writes the output from this filter to a file or device of some kind.
* In this case, only the input port is used by the filter. In cases where the filter is a standard filter or a source
* filter, you should use the FilterTemplate.java or the SourceFilterTemplate.java as a starting point for creating
* standard or source filters.
*
* Parameters: 		None
*
* Internal Methods:
*
*	public void run() - this method must be overridden by this class.
*
******************************************************************************************************************/
package System_B;
import java.io.PrintWriter;
import java.util.*;
import java.text.SimpleDateFormat;

public class SinkFilter extends FilterFramework
{
	public void run()
    {
		int tmp_ct = 0;

		// Data stream states.
		byte databyte = 0;
		int bytes_in = 0;
		int bytes_out = 0;

		// Size of the data packets (bytes)
		int IdLength = 4;
		int MeasurementLength = 8;

		// Time stamps
		Calendar timestamp = Calendar.getInstance();
		SimpleDateFormat timestamp_format = new SimpleDateFormat("yyyy MM dd::hh:mm:ss");

		// variable for storing id and corresponding measurement
		int id;				// ID (see above)
		long measurement;	// Measurement value
		int i;				// Loop counter

		// Detecting wild jumps
		boolean is_spike = false;

		/******************************
		 Measurement IDs:
		 ******************************/

/*************************************************************
*	This is the main processing loop for the filter.
*	read until there is no more data available on the input port.
**************************************************************/
		PrintWriter out_csv =  Logger.createCSVWriter("OutputB.csv");
		out_csv.println("Time,Velocity,Altitude,Pressure,Temperature");
		boolean eof = false;
		while (!eof)
		{
			try
			{

				/*************************************************************
				 *	Here we read a byte from the input port. Note that
				 * 	regardless how the data is written, data must be read one
				 *	byte at a time from the input pipe. This has been done
				 * 	to adhere to the pipe and filter paradigm and provide a
				 *	high degree of portabilty between filters. However, you
				 * 	must convert output data as needed on your own.
				 **************************************************************/

				//-------- ID --------//
				id = 0;
				for (i = 0; i < IdLength; i++){
					databyte = ReadFilterInputPort();
					id = id | (databyte & 0xFF);		// We append the byte on to ID...
					if (i != IdLength-1)				// If this is not the last byte, then slide the
					{									// previously appended byte to the left by one byte
						id = id << 8;					// to make room for the next byte we append to the ID
					}
					bytes_in++;
				}

				//-------- Measurement --------//
				measurement = 0;
				for (i=0; i<MeasurementLength; i++ )
				{
					databyte = ReadFilterInputPort();
					measurement = measurement | (databyte & 0xFF);	// append the byte onto measurement packet
					if (i != MeasurementLength-1)					// If this is not the last byte
					{
						measurement = measurement << 8;				// slide the byte to make room for the next byte we append to the
					}
					bytes_in++;
				}


				double velocity = 0;
				double altitude = 0;
				double pressure = 0;
				double temperature = 0;


				// Write out the file onto the CSV.
				switch (id){

					case (ID_TIME):
						// ID = 0, Time
						timestamp.setTimeInMillis(measurement);
						out_csv.print(timestamp_format.format(timestamp.getTime()));
						break;

					case (ID_VELOCITY):
						// ID = 1, Velocity
						velocity = Double.longBitsToDouble(measurement);
						out_csv.print("," + String.format("%3.5f", (velocity)));
						break;

					case (ID_ALTITUDE):
						// ID = 2, Altitude
						altitude =  Double.longBitsToDouble(measurement);
						databyte = ReadFilterInputPort();
						is_spike = databyte == 1;
						out_csv.print("," + String.format("%3.5f", (altitude)) + (is_spike ? "*" : ""));
						break;

					case (ID_PRESSURE):
						// ID = 3, Pressure
						pressure =  Double.longBitsToDouble(measurement);
						out_csv.print("," + String.format("%3.5f", (pressure)));
						break;

					case (ID_TEMPERATURE):
						// ID = 4, Temperature // End of the packet
						temperature =  Double.longBitsToDouble(measurement);
						out_csv.print("," + String.format("%3.5f", (temperature)) + "\n");
						break;

					default:
						System.out.printf("\nSink: Invalid Measurement ID detected %d", id);
						break;
				}

			} // try
			catch (EndOfStreamException e)
			{
				eof = true;
				out_csv.close();
				ClosePorts();
				System.out.println("All data have been outputted. Program exiting.");
				break;

			} // catch

		} // while

   } // run

} // FilterTemplate