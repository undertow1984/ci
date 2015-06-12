package pages;

import java.util.HashMap;
import java.util.Map;

import utilities.*;

public class androidHelper {
	private library lib;
	
	public androidHelper(library l)
	{
		this.lib = l;	
	}
	
	//used to accept the messaging from chrome upon first launch
	//on in case of app data reset / app update
	public void chromeFirstOpenAccepteance(int wait)
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
	
	// this needs to be further expanded out to support all available methods
	//this is not used and just here for future purposes
	public void networkSettings(String wifi, String data, String airplane) {
		String command = "mobile:networksettings:set";
		Map<String, Object> params = new HashMap<>();
		params.put("WiFi", "disabled");
		params.put("Data", "disabled");
		params.put("Airplane mode", "disabled");
		lib.getDriver().executeScript(command, params);
	}
}
