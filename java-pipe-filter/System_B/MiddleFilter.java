/******************************************************************************************************************
* File:FilterTemplate.java
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
*
* Description:
*
* This class serves as a template for creating filters. The details of threading, filter connections, input, and output
* are contained in the FilterFramework super class. In order to use this template the program should rename the class.
* The template includes the run() method which is executed when the filter is started.
* The run() method is the guts of the filter and is where the programmer should put their filter specific code.
* In the template there is a main read-write loop for reading from the input port of the filter and writing to the
* output port of the filter. This template assumes that the filter is a "normal" that it both reads and writes data.
* That is both the input and output ports are used - its input port is connected to a pipe from an up-stream filter and
* its output port is connected to a pipe to a down-stream filter. In cases where the filter is a source or sink, you
* should use the SourceFilterTemplate.java or SinkFilterTemplate.java as a starting point for creating source or sink
* filters.
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
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MiddleFilter extends FilterFramework
{

	// Filter threshold - detect when altitude spikes over 100 ft within consecutive frames
	double altitude_spike_threshold = 100.0;

	// Size of the data packets (bytes)
	int IdLength = 4;
	int MeasurementLength = 8;
	int bytes_in = 0;
	int bytes_out = 0;

	// Filter:
	public void run()
    {
		double velocity = 0;
		double altitude = 0;
		double pressure = 0;
		double temperature = 0;

		byte databyte = 0;

		// variable for storing id and corresponding measurement
		long measurement;
		int id;					// ID (see above)
		int i;					// Loop counter
		int wild_jump_count = 0 ;

		// buffer for storing the altitude history
		double[] altitude_buffer = new double[2]; // buffer for recording past altitudes for compare.

		// Time stamps
		Calendar timestamp = Calendar.getInstance();
		SimpleDateFormat timestamp_format = new SimpleDateFormat("yyyy MM dd::hh:mm:ss");

		// Preparing output writer
		PrintWriter out_csv = Logger.createCSVWriter("WildJumps.csv");
		out_csv.println("Time,Velocity,Altitude,Pressure,Temperature");


		while (true)
		{
			try {
				boolean spike_exists = false;

				//-------- ID --------//
				id = 0;
				for (i = 0; i < IdLength; i++){
					databyte = ReadFilterInputPort();
					id = id | (databyte & 0xFF);
					if (i != IdLength-1)
					{
						id = id << 8;	// Slide one byte (8-bits)
					}
					bytes_in++;

					WriteFilterOutputPort(databyte);
					bytes_out++;
				}

				// Check the ID for time.
				switch (id){
					case (ID_TIME):
						// ID = 0, Time
						measurement = getMeasurement(true);
						timestamp.setTimeInMillis(measurement);
						break;

					case (ID_VELOCITY):
						// ID = 1, Velocity
						measurement = getMeasurement(true);
						velocity = Double.longBitsToDouble(measurement);
						break;

					case (ID_ALTITUDE):
						// ID = 2, Altitude
						// check for spikes, write, and replace
						measurement = getMeasurement(false);
						altitude =  Double.longBitsToDouble(measurement);

						if(altitude_buffer[0] == 0){
							// New data. Store the current altitude
							altitude_buffer[0] = altitude;

						}
						else {
							// store the current data into buffer
							altitude_buffer[1] = altitude;

							// Compare
							if (Math.abs(altitude_buffer[0] - altitude_buffer[1]) >= altitude_spike_threshold) {
								// If the difference is larger than defined threshold (100 ft)
								// 1) write to CSV (WildJumps.csv)
								System.out.println("WildJump detected:"
										+ String.format("%3.5f", (altitude_buffer[0])) + " and " + String.format("%3.5f", (altitude_buffer[1])));

								// 2) compute average and replace
								altitude = (altitude_buffer[0] + altitude_buffer[1]) / 2.0;
								altitude_buffer[0] = altitude;
								spike_exists = true;
								wild_jump_count++;
							} else {
								spike_exists = false;
							}

						}

						// Convert the double altitude value into bytes and send

						ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
						buffer.putLong(Double.doubleToLongBits(altitude));
						byte[] bytes_to_send = buffer.array();

						for (int j = 0; j < MeasurementLength; j++) {
							databyte = bytes_to_send[j];
							WriteFilterOutputPort(databyte);
							databyte++;
						}

						if (spike_exists) {
							WriteFilterOutputPort((byte) 1);
							databyte++;
						} else {
							WriteFilterOutputPort((byte) 0);
							databyte++;
						}

						break;

					case (ID_PRESSURE):
						// ID = 3, Pressure
						measurement = getMeasurement(true);
						pressure =  Double.longBitsToDouble(measurement);;
						break;

					case (ID_TEMPERATURE):
						// ID = 4, Temperature // End of the packet
						measurement = getMeasurement(true);
						temperature =  Double.longBitsToDouble(measurement);

						if(spike_exists) {
							String line = String.join(",",
										timestamp_format.format(timestamp.getTime()),
										String.format("%3.5f", (velocity)),
										String.format("%3.5f", (altitude)),
										String.format("%3.5f", (pressure)),
										String.format("%3.5f", (temperature))
									);

							out_csv.print(line);
							out_csv.print("\n");
							spike_exists = false;
						}
						break;

					default:
						System.out.println("Middle: Invalid Measurement ID detected");
						break;
				} // switch

			} catch (EndOfStreamException e) {
				out_csv.close();
				ClosePorts();
				System.out.printf("\nEOF: Middle filter exiting. Total Wild Jumps %d \n", wild_jump_count);
				break;
			}

		} // while

   } // run

	// Below fxn gets the measurement from the input port.
	// Then depending on the input, it will send the packet to the output port.
	private long getMeasurement(boolean send){
		//-------- Measurement --------//
		long measurement = 0;
		try {
			for (int i=0; i<MeasurementLength; i++ )
			{
				byte databyte = 0;
				databyte = ReadFilterInputPort();

				measurement = measurement | (databyte & 0xFF);
				if (i != MeasurementLength-1)
				{
					measurement = measurement << 8;
				}
				bytes_in++;

				if(send == true) {
					WriteFilterOutputPort(databyte);
					bytes_out++;
				}
			}

		} catch (EndOfStreamException e) {
			e.printStackTrace();
		}

		return measurement;
	} // GetMeasurement();

} // FilterTemplate