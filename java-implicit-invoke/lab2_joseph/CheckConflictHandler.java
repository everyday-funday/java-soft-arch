package lab2_joseph;

import java.util.ArrayList;
import java.util.StringTokenizer;


/***
 *  Modification 3 - Checking Registration Conflict
 *
 *  CheckConflictHandler get subscribed to EV_CHECK_COURSE_CONFLICT event.
 *  Whenever user decides to add a student to a class, this event is announced.
 *  It will check whether if existing schedule conflicst with the newly added class.
 *  Then output a null string (if conflict), or relay the data information (if no conflict).
 *  This handler is then outputted to the EV_REGISTER_STUDENT event which will trigger RegisterStudentHandler,
 *  which then will register if conflict was not detected.
 *
 * @author Joseph Lee
 */
public class CheckConflictHandler extends CommandEventHandler {


    /**
     * Constructs a command event handler.
     * Checks for strudent registration conflict.
     *
     * @param objDataBase    reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode  output event code to send the command processing result
     */
    public CheckConflictHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);

    }

    @Override
    protected String execute(String param) {

        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();

        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);

        // Check if the given course conflicts with any of the courses the student has registered.
        if (objStudent != null && objCourse != null)
        {
            ArrayList vCourse = objStudent.getRegisteredCourses();
            for (int i = 0; i < vCourse.size(); i++) {
                if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                    // return null string if conflict is detected.
                    return null;
                }
            }
        }

        return param;
    }
}
