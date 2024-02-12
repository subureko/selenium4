package testSel4;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.v120.network.model.RequestId;
import org.openqa.selenium.devtools.v120.network.model.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class fourth {
	static int i=0;
	public static void main(String[] args) {
		RealisasiMethod("01-01-2024","26-01-2024");
	}

	static void RealisasiMethod(String Param1,String Param2){
		
		ChromeOptions Options=new ChromeOptions();
		Options.setPageLoadStrategy(PageLoadStrategy.EAGER);

		ChromeDriver driver = new ChromeDriver(Options);
		
	
		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		
		//Devtool tutorial @ https://www.youtube.com/watch?v=HtBhLdZ19iQ&t=528s
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		System.out.println(devTools.getCdpSession());
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		
		final RequestId[] requestId = new RequestId[1];
		devTools.addListener(Network.responseReceived(),responseConsumer ->{
			Response response = responseConsumer.getResponse();
			requestId[0]=responseConsumer.getRequestId();
			if (response.getStatus()==200) {
			}else {
				System.out.println(response.getStatus()+" "+response.getUrl());
			}
			if ( response.getUrl().contains("data/dataDIPA/DataRealisasi")) {
				//System.out.println(response.getStatus()+" "+response.getUrl());
				System.out.println("trying number="+i);
				System.out.println(responseConsumer.getType().toJson());
				//langsung di write ke file jadi tidak error
				//String responseBody = devTools.send(Network.getResponseBody(requestId[0])).getBody();
				if (i>=1) {
					try {
						  Thread.sleep(10000);
						} catch (InterruptedException e) {
						  Thread.currentThread().interrupt();
						}

				    try {
					       final ObjectMapper mapper = new ObjectMapper();
					       mapper.readTree(devTools.send(Network.getResponseBody(requestId[0])).getBody());
							FileWriter myWriter = new FileWriter("C:\\Selenium Webdriver\\"+Param2+".json");
							myWriter.write(devTools.send(Network.getResponseBody(requestId[0])).getBody());
						    myWriter.close();
						    System.out.println("Json Successfully wrote to the "+Param2+".json\".");
						    devTools.close();
						    driver.close();
						    
				    } catch (IOException e) {
						    System.out.println("Json Error.");
							List<WebElement> elements = driver.findElements(By.className("digitui-toolbar"));
					        //System.out.println("Number of elements:" +elements.size());
							for(int i=0; i<elements.size(); i++){
								//System.out.println("OnClick:" + elements.get(i).getAttribute("onclick"));
							}
							elements.get(2).click();

							List<WebElement> elements1 = driver.findElements(By.cssSelector("button[data-action='submit']"));
					        //System.out.println("Number of elements:" +elements1.size());
							//for(int i=0; i<elements1.size(); i++){
							//	System.out.println("OnClick:" + elements1.get(i).getAttribute("data-action"));
							//}
							elements1.get(1).click();
					    }
					
					}
				i=i+1;
				}//contain datarealisasi
			}//listener
		);
		
		driver.get("https://spanint.kemenkeu.go.id/spanint/latest/app/#span/dataDIPA/DataRealisasi");
		//Baru login
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
		
		js.executeScript("arguments[0].value='"+Param1+"'", l3);

		WebElement l4 =driver.findElement(By.name("tgl_akhir"));
		js.executeScript("arguments[0].value='"+Param2+"'", l4);

		List<WebElement> elements1 = driver.findElements(By.cssSelector("button[data-action='submit']"));
        //System.out.println("Number of elements:" +elements1.size());
		elements1.get(1).click();
	}

}
