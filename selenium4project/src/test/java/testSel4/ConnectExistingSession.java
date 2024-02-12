//C:\Program Files (x86)\Google\Chrome\Application\chrome.exe --remote-debugging-port=9222 --user-data-dir="C:\Selenium Webdriver\profiles"
package testSel4;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.v120.network.model.RequestId;
import org.openqa.selenium.devtools.v120.network.model.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.By;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ConnectExistingSession{
	static int i,j=0;

	public static void main(String[] args){
		ChromeRemote();
		RealisasiMethod("01-01-2024","03-02-2024");
	}

	static void loopDo() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ChromeRemote();
		if (j==1){
			RealisasiMethod("01-01-2023","16-02-2023");
		}
		if(j==2) {
			RealisasiMethod("01-01-2023","17-02-2023");
		}
		if(j==3) {
			RealisasiMethod("01-01-2023","18-02-2023");
		}
		if(j==4) {
			RealisasiMethod("01-01-2023","19-02-2023");
		}
		if(j==5) {
			RealisasiMethod("01-01-2023","20-02-2023");
		}
		if(j==6) {
			RealisasiMethod("01-01-2023","21-02-2023");
		}
	}
	static void ChromeRemote() {	
		ProcessBuilder processBuilder = new ProcessBuilder();
		try {
			processBuilder.command("C:\\Selenium Webdriver\\Chrome-Remote.bat").start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void RealisasiMethod(String Param1,String Param2){
		
		ChromeOptions Options=new ChromeOptions();
		Options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		Options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		ChromeDriver driver = new ChromeDriver(Options);
		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		ArrayList<String> tabs=new ArrayList<>(driver.getWindowHandles());
 		driver.switchTo().window(tabs.get(0));
 		//driver.close();

		
		//Devtool tutorial @ https://www.youtube.com/watch?v=HtBhLdZ19iQ&t=528s
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		System.out.println(devTools.getCdpSession());
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		
		final RequestId[] requestId = new RequestId[1];
		devTools.addListener(Network.responseReceived(),responseConsumer ->{
			Response response = responseConsumer.getResponse();
			requestId[0]=responseConsumer.getRequestId();
			//System.out.println(response.getStatus()+" "+response.getUrl());
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
						  Thread.sleep(7000);
						} catch (InterruptedException e) {
						  Thread.currentThread().interrupt();
						}
	
				    try {
					       final ObjectMapper mapper = new ObjectMapper();
					       mapper.readTree(devTools.send(Network.getResponseBody(requestId[0])).getBody());
							FileWriter myWriter = new FileWriter("C:\\Selenium Webdriver\\Download\\"+Param2+".json");
							myWriter.write(devTools.send(Network.getResponseBody(requestId[0])).getBody());
						    myWriter.close();
						    System.out.println("Json Successfully wrote to the "+Param2+".json\".");
						    //driver.get(Param2);
						    System.out.println(j);
						    j=j+1;
						    if(j==7) {
								devTools.close();
							    driver.close();
								System.exit(0);		
							}else {
								devTools.close();
							    driver.close();
								//loopDo();
								//System.exit(0);		
							}

					    
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
			
		JavascriptExecutor js=(JavascriptExecutor)driver;

		WebElement l3 =driver.findElement(By.name("tgl_awal"));
		js.executeScript("arguments[0].value='"+Param1+"'", l3);

		WebElement l4 =driver.findElement(By.name("tgl_akhir"));
		js.executeScript("arguments[0].value='"+Param2+"'", l4);

		List<WebElement> elements1 = driver.findElements(By.cssSelector("button[data-action='submit']"));
        //System.out.println("Number of elements:" +elements1.size());
        elements1.get(0).click();
	}
}