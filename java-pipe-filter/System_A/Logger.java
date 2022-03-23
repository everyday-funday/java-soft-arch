package System_A;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

    public static PrintWriter createCSVWriter(String filepath) {
        File file = new File(filepath);
        String test = file.getPath();

        try {
            if(file.exists()){
                // delete the file if it already exists.
                file.delete();
            }

            if (file.createNewFile()) {
                System.out.println(filepath + " created");
            } else {
                System.out.println(filepath + " create failed.");
                return null;
            }
            return new PrintWriter(new FileWriter(file));

        } catch (IOException e) {
            System.out.println("Not found");
            return null;
        }
    }
}
