package utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IInvokedMethodListener2;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.internal.thread.IExecutor;
import org.testng.internal.thread.ThreadUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfecto.splunk.ReportingFactory;
import com.perfecto.splunk.ReportingManager;
import com.perfecto.splunk.SplunkReporting;

public class TestListener implements ISuiteListener, ITestListener, IInvokedMethodListener {

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult arg0) {
		
	}
	
	
	@Override
	public void onFinish(ISuite arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ISuite arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext arg0) {

	}

	@Override
	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		// TODO Auto-generated method stub
		String result = "Fail";

		setDetails(result, arg0);



	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		setDetails("Skip", arg0);
	}

	public void setDetails(String result, ITestResult testResult) {
		Object currentClass = testResult.getMethod().getInstance();
		ClassHelper classHelper = ((ClassHelper) currentClass);

		if (result.equalsIgnoreCase("Skip")) {
			classHelper.setSplunk();
		}

		SplunkReporting splunkReport = classHelper.getReport();

		// Allows you to put any value into the reporting hashMap for submission
		// to splunk
		splunkReport.reporting.put("testStatus", result);

		splunkReport.reporting.put("className", testResult.getMethod().getInstance().getClass().getName());

		try {
			splunkReport.reporting.put("hostName", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!result.equalsIgnoreCase("Skip")) {
			if (classHelper.lib.isDevice()) {
				splunkReport.reporting.put("model",
						(String) classHelper.lib.driver.getCapabilities().getCapability("model"));
				splunkReport.reporting.put("device",
						(String) classHelper.lib.driver.getCapabilities().getCapability("deviceName"));
				splunkReport.reporting.put("os",
						(String) classHelper.lib.driver.getCapabilities().getCapability("platformName"));
			} else {
				splunkReport.reporting.put("device",
						(String) classHelper.lib.driver.getCapabilities().getPlatform().family().name());
				splunkReport.reporting.put("os",
						(String) classHelper.lib.driver.getCapabilities().getPlatform().name());
				splunkReport.reporting.put("browser",
						(String) classHelper.lib.driver.getCapabilities().getBrowserName());
				splunkReport.reporting.put("browserVersion",
						(String) classHelper.lib.driver.getCapabilities().getVersion());
			}

		}

		if (result.equalsIgnoreCase("Fail")) {
			if (testResult.getThrowable() != null)
				if (testResult.getThrowable().getStackTrace() != null) {
					StringWriter sw = new StringWriter();
					testResult.getThrowable().printStackTrace(new PrintWriter(sw));
					splunkReport.reporting.put("stackTrace", sw.toString());
				}
		}

		if (!result.equalsIgnoreCase("Skip")) {
			// Sets the end time of the test
			// Divides the start and end time to create a test duration in
			// seconds
			// and finally converts the start/end time to real date formats
			splunkReport.setTestExecutionEnd(System.currentTimeMillis());
		}

		splunkReport.reporting.put("testName", testResult.getTestContext().getName());
		splunkReport.reporting.put("methodName", testResult.getMethod().getMethodName());

		if (result.equalsIgnoreCase("Skip")) {
			// Submits the report to splunk in json format
			// Params
			// Title to append to the json
			// if null testName – methodName is used
			// otherwise custom title can be used

			splunkReport.submitReporting("PerfectoTest", "test", "test_index");
		}

	}

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub

		setDetails("Pass", arg0);
	}

}
