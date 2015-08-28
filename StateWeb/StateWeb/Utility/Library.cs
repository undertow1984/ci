using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading;
using System.Web;
using System.Drawing;
using OpenQA.Selenium.Support.UI;
using System.Diagnostics;
using TestRemote;
using OpenQA.Selenium.IE;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net;

namespace StateWeb
{
    public partial class Library
    {
        private RemoteWebDriverExtended driver;
        public string snapshotDirectory;
        public string reportDirectory;
        public string attachmentDirectory;
        public string type;
        public string os;
        public string version;
        public string manufacturer;
        public string model;
        public string browser;
        public string testType;
        public string deviceID;
        private Properties testProperties;
        private Properties pageProperties;
        public string host;
        public string user;
        public string password;
        public string applicationName;
        public string repositoryKey;
        public string uploadLocation;
        public string pageObjects;
        public string testObjects;
        public int step = 1;
        public string testName;
        public instrument instrumentApp;
        public bool overwriteUpload;
        public int implicitWaitTime;
        public int pageLoadTime;
        public bool appInstalled=false;
        public string originalContext;

        public enum androidAppIdentifer
        {
            settings, wifiSettings, runningServices, manageApplications,batteryStats, dateTime, language, security, simcard, dev, appUsage, deviceInfo, userDictionary
        }

        // test properties
        public enum testProp
        {
            implicitWaitTime, pageLoadTime, overwriteUpload, instrument, pageObjects, snapshotDirectory, attachmentDirectory, reportDirectory, uploadLocation, host, user, pass, type, deviceid, os, version, manufacturer, model, browser, testType, applicationName, repositoryKey
        }

        public string getAndroidIdentifer(androidAppIdentifer a)
        {
            switch (a)
            {
                case androidAppIdentifer.settings:
                    return "com.android.settings/.Settings";
                    break;
                case androidAppIdentifer.wifiSettings:
                    return "com.android.settings/.wifi.WifiSettings";
                    break;
                case androidAppIdentifer.runningServices:
                    return "com.android.settings/.RunningServices";
                    break;
                case androidAppIdentifer.manageApplications:
                    return "com.android.settings/.ManageApplications";
                    break;
                case androidAppIdentifer.batteryStats:
                    return "com.android.settings/.fuelgauge.PowerUsageSummary";
                    break;
                case androidAppIdentifer.dateTime:
                    return "com.android.settings/.DateTimeSettingsSetupWizard";
                    break;
                case androidAppIdentifer.language:
                    return "com.android.settings/.LanguageSettings";
                    break;
                case androidAppIdentifer.security:
                    return "com.android.settings/.SecuritySettings";
                    break;
                case androidAppIdentifer.simcard:
                    return "com.android.settings/.IccLockSettings";
                    break;
                case androidAppIdentifer.dev:
                    return "com.android.settings/.DevelopmentSettings";
                    break;
                case androidAppIdentifer.appUsage:
                    return "com.android.settings/.UsageStats";
                    break;
                case androidAppIdentifer.deviceInfo:
                    return "com.android.settings/.deviceinfo.Status";
                    break;
                case androidAppIdentifer.userDictionary:
                    return "com.android.settings/.UserDictionarySettings";
                    break;
                default:
                    return "";
                        break;
            }
        }

        public Library(string prop, string testName)
        {
            loadTestProperty(prop);
            setupAndConnect();
            this.testName = testName;
        }

        public void takeScreenStep(string name, bool mobileScreen = false)
        {
            if (type == "Desktop" || mobileScreen==true)
            {
                takeScreenShot(name);
            }
            step++;
        }

        public enum byFields
        {
            id, name, css, tag, className, linkText, partialLinkText, xpath
        }

        public enum instrument
        {
            noinstrument, instrument
        }


        public void keyboardSendText(string text)
        {
            log("keyboardSendText("+text+")");

            driver.Keyboard.SendKeys(text);

            takeScreenStep("keyboardSendText");
        }

