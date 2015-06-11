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

public class AmazonTestSystem {
	private RemoteWebDriver driver;
	private boolean device;
	private library lib;
	private testSetup tes;
	private chromeHelpers chrome;
	private home homePage;
	private searchResults searchResultsPage;
	private cartFunctions cart;
  @Parameters({ "targetEnvironment" })
  @BeforeTest
  public void beforeTest(String targetEnvironment) throws UnsupportedEncodingException, MalformedURLException {
	   //initializes testSetup class
	   tes = new testSetup( targetEnvironment, driver);
	   //sets the RemoteWebDriver and initial library settings
	   lib = tes.driverAndLibrarySetup();
	   //sets up the testNG flows based on testsuite.xml
	   tes.flowControl();
	   //initializes the chromehelper class
	   chrome = new chromeHelpers(lib);
	   //sets up the home page class
	   homePage = new home(lib);
	   //sets up the search results class
	   searchResultsPage = new searchResults(lib);
	   //sets up the cartFunctions class
	   cart = new cartFunctions(lib);
	   
  }
    
  @Test
  public void OrderBook() throws InterruptedException, IOException {
	  try
	  {
	  	
	  lib.goToPage("http://amazon.com","Amazon.com");
	   
	  chrome.firstOpenAccepteance();
	  
	  homePage.searchBoxText("Army of darkness volume one", 60);
	  
	  homePage.searchBoxSubmit(60);
	  
	  searchResultsPage.selectResult("Army of Darkness Omnibus Volume 1", 10);
	  
	  cart.addToCart(10);
	  
	  cart.proceedToCheckout(10);
	  
	  }
	  catch (Exception ex)
	  {
		  throw ex;
	  }
	  finally
	  {
		  driver.close();
		  if (device) {
			  lib.downloadReportDisplay(driver);
		  }
	  }
  }

  @AfterTest
  public void afterTest() throws IOException {
	  
	 
	  try
	  {
		 driver.close();
	  }
	  catch(Exception ex)
	  {
	  
	  }
	  
	  driver.quit();
  }
  

}
