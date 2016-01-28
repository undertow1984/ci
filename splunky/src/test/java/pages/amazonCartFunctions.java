package pages;

import utilities.*;
import utilities.library.byFields;
import utilities.library.prop;

public class amazonCartFunctions {
	private library lib;

	public amazonCartFunctions(library l) {
		this.lib = l;
	}

	//page level - add item to cart
	public void addToCart(int wait) {
		lib.clickElement(byFields.xpath, lib.getProp(prop.amazonAddToCart), wait);

	}

	//page level proceed to checkout
	public void proceedToCheckout(int wait) {
		lib.clickElement(
				byFields.xpath,
				lib.getProp(prop.amazonProceedToCheckOut),
				wait);
	}
	
	//click the cart icon
	public void clickCart()
	{
		lib.clickElement(byFields.xpath,lib.getProp(prop.amazonCartIcon),60);
	}
	
	//click the first delete button for cart items
	public void deleteFromCart()
	{
		while(lib.elementExists(byFields.xpath, lib.getProp(prop.amazonDeleteFromCart)))
		{
			lib.clickElement(byFields.xpath,lib.getProp(prop.amazonDeleteFromCart),60);
		}
	
	}

}
