using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;


namespace StateWeb
{
    [TestClass]
    public partial class StateWebTest
    {
        


        [TestMethod]
        public void SearchIphoneWeb()
        {
            testSetup("iphoneSearch.prop");
            search();
        }

        [TestMethod]
        public void SearchAndroidWeb()
        {
            testSetup("androidSearch.prop");
            search();
        }

        [TestMethod]
        public void SearchChromeWeb()
        {
            testSetup("chromeSearch.prop");
            search();
        }

        [TestMethod]
        public void SearchFirefoxWeb()
        {
            testSetup("firefoxSearch.prop");
            search();
        }

        [TestMethod]
        public void SearchInternetExplorerWeb()
        {
            testSetup("internetExplorerSearch.prop");
            search();
        }


        public void search()
        {
            search.Home homePage = new search.Home(lib);
            search.SearchResults searchPage = new search.SearchResults(lib);

            homePage.launchSite();
            homePage.search("Passport");
            searchPage.selectArticleNumber("4", "Passport");            
        }

    }
}
