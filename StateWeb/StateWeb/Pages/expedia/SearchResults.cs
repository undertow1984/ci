using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace StateWeb.expedia
{
    class SearchResults
    {
        private Library lib;

        public SearchResults(Library lib)
        {
            this.lib = lib;
        }

        public void searchCity(string searchText)
        {
            lib.sendText(Library.byFields.xpath, lib.getAppProp(Library.appProp.locationSearch), searchText, 10, true);
            lib.pressEnter();
            lib.sleep(5000);
        }

        public void filterRated()
        {
            lib.clickElement(Library.byFields.xpath, lib.getAppProp(Library.appProp.sort),10);
            lib.clickElement(Library.byFields.xpath, lib.getAppProp(Library.appProp.sortByRating), 10);
        }

        public void selectHotel()
        {
            lib.clickElement(Library.byFields.xpath, lib.getAppProp(Library.appProp.stayBridge), 10);
        }
    }
}
