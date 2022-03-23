## SWE264P: Lab1 - Pipe and Filter
Joseph Lee


This program reads FlightData.dat file and processes the data through a pipe-and-filter stream. 

Please refer to the below instructions for running the program.

----
### Running the program (System_A)
1) Open terminal and change the directory to the following:
   * cd System_A
   

2) To compile, input the following command in terminal. This will put all of the compiled class files into a sub-folder named System_A. Please note that there is a period after -d.
   * javac -d . *.java


3) Run the following command:
   * java System_A.Plumber


4) The program will run and it will output the OutputA.csv file.
---

### Running the program (System_B)
1) Open terminal and change the directory to the following:
   * cd System_B


2) To compile, input the following command in terminal. This will put all of the compiled class files into a sub-folder named System_B. Please note that there is a period after -d.
   * javac -d . *.java


3) Run the following command:
   * java System_B.Plumber


4) The program will run and it will output the OutputB.csv and WildJumps.csv files.
---
Alternatively, you can set up the folder in an IDE. Please make sure the working directory is properly configured to the corresponding folders for running the programs
