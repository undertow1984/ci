package utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.google.common.base.Function;
import com.perfecto.splunk.ReportingManager;

public class library {

	public RemoteWebDriver driver;
	private String target = "";
	private int step = 0;
	private int lastStep = 0;
	private String network;
	private String networkLatency;
	private String testName;
	private Properties properties = new Properties();
	private Wait<RemoteWebDriver> wait;

	public enum byFields {
		id, name, css, tag, className, linkText, partialLinkText, xpath
	}

	public enum availableContexts {
		WEBVIEW, NATIVE_APP, VISUAL
	}

	public enum prop {
		amazonSearchBox, amazonAddToCart, amazonProceedToCheckOut, amazonCartIcon, amazonDeleteFromCart, tdaLoginButton, tdaUsernameField, tdaPasswordField, tdaUsername, tdaPassword, tdaTradeButton, tdaOptionsButton, tdaSymbolField, tdaCompanyNameField, tdaCompanyNameShort, tdaCompanyNameFull, tdaOptionChainButton, tdaOptionChainSelection, sleepObject1, sleepObject2, sleepObject3, sleepVerifyObject, sleepNotFound
	}

	public library(RemoteWebDriver driver, String target, int step, String network, String networkLatency) {
		this.driver = driver;
		this.target = target;
		this.step = step;
		this.network = network;
		this.networkLatency = networkLatency;
	}

	public library(RemoteWebDriver driver) {
		this.driver = driver;
	}

