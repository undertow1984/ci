package pages;

import utilities.*;

public class cartFunctions {
	private library lib;

	public cartFunctions(library l) {
		this.lib = l;
	}

	public void addToCart(int wait) {
		lib.clickElement("xpath", "//*[@id='add-to-cart-button']", wait);

	}

	public void proceedToCheckout(int wait) {
		lib.clickElement(
				"xpath",
				"(//*[@id='a-autoid-0-announce' or @id='hlb-ptc-btn-native'])[1]",
				wait);
	}
	
	public void clickCart()
	{
		lib.clickElement("xpath","//*[@id='nav-button-cart' or @id='nav-cart']",60);
	}
	
	public void deleteFromCart()
	{
		while(lib.elementExists("xpath", "//input[@value='Delete']"))
		{
			lib.clickElement("xpath","//input[@value='Delete']",60);
		}
	
	}

}
