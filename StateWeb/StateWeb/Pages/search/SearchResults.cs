using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace StateWeb.search
{
    class SearchResults
    {
        private Library lib;

        public SearchResults(Library lib)
        {
            this.lib = lib;
        }

        public void selectArticleNumber(string number, string validationText)
        {
            lib.clickElement(Library.byFields.xpath, String.Format(lib.getWebProp(Library.webProp.articles), number), 10);
            
            Assert.IsTrue(lib.isElementPresent(Library.byFields.xpath, "//*[contains(text(),'" + validationText + "')]"));
            
        }
    }
}
