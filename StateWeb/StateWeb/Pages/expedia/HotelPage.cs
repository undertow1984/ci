using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace StateWeb.expedia
{
    class HotelPage
    {
        private Library lib;

        public HotelPage(Library lib)
        {
            this.lib = lib;
        }

        public void selectRoom()
        {
            lib.clickElement(Library.byFields.xpath, lib.getAppProp(Library.appProp.selectRoom), 10);
        }

        public void standardRoom()
        {
            lib.clickElement(Library.byFields.xpath, lib.getAppProp(Library.appProp.standardRoom), 10);
        }
    }
}