        public enum availableContexts
        {
            WEBVIEW, NATIVE_APP, VISUAL
        }

        public void setupAndConnect()
        {
            this.host = getTestProp(Library.testProp.host);
            this.user = HttpUtility.UrlEncode(getTestProp(Library.testProp.user));
            this.password = HttpUtility.UrlEncode(getTestProp(Library.testProp.pass));
            this.type = getTestProp(Library.testProp.type);
            this.os = (getTestProp(Library.testProp.os) == "") ? null : getTestProp(Library.testProp.os);
            this.version = (getTestProp(Library.testProp.version) == "") ? null : getTestProp(Library.testProp.version);
            this.manufacturer = (getTestProp(Library.testProp.manufacturer) == "") ? null : getTestProp(Library.testProp.manufacturer);
            this.model = (getTestProp(Library.testProp.model) == "") ? null : getTestProp(Library.testProp.model);
            this.browser = (getTestProp(Library.testProp.browser) == "") ? null : getTestProp(Library.testProp.browser);
            this.deviceID = (getTestProp(Library.testProp.deviceid) == "") ? "" : getTestProp(Library.testProp.deviceid);
            this.testType = (getTestProp(Library.testProp.testType) == "") ? null : getTestProp(Library.testProp.testType);
            this.applicationName = (getTestProp(Library.testProp.applicationName) == "") ? null : getTestProp(Library.testProp.applicationName);
            this.repositoryKey = (getTestProp(Library.testProp.repositoryKey) == "") ? null : getTestProp(Library.testProp.repositoryKey);
            this.uploadLocation = (getTestProp(Library.testProp.uploadLocation) == "") ? null : getTestProp(Library.testProp.uploadLocation);
            this.snapshotDirectory = (getTestProp(Library.testProp.snapshotDirectory) == "") ? null : getTestProp(Library.testProp.snapshotDirectory);
            this.attachmentDirectory = (getTestProp(Library.testProp.attachmentDirectory) == "") ? null : getTestProp(Library.testProp.attachmentDirectory);
            this.reportDirectory = (getTestProp(Library.testProp.reportDirectory) == "") ? null : getTestProp(Library.testProp.reportDirectory);
            this.pageObjects = (getTestProp(Library.testProp.pageObjects) == "") ? "" : getTestProp(Library.testProp.pageObjects);
            this.overwriteUpload = (getTestProp(Library.testProp.overwriteUpload) == "") ? false : bool.Parse(getTestProp(Library.testProp.overwriteUpload));
            this.implicitWaitTime=(getTestProp(Library.testProp.implicitWaitTime) == "") ? 0 : int.Parse(getTestProp(Library.testProp.implicitWaitTime));
            this.pageLoadTime = (getTestProp(Library.testProp.pageLoadTime) == "") ? 0 : int.Parse(getTestProp(Library.testProp.pageLoadTime));


            if (getTestProp(testProp.instrument)=="true")
            {
                this.instrumentApp = instrument.instrument;
            }
            else if (getTestProp(testProp.instrument)=="false")
            {
                this.instrumentApp = instrument.noinstrument;
            }
            

            if (pageObjects != "")
            {
                loadPageProperty(getTestProp(Library.testProp.pageObjects));
            }

            connectLab();

            

            if (os == "Android" && testType == "Web")
            {
                cleanApplication("chrome");
            }
        }

        public void initializeApp()
        {
            appInstalled = isAppInstalledQuick();
            if (testType == "Application" && uploadLocation != ""  && appInstalled==false)
            {
                uploadMedia(uploadLocation, repositoryKey, overwriteUpload);
                installApplication(repositoryKey, instrumentApp);
                appInstalled = true;
            }
        }

        public void loadTestProperty(string filePath)
        {
            testProperties = new Properties(filePath);
        }

        public void loadPageProperty(string filePath)
        {
            pageProperties = new Properties(filePath);
        }

        public string getTestProp(testProp propName)
        {
            return testProperties.get(propName.ToString());
        }


