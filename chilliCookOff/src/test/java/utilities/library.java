package utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.api.IMobileDriver;
import com.perfectomobile.selenium.api.IMobileWebDriver;

public class library {
	private Object driver;
	private IMobileDevice foundDevice;
	private String target = "";
	private int step = 0;
	private String network;
	private String networkLatency;
	private String testName;
	private Properties properties = new Properties();
	private Boolean local = false;
	private Boolean selenium = false;
	private Boolean device = false;
	private mobileDrivers currentMobile; 

	public enum seleniumD {
		True
	};

	public enum remoteD {
		True
	};

	public enum mobileD {
		True
	};
	
	public enum mobileDrivers{
       domDriver, visualDriver, nativeDriver
	}
	
	public void setCurrentMobileDriver(mobileDrivers md)
	{
		currentMobile = md;
	}
	
	public mobileDrivers getCurrentMobileDriver()
	{
		return currentMobile;
	}
	

	// page properties
	public enum prop {
		findBox, nearBox, searchButton, dollarDollar, highestRated, parkingLink, lotOption, streetOption, valetOption, garageOption, filterButton, iframe, highestRatedLink
	}

	// end properties

	public enum byFields {
		id, name, css, tag, className, linkText, partialLinkText, xpath,
	}

	public enum availableContexts {
		WEBVIEW, NATIVE_APP, VISUAL
	}

	public library(RemoteWebDriver driver) {
		this.driver = driver;
	}

	public library(RemoteWebDriver driver, String target, int step,
			String network, String networkLatency, Boolean device) {
		this.target = target;
		this.step = step;
		this.network = network;
		this.networkLatency = networkLatency;
		this.driver = driver;
		this.device = device;
	}

	public library(MobileDriver driver, String target, int step,
			String network, String networkLatency, Boolean local, Boolean device, IMobileDevice foundDevice) {
		this.target = target;
		this.step = step;
		this.network = network;
		this.networkLatency = networkLatency;
		this.driver = driver;
		this.device = device;
		this.foundDevice=foundDevice;
	}

	public library(WebDriver driver, String target, int step, String network,
			String networkLatency, Boolean selenium,
			Boolean device) {
		this.target = target;
		this.step = step;
		this.network = network;
		this.networkLatency = networkLatency;
		this.driver = driver;
		this.device = device;
	}
	
	public WebDriver getDriver(seleniumD a) {
		return (WebDriver) driver;
	}

	public IMobileWebDriver getDriver(mobileD a, mobileDrivers md) {
		if(md.equals(mobileDrivers.domDriver))
		{
		return ((IMobileDriver)driver).getDevice(foundDevice.getDeviceId()).getDOMDriver();
		}
		else if (md.equals(mobileDrivers.nativeDriver))
		{
			return ((IMobileDriver)driver).getDevice(foundDevice.getDeviceId()).getNativeDriver();
		}
		else
		{
			return ((IMobileDriver)driver).getDevice(foundDevice.getDeviceId()).getVisualDriver();
		}
		
		
	}

	public RemoteWebDriver getDriver(remoteD a) {
		return (RemoteWebDriver) driver;
	}
	

	// returns local variable
	public Boolean getLocal() {
		return local;
	}

	// returns selenium variable
	public Boolean getSelenium() {
		return selenium;
	}

