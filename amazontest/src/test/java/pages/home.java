package pages;

import utilities.*;

public class home {
	private library lib;

	public home(library l) {
		this.lib = l;
	}
	
	//enter text in search box
	public void searchBoxText(String searchbox, int wait) {
		lib.setText(
				"xpath",
				"(//input[@id='nav-search-keywords' or @id='twotabsearchtextbox'] )[1]",
				searchbox, true, wait);

	}
	
	//submit the search box value
	public void searchBoxSubmit(int wait) {
		lib.submitElement(
				"xpath",
				"(//input[@id='nav-search-keywords'  or @id='twotabsearchtextbox'])[1]",
				wait);
	}

}