        public void connectLab()
        {
            if (type != "Mobile")
            {
                log("connecting lab desktop browser");
                DesiredCapabilities capabilities = new DesiredCapabilities();

                if (browser == "Internet Explorer")
                {
                    capabilities.SetCapability("ignoreProtectedModeSettings", true);
                }

                capabilities.SetCapability("browserName", browser);

                ICommandExecutor commandExecutor = new HttpAuthenticatedCommandExecutor(new Uri("http://" + host + "/wd/hub"));
                driver = new RemoteWebDriverExtended(commandExecutor, capabilities);

            }
            else
            {
                log("connecting lab mobile device");
                DesiredCapabilities capabilities = new DesiredCapabilities();


                if (deviceID.Trim() == "")
                {
                    capabilities.SetCapability("platformName", os);
                    capabilities.SetCapability("platformVersion", version);
                    capabilities.SetCapability("browserName", browser);
                    capabilities.SetCapability("manufacturer", manufacturer);
                    capabilities.SetCapability("model", model);
                }
                else
                {
                    capabilities.SetCapability("deviceName", deviceID);
                    capabilities.SetCapability("browserName", browser);
                }

                ICommandExecutor commandExecutor = new HttpAuthenticatedCommandExecutor(new Uri("https://" + user + ':' + password + '@' + host + "/nexperience/wd/hub"));
                driver = new RemoteWebDriverExtended(commandExecutor, capabilities);
            }
            driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(implicitWaitTime));
            driver.Manage().Timeouts().SetPageLoadTimeout(TimeSpan.FromSeconds(pageLoadTime));
        }

        public void close()
        {
            log("closing driver");
            try
            {
                driver.Close();
            }
            catch (Exception)
            {

            }

            if (type == "Mobile")
            {
                try
                {
                    log("downloading pdf report");
                    downloadReport("pdf", "Report", ".pdf");
                }
                catch (Exception)
                {

                }
            }

            if (type == "Mobile")
            {
                try
                {
                    log("downloading mobile video");
                    downloadAttachment("video", "Video", ".mp4");
                }
                catch (Exception)
                {

                }
            }

            try
            {
                driver.Quit();
            }
            catch (Exception)
            {

            }

        }


        public void installApplication(string repositoryKey, instrument inst)
        {
            log("install Application: " + repositoryKey + " , instrumentation: " + inst.ToString());

            string command = "mobile:application:install";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("file", repositoryKey);
            parameters.Add("instrument", inst.ToString());
            driver.ExecuteScript(command, parameters);
        }

        public void uninstallApplication(string appName)
        {
            log("uninstall Application: " + appName);
            string command = "mobile:application:uninstall";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("name", appName);
            driver.ExecuteScript(command, parameters);
        }

        public void setOriginalContext()
        {
            log("setOriginalContext: " + driver.Context);
            originalContext = driver.Context;
        }

        public void restoreContext()
        {
            if (originalContext.Contains("WEB"))
            {
                if (!driver.Context.Contains("WEB"))
                {
                    log("restoreContext: " + availableContexts.WEBVIEW);
                    switchToContext(availableContexts.WEBVIEW);
                }
            }
            else if (originalContext.Contains("NATIVE"))
            {
                if (!driver.Context.Contains("NATIVE"))
                {
                    switchToContext(availableContexts.NATIVE_APP);
                    log("restoreContext: " + availableContexts.NATIVE_APP);
                }
            }
            else if (originalContext.Contains("VISUAL"))
            {
                if (!driver.Context.Contains("VISUAL"))
                {
                    switchToContext(availableContexts.VISUAL);
                    log("restoreContext: " + availableContexts.VISUAL);
                }
            }
        }

