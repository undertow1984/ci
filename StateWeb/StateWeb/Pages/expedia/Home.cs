using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace StateWeb.expedia
{
    class Home
    {
        private Library lib;

        public Home(Library lib)
        {
            this.lib = lib;
        }               

        public void startApp()
        {
            lib.initializeApp();
            lib.startApplication(lib.applicationName);
        }

        public void selectHotels()
        {
            lib.clickElement(Library.byFields.xpath, lib.getAppProp(Library.appProp.hotel), 10);
        }

        
    }
}
