package pages;

import utilities.*;

public class chromeHelpers {
	private library lib;
	
	public chromeHelpers(library l)
	{
		this.lib = l;	
	}
	
	
	public void firstOpenAccepteance(int wait)
	{
		if (lib.getDriver().getCapabilities().getCapability("platform")
				.equals("Android"))			
		  {
			lib.switchToContext("VISUAL");
			  if (lib.elementExists("partialLinkText", "Accept"))
			  {
				  lib.clickElement("partialLinkText", "Accept", wait);
			  }
			  
			  if (lib.elementExists("partialLinkText", "NO THANKS"))
			  {
				  lib.clickElement("partialLinkText", "NO THANKS", wait);
			  }
			  
			  lib.switchToContext("WEBVIEW");
		  }
		}
}