        public bool isAppInstalledManual()
        {
            if (os=="Android")
            {
                startApplication(getAndroidIdentifer(androidAppIdentifer.manageApplications), true);
                sleep(2000);
                switchToContext(availableContexts.VISUAL);
                int trying = 0;

                while (!isElementPresent(byFields.linkText, applicationName) || trying < 20)
                {
                    string command = "mobile:touch:swipe";
                    Dictionary<string, object> parameters = new Dictionary<string, object>();
                    parameters.Add("start", "50%,90%");
                    parameters.Add("end", "50%,35%");
                    driver.ExecuteScript(command, parameters);
                    trying++;
                }

                if (trying < 20)
                {
                    appInstalled = true;
                    restoreContext();
                    return true;
                }
                else
                {
                    restoreContext();
                    return false;
                }
            }
            else
            {
                try
                {
                    startApplication(applicationName);
                    closeApplication(applicationName);
                    try
                    {
                        closeApplication(applicationName);
                    }
                    catch (Exception ex)
                    {
                    }
                    restoreContext();
                    return true;
                }
                catch (Exception ex)
                {
                    restoreContext();
                    return false;
                }
                
            }
            
            
        }

        public bool isAppInstalledQuick()
        {
           
                if (startApplication(applicationName))
                { 
                closeApplication(applicationName);
                try
                {
                    closeApplication(applicationName);
                    closeApplication(applicationName);
                }
                catch (Exception ex)
                { 
                }

                log("isAppInstalledQuick: true");
                return true;
                }
                else
                {
                    log("isAppInstalledQuick: false");
                    return false;
                }
                
           
        }

        public void uploadMedia(string path, string repositoryKey, Boolean overwrite)
        {
            log("uploadMedia: " + path + "," + repositoryKey + "," + overwrite);
            string urlStr = "https://" + host + "/services/repositories/media/" + repositoryKey + "?operation=upload&overwrite=" + overwrite.ToString() + "&user=" + user + "&password=" + password;
            uploadFile(urlStr, path);
        }

        public void uploadFile(string url, string filename)
        {
            log("uploadFile: " + url + "," + filename);
            Uri uri = new Uri(url);
            using (var stream = File.OpenRead(filename))
            {
                HttpClient client = new HttpClient();
                var response = client.PutAsync(uri, new StreamContent(stream)).Result;
                if (response.IsSuccessStatusCode)
                {
                    var responseContent = response.Content;
                    string responseString = responseContent.ReadAsStringAsync().Result;
                    Console.WriteLine(responseString);
                }
            }
        }


        public void sendKeyEvent(string key)
        {
            log("sendKeyEvent: " + key);
            string command = "mobile:key:event";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("key", key);
            driver.ExecuteScript(command, parameters);
            takeScreenStep("sendKeyEvent");
        }

        public void visualSetText(string label, string labelOffset, string text)
        {
            log("visualSetText: " + label + "," + labelOffset + "," + text);
            string command = "mobile:edit-text:set";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("label", label);
            parameters.Add("text", text);
            parameters.Add("label.offset", labelOffset);
            driver.ExecuteScript(command, parameters);
            takeScreenStep("visualSetText");
        }


        public void switchToContext(availableContexts context)
        {
            log("switchToContext: " + context);
            setOriginalContext();
            driver.Context = context.ToString();
        }


        public bool startApplication(string appName, bool identifier=false)
        {
            log("startApplication: " + appName + "," + identifier);
            switchToContext(availableContexts.NATIVE_APP);
            string command = "mobile:application:open";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            if (identifier)
            {
                parameters.Add("identifier", "com.android.settings/.ManageApplications");    
            }
            else
            {
                parameters.Add("name", appName);
            }
            
            try
            { 
            driver.ExecuteScript(command, parameters);
            restoreContext();
            return true;
                }
            catch (Exception ex)
            {
                restoreContext();
                return false;
            }

            takeScreenStep("startApplication");
        }


        public String getOS()
        {
            return driver.Capabilities.Platform.PlatformType.ToString();
        }


        public void closeApplication(string application)
        {
            log("closeApplication: " + application);
            switchToContext(availableContexts.NATIVE_APP);
            string command = "mobile:application:close";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("name", application);
            driver.ExecuteScript(command, parameters);
            
            takeScreenStep("closeApplication");
            restoreContext();
        }


