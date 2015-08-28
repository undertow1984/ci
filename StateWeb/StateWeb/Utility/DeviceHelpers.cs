using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace StateWeb.Utility
{
    class DeviceHelpers
    {
        private Library lib;

        public DeviceHelpers(Library lib)
        {
            this.lib = lib;
        }

        public void initializeChrome()
        {
            lib.switchToContext(Library.availableContexts.VISUAL);

            if (lib.isElementPresent(Library.byFields.partialLinkText,"Accept"))
            { 
                lib.clickElement(Library.byFields.partialLinkText, "Accept", 15);
            }
            if (lib.isElementPresent(Library.byFields.partialLinkText, "THANKS"))
            {
                lib.clickElement(Library.byFields.partialLinkText, "THANKS", 15);
            }

            if (lib.isElementPresent(Library.byFields.partialLinkText, "Done"))
            {
                lib.clickElement(Library.byFields.partialLinkText, "Done", 15);
            }
            lib.switchToContext(Library.availableContexts.WEBVIEW);
        }



    }
}
