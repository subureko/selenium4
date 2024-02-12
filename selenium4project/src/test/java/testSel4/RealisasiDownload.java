package testSel4;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.v120.network.model.RequestId;
import org.openqa.selenium.devtools.v120.network.model.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RealisasiDownload {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChromeRemote();
		//RealisasiMethod("01-01-2024","04-02-2024");
		RealisasiMethod(args[0],args[1]);
	}
	static int i,j=0;
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
		
		//System.setProperty("webdriver.chrome.driver", "C:\\Selenium Webdriver\\ChromeDriver\\chromedriver_win32");
		ChromeOptions Options=new ChromeOptions();
		Options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		Options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		//Options.addArguments("headless"); //nggak jalan karena meremote chrome yang sudah jalan dulu di atas
		ChromeDriver driver = new ChromeDriver(Options);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		ArrayList<String> tabs=new ArrayList<>(driver.getWindowHandles());
 		driver.switchTo().window(tabs.get(0));
 		driver.manage().window().maximize();
		
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
			if ( response.getUrl().contains("data/dataDIPA/DataRealisasi"))  {
				//System.out.println(response.getStatus()+" "+response.getUrl());
				System.out.println("trying number="+i);
				System.out.println(responseConsumer.getType().toJson());
				//langsung di write ke file jadi tidak error
				//String responseBody = devTools.send(Network.getResponseBody(requestId[0])).getBody();
				if (i>=1) { //yang ke 0 adalah XHR untuk filter saja
				    try {
					  		Thread.sleep(7000);
				    		final ObjectMapper mapper = new ObjectMapper();
				    		mapper.readTree(devTools.send(Network.getResponseBody(requestId[0])).getBody());
							FileWriter myWriter = new FileWriter("C:\\Selenium Webdriver\\Download\\"+Param2+".json");
							myWriter.write(devTools.send(Network.getResponseBody(requestId[0])).getBody());
						    myWriter.close();
						    System.out.println("Json Successfully wrote to the "+Param2+".json\".");
					        //driver.get(Param2);
						    //System.out.println(j);
							devTools.close();
						    driver.close();
					 		driver.switchTo().window(tabs.get(1));
						    driver.close();
							driver.quit();
							System.exit(0);		
				    } catch (IOException e) {
						    System.out.println("Json Error.");
					} 	catch (InterruptedException e) {
							Thread.currentThread().interrupt();
					}
					
				}
				i=i+1;
			}//contain datarealisasi
		}//listener
		);
		try {

			driver.get("https://spanint.kemenkeu.go.id/spanint/latest/app/#span/dataDIPA/DataRealisasi");
			System.out.println("1. Akses Halaman Utama");
	//		JavascriptExecutor js=(JavascriptExecutor)driver;
	
			WebElement l3 =driver.findElement(By.name("tgl_awal"));
			js.executeScript("arguments[0].value='"+Param1+"'", l3);
			System.out.println("1.1 Masukan tanggal mulai");
	
			WebElement l4 =driver.findElement(By.name("tgl_akhir"));
			js.executeScript("arguments[0].value='"+Param1+"'", l4);
			System.out.println("1.2 Masukan tanggal selesai");
	
			List<WebElement> elements1 = driver.findElements(By.cssSelector("button[data-action='submit']"));
	        //System.out.println("Number of elements:" +elements1.size());
	        elements1.get(0).click();
			System.out.println("2. Klik Submit Halaman Utama");
	        
			List<WebElement> elements = driver.findElements(By.className("digitui-toolbar"));
	        //System.out.println("Number of elements:" +elements.size());
			for(int i=0; i<elements.size(); i++){
				//System.out.println("OnClick:" + elements.get(i).getAttribute("onclick"));
			}
		  	Thread.sleep(2000);
			System.out.println("2.1 Klik Tombol Filter");
			elements.get(2).click();
	
			WebElement l5 =driver.findElement(By.name("tgl_akhir"));
			js.executeScript("arguments[0].value='"+Param2+"'", l5);
			System.out.println("2.3 Masukan tanggal Akhir");
	
			List<WebElement> elements2 = driver.findElements(By.cssSelector("button[data-action='submit']"));
	        //System.out.println("Number of elements:" +elements1.size());
			//for(int i=0; i<elements1.size(); i++){
			//	System.out.println("OnClick:" + elements1.get(i).getAttribute("data-action"));
			//}
		  	Thread.sleep(2000);
			System.out.println("3. Klik Submit Halaman Akhir");
			elements2.get(1).click();
		} 	catch (Exception e) {
				System.out.println("Kesalahan pada akses web Element!!!");
				System.out.println(e);
				devTools.close();
			    driver.close();
		 		driver.switchTo().window(tabs.get(1));
			    driver.close();
				driver.quit();
				System.exit(0);		
		} 		
	}
}
