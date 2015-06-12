package utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
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

public class library {

	private RemoteWebDriver driver;
	private String target="";
	private int step = 0;

	public library(RemoteWebDriver driver, String target, int step) {
		this.driver = driver;
		this.target = target;
		this.step = step;
	}
		
	public library(RemoteWebDriver driver) {
		this.driver = driver;
	}
	
	//can be called from test to automatically fail for debug purposes
	public void autoFail()
	{
		Assert.assertEquals("1", "2");
	}

	//returns the string value setup in testNG for the targetEnvironment
	public String getTarget() {
		return target;
	}

	//returns the current step
	public int getStep() {
		return step;
	}
	
	//checks if current test is running on a device
	public Boolean isDevice() {
		try
		{
		if (driver.getCapabilities().getCapability("platformName").equals("Android") || driver.getCapabilities().getCapability("platformName").equals("iOS")) {
			return true;
		} else {
			return false;
		}
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	//returns the driver
	public RemoteWebDriver getDriver() {
		return driver;
	}

	// switch driver method
	// switches dom between native and web and visual
	// "WEBVIEW", "NATIVE_APP" or "VISUAL"
	public RemoteWebDriver switchToContext(String context) {
		
		log("switchContext: " + context, false);
		
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
		return driver;
	}

	// get current driver context
	public String getCurrentContextHandle() {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context = (String) executeMethod.execute(
				DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	// get a list of all available contexts
	public List<String> getContextHandles() {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
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
		driver.close();
		driver.quit();
	}

	// will return the appropriate by object based on string
	public By getBy(String by, String element) {
		if (by == "id") {
			return By.id(element);
		} else if (by == "name") {
			return By.name(element);
		} else if (by == "css") {
			return By.cssSelector(element);
		} else if (by == "tag") {
			return By.tagName(element);
		} else if (by == "class") {
			return By.className(element);
		} else if (by == "linkText") {
			return By.linkText(element);
		} else if (by == "partialLinkText") {
			return By.partialLinkText(element);
		} else if (by == "xpath") {
			return By.xpath(element);
		} else {
			return By.id("");
		}
	}

	// close native application
	// this needs to be further expanded out to support all available methods
	//this is not used and just here for future purposes
	public void closeApplication(String application) {
		String command = "mobile:application.close";
		Map<String, Object> params = new HashMap<>();
		params.put("name", application);
		driver.executeScript(command, params);
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
			String destFile = target + "_Step" + step + "_"
					+ dateFormat.format(new Date()) + ".png";

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
				log("<u><b>||||||" + text + "</b></u><br><a href=\"" + userDirector
						+ destFile + "\"><img src=\"" + userDirector + destFile
						+ "\" alt=\"\"" + "height='100' width='100'/> "
						+ "<br />", addReport);
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
			Reporter.log(text.replace("<u><b>||||||", "<u><b>" + target + "_Step" + step + "_"));
		}
		else
		{
			System.out.println(target + "_Step" + step + "_" + text + newLine );
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

	//download report from perfecto
	private void downloadReport(RemoteWebDriver driver, String type,
			String fileName) throws IOException {
			//downloads report from perfecto
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

	//sets the initial page for the browser
	public void goToPage(String url, String title) {		
		driver.get(url);
		waitForTitle(10, title);
		takeScreen("goToPage: " + url + "_" + title, true);
		step++;
	}

	//gets the current url
	public String getUrl() {
		String url = "can't find url";
		try {
			url = driver.getCurrentUrl().toString();
		} catch (Exception ex) {

		}
		return url;
	}

	//will attempt to wait on the page to load and searches for the title supplied
	public void waitForTitle(int seconds, String title) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.titleContains(title));
		} catch (Exception ex) {

		}
	}

	//will attempt to wait on the page to load and searches for the element supplied
	public void waitForElement(int seconds, String by, String element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.elementToBeClickable(getBy(by,
					element)));
		} catch (Exception ex) {
		}
	}
	
	//returns an element
	public WebElement getElement(String by, String element) {

		try {
			driver.findElement(getBy(by, element));
		} catch (Exception ex) {

		}

		return driver.findElement(getBy(by, element));
	}

	//checks if element exists
	public Boolean elementExists(String by, String element) {

		try {
			driver.findElement(getBy(by, element));
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	//clears text field
	public void clearText(String by, String element, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).clear();
			takeScreen("clearText: " + "_" + by + "_" + element, true);
			step++;
		} catch (Exception ex) {

		}

	}
	
	//sets text field value and will clear the field prior to writing based on the clear boolean
	public void setText(String by, String element, String data, Boolean clear,
			int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			if (clear) {
				clearText(by, element, timeOut);
			}
			driver.findElement(getBy(by, element)).sendKeys(data);
			waitForElement(timeOut, by, element);
			takeScreen("setText: " + by + "_" + element + "_"
					+ data + "_" + clear, true);
			step++;
		} catch (Exception ex) {

		}
	}