	// read in Properties
	public void loadPropertyFile(String fileName) throws IOException {
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(fileName);
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
	
	//increment step
	public int incStep() {
		return step++;
	}

	// sets network virtualization
	public Object startNetworkVirtualization(String profile, String latency) {

		log("setting network settings: " + profile + "_" + latency, true);
		String command = "mobile:vnetwork:start";
		Map<String, Object> params = new HashMap<>();
		params.put("profile", profile);
		params.put("latency", latency);
		Object result = getDriver(remoteD.True).executeScript(command,
				params);
		return result;

	}

	// updates network virtualization
	public Object updateNetworkVirtualization(String profile, String latency) {

		log("updating network settings: " + profile + "_" + latency, true);
		String command = "mobile:vnetwork:update";
		Map<String, Object> params = new HashMap<>();
		params.put("profile", profile);
		params.put("latency", latency);
		Object result = getDriver(remoteD.True).executeScript(command,
				params);
		return result;

	}

	// stops network virtualization
	public Object stopNetworkVirtualization() {

		log("stopping network settings", true);
		String command = "mobile:vnetwork:stop";
		Map<String, Object> params = new HashMap<>();
		Object result = getDriver(remoteD.True).executeScript(command,
				params);
		return result;

	}

	// checks if current test is running on a device
	public Boolean isDevice() {
		if (device) {
			return true;
		} else {
			return false;
		}
	}

	// switch driver method
	// switches dom between native and web and visual
	// "WEBVIEW", "NATIVE_APP" or "VISUAL"
	public RemoteWebDriver switchToContext(availableContexts context) {

		log("switchContext: " + context, false);

		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(
				getDriver(remoteD.True));
		Map<String, String> params = new HashMap<>();
		params.put("name", context.toString());
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
		return (RemoteWebDriver) driver;

	}

	// get current driver context
	public String getCurrentContextHandle() {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(
				getDriver(remoteD.True));
		String context = (String) executeMethod.execute(
				DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	// get a list of all available contexts
	public List<String> getContextHandles() {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(
				getDriver(remoteD.True));
		List<String> contexts = (List<String>) executeMethod.execute(
				DriverCommand.GET_CONTEXT_HANDLES, null);
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
		if (selenium)
			getDriver(seleniumD.True).quit();
		else
		{
			getDriver(remoteD.True).close();
		getDriver(remoteD.True).quit();
		}

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
		String command = "mobile:application.close";
		Map<String, Object> params = new HashMap<>();
		params.put("name", application);
		getDriver(remoteD.True).executeScript(command, params);
	}

	// opens application
	public void openApplication(String application) {
		String command = "mobile:application.close";
		Map<String, Object> params = new HashMap<>();
		params.put("name", application);
		getDriver(remoteD.True).executeScript(command, params);
	}

	// takes screen shot of non-device platforms
	// String param is text associated with screen shot
	// boolean will either add to report or not
	public void takeScreen(String text, Boolean addReport) {

		log(text, false);

		if (!isDevice()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

			// copy screen shot to directory
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
	public void downloadReportDisplay(RemoteWebDriver driver, Boolean addReport)
			throws IOException {

		// set file format and destination for report
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
		String destDir = "./surefire-reports/html/screenshots/";
		new File(destDir).mkdirs();
		String destFile = dateFormat.format(new Date());

		// download report from driver
		downloadReport(driver, "pdf", destDir + "/" + destFile);

		// Display screenshot to ReportNG
		String userDirector = "./screenshots/";

		String destFileNew = destFile + ".pdf";

		log("perfectoReport: " + userDirector + destFileNew, false);
		if (addReport) {
			log("<a href=\"" + userDirector + destFileNew
					+ "\">Perfecto Report</a><br />", addReport);
		}
	}

	// download report from perfecto
	private void downloadReport(RemoteWebDriver driver, String type,
			String fileName) throws IOException {
		// downloads report from perfecto
		String command = "mobile:report:download";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		String report = (String) driver.executeScript(command, params);
		File reportFile = new File(fileName + "." + type);
		BufferedOutputStream output = new BufferedOutputStream(
				new FileOutputStream(reportFile));
		byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
		output.write(reportBytes);
		output.close();

	}

	// sets the initial page for the browser
	public void goToPage(String url, String title) {

		if (selenium)
			getDriver(seleniumD.True).get(url);
		else if (local)
			getDriver(mobileD.True, currentMobile);
			else
			getDriver(remoteD.True).get(url);
		waitForTitle(10, title);
		takeScreen("goToPage: " + url + "_" + title, true);
		step++;
	}

	// gets the current url
	public String getUrl() {
		String url = "can't find url";
		try {
			url = (selenium) ? getDriver(seleniumD.True).getCurrentUrl()
					: getDriver(remoteD.True).getCurrentUrl();
		} catch (Exception ex) {

		}
		return url;
	}

	// will attempt to wait on the page to load and searches for the title
	// supplied
	public void waitForTitle(int seconds, String title) {

		try {
			WebDriverWait wait = (selenium) ? new WebDriverWait(
					getDriver(seleniumD.True), seconds) : new WebDriverWait(
					getDriver(remoteD.True), seconds);
			wait.until(ExpectedConditions.titleContains(title));
		} catch (Exception ex) {

		}
	}

	// will attempt to wait on the page to load and searches for the element
	// supplied
	public void waitForElement(int seconds, byFields by, String element) {
		try {
			WebDriverWait wait = (selenium) ? new WebDriverWait(
					getDriver(seleniumD.True), seconds) : new WebDriverWait(
					getDriver(remoteD.True), seconds);
			wait.until(ExpectedConditions.elementToBeClickable(getBy(by,
					element)));
		} catch (Exception ex) {
		}
	}

	// returns an element
	public WebElement getElement(byFields by, String element) {

		return (selenium) ? getDriver(seleniumD.True)
				.findElement(getBy(by, element)) : getDriver(remoteD.True)
				.findElement(getBy(by, element));
	}

	// checks if element exists
	public Boolean elementExists(byFields by, String element) {

		try {
			if (selenium)
				getDriver(seleniumD.True).findElement(getBy(by, element));
			else
				getDriver(remoteD.True).findElement(getBy(by, element));
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	// clears text field
	public void clearText(byFields by, String element, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			if (selenium)
				getDriver(seleniumD.True).findElement(getBy(by, element)).clear();
			else
				getDriver(remoteD.True).findElement(getBy(by, element))
						.clear();
			takeScreen("clearText: " + "_" + by + "_" + element, true);
			step++;
		} catch (Exception ex) {

		}

	}

	// sets text field value and will clear the field prior to writing based on
	// the clear boolean
	public void setText(byFields by, String element, String data,
			Boolean clear, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			if (clear) {
				clearText(by, element, timeOut);
			}
			if (selenium)
				getDriver(seleniumD.True).findElement(getBy(by, element)).sendKeys(
						data);
			else
				getDriver(remoteD.True).findElement(getBy(by, element))
						.sendKeys(data);
			waitForElement(timeOut, by, element);
			takeScreen("setText: " + by + "_" + element + "_" + data + "_"
					+ clear, true);
			step++;
		} catch (Exception ex) {

		}
	}

	// gets the text of an element
	public String getText(byFields by, String element, int timeOut) {

		waitForElement(timeOut, by, element);

		return (selenium) ? getDriver(seleniumD.True)
				.findElement(getBy(by, element)).getText().toString()
				: getDriver(remoteD.True).findElement(getBy(by, element))
						.getText().toString();
	}

	// gets the value of an element
	public String getValue(byFields by, String element, int timeOut) {

		waitForElement(timeOut, by, element);
		return (selenium) ? getDriver(seleniumD.True)
				.findElement(getBy(by, element)).getAttribute("value")
				: getDriver(remoteD.True).findElement(getBy(by, element))
						.getAttribute("value");
	}

	// clicks and element
	public void clickElement(byFields by, String element, int timeOut) {

		waitForElement(timeOut, by, element);
		if (selenium)
			getDriver(seleniumD.True).findElement(getBy(by, element)).click();
		else
			getDriver(remoteD.True).findElement(getBy(by, element)).click();
		waitForElement(timeOut, by, element);
		takeScreen("clickElement: " + by + "_" + element, true);
		step++;

	}

	// will submit a form based on an element
	public void submitElement(byFields by, String element, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			if (selenium)
				getDriver(seleniumD.True).findElement(getBy(by, element)).submit();
			else
				getDriver(remoteD.True).findElement(getBy(by, element))
						.submit();
			waitForElement(timeOut, by, element);
			takeScreen("submitElement: " + by + "_" + element, true);
			step++;
		} catch (Exception ex) {

		}

	}

	// sets a drop down field based on a text
	public void setDropDownText(byFields by, String element, String text,
			int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			if (selenium)
				new Select(getDriver(seleniumD.True).findElement(getBy(by, element)))
						.selectByVisibleText(text);
			else
				new Select(getDriver(remoteD.True).findElement(getBy(by,
						element))).selectByVisibleText(text);

			waitForElement(timeOut, by, element);
			takeScreen("setDropDownText: " + by + "_" + element + "_" + text,
					true);
			step++;

		} catch (Exception ex) {

		}

	}

	// sets drop down field based on value
	public void setDropDownValue(byFields by, String element, String text,
			int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			if (selenium)
				new Select(getDriver(seleniumD.True).findElement(getBy(by, element)))
						.selectByValue(text);
			else
				new Select(getDriver(remoteD.True).findElement(getBy(by,
						element))).selectByValue(text);
			waitForElement(timeOut, by, element);
			takeScreen("setDropDownValue: " + by + "_" + element + "_" + text,
					true);
			step++;
		} catch (Exception ex) {

		}
	}

	// gets windows size of desktop browser
	public String getWindowSize() {
		return (selenium) ? getDriver(seleniumD.True).manage().window().getSize()
				.toString() : getDriver(remoteD.True).manage().window()
				.getSize().toString();
	}

	// gets windows position of desktop browser
	public String getWindowPosition() {

		return (selenium) ? getDriver(seleniumD.True).manage().window()
				.getPosition().toString() : getDriver(remoteD.True).manage()
				.window().getPosition().toString();

	}

	// gets maximizes window of desktop browser
	public void maximizeWindow() {

		try {
			if (selenium)
				getDriver(seleniumD.True).manage().window().maximize();
			else
				getDriver(remoteD.True).manage().window().maximize();
			takeScreen("maximizeWindow", true);
			step++;
		} catch (Exception ex) {

		}
	}

	// sets windows position of desktop browser
	public void setWindowPosition(int x, int y) {

		try {
			if (selenium)
				getDriver(seleniumD.True).manage().window()
						.setPosition(new Point(x, y));
			else
				getDriver(remoteD.True).manage().window()
						.setPosition(new Point(x, y));
			takeScreen("setWindowPosition: " + x + "," + y, true);
			step++;
		} catch (Exception ex) {

		}
	}

	// sets windows size of desktop browser
	public void setWindowSize(int x, int y) {

		try {
			if (selenium)
				getDriver(seleniumD.True).manage().window()
						.setSize(new Dimension(x, y));
			else
				getDriver(remoteD.True).manage().window()
						.setSize(new Dimension(x, y));
			takeScreen("setWindowSize: " + x + "," + y, true);
			step++;
		} catch (Exception ex) {

		}
	}

	public void switchToFrame(Object find) {
		if (selenium)
			getDriver(seleniumD.True).switchTo().frame((WebElement) find);
		else
			getDriver(remoteD.True).switchTo().frame((WebElement) find);
	}
}
