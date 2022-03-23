package lab2_joseph;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Logger.java
 *
 * Helper class that creates file writer outputs for logging.
 * Also handles cases with creating files and directories.
 *
 */
public class Logger {

    public static PrintWriter createPrintWriter(String filepath) {


        final String curr_dir = System.getProperty("user.dir");

        try {

            File file = new File(curr_dir + filepath);
            if(file.getParentFile().mkdir()){
                System.out.println("output folder created: " + file.getAbsolutePath());
            } else {
                System.out.println("directory create FAIL");
            }

            if(file.exists()){
                file.delete();
            }

            if (file.createNewFile()) {
                System.out.println("New file created: " + file.getAbsolutePath());
            } else {
                System.out.println("create" + file.getName() + "failed");
                return null;
            }
            return new PrintWriter(new FileWriter(file));

        } catch (IOException e) {
            System.out.println("Not found");
            return null;
        }
    }
}
