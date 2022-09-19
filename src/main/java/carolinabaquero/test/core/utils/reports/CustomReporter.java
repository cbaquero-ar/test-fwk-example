package com.carolinabaquero.test.core.utils.reports;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Reporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


/**
 * @author Carolina Baquero
 *         Logs all test messages in the console output as well as in Log4J file and
 *         TestNG Reporter output including date, message type and message.
 */
public class CustomReporter {

    private static boolean debug = false;
    private static Vector<String> traceBuffer = new Vector<String>();
    private static boolean warningReported = false;
    private static DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

    static final Logger logger = Logger.getLogger(CustomReporter.class);


    /**
     * Wrapper for TestNG Reporter.log(), includes formatted date and level
     * label
     *
     * @param levelLabel the level label to show in this reporter's log entry
     * @param msg        the message to show in the reporter's log
     */
    private static void reporterLog(final String levelLabel, final String msg) {

        Reporter.log(dateFormat.format(new Date()) + " - " + levelLabel + " - " + msg);
    }


    /**
     * Set DEBUG flag to active/inactive, to decide whether to print or not the
     * debug messages
     *
     * @param active
     */
    public static void setDebug(final boolean active) {

        debug = active;
        logger.setLevel(Level.DEBUG);

    }


    /**
     * DEFAULT LOGGING METHODS: info, debug and warning.
     */

    /**
     * Logs a message as an INFO message
     *
     * @param msg the info message
     */
    public static void info(final String msg) {

        logger.info(msg);
        reporterLog("INFO", msg);
        traceBuffer.add(" >> TRACE - " + msg);
    }


    /**
     * Logs the message as a WARNING and sets a flag to know this execution has
     * had warnings
     *
     * @param msg
     */
    public static void warning(final String msg) {

        warningReported = true;
        logger.warn(msg);
        reporterLog("WARNING", msg);
        traceBuffer.add(" >> TRACE - " + msg);
    }


    /**
     * Logs a DEBUG msg (only when debug flag is active)
     *
     * @param msg to log in debug output
     */
    public static void debug(final String msg) {

        if (debug) {
            logger.debug(msg);
            reporterLog("DEBUG", msg);
        } else {
            traceBuffer.add(" >> TRACE - " + msg);
        }
    }


    /**
     * Logs on ERROR level the msg passed as a parameter.
     *
     * @param msg
     */
    public static void error(final String msg) {

        logger.error(msg);
        reporterLog("ERROR", msg);
        traceBuffer.add(" >> TRACE - " + msg);
    }


    /**
     * Logs on ERROR level the msg passed as a parameter.
     *
     * @param msg
     */
    public static void error(final String msg, final Throwable e) {

        error(msg);
        e.printStackTrace();
    }


    /**
     * Says if debug is currently enabled for the custom reporter
     *
     * @return debug boolean value
     */
    public static boolean isDebugEnabled() {

        return debug;
    }


    /**
     * Says if some warning message was logged
     *
     * @return boolean value indicating if some war
     */
    public static boolean warningReported() {

        return warningReported;
    }


    /**
     * Clean the trace buffer
     */
    public static void flushTraceBuffer() {

        traceBuffer.removeAllElements();
    }


    /**
     * Prints the debug buffer at INFO log level
     */
    public static void printTraceBuffer() {

        CustomReporter.info(" >> Log Trace:");
        traceBuffer.removeElementAt(traceBuffer.size() - 1);
        for (String line : traceBuffer) {
            reporterLog("INFO", "      " + line);
            logger.info("      " + line);
        }

    }


    /**
     * If some warning message was reported, logs a WARNING notice.
     */
    public static void printWarningNotice() {

        if (warningReported) {
            warning(" >> Test displayed some warnings, please verify the log for details.");
        }
    }


}
