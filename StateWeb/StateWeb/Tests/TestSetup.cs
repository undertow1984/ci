using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium.Remote;
using System.Web;

namespace StateWeb
{
    public partial class Library
    {
        

        // page properties
        public enum webProp
        {
            site, searchField, pressRelease, articles, searchButton, searchIcon
        }

        public enum appProp
        {
            hotel,locationSearch,sort,sortByRating,stayBridge,selectRoom,standardRoom
        }

        public string getWebProp(webProp propName)
        {
            return pageProperties.get(propName.ToString());
        }

        public string getAppProp(appProp propName)
        {
            return pageProperties.get(propName.ToString());
        }

    }

    

    public partial class StateWebTest
    {
        
        Library lib;
        public TestContext TestContext { get; set; }
        public string testName;

        [TestInitialize]
        public void Initialize()
        {
            testName = TestContext.TestName;
        }

        public void testSetup(string prop)
        {
            lib = new Library(prop, testName);            
        }

        [TestCleanup()]
        public void CleanUp()
        {
            lib.close();
        }
    }
}
