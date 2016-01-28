package pages;

import org.openqa.selenium.NoSuchElementException;

import utilities.*;
import utilities.library.byFields;
import utilities.library.prop;

public class SleepHome {
	private library lib;

	public SleepHome(library l) {
		this.lib = l;
	}

	public void clickObject1(int wait) {
		lib.clickElement(byFields.xpath, lib.getProp(prop.sleepObject1), wait);
	}
	
	public void clickObject2(int wait) {
		lib.clickElement(byFields.xpath, lib.getProp(prop.sleepObject2), wait);
	}

	public void clickObject3(int wait) {
		lib.clickElement(byFields.xpath, lib.getProp(prop.sleepObject3), wait);
	}
		
	public void clickObject3Fluent(int timeout, int polling) {
		lib.setFluentWait(timeout, polling);
		lib.getElementFluent(byFields.xpath, lib.getProp(prop.sleepObject3)).click();
	}
	
	public void clickObject2Fluent(int timeout, int polling) {
		lib.setFluentWait(timeout, polling);
		lib.getElementFluent(byFields.xpath, lib.getProp(prop.sleepObject2)).click();
	}
	
	public void findObject()
	{
		lib.elementExists(byFields.xpath, lib.getProp(prop.sleepNotFound));
	}
	
	public void fluentVerify()
	{
		lib.getElementFluent(byFields.xpath, lib.getProp(prop.sleepVerifyObject));	
	}
	
	public void verify()
	{
		lib.getElement(byFields.xpath, lib.getProp(prop.sleepVerifyObject));	
	}
}