        public void cleanApplication(string application)
        {
            log("cleanApplication: " + application);
            string command = "mobile:application:clean";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("name", application);
            driver.ExecuteScript(command, parameters);
        }
                

        public String getCurrentTime()
        {

            DateTime saveNow = DateTime.Now;

            DateTime myDt;
            myDt = DateTime.SpecifyKind(saveNow, DateTimeKind.Local);

            String datePatt = @"yyyyMMdd_HHmmss";
            DateTime dispDt = saveNow;
            String dtString = dispDt.ToString(datePatt);

            return dtString;
        }



        public void sleep(int sleeptime)
        {
            System.Threading.Thread.Sleep(sleeptime);
        }

        public By getBy(byFields by, String element)
        {
            if (by == byFields.id)
            {
                return By.Id(element);
            }
            else if (by == byFields.name)
            {
                return By.Name(element);
            }
            else if (by == byFields.css)
            {
                return By.CssSelector(element);
            }
            else if (by == byFields.tag)
            {
                return By.TagName(element);
            }
            else if (by == byFields.className)
            {
                return By.ClassName(element);
            }
            else if (by == byFields.linkText)
            {
                return By.LinkText(element);
            }
            else if (by == byFields.partialLinkText)
            {
                return By.PartialLinkText(element);
            }
            else if (by == byFields.xpath)
            {
                return By.XPath(element);
            }
            else
            {
                return By.Id("");
            }
        }

        public bool isElementPresent(byFields by, string element)
        {
            log("isElementPresent(" + by + "," + element + ")");
            try
            {
                driver.FindElement(getBy(by, element));
                log("isElementPresent return: true");
                return true;
            }
            catch (Exception)
            {
                log("isElementPresent return: false");
                return false;
            }
        }

        public void clickImage(string imagePath)
        {
            log("clickImage: " + imagePath);
            switchToContext(availableContexts.VISUAL);

            string command = "mobile:button-image:click";
            Dictionary<string, object> parameters = new Dictionary<string, Object>();
            parameters.Add("label", imagePath);
            parameters.Add("timeout", "20");
            parameters.Add("threshold", "100");
            parameters.Add("match", "bounded");
            parameters.Add("imageBounds.needleBound", "60");
            driver.ExecuteScript(command, parameters);

            takeScreenStep("clickImage");
            restoreContext();
        }

        public void pressEnter()
        {
            log("pressEnter");
            if (os == "Android")
            {
                sendKeyEvent("66");
            }
            else if (os == "iOS")
            {
                clickImage("PUBLIC:Jeremy\\iOS\\KeyboardSearch.png");
            }
            else
            {
                driver.Keyboard.SendKeys(Keys.Enter);
            }

            takeScreenStep("pressEnter");
        }

        public string alertControl(bool acceptAlert)
        {
            log("alertControl(" + acceptAlert + ")");
            try
            {
                IAlert alert = driver.SwitchTo().Alert();
                string alertText = alert.Text;
                if (acceptAlert)
                {
                    alert.Accept();
                }
                else
                {
                    alert.Dismiss();
                }
                return alertText;
            }
            finally
            {
                acceptAlert = true;
            }
        }

        public void scrollToElement(byFields by, string element)
        {
            log("scrollToElement");
            IWebElement elem = driver.FindElement(getBy(by, element));
            ((IJavaScriptExecutor)driver).ExecuteScript("arguments[0].scrollIntoView(true);", element);
        }

        public void cleanAllDirs()
        {
            log("cleanAllDirs");
            cleanDir(snapshotDirectory);
            cleanDir(reportDirectory);
            cleanDir(attachmentDirectory);
        }

        public void cleanDir(string location)
        {
            log("cleanDir:" + location);
            if (Directory.Exists(location))
            {
                DirectoryInfo dir = new DirectoryInfo(location);
                foreach (FileInfo item in dir.GetFiles())
                {
                    item.Delete();
                }
                Directory.Delete(location);
            }

        }

