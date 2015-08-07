package AmazonTesting;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import pages.*;
import utilities.*;
import utilities.library.byFields;

public class AmazonTestSystem {
	private RemoteWebDriver driver;
	private library lib;
	private testSetup tes;
	private androidHelper android;
	private home homePage;
	private searchResults searchResultsPage;
	private cartFunctions cart;
	private String testName;
	
	

	@Parameters({ "targetEnvironment", "network", "networkLatency" })
	@BeforeTest
	public void beforeTest(String targetEnvironment, String network, String networkLatency)
			throws UnsupportedEncodingException, MalformedURLException {
		// initializes testSetup class
		tes = new testSetup(targetEnvironment, driver, network, networkLatency);
		// sets up the testNG flows based on testsuite.xml
		tes.flowControl();
		org.apache.log4j.BasicConfigurator.configure();
	}

	public void setPagesAndHelpers(library lib)
			throws IOException {
		// initializes the androidHelper class
		android = new androidHelper(lib);
		// sets up the home page class
		homePage = new home(lib);
		// sets up the search results class
		searchResultsPage = new searchResults(lib);
		// sets up the cartFunctions class
		cart = new cartFunctions(lib);
		lib.setTestName(testName);
		lib.log("testStarted", false);
		lib.loadPropertyFile("_elements.properties");
	}
	
	@BeforeMethod
	public void getTestMethod(Method method)
	{
		testName = method.getName(); 
		
	}

	@Test
	public void OrderBook() throws InterruptedException, IOException {
		// sets the RemoteWebDriver and initial library settings
		lib = tes.driverAndLibrarySetup();
		setPagesAndHelpers(lib);
		
		//sets Network and latency based on test parameters
		//network virtualization is per cloud and per device basis
		//i'm unable to reliably get this working
		//if(lib.getNetwork().equals("4G"))
		//{
			//lib.startNetworkVirtualization("4g_lte_average", lib.getNetworkLatency());
		//}
				
		try {
						
			lib.log("Going to amazon.com",false);
			lib.goToPage("http://amazon.com", "Amazon.com");
						
			lib.log("Checking if Chrome needs acceptance",false);
		    android.chromeFirstOpenAccepteance(60);
		    
		    if (lib.getDriver().findElementsByXPath("//*[@data-nav-tabindex='9' or @data-nav-tabindex='10' or @data-nav-tabindex='12']").size() == 3)
		    {
		    
		    }
		    
		                
			lib.log("Enter book into search box",false);
			homePage.searchBoxText("Army of darkness volume one", 60);
						
			lib.log("Searching for book", false);
			homePage.searchBoxSubmit(60);

			lib.log("Selecting book from results", false);
			searchResultsPage.selectResult("Army of Darkness Omnibus Volume 1",
					60);
						
			lib.log("Adding book to cart", false);
			cart.addToCart(60);

			lib.log("Proceeding to checkout", false);
			cart.proceedToCheckout(60);

		} catch (Exception ex) {
			throw ex;
		} finally {
			//downloading the perfecto report and cleaning up the drivers 
			if (lib.isDevice()) {
				lib.getDriver().close();
				lib.downloadReportDisplay(lib.getDriver(), true);
			} else {
				lib.getDriver().close();				
			}
			lib.getDriver().quit();
		}
	}
	
	@Test(dependsOnMethods = { "OrderBook" })
	public void cleanUpCart() throws InterruptedException, IOException {
		// sets the RemoteWebDriver and initial library settings
		lib = tes.driverAndLibrarySetup();
		setPagesAndHelpers(lib);
		
		//sets Network and latency based on test parameters
				//network virtualization is per cloud and per device basis
				//i'm unable to reliably get this working
				//if(lib.getNetwork().equals("4G"))
				//{
					//lib.startNetworkVirtualization("4g_lte_average", lib.getNetworkLatency());
				//}

		try {

			lib.log("Going to amazon.com", false);
			lib.goToPage("http://amazon.com", "Amazon.com");
			
			lib.log("Checking if Chrome needs acceptance",false);
		    android.chromeFirstOpenAccepteance(60);
		    
		    lib.log("Clicking cart icon",false);
		    cart.clickCart();
		    
		    lib.log("Deleting all items from the cart",false);
		    cart.deleteFromCart();
			
		} catch (Exception ex) {			
			throw ex;
		} finally {
			//downloading the perfecto report and cleaning up the drivers 
			if (lib.isDevice()) {
				lib.getDriver().close();
				lib.downloadReportDisplay(lib.getDriver(), true);
			} else {
				lib.getDriver().close();				
			}
			lib.getDriver().quit();
		}
	}

	@AfterTest
	public void afterTest() throws IOException {

		//re-attempt driver clean up in case of massive failure
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
