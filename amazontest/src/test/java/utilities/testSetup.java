package utilities;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class testSetup {

	private RemoteWebDriver driver;
	private boolean device;
	private String SELENIUM_HUB_URL;
	private DesiredCapabilities capabilities = new DesiredCapabilities();
	private library lib;
	private String target;

	public testSetup(String targetEnvironment, RemoteWebDriver driver) {
		this.target = targetEnvironment;
		this.driver = driver;
	}

	public void flowControl() {
		switch (target) {
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
		case "iPhone-6":
			device = true;
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("model", "iPhone-6");
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
			lib.errorCleanup();
			break;
		}
	}

	public library driverAndLibrarySetup() throws UnsupportedEncodingException,
			MalformedURLException {
		if (device) {
			String host = "partners.perfectomobile.com";
			String user = URLEncoder.encode("undertow1984@gmail.com", "UTF-8");
			String password = URLEncoder.encode("perfecto2", "UTF-8");

			URL gridURL = new URL("https://" + user + ":" + password + "@"
					+ host + "/nexperience/wd/hub");
			SELENIUM_HUB_URL = getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url", gridURL.toString());
		} else {

			SELENIUM_HUB_URL = getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url",
					"http://seleniumgrid.perfectomobilelab.net:4444/wd/hub");
		}

		driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), capabilities);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		
		lib = new library(driver, target, 1);
		
		return lib;
	}

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
