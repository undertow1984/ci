package pages;

public class chromeHelpers {
	private library lib;
	
	public chromeHelpers(library l)
	{
		this.lib = l;	
	}
	
	
	public void firstOpenAccepteance()
	{
		if (lib.getTarget().contains("Galaxy"))
		  {
			lib.switchToContext("VISUAL");
			  if (lib.elementExists("partialLinkText", "Accept"))
			  {
				  lib.clickElement("partialLinkText", "Accept", 10);
			  }
			  
			  if (lib.elementExists("partialLinkText", "NO THANKS"))
			  {
				  lib.clickElement("partialLinkText", "NO THANKS", 10);
			  }
			  
			  lib.switchToContext("WEBVIEW");
		  }
		}
}
