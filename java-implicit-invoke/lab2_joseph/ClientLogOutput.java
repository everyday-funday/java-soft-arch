package lab2_joseph;

import java.io.*;
import java.util.Observable;
import java.util.Observer;


/**
 * Modification 1 -
 *
 * ClientLogOutput Observer subscribes to the EV_SHOW event.
 * When the event is triggered, it logs the outputs into LogOutput.txt file.
 *
 * @autor Joseph Lee
 */
public class ClientLogOutput implements Observer {


    private static PrintWriter out;

    /**
     * Constructs a log output component. A new log output component subscribes to show events
     * at the time of creation, and it outputs the file into a Log
     */
    public ClientLogOutput(){

        // Subscribe to EV_SHOW EVENT.
        EventBus.subscribeTo(EventBus.EV_SHOW,this);

        // Initialize the output writer
        out = Logger.createPrintWriter("/log/LogOutput.txt");
    }

    /**
     * on each EV_SHOW events, received output is writen to the log
     */
    @Override
    public void update(Observable o, Object param) {
        out.println((String) param);
        out.flush();
    }

    /**
     * close the writer when finished
     */
    public static void close(){ out.close(); }
}
