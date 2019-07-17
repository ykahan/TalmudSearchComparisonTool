package management;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import searchResultsMachinery.Hit;

public class ExplicitlyWait {
	WebDriver driver;

	public ExplicitlyWait(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement awaitElement(WebElement element, int timeout) {
		WebElement newElement = null;
		System.out.println("MAXWAIT: {" + timeout + "}");
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			newElement = wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			System.out.println("START GOT EXCEPTION");
			System.out.println("Error message: " + e.getMessage());
			System.out.println("END GOT EXCEPTION");
		}
		return element;
	}

	public List<WebElement> awaitList(List<WebElement> list, int timeout) throws InterruptedException {
		int size = 0;  // initial size of list prior to loading
		int waitPerCheck = 500; // time in milliseconds to wait between checks
		int totalTimeToWait = timeout * 1000; // timeout converted to milliseconds
		int cycles = totalTimeToWait / waitPerCheck;
		for(int i = 0; i < cycles; i++) {
			size = list.size();
			if(size > 0) {
				return list;
			}
			Thread.sleep(waitPerCheck);			
		}
		return null;
	}
}
