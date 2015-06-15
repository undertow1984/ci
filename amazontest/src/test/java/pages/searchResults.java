package pages;

import utilities.*;
import utilities.library.byFields;

public class searchResults {
	private library lib;

	public searchResults(library l) {
		this.lib = l;

	}

	//select search result element based on visible text
	public void selectResult(String result, int wait) {
		lib.clickElement(byFields.linkText, result, wait);
	}

}