        public void takeScreenShot(string name)
        {
            if (!Directory.Exists(snapshotDirectory))
            {
                Directory.CreateDirectory(snapshotDirectory);
            }

            log(snapshotDirectory);

            String now = getCurrentTime();
            Screenshot screen = driver.GetScreenshot();
            String newName = snapshotDirectory + now + "_" + testName + "_" + name + "_step" + step + ".png";
            screen.SaveAsFile(newName, System.Drawing.Imaging.ImageFormat.Png);
            log("Screenshot: " + newName);            
            
        }

        public void changeUrl(string url)
        {
            log("changeUrl(" + url + ")");
            try
            {
                driver.Url = url;
            }
            catch (Exception ex)
            {
                log("changeurl error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("changeURL");
        }

        public void goToUrl(string url)
        {
            log("changeUrl(" + url + ")");
            try
            {
                driver.Navigate().GoToUrl(url);
            }
            catch (Exception ex)
            {
                log("changeurl error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("gotoUrl");
        }


        public string getUrl()
        {
            log("getUrl()");
            string url = "can't find url";
            try
            {
                url = driver.Url.ToString();
            }
            catch (Exception ex)
            {
                log("geturl error" + ex.ToString());
                methodError(ex);
            }
            log("getUrl return: " + url);
            return url;
        }

        public void waitForTitle(int seconds, string title)
        {
            log("waitForTitle(" + seconds + ", " + title + ")");

            try
            {
                driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(0));
                WebDriverWait wait = new WebDriverWait(driver, TimeSpan.FromSeconds(seconds));
                wait.Until((d) => { return d.Title.ToLower().StartsWith(title); });
                driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(implicitWaitTime));
            }
            catch (Exception ex)
            {
                log("waitfortitle error" + ex.ToString());
                methodError(ex);
            }
        }

        public void waitForElement(int seconds, object rule)
        {
            log("waitForElement(" + seconds + ", " + rule + ")");
            
            try
            {
                driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(0));
                WebDriverWait wait = new WebDriverWait(driver, TimeSpan.FromSeconds(seconds));
                wait.Until((d) => { return rule; });
                driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(implicitWaitTime));
            }
            catch (Exception ex)
            {
                log("waitforelement error" + ex.ToString());
                methodError(ex);
            }
        }

        public void clearText(byFields by, string element, int timeOut)
        {
            log("clearText(" + by + ", " + element + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                driver.FindElement(getBy(by, element)).Clear();
            }
            catch (Exception ex)
            {
                log("cleartext error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("clearText");
        }

        public void sendText(byFields by, string element, string data, int timeOut, bool clear = false)
        {
            log("sendText(" + by + ", " + element + ", " + data + ", " + clear + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                if (clear)
                {
                    clearText(by, element, timeOut);
                }
                driver.FindElement(getBy(by, element)).Click();
                driver.FindElement(getBy(by, element)).SendKeys(data);
            }
            catch (Exception ex)
            {
                log("sendtext error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("sendText");
        }



        public string getText(byFields by, string element, int timeOut)
        {
            log("getText(" + by + ", " + element + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                driver.FindElement(getBy(by, element)).Text.ToString();
            }
            catch (Exception ex)
            {
                log("gettext error" + ex.ToString());
                methodError(ex);
            }
            return driver.FindElement(getBy(by, element)).Text;
        }

        public string getValue(byFields by, string element, int timeOut)
        {
            log("getValue(" + by + ", " + element + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                driver.FindElement(getBy(by, element)).GetAttribute("value");
            }
            catch (Exception ex)
            {
                log("getvalue error" + ex.ToString());
                methodError(ex);
            }
            return driver.FindElement(getBy(by, element)).GetAttribute("value");
        }

        public void waitOnPageLoad()
        {
            if (testType == "Web")
            {
                if (type == "Mobile")
                {
                    if (driver.Context.Contains("WEB"))
                    {
                        driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(0));
                        IWait<IWebDriver> wait = new OpenQA.Selenium.Support.UI.WebDriverWait(driver, TimeSpan.FromSeconds(30.00));
                        wait.Until(driver1 => ((IJavaScriptExecutor)driver).ExecuteScript("return document.readyState").Equals("complete"));
                        driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(implicitWaitTime));
                    }
                }
                else
                {
                    driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(0));
                    IWait<IWebDriver> wait = new OpenQA.Selenium.Support.UI.WebDriverWait(driver, TimeSpan.FromSeconds(30.00));
                    wait.Until(driver1 => ((IJavaScriptExecutor)driver).ExecuteScript("return document.readyState").Equals("complete"));
                    driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(implicitWaitTime));
                }
            }
        }

