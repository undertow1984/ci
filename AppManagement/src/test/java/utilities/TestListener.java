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

public class TestListener implements ISuiteListener, ITestListener, IInvokedMethodListener {

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult arg0) {
	}

	@Override
	public void onFinish(ISuite arg0) {
	}

	@Override
	public void onStart(ISuite arg0) {
	}

	@Override
	public void onFinish(ITestContext arg0) {
	}

	@Override
	public void onStart(ITestContext arg0) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		setDetails("Pass", arg0);
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		String result = "Fail";
		setDetails(result, arg0);
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		setDetails("Skip", arg0);
	}

	public void setDetails(String result, ITestResult testResult) {
		// gets the instance of the ClassHelper associated with the thread
		Object currentClass = testResult.getMethod().getInstance();
		ClassHelper classHelper = ((ClassHelper) currentClass);

	}
}
