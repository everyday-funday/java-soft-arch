package System_B;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logger {

//    public static String dateTime() {
//        // yyyy-mm-ddTHH:mm:ss
//        return LocalDateTime.now().toString().split("[.]")[0];
//    }

//    public static void logi(String s) {
//        System.out.println(dateTime() + " INFO: " + s);
//    }
//
//    public static void logit(String s) {
//        System.out.println(dateTime() + " INFO: " + Thread.currentThread().getName() + ": " + s);
//    }

    public static PrintWriter createCSVWriter(String filepath) {
        File file = new File(filepath);

        if(file.exists()){
            // delete the file if it already exists.
            file.delete();
        }

        String test = file.getPath();
        try {
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
