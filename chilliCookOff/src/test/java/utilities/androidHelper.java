package utilities;

import java.util.HashMap;
import java.util.Map;

import utilities.library;
import utilities.library.availableContexts;
import utilities.library.byFields;
import utilities.library.remoteD;

import org.openqa.selenium.By;

import utilities.*;

public class androidHelper {
	private library lib;
	
	public androidHelper(library l) {
		this.lib = l;
	}
	
	//used to accept the messaging from chrome upon first launch
	//on in case of app data reset / app update
	public void chromeFirstOpenAccepteance(int wait)
	{
		if (lib.getDriver(remoteD.True).getCapabilities().getCapability("platform")
				.equals("Android"))			
		  {
			lib.switchToContext(availableContexts.VISUAL);
			  if (lib.elementExists(byFields.partialLinkText, "Accept"))
			  {
				  lib.clickElement(byFields.partialLinkText, "Accept", wait);
			  }
			  
			  if (lib.elementExists(byFields.partialLinkText, "NO THANKS"))
			  {
				  lib.clickElement(byFields.partialLinkText, "NO THANKS", wait);
			  }
			  
			  lib.switchToContext(availableContexts.WEBVIEW);
		  }
		  lib.incStep();
		}
	
	// this needs to be further expanded out to support all available methods
	//this is not used and just here for future purposes
	public void networkSettings(Boolean wifi, Boolean data, Boolean airplane) {
		String command = "mobile:network.settings:set";
		Map<String, Object> params = new HashMap<>();
		if(wifi)
		{
			params.put("WiFi", "enabled");
		}
		else
		{
			params.put("WiFi", "disabled");
		}			
		if(data)
		{
			params.put("Data", "enabled");
		}
		else
		{
			params.put("Data", "disabled");
		}		
		if(airplane)
		{
			params.put("Airplane mode", "enabled");
		}
		else
		{			
			params.put("Airplane mode", "disabled");
		}
		
		lib.getDriver(remoteD.True).executeScript(command, params);
	}
}
