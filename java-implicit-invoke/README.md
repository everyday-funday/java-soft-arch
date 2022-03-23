Implicit-Invocation System
Joseph Young Lee
____
## Introduction
This package contains program that allows management of students and course enrollment, using publish-subscribe model.
The core architectural style that it uses is Implicit-Invocation system.


### Compiling and Running the Program
___
Note that this program requires JAVA SDK 11 or above.
First, unzip the downloaded file.

Then open the lab2_joseph directory using a terminal
  ```text
  cd lab2_joseph
  ```

Then use the following command to compile. This will put all of the compiled class files into a sub-folder named lab2_joseph. Please note that there is a period after -d.
  ```text
   javac -d . *.java
  ```
  
To run, input the following command.
	```text
   java lab2_joseph.SystemMain Students.txt Courses.txt
	```

Now, follow the prompts to manage student course registration.

## Testing Modification 1
___

To test modification 1, simply run the program and perform actions that trigger console output, 
which writes an output file into /output/LogOutput.txt
ClientLogOutput subscribes to the EV_SHOW event and writes the outputs into PrintWriter.

___
## Testing Modification 2
Modification 2 adds functionality to notify overbooked classes
When a class is booked with 3 students, it will notify that it's fully booked.
If a class has more than 4 students, it will notify that it's overbooked, but still register the student.

___
## Testing Modification 3
Modification 3 adds a new CheckConflictHandler object to check schedule conflicts.
New event,EV_CHECK_COURSE_CONFLICT, was added. Checking algorithm was extracted from RegisterStudentHandle, 
and the CheckConflictHandler and RegisterStudentHandle has been chained together in a series of EVENTs.