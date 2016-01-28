package utilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfecto.splunk.ReportingFactory;
import com.perfecto.splunk.ReportingManager;
import com.perfecto.splunk.SplunkReporting;

public abstract class ClassHelper {
	private RemoteWebDriver driver;
	public library lib;
	private testSetup tes;

	// Call this at the start of test to set the reporting class and define
	// splunk details
	// Params
	// SLA in milliseconds - this will define pass/fail threshold for steps
	// based on response time
	// Splunk host
	// Splunk port
	// Splunk username
	// Splunk password
	public void setSplunk() {
		SplunkReporting reporting = ReportingFactory.createInstance(2000, "52.70.207.153", 8089, "admin",
				"splunkDemo1234");
		ReportingManager.setReporting(reporting);
	}

	// Returns the reporting class for use
	public SplunkReporting getReport() {
		return ReportingManager.getReporting();
	}

	@Parameters({ "targetEnvironment", "network", "networkLatency" })
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method, String targetEnvironment, String network, String networkLatency) {
		setSplunk();
		// initializes testSetup class
		tes = new testSetup(targetEnvironment, driver, network, networkLatency);
		// sets up the testNG flows based on testsuite.xml
		tes.flowControl();

		try {
			lib = tes.driverAndLibrarySetup();
		} catch (UnsupportedEncodingException | MalformedURLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Sets the start time of the test
		getReport().setTestExecutionStart(System.currentTimeMillis());
		getReport().reporting.put("seleniumHost", tes.host);
		getReport().reporting.put("threadNo", Thread.currentThread().getName() + " " + Thread.currentThread().getId());

		lib.setTestName(method.getName());
		lib.log("testStarted", false);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult testResult, ITestContext tcc) throws IOException {
		try {
			if (lib.isDevice()) {
				Reporter.setCurrentTestResult(testResult);
				
				lib.getDriver().close();
				lib.downloadReportDisplay(true);

				getReport().reporting.put("executionID",
						(String) lib.getDriver().getCapabilities().getCapability("executionId"));
				getReport().reporting.put("reportKey",
						(String) lib.getDriver().getCapabilities().getCapability("reportKey"));
				getReport().reporting.put("perfectoReport",
						"https://" + tes.host + "/nexperience/Report.html?reportId=SYSTEM%3Adesigns%2Freport&key="
								+ (String) lib.getDriver().getCapabilities().getCapability("reportKey").toString()
										.replace(".xml", "")
								+ "%2Exml&liveUrl=rtmp%3A%2F%2F" + tes.host.replace(".", "%2E")
								+ "%2Fengine&appUrl=https%3A%2F%2F" + tes.host.replace(".", "%2E") + "%2Fnexperience");
				getReport().reporting.put("windTunnelReport",
						(String) lib.getDriver().getCapabilities().getCapability("windTunnelReportUrl"));
			} else {
				lib.getDriver().quit();
			}
		} catch (Exception ex) {

		}

		// Submits the report to splunk in json format
		if (!(testResult.getStatus() == ITestResult.SKIP)) {
			getReport().submitReporting(null, "test_index");
		}

		try {
			lib.getDriver().quit();
			
		} catch (Exception ex) {

		}
	}
}