	public void setFluentWait(int timeout, long polling) {
		wait = new FluentWait<RemoteWebDriver>(getDriver()).withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(polling, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
	}

	public WebElement getElementFluent(final byFields by, final String element) {
		WebElement we = wait.until(new Function<RemoteWebDriver, WebElement>() {
			public WebElement apply(RemoteWebDriver driver) {
				return driver.findElement(getBy(by, element));

			}
		});

		log("getFluentElement " + getBy(by, element) + " " + we, false);

		return we;
	}

	public void setImplicit(int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	public void clearImplicit() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	// read in Properties
	public void loadPropertyFile(String fileName) throws IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		properties.load(inputStream);
	}

	public String getProp(prop propName) {
		return properties.getProperty(propName.toString());
	}

	// used by testNG beforeMethod to set the testName
	public void setTestName(String test) {
		testName = test;
	}

	// returns the testName
	public String getTestName() {
		return testName;
	}

	// gets the Network
	public String getNetwork() {
		return network;
	}

	// gets the network latency
	public String getNetworkLatency() {
		return networkLatency;
	}

	// can be called from test to automatically fail for debug purposes
	public void autoFail() {
		Assert.assertEquals("1", "2");
	}

	// returns the string value setup in testNG for the targetEnvironment
	public String getTarget() {
		return target;
	}

	// returns the current step
	public int getStep() {
		return step;
	}

	// sets network virtualization
	public Object startNetworkVirtualization(String profile, String latency) {
		log("setting network settings: " + profile + "_" + latency, true);
		String command = "mobile:vnetwork:start";
		Map<String, Object> params = new HashMap<>();
		params.put("profile", profile);
		params.put("latency", latency);
		Object result = driver.executeScript(command, params);
		return result;
	}

	// updates network virtualization
	public Object updateNetworkVirtualization(String profile, String latency) {
		log("updating network settings: " + profile + "_" + latency, true);
		String command = "mobile:vnetwork:update";
		Map<String, Object> params = new HashMap<>();
		params.put("profile", profile);
		params.put("latency", latency);
		Object result = driver.executeScript(command, params);
		return result;
	}

	// stops network virtualization
	public Object stopNetworkVirtualization() {
		log("stopping network settings", true);
		String command = "mobile:vnetwork:stop";
		Map<String, Object> params = new HashMap<>();
		Object result = driver.executeScript(command, params);
		return result;
	}

	// checks if current test is running on a device
	public Boolean isDevice() {
		try {
			if (driver.getCapabilities().getCapability("platformName").equals("Android")
					|| driver.getCapabilities().getCapability("platformName").equals("iOS")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	// returns the driver
	public RemoteWebDriver getDriver() {
		return driver;
	}

	// switch driver method
	// switches dom between native and web and visual
	// "WEBVIEW", "NATIVE_APP" or "VISUAL"
	public RemoteWebDriver switchToContext(availableContexts context) {

		log("switchContext: " + context, false);

		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<>();
		params.put("name", context.toString());
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
		return driver;
	}

	// get current driver context
	public String getCurrentContextHandle() {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context = (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	// get a list of all available contexts
	public List<String> getContextHandles() {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts = (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}

	// sleeps current thread
	public void sleep(long millis) {

		log("sleep: " + millis, false);

		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	// can exit driver during exception although afterTest from testNG should
	// handle this in almost all cases
	public void errorCleanup() {
		driver.close();
		driver.quit();
	}

	// will return the appropriate by object based on string
	public By getBy(byFields by, String element) {
		if (by == byFields.id) {
			return By.id(element);
		} else if (by == byFields.name) {
			return By.name(element);
		} else if (by == byFields.css) {
			return By.cssSelector(element);
		} else if (by == byFields.tag) {
			return By.tagName(element);
		} else if (by == byFields.className) {
			return By.className(element);
		} else if (by == byFields.linkText) {
			return By.linkText(element);
		} else if (by == byFields.partialLinkText) {
			return By.partialLinkText(element);
		} else if (by == byFields.xpath) {
			return By.xpath(element);
		} else {
			return By.id("");
		}
	}

	// close native application
	// this needs to be further expanded out to support all available methods
	// this is not used and just here for future purposes
	public void closeApplication(String application) {
		String command = "mobile:application:close";
		Map<String, Object> params = new HashMap<>();
		params.put("name", application);
		driver.executeScript(command, params);
	}

	// close native application
	// this needs to be further expanded out to support all available methods
	// this is not used and just here for future purposes
	public void startApplication(String application) {
		String command = "mobile:application:open";
		Map<String, Object> params = new HashMap<>();
		params.put("name", application);
		driver.executeScript(command, params);
	}

	public long timerGet(String timerType) {
		String command = "mobile:timer:info";
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", timerType);
		long result = (long) driver.executeScript(command, params);
		return result;
	}

	// returns ux timer
	public long getUXTimer() {
		return timerGet("ux");
	}

	public void takeScreen(String text, Boolean addReport) {
		if(lastStep!=step)
		{
			if(isDevice())
			{			
				long time = getUXTimer();	
				ReportingManager.getReporting().reporting.put("testType", "MobileDevice");
				//builds the steps json in reporting class
				//params
				//high level step and/or step name - in my usage i do Step_1 / Step_2 etc
				//step description / more details step details
				//response time of the step in question
				//pass fail on the backend will be determined by the sla submitted upon setup
				ReportingManager.getReporting().setStepDetails("Step_" + step, text, time);
			}
			else
			{
				ReportingManager.getReporting().reporting.put("testType", "Desktop");
				ReportingManager.getReporting().setStepDetails("Step_" + step,text, null);
			}
		}
		
		log(text, false);

			// set file name and destination for screen shot
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			DateFormat dateFormat = new SimpleDateFormat(
					"dd_MMM_yyyy__hh_mm_ssaa");
			String destDir = "./surefire-reports/html/screenshots/";
			new File(destDir).mkdirs();
			String destFile = testName + "_" + target + "_" + getNetwork()
					+ "_Step" + step + "_" + dateFormat.format(new Date())
					+ ".png";

			// copy screen shot to directory for jenkins
			try {
				FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			log("screenShot: " + destDir + "/" + destFile, false);
			// Display screenshot to ReportNG
			if (addReport) {

				String userDirector = "./screenshots/";
				log("<u><b>||||||" + text + "</b></u><br><a href=\""
						+ userDirector + destFile + "\"><img src=\""
						+ userDirector + destFile + "\" alt=\"\""
						+ "height='100' width='100'/> " + "<br />", addReport);
			}
		
	}

	// writes to console or/and report log
	// boolean controls whether report log is written to
	public void log(String text, Boolean addReport) {
		String newLine = System.getProperty("line.separator");

		if (addReport) {
			final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";
			System.setProperty(ESCAPE_PROPERTY, "false");
			Reporter.log(text.replace("<u><b>||||||", "<u><b>" + testName + "_"
					+ target + "_" + getNetwork() + "_Step" + step + "_"));
		} else {
			System.out.println(testName + "_" + target + "_" + getNetwork()
					+ "_Step" + step + "_" + text + newLine);
		}
	}

	// Calls downloadreport, copys the perfecto report to the screen directory
	// boolean will add the report to the testNG report
	public void downloadReportDisplay(Boolean addReport) throws IOException {

		if (isDevice()) {
			// set file format and destination for report
			DateFormat dateFormat = new SimpleDateFormat(
					"dd_MMM_yyyy__hh_mm_ssaa");
			String destDir = "./surefire-reports/html/screenshots/";
			new File(destDir).mkdirs();
			String destFile = dateFormat.format(new Date());

			// download report
			downloadReport("pdf", destDir , destFile);
			// Display screenshot to ReportNG
			String userDirector = "./screenshots/";

			String destFileNew = destFile + ".pdf";

			log("perfectoReport: " + userDirector + destFileNew, false);
			if (addReport) {
				log("<a href=\"" + userDirector + destFileNew
						+ "\">Perfecto Report</a><br />", addReport);
			}
		}
	}

	// download report from perfecto
	private void downloadReport(String type, String fileLocation, String file)
			throws IOException {
		if (isDevice()) {
			
			
				// downloads report for remote web driver
				String command = "mobile:report:download";
				Map<String, Object> params = new HashMap<>();
				params.put("type", type);
				String report;				
					report = (String) getDriver().executeScript(
							command, params);
					
				//download to directory for jenkins
				File reportFile = new File(fileLocation + "/" + file + "." + type);
				BufferedOutputStream output = new BufferedOutputStream(
						new FileOutputStream(reportFile));
				byte[] reportBytes = OutputType.BYTES
						.convertFromBase64Png(report);
				output.write(reportBytes);
				output.close();
								
			
			}
			
		
	}

	// sets the initial page for the browser
	public void goToPage(String url, String title) {
		driver.get(url);
		waitForTitle(10, title);
		takeScreen("goToPage: " + url + "_" + title, true);
		step++;
	}

	// sets the initial page for the browser
	public void goToPage(String url) {
		driver.get(url);
		takeScreen("goToPage: " + url, true);
		step++;
	}

	// gets the current url
	public String getUrl() {
		String url = "can't find url";
		try {
			url = driver.getCurrentUrl().toString();
		} catch (Exception ex) {

		}
		return url;
	}

	// will attempt to wait on the page to load and searches for the title
	// supplied
	public void waitForTitle(int seconds, String title) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.titleContains(title));
		} catch (Exception ex) {

		}
	}

	// will attempt to wait on the page to load and searches for the element
	// supplied
	public WebElement waitForElement(int seconds, byFields by, String element) {
		WebElement we = null;
		if (seconds != 0) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, seconds);
				we = wait.until(ExpectedConditions.elementToBeClickable(getBy(by, element)));
			} catch (Exception ex) {
			}

		}
		return we;
	}

	// will attempt to wait on the page to load and searches for the element
	// supplied
	public void waitForElementToDisappear(int seconds, byFields by, String element) {
		if (seconds != 0) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, seconds);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(getBy(by, element)));
			} catch (Exception ex) {
			}
		}
	}

	// will attempt to wait on the page to load and searches for the element
	// supplied
	public void waitForElementToDisappearFluent(int seconds, byFields by, String element) {
		if (seconds != 0) {
			try {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(getBy(by, element)));
			} catch (Exception ex) {
			}
		}
	}

	// returns an element
	public WebElement getElement(byFields by, String element) {
		WebElement we = null;
		try {
			we = driver.findElement(getBy(by, element));
		} catch (Exception ex) {

		}

		return we;
	}

	// returns an element
	public int getElements(byFields by, String element) {

		try {
			driver.findElements(getBy(by, element));
		} catch (Exception ex) {

		}

		return driver.findElements(getBy(by, element)).size();
	}

	// checks if element exists
	public Boolean elementExists(byFields by, String element) {

		try {
			driver.findElement(getBy(by, element));
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	// clears text field
	public void clearText(byFields by, String element, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).clear();
			takeScreen("clearText: " + "_" + by + "_" + element, true);
			step++;
		} catch (Exception ex) {

		}

	}

	// sets text field value and will clear the field prior to writing based on
	// the clear boolean
	public void setText(byFields by, String element, String data, Boolean clear, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			if (clear) {
				clearText(by, element, timeOut);
			}
			driver.findElement(getBy(by, element)).sendKeys(data);
			waitForElement(timeOut, by, element);
			takeScreen("setText: " + by + "_" + element + "_" + data + "_" + clear, true);
			step++;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	// gets the text of an element
	public String getText(byFields by, String element, int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).getText().toString();
		} catch (Exception ex) {

		}
		return driver.findElement(getBy(by, element)).getText().toString();
	}

	// gets the value of an element
	public String getValue(byFields by, String element, int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).getAttribute("value");
		} catch (Exception ex) {

		}
		return driver.findElement(getBy(by, element)).getAttribute("value");
	}

	// clicks and element
	public void clickElement(byFields by, String element, int timeOut) {
		if (timeOut != 0) {
			waitForElement(timeOut, by, element).click();
		} else {
			driver.findElement(getBy(by, element)).click();
		}

		takeScreen("clickElement: " + by + "_" + element, true);
		step++;

	}

	// will submit a form based on an element
	public void submitElement(byFields by, String element, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).submit();
			waitForElement(timeOut, by, element);
			takeScreen("submitElement: " + by + "_" + element, true);
			step++;
		} catch (Exception ex) {

		}

	}

	// sets a drop down field based on a text
	public void setDropDownText(byFields by, String element, String text, int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			new Select(driver.findElement(getBy(by, element))).selectByVisibleText(text);
			waitForElement(timeOut, by, element);
			takeScreen("setDropDownText: " + by + "_" + element + "_" + text, true);
			step++;

		} catch (Exception ex) {

		}

	}

	// sets drop down field based on value
	public void setDropDownValue(byFields by, String element, String text, int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			new Select(driver.findElement(getBy(by, element))).selectByValue(text);
			waitForElement(timeOut, by, element);
			takeScreen("setDropDownValue: " + by + "_" + element + "_" + text, true);
			step++;
		} catch (Exception ex) {

		}
	}

	// gets windows size of desktop browser
	public String getWindowSize() {

		try {
			driver.manage().window().getSize().toString();
		} catch (Exception ex) {

		}

		return driver.manage().window().getSize().toString();
	}

	// gets windows position of desktop browser
	public String getWindowPosition() {

		try {
			driver.manage().window().getPosition();
		} catch (Exception ex) {

		}

		return driver.manage().window().getPosition().toString();
	}

	// gets maximizes window of desktop browser
	public void maximizeWindow() {

		try {
			driver.manage().window().maximize();
			takeScreen("maximizeWindow", true);
			step++;
		} catch (Exception ex) {

		}
	}

	// sets windows position of desktop browser
	public void setWindowPosition(int x, int y) {

		try {
			driver.manage().window().setPosition(new Point(x, y));
			takeScreen("setWindowPosition: " + x + "," + y, true);
			step++;
		} catch (Exception ex) {

		}
	}

	// sets windows size of desktop browser
	public void setWindowSize(int x, int y) {

		try {
			driver.manage().window().setSize(new Dimension(x, y));
			takeScreen("setWindowSize: " + x + "," + y, true);
			step++;
		} catch (Exception ex) {

		}
	}
}
