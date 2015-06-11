package pages;

public class cartFunctions {
	private library lib;
	
	public cartFunctions(library l)
	{
		this.lib = l;
	}
	
	public void addToCart(int wait)
	{
		lib.clickElement("xpath", "//*[@id='add-to-cart-button']", wait);
		  
		
		
	}
	
	public void proceedToCheckout(int wait)
	{
		lib.clickElement("xpath", "(//*[@id='a-autoid-0-announce' or @id='hlb-ptc-btn-native'])[1]", wait);
	}

}
