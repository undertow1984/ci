package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.xml.sax.SAXException;

public abstract class ClassHelper {
	private RemoteWebDriverExtended driver;
	public Library lib;
	private TestSetup tes;
	private static String build;
	private static String repoKey;
	public String appName;
	@BeforeSuite
	@Parameters({ "targetEnvironment", "perfectoHost", "perfectoUsername", "perfectoPassword", "repoKey", "jenkinsHost",
			"jenkinsPort","jenkinsJobName" })
	public void beforeSuite(String targetEnvironment, String perfectoHost, String perfectoUsername,
			String perfectoPassword, String repo, String jenkinsHost, String jenkinsPort, String jenkinsJobName) {

		Object lock = new Object();

		synchronized (lock) {
			HttpClient hc = new HttpClient();
			String response = "";
			try {
				response = hc.sendRequest(
						"http://" + jenkinsHost + ":" + jenkinsPort + "/job/"+jenkinsJobName+"/lastStableBuild/api/xml");
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				build = hc.getXMLValue(response, "fileName");
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String localBuildPath = "builds/" + build;

			repoKey = build;

			File buildFile = new File(localBuildPath);
			if (!buildFile.exists()) {

				try {
					hc.download("http://" + jenkinsHost + ":" + jenkinsPort + "/job/"+jenkinsJobName+"/lastStableBuild/artifact/" + build,
							localBuildPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				if (!hc.repoFileExists(perfectoHost, perfectoUsername, perfectoPassword, repo, build)) {
					try {
						hc.uploadMedia(perfectoHost, perfectoUsername, perfectoPassword, buildFile, repoKey);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@AfterSuite
	public void afterSuite() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Parameters({ "targetEnvironment", "perfectoHost", "perfectoUsername", "perfectoPassword", "instrument", "appName" })
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method, String targetEnvironment, String perfectoHost, String perfectoUsername,
			String perfectoPassword, boolean instrument, String appName) {
		this.appName=appName;
		// initializes testSetup class
		tes = new TestSetup(targetEnvironment, perfectoHost, perfectoUsername, perfectoPassword, driver);
		// sets up the testNG flows based on testsuite.xml
		tes.flowControl();

		try {
			lib = tes.driverAndLibrarySetup();
		} catch (UnsupportedEncodingException | MalformedURLException | InterruptedException e) {
			e.printStackTrace();
		}

		lib.installApp(repoKey, false);
		
		lib.closeApp(appName);
		lib.launchApp(appName);		

		String testName = method.getDeclaringClass().getSimpleName() + "::" + method.getName();

		lib.setTestName(method.getName());
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult testResult, ITestContext tcc) throws Exception {

		try {
			lib.getDriver().close();
		} catch (Exception ex) {

		}

		try {
			lib.getDriver().quit();

		} catch (Exception ex) {

		}
	}
}
