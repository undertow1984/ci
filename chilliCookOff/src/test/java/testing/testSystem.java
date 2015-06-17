package testing;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.perfectomobile.selenium.*;
import com.perfectomobile.selenium.api.*;
import com.perfectomobile.selenium.by.*;
import com.perfectomobile.selenium.output.*;
import com.perfectomobile.selenium.options.*;
import com.perfectomobile.selenium.options.rotate.*;
import com.perfectomobile.selenium.options.touch.*;
import com.perfectomobile.selenium.options.visual.*;
import com.perfectomobile.selenium.options.visual.image.*;
import com.perfectomobile.selenium.options.visual.text.*;
import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.utils.FileUtils;

import pages.*;
import utilities.*;
import utilities.library.availableContexts;
import utilities.library.byFields;
import utilities.library.remoteD;
import utilities.library.seleniumD;

public class testSystem {
	private RemoteWebDriver rdriver;
	private WebDriver wdriver;
	private MobileDriver mdriver;
	private library lib;
	private testSetup tes;
	private androidHelper android;
	private String testName;
	private home homePage;
	private resultsPage results;
	private Boolean local;
	private Boolean selenium;

	@Parameters({ "targetEnvironment", "network", "networkLatency", "local",
			"selenium" })
	@BeforeTest
	public void beforeTest(String targetEnvironment, String network,
			String networkLatency, String local, String selenium)
			throws InterruptedException, IOException {

		// debug options
		// set for local vs remote execution
		this.local = Boolean.parseBoolean(local);
		// set selenium for local desktop execution
		this.selenium = Boolean.parseBoolean(selenium);
		// end debug options

		// initializes testSetup class
		if (this.selenium) {
			tes = new testSetup(targetEnvironment, wdriver, network,
					networkLatency, this.selenium);
			lib = tes.driverAndLibrarySetupSelenium();
		} else if (this.local) {
			tes = new testSetup(targetEnvironment, mdriver, network,
					networkLatency, this.local);
			tes.flowControl();
			lib = tes.driverAndLibrarySetupLocal();
			
		} else {			
			tes = new testSetup(targetEnvironment, rdriver, network,
					networkLatency);
			tes.flowControl();
			// sets up the testNG flows based on testsuite.xml
			
			lib = tes.driverAndLibrarySetupRemote();
		}

		setPagesAndHelpers(lib);

	}

	public void setPagesAndHelpers(library lib) throws IOException {
		// initializes the androidHelper class
		android = new androidHelper(lib);

		// set the pages here
		homePage = new home(lib);
		results = new resultsPage(lib);
		// end set the pages here

		lib.setTestName(testName);
		lib.log("testStarted", false);
		lib.loadPropertyFile("_elements.properties");
	}

	@BeforeMethod
	public void getTestMethod(Method method) {
		lib.setTestName(method.getName());
	}

	@Test
	public void highestRatedSushi() throws InterruptedException, IOException {

		try {
			lib.log("Going to yelp.com", false);
			lib.goToPage("http://m.yelp.com/la",
					"Los Angeles Restaurants, Dentists, Bars, Beauty Salons, Doctors");

			if (lib.isDevice()) {
				lib.log("Checking if Chrome needs acceptance", false);
				android.chromeFirstOpenAccepteance(15);
			}

			// Seeing if the download app nag is showing and handle it
			if (lib.isDevice()) {
				lib.switchToContext(availableContexts.VISUAL);
				if (lib.elementExists(byFields.partialLinkText, "No, thanks")) {
					homePage.ignoreApp();
				}
				lib.switchToContext(availableContexts.WEBVIEW);
			}

			// switch to desktop
			if (lib.isDevice()) {
			homePage.switchToDesktopSite();
			}

			// entering business to search for
			homePage.enterBusiness("sushi");

			// entering the city to search for the business in
			homePage.enterLocation("los angeles, ca");

			// submit the results
			homePage.submitResults();			

			results.selectMoreFeatures();

			results.selectParking();

			results.parkingRequired();
			
			results.filterFeatures();
			
			results.sortHighestRated();

			results.selectDollarDollar();

			lib.log("The highest rated sushi restraunt in Los Angeles, CA with parking and a cost of $$ is : " + results.returnHighestRated(), false);

		} catch (Exception ex) {
			throw ex;
		} finally {
			// downloading the perfecto report and cleaning up the drivers
			
			if (selenium)
			{
				if (lib.isDevice()) {
					lib.getDriver(seleniumD.True).close();					
				} else {
					lib.getDriver(seleniumD.True).close();
				}
				lib.getDriver(seleniumD.True).quit();
			}
			else
			{
				if (lib.isDevice()) {
					lib.getDriver(remoteD.True).close();
					lib.downloadReportDisplay(lib.getDriver(remoteD.True), true);
				} else {
					lib.getDriver(remoteD.True).close();
				}
				lib.getDriver(remoteD.True).quit();
			}
			
			
		}
	}

	@AfterTest
	public void afterTest() throws IOException {

		// re-attempt driver clean up in case of massive failure
		if (selenium)
		{
			try {
				lib.getDriver(seleniumD.True).close();
			} catch (Exception ex) {

			}

			try {
				lib.getDriver(seleniumD.True).quit();
			} catch (Exception ex) {

			}

		}
		else
		{
			try {
				lib.getDriver(remoteD.True).close();
			} catch (Exception ex) {

			}

			try {
				lib.getDriver(remoteD.True).quit();
			} catch (Exception ex) {

			}

		}
		
	}

}
