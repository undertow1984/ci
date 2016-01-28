package AmazonTesting;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;


import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;

import pages.*;
import utilities.*;
import utilities.library.availableContexts;
import utilities.library.byFields;
import utilities.library.prop;

public class SleepTestSystem extends ClassHelper {
	public SleepHome home;
	int wait = 0;

	public void setPagesAndHelpers(library lib) throws IOException {
		home = new SleepHome(lib);
		lib.log("testStarted", false);
		lib.loadPropertyFile("_sleepElements.properties");
	}

	@Test
	public void implicitNotVisible() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);
			lib.setImplicit(15);
			lib.goToPage("http://localhost/sleeptest");
			home.clickObject1(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

	@Test
	public void explicitNotVisible() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			wait = 30;
			setPagesAndHelpers(lib);

			lib.setImplicit(0);
			lib.goToPage("http://localhost/sleeptest");
			home.clickObject1(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Test
	public void implicitConflict() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			wait = 10;
			lib.setImplicit(60);
			lib.goToPage("http://localhost/sleeptest");
			lib.waitForElementToDisappear(wait, byFields.xpath, lib.getProp(prop.sleepObject3));
			home.clickObject2(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Test
	public void implicitConflictFixed() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			wait = 10;
			lib.setImplicit(0);
			lib.goToPage("http://localhost/sleeptest");
			lib.waitForElementToDisappear(wait, byFields.xpath, lib.getProp(prop.sleepObject3));
			home.clickObject2(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Test
	public void implicitTest() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			lib.setImplicit(15);
			lib.goToPage("http://localhost/sleeptest");
			home.clickObject2(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Test
	public void fluentWait() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			lib.goToPage("http://localhost/sleeptest");
			home.clickObject2Fluent(30, 500);
			home.fluentVerify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Test
	public void implicitConditional() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			lib.setImplicit(15);
			lib.goToPage("http://localhost/sleeptest");
			home.findObject();
			home.findObject();
			home.findObject();
			home.findObject();
			home.clickObject2(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Test
	public void implicitConditionalFixed() throws InterruptedException, IOException {
		try {
			// sets the RemoteWebDriver and initial library settings
			setPagesAndHelpers(lib);

			lib.setImplicit(15);
			lib.goToPage("http://localhost/sleeptest");
			lib.setImplicit(0);
			home.findObject();
			home.findObject();
			home.findObject();
			home.findObject();
			lib.setImplicit(15);
			home.clickObject2(wait);
			home.verify();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}