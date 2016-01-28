package AmazonTesting;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;


import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import pages.*;
import utilities.*;
import utilities.library.byFields;

public class AmazonTestSystem extends ClassHelper {
	private androidHelper android;
	private amazonHome homePage;
	private amazonSearchResults searchResultsPage;
	private amazonCartFunctions cart;

	public void setPagesAndHelpers(library lib) throws IOException {
		// initializes the androidHelper class
		android = new androidHelper(lib);
		// sets up the home page class
		homePage = new amazonHome(lib);
		// sets up the search results class
		searchResultsPage = new amazonSearchResults(lib);
		// sets up the cartFunctions class
		cart = new amazonCartFunctions(lib);
		lib.loadPropertyFile("_amazonElements.properties");
	}

	@Test
	public void OrderBook() throws InterruptedException, IOException {
		// sets the RemoteWebDriver and initial library settings
		try {
			setPagesAndHelpers(lib);

			// sets Network and latency based on test parameters
			// network virtualization is per cloud and per device basis
			// i'm unable to reliably get this working
			// if(lib.getNetwork().equals("4G"))
			// {
			// lib.startNetworkVirtualization("4g_lte_average",
			// lib.getNetworkLatency());
			// }
			lib.log("Going to amazon.com", false);
			lib.goToPage("http://amazon.com", "Amazon.com");

			/*
			 * lib.log("Checking if Chrome needs acceptance",false);
			 * android.chromeFirstOpenAccepteance(60);
			 * 
			 * if (lib.getDriver().findElementsByXPath(
			 * "//*[@data-nav-tabindex='9' or @data-nav-tabindex='10' or @data-nav-tabindex='12']"
			 * ).size() == 3) {
			 * 
			 * }
			 

			lib.log("Enter book into search box", false);
			homePage.searchBoxText("Army of darkness volume one", 60);
			
			lib.waitForElement(20, byFields.xpath, "//*[text()='@#$#@']");*/

			//lib.log("Searching for book", false);
			//homePage.searchBoxSubmit(60);
			/*
			 * lib.log("Selecting book from results", false);
			 * searchResultsPage.selectResult(
			 * "Army of Darkness Omnibus Volume 1", 60);
			 * 
			 * lib.log("Adding book to cart", false); cart.addToCart(60);
			 * 
			 * lib.log("Proceeding to checkout", false);
			 * cart.proceedToCheckout(60);
			 */
			
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/*@Test(dependsOnMethods = { "OrderBook" })
	public void cleanUpCart() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			// sets Network and latency based on test parameters
			// network virtualization is per cloud and per device basis
			// i'm unable to reliably get this working
			// if(lib.getNetwork().equals("4G"))
			// {
			// lib.startNetworkVirtualization("4g_lte_average",
			// lib.getNetworkLatency());
			// }
			lib.log("Going to amazon.com", false);
			lib.goToPage("http://amazon.com", "Amazon.com");

			
			 * lib.log("Checking if Chrome needs acceptance",false);
			 * android.chromeFirstOpenAccepteance(60);
			 * 
			 * lib.log("Clicking cart icon",false); cart.clickCart();
			 * 
			 * lib.log("Deleting all items from the cart",false);
			 * cart.deleteFromCart();
			 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}*/

}
