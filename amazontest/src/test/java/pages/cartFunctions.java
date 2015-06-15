package pages;

import utilities.*;
import utilities.library.byFields;
import utilities.library.prop;

public class cartFunctions {
	private library lib;

	public cartFunctions(library l) {
		this.lib = l;
	}

	//page level - add item to cart
	public void addToCart(int wait) {
		lib.clickElement(byFields.xpath, lib.getProp(prop.addToCart), wait);

	}

	//page level proceed to checkout
	public void proceedToCheckout(int wait) {
		lib.clickElement(
				byFields.xpath,
				lib.getProp(prop.proceedToCheckOut),
				wait);
	}
	
	//click the cart icon
	public void clickCart()
	{
		lib.clickElement(byFields.xpath,lib.getProp(prop.cartIcon),60);
	}
	
	//click the first delete button for cart items
	public void deleteFromCart()
	{
		while(lib.elementExists(byFields.xpath, lib.getProp(prop.deleteFromCart)))
		{
			lib.clickElement(byFields.xpath,lib.getProp(prop.deleteFromCart),60);
		}
	
	}

}
