package pages;

import utilities.library;
import testing.*;
import utilities.androidHelper;
import utilities.library.availableContexts;
import utilities.library.byFields;
import utilities.library.mobileDrivers;
import utilities.library.prop;

public class home {
	
	private library lib;
	private androidHelper android;

	public home(library l, androidHelper a) {
		this.lib = l;
		this.android = a;
	}
	
	public void goTo(String url, String title)
	{
	lib.log("Going to " + url, false);
	lib.goToPage(url,
			title);

	if (lib.isDevice()) {
		lib.log("Checking if Chrome needs acceptance", false);
		android.chromeFirstOpenAccepteance(15);
	}
	}

	
	public void ignoreApp()
	{
	// Seeing if the download app nag is showing and handle it
				if (lib.isDevice()) {
					if (lib.getLocal()) {
						lib.setCurrentMobileDriver(mobileDrivers.visualDriver);
						if (lib.elementExists(byFields.partialLinkText,
								"No, thanks")) {
							lib.clickElement(byFields.partialLinkText, "No, thanks", 60);
						}
						lib.setCurrentMobileDriver(mobileDrivers.domDriver);
					} else {
						lib.switchToContext(availableContexts.VISUAL);
						if (lib.elementExists(byFields.partialLinkText,
								"No, thanks")) {
							lib.clickElement(byFields.partialLinkText, "No, thanks", 60);
						}
						lib.switchToContext(availableContexts.WEBVIEW);
					}
				}
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
