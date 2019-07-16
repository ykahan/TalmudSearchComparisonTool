package management;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
}
