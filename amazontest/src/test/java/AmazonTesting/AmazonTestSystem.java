package AmazonTesting;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import pages.*;
import utilities.*;

public class AmazonTestSystem {
	private RemoteWebDriver driver;
	private library lib;
	private testSetup tes;
	private chromeHelpers chrome;
	private home homePage;
	private searchResults searchResultsPage;
	private cartFunctions cart;

	@Parameters({ "targetEnvironment" })
	@BeforeTest
	public void beforeTest(String targetEnvironment)
			throws UnsupportedEncodingException, MalformedURLException {
		// initializes testSetup class
		tes = new testSetup(targetEnvironment, driver);
		// sets up the testNG flows based on testsuite.xml
		tes.flowControl();

	}

	public void setPagesAndHelpers(library lib)
			throws UnsupportedEncodingException, MalformedURLException {
		// initializes the chromehelper class
		chrome = new chromeHelpers(lib);
		// sets up the home page class
		homePage = new home(lib);
		// sets up the search results class
		searchResultsPage = new searchResults(lib);
		// sets up the cartFunctions class
		cart = new cartFunctions(lib);
	}

	@Test
	public void OrderBook() throws InterruptedException, IOException {
		// sets the RemoteWebDriver and initial library settings
		lib = tes.driverAndLibrarySetup();
		setPagesAndHelpers(lib);

		// test start
		lib.log("orderBookStarted", false);
		try {

			lib.goToPage("http://amazon.com", "Amazon.com");

			chrome.firstOpenAccepteance(60);

			homePage.searchBoxText("Army of darkness volume one", 60);

			homePage.searchBoxSubmit(60);

			searchResultsPage.selectResult("Army of Darkness Omnibus Volume 1",
					60);

			cart.addToCart(60);

			cart.proceedToCheckout(60);

		} catch (Exception ex) {
			lib.log("Got exception " + ex, true);
			throw ex;
		} finally {

			if (lib.isDevice()) {
				lib.getDriver().close();
				lib.downloadReportDisplay(lib.getDriver(), true);
			} else {
				lib.getDriver().close();
			}
		}
	}

	@AfterTest
	public void afterTest() throws IOException {

		try {
			lib.getDriver().close();
		} catch (Exception ex) {

		}

		lib.getDriver().quit();
	}

}
