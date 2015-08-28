using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace StateWeb.search
{
    class Home
    {
        private Library lib;

        public Home(Library lib)
        {
            this.lib = lib;
        }

        public void launchSite()
        {
            Utility.DeviceHelpers dh = new Utility.DeviceHelpers(lib);

            lib.goToUrl(lib.getWebProp(Library.webProp.site)); 
            if (lib.browser == "Chrome" && lib.os=="Android")
            {
                dh.initializeChrome();
            }

            
        }

        public void search(string searchText)
        {

            if(lib.os=="Android")
            {
                lib.clickElement(Library.byFields.xpath, lib.getWebProp(Library.webProp.searchIcon), 10);
                lib.sendText(Library.byFields.xpath, lib.getWebProp(Library.webProp.searchField), searchText, 10, true);
                lib.pressEnter();
            }
            else if (lib.os=="iOS")
            {
                lib.clickElement(Library.byFields.xpath, lib.getWebProp(Library.webProp.searchIcon), 10);
                lib.switchToContext(Library.availableContexts.VISUAL);               
                lib.visualSetText("Search:", "25%", searchText);
                lib.switchToContext(Library.availableContexts.WEBVIEW);
                lib.pressEnter();
            }

            if (lib.type!="Mobile")
            {
                lib.sendText(Library.byFields.xpath, lib.getWebProp(Library.webProp.searchField), searchText, 10, true);
                lib.clickElement(Library.byFields.xpath, lib.getWebProp(Library.webProp.searchButton), 10);
            }
        }
    }
}
