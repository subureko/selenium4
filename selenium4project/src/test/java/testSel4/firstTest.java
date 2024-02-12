package testSel4;

import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class firstTest {

	public static void main(String[] args) {
		
		ChromeOptions Options=new ChromeOptions();
		Options.setPageLoadStrategy(PageLoadStrategy.EAGER);

		WebDriver driver = new ChromeDriver(Options);
//		ChromeDriver driver = new ChromeDriver(Options);
	    driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));


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
		js.executeScript("arguments[0].value='19-01-2024'", l4);

		WebElement l5 =driver.findElement(By.cssSelector("button[data-action='submit']"));
		l5.sendKeys(Keys.ENTER);
		
	}
}