	//gets the text of an element
	public String getText(String by, String element, int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).getText().toString();
		} catch (Exception ex) {

		}
		return driver.findElement(getBy(by, element)).getText().toString();
	}

	//gets the value of an element
	public String getValue(String by, String element, int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).getAttribute("value");
		} catch (Exception ex) {

		}
		return driver.findElement(getBy(by, element)).getAttribute("value");
	}

	//clicks and element
	public void clickElement(String by, String element, int timeOut) {

		waitForElement(timeOut, by, element);
		driver.findElement(getBy(by, element)).click();
		waitForElement(timeOut, by, element);
		takeScreen("clickElement: " + by + "_" + element, true);
		step++;

	}

	//will submit a form based on an element
	public void submitElement(String by, String element, int timeOut) {
		try {
			waitForElement(timeOut, by, element);
			driver.findElement(getBy(by, element)).submit();
			waitForElement(timeOut, by, element);
			takeScreen("submitElement: " + by + "_" + element, true);
			step++;
		} catch (Exception ex) {

		}

	}

	//sets a drop down field based on a text
	public void setDropDownText(String by, String element, String text,
			int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			new Select(driver.findElement(getBy(by, element)))
					.selectByVisibleText(text);
			waitForElement(timeOut, by, element);
			takeScreen("setDropDownText: " + by + "_"
					+ element + "_" + text, true);
			step++;

		} catch (Exception ex) {

		}

	}

	//sets drop down field based on value
	public void setDropDownValue(String by, String element, String text,
			int timeOut) {

		try {
			waitForElement(timeOut, by, element);
			new Select(driver.findElement(getBy(by, element)))
					.selectByValue(text);
			waitForElement(timeOut, by, element);
			takeScreen("setDropDownValue: " + by + "_"
					+ element + "_" + text, true);
			step++;
		} catch (Exception ex) {

		}
	}

	//gets windows size of desktop browser
	public String getWindowSize() {

		try {
			driver.manage().window().getSize().toString();
		} catch (Exception ex) {

		}

		return driver.manage().window().getSize().toString();
	}

	//gets windows position of desktop browser
	public String getWindowPosition() {

		try {
			driver.manage().window().getPosition();
		} catch (Exception ex) {

		}

		return driver.manage().window().getPosition().toString();
	}

	//gets maximizes window of desktop browser
	public void maximizeWindow() {

		try {
			driver.manage().window().maximize();
			takeScreen("maximizeWindow", true);
			step++;
		} catch (Exception ex) {

		}
	}

	//sets windows position of desktop browser
	public void setWindowPosition(int x, int y) {

		try {
			driver.manage().window().setPosition(new Point(x, y));
			takeScreen("setWindowPosition: " + x + "," + y, true);
			step++;
		} catch (Exception ex) {

		}
	}

	//sets windows size of desktop browser
	public void setWindowSize(int x, int y) {

		try {
			driver.manage().window().setSize(new Dimension(x, y));
			takeScreen("setWindowSize: " + x + "," + y, true);
			step++;
		} catch (Exception ex) {

		}
	}
}
