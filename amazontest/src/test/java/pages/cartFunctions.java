package pages;

import utilities.*;

public class cartFunctions {
	private library lib;

	public cartFunctions(library l) {
		this.lib = l;
	}

	//page level - add item to cart
	public void addToCart(int wait) {
		lib.clickElement("xpath", "//*[@id='add-to-cart-button']", wait);

	}

	//page level proceed to checkout
	public void proceedToCheckout(int wait) {
		lib.clickElement(
				"xpath",
				"(//*[@id='a-autoid-0-announce' or @id='hlb-ptc-btn-native'])[1]",
				wait);
	}
	
	//click the cart icon
	public void clickCart()
	{
		lib.clickElement("xpath","//*[@id='nav-button-cart' or @id='nav-cart']",60);
	}
	
	//click the first delete button for cart items
	public void deleteFromCart()
	{
		while(lib.elementExists("xpath", "//input[@value='Delete'][1]"))
		{
			lib.clickElement("xpath","//input[@value='Delete'][1]",60);
		}
	
	}

}
