package pages;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.api.IMobileDevice;

import java.io.UnsupportedEncodingException;

import pages.*;

public class library {
	
	private RemoteWebDriver driver;
	private String targetEnvironment="";
	private int step=0;
	private Boolean device=false;
			
	public library(RemoteWebDriver driver, String target, int Step, Boolean device)
	{
		targetEnvironment = target;
		step=Step;
		this.device=device;
		this.driver = driver;
	}
	
	public library(RemoteWebDriver driver)
	{		
		this.driver = driver;
	}
	
	//switch driver method
	//switches dom between native and web
	// "WEBVIEW", "NATIVE_APP" or "VISUAL"
	public RemoteWebDriver switchToContext(String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        Map<String,String> params = new HashMap<>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
        return driver;
    }
	
	//get current driver method
	public String getCurrentContextHandle() {          
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
        return context;
    }
	
	//get available contexts
	public List<String> getContextHandles() {          
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
        return contexts;
    }
	
	
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	
	
	public void errorCleanup()
	{
		  driver.close();
		  driver.quit();
	}
	
	 public By getBy(String by, String element)
     {        
         if (by == "id")
         {
             return By.id(element);
         }
         else if (by == "name")
         {
             return By.name(element);
         }
         else if (by == "css")
         {
             return By.cssSelector(element);
         }
         else if (by == "tag")
         {
             return By.tagName(element);
         }
         else if (by == "class")
         {
             return By.className(element);
         }
         else if (by == "linkText")
         {
             return By.linkText(element);
         }
         else if (by == "partialLinkText")
         {
             return By.partialLinkText(element);
         }
         else if (by == "xpath")
         {
             return By.xpath(element);                
         }
         else
         {
        	 return By.id("");
         }                  
     }
	
	public void closeApplication(String application)
	{	
		String command = "mobile:application.close";
		Map<String, Object> params = new HashMap<>();
		params.put("name", application);		
		driver.executeScript(command, params);
	}

	
	public void takeScreen(String text)
	{
		if(!device)
		{
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//taking screenshot	
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
			String destDir = "./test-output/html/screenshots/";
			new File(destDir).mkdirs();
			String destFile = targetEnvironment+"_"+step+"_"+dateFormat.format(new Date()) + ".png";

			try {
				FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
			//Display screenshot to ReportNG
		    final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";
		    System.setProperty(ESCAPE_PROPERTY, "false");
		    
			String userDirector = "/test-output/html/screenshots/"; 
			System.out.println(userDirector);
			Reporter.log("<u><b>" + text + "</b></u><br><a href=\""+ userDirector + destFile +"\"><img src=\"file:///" + userDirector 
	                     + destFile + "\" alt=\"\""+ "height='100' width='100'/> "+"<br />"); 
	        //Reporter.setCurrentTestResult(null);
		}
	}
	
	public void log(String text)
	{
		Reporter.log("<b>" + text + "</b>");	
	}
	
	public void downloadReportDisplay(RemoteWebDriver driver) throws IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
		String destDir = "./test-output/html/screenshots/";
		new File(destDir).mkdirs();
		String destFile = dateFormat.format(new Date());

		
		downloadReport(driver, "pdf", destDir + "/" + destFile);
		//Display screenshot to ReportNG
	    final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";
	    System.setProperty(ESCAPE_PROPERTY, "false");
	    
		String userDirector = "/test-output/html/screenshots/"; 
		System.out.println(userDirector);
		String destFileNew = destFile + ".pdf";
		Reporter.log("<a href=\""+ userDirector + destFileNew +"\">Perfecto Report</a><br />");
	}
	