        public void clickElement(byFields by, string element, int timeOut)
        {
            log("clickElement(" + by + ", " + element + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                driver.FindElement(getBy(by, element)).Click();
                waitOnPageLoad();
            }
            catch (Exception ex)
            {
                log("clickelement error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("clickElement");
        }

        public void submitElement(byFields by, string element, int timeOut)
        {
            log("submitElement(" + by + ", " + element + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                driver.FindElement(getBy(by, element)).Submit();
            }
            catch (Exception ex)
            {
                log("submitelement error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("submitElement");
        }

        public void setDropDownText(byFields by, string element, string text, int timeOut)
        {
            log("setDropDownText(" + by + ", " + element + "," + text + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                new SelectElement(driver.FindElement(getBy(by, element))).SelectByText(text);
            }
            catch (Exception ex)
            {
                log("setdropdowntext error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("setDropDownText");
        }

        public void setDropDownValue(byFields by, string element, string text, int timeOut)
        {
            log("setDropDownValue(" + by + ", " + element + "," + text + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                new SelectElement(driver.FindElement(getBy(by, element))).SelectByValue(text);
            }
            catch (Exception ex)
            {
                log("setdropdownvalue error" + ex.ToString());
                methodError(ex);
            }
            takeScreenStep("setDrop");
        }

        public string getWindowSize()
        {
            log("getWindowSize()");

            try
            {
                driver.Manage().Window.Size.ToString();
            }
            catch (Exception ex)
            {
                log("getWindowSize error" + ex.ToString());
                methodError(ex);
            }

            return driver.Manage().Window.Size.ToString();
        }

        public string getWindowPosition()
        {
            log("getWindowPosition()");

            try
            {
                driver.Manage().Window.Position.ToString();
            }
            catch (Exception ex)
            {
                log("getWindowPosition error" + ex.ToString());
                methodError(ex);
            }

            return driver.Manage().Window.Position.ToString();
        }

        public void maximizeWindow()
        {
            log("maximizeWindow()");

            try
            {
                driver.Manage().Window.Maximize();
            }
            catch (Exception ex)
            {
                log("maximizeWindow error" + ex.ToString());
                methodError(ex);
            }
        }

        public void setWindowPosition(int x, int y)
        {
            log("setWindowPosition(" + x + ", " + y + " )");

            try
            {
                driver.Manage().Window.Position = new Point(x, y);
            }
            catch (Exception ex)
            {
                log("setWindowPosition error" + ex.ToString());
                methodError(ex);
            }
        }

        public void setWindowSize(int x, int y)
        {
            log("setWindowSize(" + x + ", " + y + " )");

            try
            {
                driver.Manage().Window.Size = new Size(x, y);
            }
            catch (Exception ex)
            {
                log("setWindowSize error" + ex.ToString());
                methodError(ex);
            }
        }

        public IWebElement getElement(byFields by, string element, int timeOut)
        {
            log("getElement(" + by + ", " + element + ")");

            try
            {
                waitForElement(timeOut, getBy(by, element));
                driver.FindElement(getBy(by, element));
            }
            catch (Exception ex)
            {
                log("getElement error" + ex.ToString());
                methodError(ex);
            }

            return driver.FindElement(getBy(by, element));
        }
    }
}
