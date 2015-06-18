package pages;

import utilities.androidHelper;
import utilities.library;
import utilities.library.byFields;
import utilities.library.prop;

public class resultsPage {
		
	private library lib;	
	private androidHelper android;

	public resultsPage(library l, androidHelper a) {
		this.lib = l;
		this.android=a;
	}
	
	
	public void selectDollarDollar()
	{
		lib.clickElement(byFields.xpath, lib.getProp(prop.dollarDollar), 60);
	}
	
	public void selectMoreFeatures()
	{
		lib.clickElement(byFields.linkText, "More Features", 60);
		//lib.switchToFrame(lib.getElement(byFields.xpath, lib.getProp(prop.iframe)));
	}
	
	public void selectParking()
	{
		lib.clickElement(byFields.xpath, lib.getProp(prop.parkingLink), 60);
	}
	
	public void parkingRequired()
	{
		lib.clickElement(byFields.xpath, lib.getProp(prop.streetOption), 60);
		lib.clickElement(byFields.xpath, lib.getProp(prop.garageOption), 60);
		lib.clickElement(byFields.xpath, lib.getProp(prop.valetOption), 60);
		lib.clickElement(byFields.xpath, lib.getProp(prop.lotOption), 60);
	}
	
	public void sortHighestRated()
	{
		lib.clickElement(byFields.xpath, lib.getProp(prop.highestRatedLink), 60);
	}
	
	public String returnHighestRated()
	{
		return lib.getText(byFields.xpath, lib.getProp(prop.highestRated), 60);
	}
	
	public void filterFeatures()
	{
		lib.clickElement(byFields.xpath, lib.getProp(prop.filterButton), 60);
	}

}
