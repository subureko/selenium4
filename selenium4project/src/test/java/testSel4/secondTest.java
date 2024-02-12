package testSel4;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
//import org.openqa.selenium.devtools.v118.network.Network;
import org.openqa.selenium.devtools.v120.network.Network;
//import org.openqa.selenium.devtools.v118.network.model.Response;
import org.openqa.selenium.devtools.v120.network.model.Response;
//import org.openqa.selenium.devtools.v118.network.model.Request;
import org.openqa.selenium.devtools.v120.network.model.Request;
//import org.openqa.selenium.devtools.v118.network.model.RequestId;
import org.openqa.selenium.devtools.v120.network.model.RequestId;

//import org.testng.annotations.Test;

public class secondTest {
	
	public static void main(String[] args){
	
		ChromeOptions Options=new ChromeOptions();
		Options.setPageLoadStrategy(PageLoadStrategy.EAGER);

		ChromeDriver driver = new ChromeDriver(Options);

		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		
		//Devtool tutorial @ https://www.youtube.com/watch?v=HtBhLdZ19iQ&t=528s
		
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.of(100000000)));
		
		devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
			Request request = requestConsumer.getRequest();
			System.out.println(request.getUrl());
			}
		);

		final RequestId[] requestId = new RequestId[1];
		devTools.addListener(Network.responseReceived(),responseConsumer ->{
			Response response = responseConsumer.getResponse();
			requestId[0]=responseConsumer.getRequestId();
			System.out.println(response.getStatus()+" "+response.getUrl());
			if ( response.getUrl().contains("data/dataDIPA/DataRealisasi")) {

				try {
					  Thread.sleep(500);
					} catch (InterruptedException e) {
					  Thread.currentThread().interrupt();
					}
				String responseBody = devTools.send(Network.getResponseBody(requestId[0])).getBody();
				
				try {
					  FileWriter myWriter = new FileWriter("C:\\Selenium Webdriver\\filename.json");
				      myWriter.write(responseBody);
				      myWriter.close();
				      System.out.println("Successfully wrote to the file.");
				    } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
	
				}
			}
		);
		
		driver.get("https://spanint.kemenkeu.go.id/spanint/latest/app/#span/dataDIPA/DataRealisasi");
	
		WebElement l1 =driver.findElement(By.id("digitui-app-username-field"));
	      l1.sendKeys("K11");

		WebElement l2 =driver.findElement(By.id("digitui-app-password-field"));
	      l2.sendKeys("13161473");

		//Memasukan kode Captca
		try {
			  Thread.sleep(7000);
			} catch (InterruptedException e) {
			  Thread.currentThread().interrupt();
			}
	    l2.sendKeys(Keys.ENTER);

		JavascriptExecutor js=(JavascriptExecutor)driver;
		WebElement l3 =driver.findElement(By.name("tgl_awal"));
		js.executeScript("arguments[0].value='01-01-2024'", l3);

		WebElement l4 =driver.findElement(By.name("tgl_akhir"));
		js.executeScript("arguments[0].value='21-01-2024'", l4);

		WebElement l5 =driver.findElement(By.cssSelector("button[data-action='submit']"));
		l5.sendKeys(Keys.ENTER);
	}
}
