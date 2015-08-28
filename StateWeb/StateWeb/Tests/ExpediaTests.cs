using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;


namespace StateWeb
{

    public partial class StateWebTest
    {
        [TestMethod]
        public void SearchAndroidExpediaHotel()
        {
            testSetup("androidExpedia.prop");
            searchHotel();
        }

        public void searchHotel()
        {
            lib.switchToContext(Library.availableContexts.NATIVE_APP);
            expedia.Home homePage = new expedia.Home(lib);
            expedia.SearchResults searchPage = new expedia.SearchResults(lib);
            expedia.HotelPage hotelPage = new expedia.HotelPage(lib);
            expedia.Room selectRoom = new expedia.Room(lib);

            homePage.startApp();

            homePage.selectHotels();

            searchPage.searchCity("Las Vegas, Nevada");

            searchPage.filterRated();

            searchPage.selectHotel();

            selectRoom.selectRoom();

            selectRoom.selectStandard();

        }

        
    }
}
