package com.carolinabaquero.test.core.utils.reports;
import com.carolinabaquero.test.core.utils.reports.CustomReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

/**
 * @author cbaquero
 */
public class ReporterTestListener implements ITestListener {

    /**
     * Returns a string representation of the object.
     * In general, the toString method returns a string that "textually represents" each object in the array.
     * The result should be a concise but informative representation that is easy for a person to read.
     * It is recommended that all objects in the parameters array override this method.
     *
     * @param parameters array of Objects
     * @return a String with all the parameters as key:value.
     */
    private String parametersToString(Object[] parameters) {
        String allParams = "[none]";
        if ((parameters != null) && (parameters.length != 0)) {
            allParams = "";
            for (Object param : parameters) {
                allParams += param + ",";
            }
        }
        return allParams;
    }

    /* (non-Javadoc)
     * @see org.testng.ITestListener#onTestStart(org.testng.ITestResult)
     */
    @Override
    public void onTestStart(ITestResult result) {
        //Flush trace buffer to log information for current test
        CustomReporter.flushTraceBuffer();

        //Log test start
        CustomReporter.debug("");
        CustomReporter.debug("[TEST START]" + getTestInfo(result));
    }

    /* (non-Javadoc)
     * @see org.testng.ITestListener#onTestSuccess(org.testng.ITestResult)
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        CustomReporter.info("[TEST PASSED] " + getTestInfo(result));
    }

    /* (non-Javadoc)
     * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
     */
    @Override
    public void onTestFailure(ITestResult result) {
        //report failure
        CustomReporter.info("[TEST FAILED] " + getTestInfo(result));
        CustomReporter.info(" >> Caused by : " + result.getThrowable());

        //if debug information was not shown before, dump trace when an error occurs
        if (!CustomReporter.isDebugEnabled()) {
            CustomReporter.printTraceBuffer();
        }
    }

    /**
     * Gets the test information from TestNG {@link ITestResult}
     *
     * @param result test result from TestNG execution
     * @return tetsInfo test name, description and parameters formatted in a String
     */
    private String getTestInfo(ITestResult result) {
        ITestNGMethod method = result.getMethod();

        //set test name and description
        StringBuilder testInfo = new StringBuilder();
        testInfo.append("Test Name: ");
        testInfo.append(method.getMethodName());
        testInfo.append(" - Description: ");
        testInfo.append(method.getDescription());
        testInfo.append(" Parameters: ");
        testInfo.append(parametersToString(result.getParameters()));

        return testInfo.toString();
    }

    /* (non-Javadoc)
     * @see org.testng.ITestListener#onTestSkipped(org.testng.ITestResult)
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        CustomReporter.info("[TEST  SKIPPED] " + result.getName());
    }

    /* (non-Javadoc)
     * @see org.testng.ITestListener#onTestFailedButWithinSuccessPercentage(org.testng.ITestResult)
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // nothing to do here, for noe
    }

    /**
     * Invoked before the test class is instantiated and before any configuration method is called.
     * @see org.testng.ITestListener#onStart(org.testng.ITestContext)
     */
    @Override
    public void onStart(ITestContext context) {
        CustomReporter.debug("[TEST CLASS " + context.getClass() + " STARTED]");
    }

    /**
     * Invoked after all the tests have run and all their Configuration methods have been called.
     * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onFinish(ITestContext context) {
        //If some warning was logged during the execution print a final warning.
        if (CustomReporter.warningReported()) {
            CustomReporter.debug("");
            CustomReporter.debug("--------------------");
            CustomReporter.debug("[EXECUTION WARNING]");
            CustomReporter.printWarningNotice();
        }
    }
}
