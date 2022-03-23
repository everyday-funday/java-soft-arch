package lab2_joseph; /**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.util.StringTokenizer;


/**
 * "Register a student for a course" command event handler.
 */
public class RegisterStudentHandler extends CommandEventHandler {

    /**
     * Construct "Register a student for a course" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    public RegisterStudentHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);
    }

    /**
     * Process "Register a student for a course" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    protected String execute(String param) {

        if((String) param == null){
            return "Class registration conflict. ABORTING";
        }

        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();

        // Get the student and course records.
        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);

        if (objStudent == null) {
            return "Invalid student ID";
        }
        if (objCourse == null) {
            return "Invalid course ID or course section";
        }

        /**
         *  Registration conflict cases are handled in RegisterStudentConflictHandler
         */
//        // Check if the given course conflicts with any of the courses the student has registered.
//        ArrayList vCourse = objStudent.getRegisteredCourses();
//        for (int i=0; i<vCourse.size(); i++) {
//            if (((Course) vCourse.get(i)).conflicts(objCourse)) {
//                return "Registration conflicts";
//            }
//        }

        //TODO: Check conflict
        // Request validated. Proceed to register.


        /**
         *  Modification 2 -
         *
         *  Make registration now checks overbooked status within the Database class.
         *  makeARegistration method checks overbooked courses in the database,
         *  returns warning message when overbooked.
         */
        String message = this.objDataBase.makeARegistration(sSID, sCID, sSection);
        if(message != null){
            return "Successful!" + message;
        }

        return "Successful!" ;
    }
}