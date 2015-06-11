package pages;

import utilities.*;

public class home {
	private library lib;

	public home(library l) {
		this.lib = l;

	}

	public void searchBoxText(String searchbox, int wait) {
		lib.setText(
				"xpath",
				"(//input[@id='nav-search-keywords' or @id='twotabsearchtextbox'] )[1]",
				searchbox, true, wait);

	}

	public void searchBoxSubmit(int wait) {
		lib.submitElement(
				"xpath",
				"(//input[@id='nav-search-keywords'  or @id='twotabsearchtextbox'])[1]",
				wait);
	}

}
