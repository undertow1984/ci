package AmazonTesting;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.remote.*;
import com.perfectomobile.selenium.by.*;
import com.perfectomobile.selenium.output.*;

import java.io.UnsupportedEncodingException;

import pages.*;

public class AmazonTestSystem {
	public RemoteWebDriver driver;
	public boolean device;
	public String SELENIUM_HUB_URL;
	DesiredCapabilities capabilities = new DesiredCapabilities();
	public String target;
	public library l;
	
	
  @Parameters({ "targetEnvironment" })
  @BeforeTest
  public void beforeTest(String targetEnvironment) throws UnsupportedEncodingException, MalformedURLException {
	  target = targetEnvironment;
	  
	  switch (targetEnvironment) {
	  case "iPad Mini":
			device = true;
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("model", "iPad Mini 2");
			capabilities.setCapability("browserName", "Safari");
			break;
		case "Galaxy S5":
			device = true;
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("model", "Galaxy S5");
			capabilities.setCapability("browserName", "mobileOS");
			break;
		case "iPhone-5C":
			device = true;
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("model", "iPhone-5");
			capabilities.setCapability("browserName", "Safari");
			break;
		case "Galaxy Tab":
			device = true;
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("model", "SCH-I705 Galaxy Tab 2");
			capabilities.setCapability("browserName", "mobileChrome");
			break;		
		case "Firefox":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "firefox");
			capabilities.setCapability("version", "");
			break;
		case "Chrome":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "chrome");
			capabilities.setCapability("version", "");
			break;
		case "Internet Explorer":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "internet explorer");
			capabilities.setCapability("version", "");
			break;
		default: 
			l.errorCleanup();
			break;
		}
	  
	  	l = new library(driver);

		if (device) {
			String host = "partners.perfectomobile.com";
			String user = URLEncoder.encode("undertow1984@gmail.com", "UTF-8");
			String password = URLEncoder.encode("perfecto2", "UTF-8");

			URL gridURL = new URL("https://" + user + ":" + password + "@" + host + "/nexperience/wd/hub");
			SELENIUM_HUB_URL = l.getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url", gridURL.toString());
		} else {

			SELENIUM_HUB_URL = l.getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url",
					"http://seleniumgrid.perfectomobilelab.net:4444/wd/hub");
		}
		
		driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), capabilities);
  }
  
  
  @Test
  public void OrderBook() throws InterruptedException, IOException {
	  try
	  {
	  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	  driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
	  library l = new library(driver, target, 1, device);	  
	  	
	  l.goToPage("http://amazon.com","Amazon.com");
	  
	  if (target.contains("Galaxy"))
	  {
		  l.switchToContext("VISUAL");
		  if (l.elementExists("partialLinkText", "Accept"))
		  {
	  			l.clickElement("partialLinkText", "Accept", 10);
		  }
		  
		  if (l.elementExists("partialLinkText", "NO THANKS"))
		  {
		  l.clickElement("partialLinkText", "NO THANKS", 10);
		  }
		  
		  l.switchToContext("WEBVIEW");
	  }
	   
	  l.setText("xpath", "(//input[@id='nav-search-keywords' or @id='twotabsearchtextbox'] )[1]", "Army of darkness volume one", true,60);
	  	  
	  l.submitElement("xpath", "(//input[@id='nav-search-keywords'  or @id='twotabsearchtextbox'])[1]",60);
	  
	  l.clickElement("linkText", "Army of Darkness Omnibus Volume 1", 10);
	  
	  l.clickElement("xpath", "//*[@id='add-to-cart-button']", 10);
	  
	  l.clickElement("xpath", "(//*[@id='a-autoid-0-announce' or @id='hlb-ptc-btn-native'])[1]", 10);
	  
	  
	  }
	  catch (Exception ex)
	  {
		  
		  
		  throw ex;
	  }
	  finally
	  {
		  driver.close();
		  if (device) {
			  l.downloadReportDisplay(driver);
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
