package pages;

import utilities.library;
import utilities.library.byFields;
import utilities.library.prop;

public class home {
	
	private library lib;

	public home(library l) {
		this.lib = l;
	}

	
	public void ignoreApp()
	{
		lib.clickElement(byFields.partialLinkText, "No, thanks", 60);
	}
	
	public void switchToDesktopSite()
	{
		lib.clickElement(byFields.linkText, "Desktop Site", 60);
	}
	
	public void enterBusiness(String text)
	{
		lib.setText(byFields.xpath, lib.getProp(prop.findBox), text, true, 60);
	}
	
	public void enterLocation(String text)
	{
		lib.setText(byFields.xpath, lib.getProp(prop.nearBox), text, true, 60);
	}
	
	public void submitResults()
	{
		lib.submitElement(byFields.xpath, lib.getProp(prop.searchButton), 60);
	}

}
