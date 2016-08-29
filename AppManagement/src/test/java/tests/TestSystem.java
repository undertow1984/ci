package tests;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import utilities.*;
import utilities.Library.byFields;

public class TestSystem extends ClassHelper {

	public void setPagesAndHelpers(Library lib) throws IOException {
		// sets up the page classes if required
	}

	@Test
	public void LoadFlights() throws Exception {
		setPagesAndHelpers(lib);

		if (lib.elementExists(byFields.xpath, "//*[text()=\"Please update!\"]")) {
			lib.clickElement(byFields.xpath, "//*[@resourceid=\"android:id/button2\"]", 10);
		}

		lib.clickElement(byFields.xpath, "//*[text()=\"Flights\"]", 10);
		
		lib.waitForElement(20, byFields.xpath, "//*[@resourceid=\"android:id/action_bar_title\"]");
		
	}

	@Test
	public void LoadHotels() throws Exception {
		setPagesAndHelpers(lib);

		if (lib.elementExists(byFields.xpath, "//*[text()=\"Please update!\"]")) {
			lib.clickElement(byFields.xpath, "//*[@resourceid=\"android:id/button2\"]", 10);

		}

		lib.clickElement(byFields.xpath, "//*[text()=\"Hotels\"]", 10);
		
		lib.waitForElement(20, byFields.xpath, "//*[@resourceid=\"com.expedia.bookings:id/dates_button\"]");
		
	}

	@Test
	public void LoadCars() throws Exception {
		setPagesAndHelpers(lib);

		if (lib.elementExists(byFields.xpath, "//*[text()=\"Please update!\"]")) {
			lib.clickElement(byFields.xpath, "//*[@resourceid=\"android:id/button2\"]", 10);

		}

		lib.clickElement(byFields.xpath, "//*[text()=\"Cars\"]", 10);
		lib.waitForElement(20, byFields.xpath, "//*[@resourceid=\"com.expedia.bookings:id/pickup_location\"]");
		
	}
}
