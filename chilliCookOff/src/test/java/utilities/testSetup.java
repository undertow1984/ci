package utilities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

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

import utilities.library;
import utilities.library.mobileDrivers;

public class testSetup {

	private RemoteWebDriver rdriver;
	private WebDriver wdriver;
	private MobileDriver mdriver;
	private IMobileWebDriver imdriver;
	private Boolean device = false;
	private String SELENIUM_HUB_URL;
	private DesiredCapabilities capabilities = new DesiredCapabilities();
	private library lib;
	private String target;
	private String network;
	private String networkLatency;
	private Boolean local = false;
	private Boolean selenium = false;
	private IMobileDevice foundDevice;
	private MobileBrowserType localBrowser;
	private MobileDeviceFindOptions options = new MobileDeviceFindOptions();
	
	//initializes the class, an option for each driver type
	public testSetup(String targetEnvironment, RemoteWebDriver driver,
			String network, String networkLatency) {
		this.target = targetEnvironment;
		this.rdriver = driver;
		this.network = network;
		this.networkLatency = networkLatency;
		
	}

	public testSetup(String targetEnvironment, MobileDriver driver,
			String network, String networkLatency, Boolean local) {
		this.target = targetEnvironment;
		this.mdriver = driver;
		this.network = network;
		this.networkLatency = networkLatency;
		this.local = local;
		
	}

	public testSetup(String targetEnvironment, WebDriver driver,
			String network, String networkLatency, Boolean selenium) {
		this.target = targetEnvironment;
		this.wdriver = driver;
		this.network = network;
		this.networkLatency = networkLatency;
		this.selenium = selenium;
	}

	// sets capabilities based on environment returned from testNG
	public void flowControl() {

		if (!local) {
			//remote web driver capabilities - includes desktop for selenium grid
			switch (target) {
			case "iPad Mini":
				device = true;
				capabilities.setCapability("platformName", "iOS");
				capabilities.setCapability("model", "iPad Mini 2");
				capabilities.setCapability("browserName", "Safari");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			case "Galaxy S5":
				device = true;
				capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("model", "Galaxy S5");
				capabilities.setCapability("browserName", "mobileOS");
				capabilities.setCapability("osVersion", "5.0");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			case "iPhone-6 Plus":
				device = true;
				capabilities.setCapability("platformName", "iOS");
				capabilities.setCapability("model", "iPhone-6 Plus");
				capabilities.setCapability("browserName", "Safari");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			case "Galaxy Tab":
				device = true;
				capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("model", "SCH-I705 Galaxy Tab 2");
				capabilities.setCapability("browserName", "mobileChrome");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			case "Firefox":
				device = false;
				capabilities.setCapability("platform", Platform.ANY);
				capabilities.setCapability("browserName", "firefox");
				capabilities.setCapability("version", "");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			case "Chrome":
				device = false;
				capabilities.setCapability("platform", Platform.ANY);
				capabilities.setCapability("browserName", "chrome");
				capabilities.setCapability("version", "");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			case "Internet Explorer":
				device = false;
				capabilities.setCapability("platform", Platform.ANY);
				capabilities.setCapability("browserName", "internet explorer");
				capabilities.setCapability("version", "");
				capabilities.setCapability("automationName", "PerfectoMobile");
				break;
			default:

				break;
			}
		} else {
			//capabilities for mobile driver - local - excludes desktop
			switch (target) {
			case "iPad Mini":
				device = true;
				options.setModel("iPad Mini 2");
				options.setOS("Android");
				localBrowser = MobileBrowserType.SAFARI;
				break;
			case "Galaxy S5":
				device = true;
				options.setModel("Galaxy S5");
				options.setOSVersion("5.0");
				localBrowser = MobileBrowserType.CHROME;
				break;
			case "iPhone-6 Plus":
				device = true;
				options.setModel("iPhone-6 Plus");
				localBrowser = MobileBrowserType.SAFARI;
				break;
			case "Galaxy Tab":
				device = true;
				options.setModel("SCH-I705 Galaxy Tab 2");
				localBrowser = MobileBrowserType.CHROME;
				break;
			default:

				break;
			}
		}
	}

