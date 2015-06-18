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
import utilities.library.mobileD;
import utilities.library.mobileDrivers;
import utilities.library.remoteD;
import utilities.library.seleniumD;

public class testSystem {
	private RemoteWebDriver rdriver;
	private WebDriver wdriver;
	private MobileDriver mdriver;
	private library lib;
	private testSetup tes;	
	private String testName;	
	private Boolean local;
	private Boolean selenium;
	private androidHelper android;
	private home homePage;
	private resultsPage results;
	private String sushiFind;

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
		homePage = new home(lib, android);
		results = new resultsPage(lib, android);
		// end set the pages here

		lib.setTestName(testName);
		lib.loadPropertyFile("_elements.properties");
	}

	@BeforeMethod
	public void getTestMethod(Method method) {
		lib.setTestName(method.getName());
	}

	@Test
	public void highestRatedSushi() throws InterruptedException, IOException {
		lib.log("testStarted", false);
		try {
			//goto site
			homePage.goTo("http://m.yelp.com/la", "Los Angeles Restaurants, Dentists, Bars, Beauty Salons, Doctors");
			
			//ignore app nagg
			homePage.ignoreApp();

			// switch to desktop version of site -- parking options aren't available via app or mobile site
			if (lib.isDevice()) {
				homePage.switchToDesktopSite();
			}

			// entering business to search for
			homePage.enterBusiness("sushi");

			// entering the city to search for the business in
			homePage.enterLocation("los angeles, ca");

			// submit the search results
			homePage.submitResults();

			//opening more features box
			results.selectMoreFeatures();

			//opening he parking options
			results.selectParking();

			//selecting parking options minus validated parking
			results.parkingRequired();

			//filter results based on the selected "features"
			results.filterFeatures();

			//selecting dollar amount of $$
			results.selectDollarDollar();
			
			//sorting results by the highest rated
			results.sortHighestRated();
			
			//storing the the highest rated sushi within the specified criteria
			sushiFind="The highest rated sushi restraunt in Los Angeles, CA with parking and a cost of $$ is : "
					+ results.returnHighestRated();

			//display the results to the console and reportng/testng report
			lib.log(sushiFind, true);

		} catch (Exception ex) {
			throw ex;
		} finally {
			// downloading the perfecto report and cleaning up the drivers
			lib.testCleanUpWithReport();
		}
	}

	@AfterTest
	public void afterTest() throws IOException {

		// re-attempt driver clean up in case of massive failure
		lib.testCleanUp();

	}

}
