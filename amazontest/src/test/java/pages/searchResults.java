package pages;

import utilities.*;

public class searchResults {
	private library lib;

	public searchResults(library l) {
		this.lib = l;

	}

	public void selectResult(String result, int wait) {
		lib.clickElement("linkText", result, wait);
	}

}
