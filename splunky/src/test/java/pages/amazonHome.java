package pages;

import utilities.*;
import utilities.library.byFields;
import utilities.library.prop;

public class amazonHome {
	private library lib;

	public amazonHome(library l) {
		this.lib = l;
	}
	
	//enter text in search box
	public void searchBoxText(String searchbox, int wait) {
		lib.setText(
				byFields.xpath,
				lib.getProp(prop.amazonSearchBox),
				searchbox, true, wait);

	}
	
	//submit the search box value
	public void searchBoxSubmit(int wait) {
		lib.submitElement(
				byFields.xpath,
				lib.getProp(prop.amazonSearchBox),
				wait);
	}

}