	 private void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
			try { 
				String command = "mobile:report:download"; 
				Map<String, Object> params = new HashMap<>(); 
				params.put("type", type); 
				String report = (String)driver.executeScript(command, params); 
				File reportFile = new File(fileName + "." + type); 
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
				byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
				output.write(reportBytes); output.close(); 
			} catch (Exception ex) { 
				System.out.println("Got exception " + ex); }
			}
			
	  public String getConfigurationProperty(String envKey,
				String sysKey, String defValue) {
			String retValue = defValue;
			String envValue = System.getenv(envKey);
			String sysValue = System.getProperty(sysKey);
			// system property prevails over environment variable
			if (sysValue != null) {
				retValue = sysValue;
			} else if (envValue != null) {
				retValue = envValue;
			}
			return retValue;
	}	    
	  
		public void goToPage(String url, String title)
		{
			driver.get(url);	
			waitForTitle(10, title);
			takeScreen("Step_" + step + "_goToPage" + "_" + url + "_" + title);
			step++;
			
		}	

      public String getUrl()
      {
    	  String url = "can't find url";
          try
          {
              url = driver.getCurrentUrl().toString();
          }
          catch (Exception ex)
          {
              
              
          }
          
          return url;
      }

      public void waitForTitle(int seconds, String title)
      {
        
          try
          {
        	  WebDriverWait wait = new WebDriverWait(driver, seconds);
        	  wait.until(ExpectedConditions.titleContains(title));
          }
          catch (Exception ex)
          {
            
          }
      }

      public void waitForElement(int seconds, String by, String element)
      {
          try
          {
        	  WebDriverWait wait = new WebDriverWait(driver, seconds);
              wait.until(ExpectedConditions.elementToBeClickable(getBy(by, element)));
          }
          catch (Exception ex)
          {
          }
      }

      public void clearText(String by, String element, int timeOut)
      {
    	  
          try
          {
        	  waitForElement(timeOut, by, element);
              driver.findElement(getBy(by, element)).clear();
              takeScreen("Step_" + step + "_clearText" + "_" + by + "_" + element);
              step++;
          }
          catch (Exception ex)
          {
             
          }

      }

      public void setText(String by, String element, String data, Boolean clear, int timeOut)
      {
          try
          {
        	  waitForElement(timeOut, by, element);
              if (clear)
              {
                  clearText(by, element, timeOut);
              }
              driver.findElement(getBy(by, element)).sendKeys(data);
              waitForElement(timeOut, by, element);
              takeScreen("Step_" + step + "_setText_" + by + "_" + element + "_" + data + "_" + clear );
              step++;
          }
          catch (Exception ex)
          {
         
          }

      }

      public String getText(String by, String element, int timeOut)
      {
     
          try
          {
        	  waitForElement(timeOut, by, element);
              driver.findElement(getBy(by, element)).getText().toString();
          }
          catch (Exception ex)
          {
           
          }
          return driver.findElement(getBy(by, element)).getText().toString();
      }

      public String getValue(String by, String element, int timeOut)
      {
  
          try
          {
        	  waitForElement(timeOut, by, element);
              driver.findElement(getBy(by, element)).getAttribute("value");
          }
          catch (Exception ex)
          {
            
          }
          return driver.findElement(getBy(by, element)).getAttribute("value");
      }

      public void clickElement(String by, String element, int timeOut)
      {
    	 
        	  waitForElement(timeOut, by, element);
              driver.findElement(getBy(by, element)).click();
              waitForElement(timeOut, by, element);
              takeScreen("Step_" + step + "_clickElement_" + by + "_" + element);
              step++;
          
      }

      public void submitElement(String by, String element, int timeOut)
      { 
          try
          {              
        	  waitForElement(timeOut, by, element);
        	  driver.findElement(getBy(by, element)).submit();
        	  waitForElement(timeOut, by, element);
        	  takeScreen("Step_" + step + "_submitElement_" + by + "_" + element);
        	  step++;
          }
          catch (Exception ex)
          {
       
          }

      }

      public void setDropDownText(String by, String element, String text, int timeOut)
      {
         
          try
          {
        	  waitForElement(timeOut, by, element);
              new Select(driver.findElement(getBy(by, element))).selectByVisibleText(text);
              waitForElement(timeOut, by, element);
              takeScreen("Step_" + step + "_setDropDownText_"+ by + "_" + element + "_" + text);
              step++;
              
          }
          catch (Exception ex)
          {
     
          }

      }

      public void setDropDownValue(String by, String element, String text, int timeOut)
      {
  
          try
          {
        	  waitForElement(timeOut, by, element);
              new Select(driver.findElement(getBy(by, element))).selectByValue(text);
              waitForElement(timeOut, by, element);
              takeScreen("Step_" + step + "_setDropDownValue_"+by + "_" + element + "_" + text);
              step++;
          }
          catch (Exception ex)
          {
        
          }
      }

      public String getWindowSize()
      {
       
          try
          {
              driver.manage().window().getSize().toString();
          }
          catch (Exception ex)
          {
 
          }

          return driver.manage().window().getSize().toString();
      }

      public String getWindowPosition()
      {

          try
          {
        	  driver.manage().window().getPosition();
          }
          catch (Exception ex)
          {

          }

          return driver.manage().window().getPosition().toString();
      }

      public void maximizeWindow()
      {

          try
          {
              driver.manage().window().maximize();
              takeScreen("");
              step++;
          }
          catch (Exception ex)
          {
   
          }
      }

      public void setWindowPosition(int x, int y)
      {
       
          try
          {
              driver.manage().window().setPosition(new Point(x,y));
              takeScreen("");step++;
          }
          catch (Exception ex)
          {

          }
      }

      public void setWindowSize(int x, int y)
      {

          try
          {
              driver.manage().window().setSize(new Dimension(x, y));
              takeScreen("");
              step++;
          }
          catch (Exception ex)
          {
       
          }
      }

      public WebElement getElement(String by, String element)
      {
 
          try
          {
              driver.findElement(getBy(by, element));
          }
          catch (Exception ex)
          {

          }

          return driver.findElement(getBy(by, element));
      }
      
      public Boolean elementExists(String by, String element)
      {
 
          try
          {
              driver.findElement(getBy(by, element));
              return true;
          }
          catch (Exception ex)
          {
        	  return false;
          }
          
      }
      
}
