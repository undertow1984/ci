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

namespace StateWeb
{
    public partial class Library
    {


        public void methodError(Exception ex)
        {

            // Get the line number from the stack frame
            var st = new StackTrace(ex, true);

            // Get the line number from the stack frame

            string erroroutput = "Error during execution " + System.Environment.NewLine;


            var frame = st.GetFrame(st.FrameCount - 1);
            erroroutput = erroroutput + System.Environment.NewLine + " " + frame.GetFileLineNumber() + " " + frame.GetMethod() + " " + frame.ToString() + System.Environment.NewLine + System.Environment.NewLine + ex.ToString();

            takeScreenShot("failure");

            close();


           Assert.Fail(erroroutput);

        }

        public void log(string msg)
        {
            Trace.WriteLine(testName + "_step" + step + "_" + msg);
            Console.WriteLine(testName + "_step" + step + "_" + msg);
        }

        

        // Example: downloadReport("pdf", "C:\\test\\report", ".pdf");
        public void downloadReport(string type, string fileName, string suffix)
        {
            String now = getCurrentTime();
            fileName = reportDirectory + now + "_" + fileName + suffix;

            try
            {
                string command = "mobile:report:download";
                Dictionary<string, object> parameters = new Dictionary<string, Object>();
                parameters.Add("type", type);
                string report = (String)driver.ExecuteScript(command, parameters);
                byte[] reportBytes = Convert.FromBase64String(report);
                File.WriteAllBytes(fileName, reportBytes);
            }
            catch (Exception e)
            {
                Console.WriteLine("ERROR while downloading report");
                Console.WriteLine(e.ToString());
            }

        }

        // Example: downloadAttachment("video", "C:\\test\\video", ".flv");
        public void downloadAttachment(string type, string fileName, string suffix)
        {
            String now = getCurrentTime();
            fileName = attachmentDirectory + now + "_" + fileName + suffix;

            try
            {
                string command = "mobile:report:attachment";
                Dictionary<string, object> parameters = new Dictionary<string, object>();
                parameters.Add("type", type);
                string attachment = (String)driver.ExecuteScript(command, parameters);
                byte[] attachmentBytes = Convert.FromBase64String(attachment);
                File.WriteAllBytes(fileName, attachmentBytes);
            }
            catch (Exception e)
            {
                Console.WriteLine("ERROR while downloading video");
                Console.WriteLine(e.ToString());
            }
        }
    }
}