	// connects to selenium grid at perfecto as well as establishes
	// device connectivity to mobile cloud
	public library driverAndLibrarySetupRemote()
			throws UnsupportedEncodingException, MalformedURLException,
			InterruptedException {
		if (device) {
			//sets mobile cloud credentials and host for devices through remote web driver
			String host = "partners.perfectomobile.com";
			String user = URLEncoder.encode("jeremyp@perfectomobile.com",
					"UTF-8");
			String password = URLEncoder.encode("Perfecto123", "UTF-8");

			// String host = "mobilecloud.perfectomobile.com";
			// String user = URLEncoder.encode("jeremyp@perfectomobile.com",
			// "UTF-8");
			// String password = URLEncoder.encode("Perfecto123", "UTF-8");

			// String host = "vzw.perfectomobile.com";
			// String user = URLEncoder.encode("jeremyp@perfectomobile.com",
			// "UTF-8");
			// String password = URLEncoder.encode("JP123!", "UTF-8");
			
			//finalizes connection string
			URL gridURL = new URL("https://" + user + ":" + password + "@"
					+ host + "/nexperience/wd/hub");
			SELENIUM_HUB_URL = getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url", gridURL.toString());
		} else {
			//perfecto grid system for desktop browsers through remote web driver
			SELENIUM_HUB_URL = getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url",
					"http://seleniumgrid.perfectomobilelab.net:4444/wd/hub");
		}

		int z = 0;
		int tryCount = 0;
		//will try to retrieve device/browser up to 5 time with a 2 minute wait in between
		while (z != 1) {
			try {
				//retriving device/browser for remote web driver
				rdriver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL),
						capabilities);
				rdriver.manage().timeouts()
						.implicitlyWait(60, TimeUnit.SECONDS);
				rdriver.manage().timeouts()
						.pageLoadTimeout(60, TimeUnit.SECONDS);

				lib = new library(rdriver, target, 1, network, networkLatency,
						device);
				z = 1;
			} catch (Exception ex) {
				tryCount++;
				if (tryCount > 5) {
					z = 1;
					throw ex;
				} else {
					Thread.sleep(120000);
				}
			}
		}

		return lib;
	}

	//connects mobile driver -- local
	public library driverAndLibrarySetupLocal()
			throws UnsupportedEncodingException, MalformedURLException,
			InterruptedException {
		
		mdriver = new MobileDriver("https://partners.perfectomobile.com","jeremyp@perfectomobile.com","Perfecto123");
		//pulls device from the driver
		foundDevice = mdriver.findDevice(options);
		
		lib = new library(mdriver, target, 1, network, networkLatency, local,
				device, foundDevice, localBrowser);
		
		//sets default mobile driver type
		lib.setCurrentMobileDriver(mobileDrivers.domDriver);
		
		//initializes the device
		foundDevice.open();
		foundDevice.home();
		
		return lib;
	}

	// initializes the selenium driver based on the browser type - selenium
	public library driverAndLibrarySetupSelenium()
			throws UnsupportedEncodingException, MalformedURLException,
			InterruptedException {
		WebDriver driver;
		if (target.equals("Firefox")) {
			driver = new FirefoxDriver();
			lib = new library(driver, target, 1, network, networkLatency,
					selenium, device);
		} else if (target.equals("Chrome")) {
			
			System.setProperty("webdriver.chrome.driver", ".\\target\\test-classes\\ChromeDriver.exe");
			driver = new ChromeDriver();			
			lib = new library(driver, target, 1, network, networkLatency,
					selenium, device);
		} else if (target.equals("Internet Explorer")) {
			System.setProperty("webdriver.ie.driver", ".\\target\\test-classes\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();			
			lib = new library(driver, target, 1, network, networkLatency,
					selenium, device);
		}
		return lib;
	}

	// parses the cloud/grid connectivity parameters
	public String getConfigurationProperty(String envKey, String sysKey,
			String defValue) {
		String retValue = defValue;
		String envValue = System.getenv(envKey);
		String sysValue = System.getProperty(sysKey);
		// system property prevails over environment variable
		if (sysValue != null) {
			retValue = sysValue;
		} else if (envValue != null) {
			retValue = envValue;
		}
		return retValue;
	}
}
