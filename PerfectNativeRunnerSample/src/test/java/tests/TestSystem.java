package tests;

import java.lang.reflect.Method;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import PerfectoNativeRunner.PerfectoRunner;

public class TestSystem {

	// the map to store the test results
	private Map<PerfectoRunner.availableReportOptions, Object> testResults = new HashMap<PerfectoRunner.availableReportOptions, Object>();

	// Executes the Native Tests
	// define Perfecto and Script details
	// Params
	// @1 Perfecto Host
	// @2 Perfecto username
	// @3 Perfecto password
	// @4 Perfect script key "Private:yourscript or Private:directory/yourscript
	// @5 Device Id to execute the script
	// @6 Additional Params -- the format for the parameters
	// @7 Number of times to loop and wait for the completion of the script ---
	// suggested value in the 1000s
	// @8 Number of milliseconds to wait between each status check of the script
	// ---- suggest 5000 milliseconds
	public void bleh(String host, String username, String password, String scriptKey, String deviceId,
			String additionalParams, int cycles, long waitForCycles) throws Exception {

		PerfectoRunner pr = new PerfectoRunner();
		String xml = pr.getXMLReport(host, username, password,
				"PRIVATE:Prod/NativeRunner/160923/01_P1-K-SG5-FTW_P1-N-SG7-FTW_16-09-23_01_12_05_55379.xml");

		testResults = pr.parseReport(xml, host);

		long startTime = Long
				.parseLong(testResults.get(PerfectoRunner.availableReportOptions.scriptStartTime).toString());

		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		String dateTime = sdf.format(new Date(startTime));
		String testCaseId = testResults.get(PerfectoRunner.availableReportOptions.scriptName).toString();
		String MODevicePh = pr.getXPathValue(xml,
				"(execution/input/handsets/handset)[1]/properties/property/name[@displayName=\"Phone Number\"]/following-sibling::value");
		String MTDevicePh = pr.getXPathValue(xml,
				"(execution/input/handsets/handset)[2]/properties/property/name[@displayName=\"Phone Number\"]/following-sibling::value");
		String testDuration = testResults.get(PerfectoRunner.availableReportOptions.scriptTimerElapsed).toString();

		Table<String, String, String> transactions = (Table<String, String, String>) testResults
				.get(PerfectoRunner.availableReportOptions.transactions);
		String callSetupTime = "";
		for (Cell<String, String, String> cell : transactions.cellSet()) {
			System.out.println("transactionSuccess:" + cell.getValue());
			System.out.println("transactionName:" + cell.getRowKey());
			System.out.println("transactionTimer: " + cell.getColumnKey());
			callSetupTime = cell.getColumnKey();
		}

		Map<String, String> variables = new HashMap<String, String>();

		variables = (Map<String, String>) testResults.get(PerfectoRunner.availableReportOptions.variables);

		String callDuration = variables.get("callTime");

		String reportUrl = testResults.get(PerfectoRunner.availableReportOptions.reportUrl).toString();

		try (FileWriter writer = new FileWriter("D:\\data\\sprintFile.csv", true)) {

			writer.write(
					"\"DateTime\",\"TestCaseID\",\"MODevicePh\",\"MTDevicePh\",\"CallConnected\",\"CallDisconnected\",\"LastedDurationoftest\",\"CallDuration\",\"CallSetuptime\",\"LogsLinks\"\r\n");

			int counter = 0;

			String out = "\"" + dateTime + "\",\"" + testCaseId + "\",\"" + MODevicePh + "\",\"" + MTDevicePh
					+ "\",\"pass\",\"pass\",\"" + testDuration + "\",\"" + callDuration + "\",\"" + callSetupTime
					+ "\",\"" + reportUrl + "\r\n";

			writer.write(out);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Parameters({ "host", "username", "password", "scriptKey", "deviceId", "additionalParams", "cycles",
			"waitForCycles" })
	@Test
	public void NativeTests(String host, String username, String password, String scriptKey, String deviceId,
			String additionalParams, int cycles, long waitForCycles) throws Exception {
		// executes the script and gathers the test results
		PerfectoRunner pr = new PerfectoRunner();
		// Executes the Native Tests
		// define Perfecto and Script details
		// Params
		// @1 Perfecto Host
		// @2 Perfecto username
		// @3 Perfecto password
		// @4 Perfect script key "Private:yourscript or
		// Private:directory/yourscript
		// @5 Device Id to execute the script
		// @6 Additional Params -- the format for the parameters
		// &param.url=m.newegg.com
		// @7 Number of times to loop and wait for the completion of the script
		// ---
		// suggested value in the 1000s
		// @8 Number of milliseconds to wait between each status check of the
		// script
		// ---- suggest 5000 milliseconds
		testResults = pr.executeScript(host, username, password, scriptKey, deviceId, additionalParams, cycles,
				waitForCycles);

		// to grab the test results use the following syntax
		// PerfectoRunner.availableReportOptions.<selected report
		// options>.toString()

		long startTime = Long
				.parseLong(testResults.get(PerfectoRunner.availableReportOptions.scriptStartTime).toString());

		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		String dateTime = sdf.format(new Date(startTime));
		String testCaseId = testResults.get(PerfectoRunner.availableReportOptions.scriptName).toString();
		String MODevicePh = pr.getXPathValue(
				testResults.get(PerfectoRunner.availableReportOptions.xmlReport).toString(),
				"(execution/input/handsets/handset)[1]/properties/property/name[@displayName=\"Phone Number\"]/following-sibling::value");
		
		String testDuration = testResults.get(PerfectoRunner.availableReportOptions.scriptTimerElapsed).toString();

		Table<String, String, String> transactions = (Table<String, String, String>) testResults
				.get(PerfectoRunner.availableReportOptions.transactions);
		String callSetupTime = "";
		String callConnected = "";
		String callDuration = "";
		String callDisconnected = "";
		for (Cell<String, String, String> cell : transactions.cellSet()) {
			System.out.println("transactionSuccess:" + cell.getValue());
			System.out.println("transactionName:" + cell.getRowKey());
			System.out.println("transactionTimer: " + cell.getColumnKey());

			if (cell.getRowKey().equalsIgnoreCase("Call received MOMT")) {
				if (cell.getValue().equalsIgnoreCase("true")) {
					callConnected = "Yes";
					callSetupTime = cell.getColumnKey();
				} else {
					callConnected = "No";
					callSetupTime = cell.getColumnKey();
				}
			}

			if (cell.getRowKey().equalsIgnoreCase("Call duration MOMT")) {
				if (cell.getValue().equalsIgnoreCase("true")) {
					callDisconnected = "Yes";
					callDuration = cell.getColumnKey();
				} else {
					callDisconnected = "No";
					callDuration = cell.getColumnKey();
				}
			}

		}

		Map<String, String> variables = new HashMap<String, String>();

		/*
		 * variables = (Map<String, String>)
		 * testResults.get(PerfectoRunner.availableReportOptions.variables);
		 * 
		 * String callDuration = variables.get("callTime");
		 */

		String reportUrl = testResults.get(PerfectoRunner.availableReportOptions.reportUrl).toString();
		
		System.out.println(testResults.get(PerfectoRunner.availableReportOptions.exception).toString());

		/*try (FileWriter writer = new FileWriter("D:\\data\\sprintFile.csv", false)) {

			writer.write(
					"\"DateTime\",\"TestCaseID\",\"MODevicePh\",\"MTDevicePh\",\"CallConnected\",\"CallDisconnected\",\"LastedDurationoftest\",\"CallDuration\",\"CallSetuptime\",\"LogsLinks\"\r\n");

			int counter = 0;

			String out = "\"" + dateTime + "\",\"" + testCaseId + "\",\"" + MODevicePh + "\",\"" + MTDevicePh + "\",\""
					+ callConnected + "\",\"" + callDisconnected + "\",\"" + testDuration + "\",\"" + callDuration
					+ "\",\"" + callSetupTime + "\",\"" + reportUrl + "\r\n";

			writer.write(out);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}