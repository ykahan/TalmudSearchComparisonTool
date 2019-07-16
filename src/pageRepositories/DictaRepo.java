package pageRepositories;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import management.ExplicitlyWait;
import java.util.ArrayList;
import java.util.List;

import management.ExplicitlyWait;

public class DictaRepo {
	WebDriver driver;
	final int MAXWAIT;
	ExplicitlyWait ew;

	public DictaRepo(WebDriver driver, int MAXWAIT) {
		this.driver = driver;
		this.ew = new ExplicitlyWait(driver);
		this.MAXWAIT = MAXWAIT;
	}

	@FindBy(xpath = "//input[@id='search_box']")
	WebElement searchBox;

	@FindBy(xpath = "//button[@id='search_button']")
	WebElement searchBoxButton;

	public void sendPhrase(String targetPhrase) {
		WebElement box = ew.awaitElement(searchBox, MAXWAIT);
		WebElement button = ew.awaitElement(searchBoxButton, MAXWAIT);
		if (box != null) {
			System.out.println("box found");
			box.sendKeys(targetPhrase);
		} else {
			System.out.println("box not found");
		}
		if (button != null) {
			System.out.println("button found");
			button.click();
		} else {
			System.out.println("button not found");
		}
	}

	@FindBy(xpath = "//div[@id='verses-text']//p[@class='f gray top-number-of-results']")
	WebElement resultText;

	public String getNumHitsFound() {
		String result = "number of hits not found";
		System.out.println("Now commencing search for element");
		WebElement element = ew.awaitElement(resultText, MAXWAIT);
		System.out.println("Element search concluded");
		result = element.getText();
		if (resultText != null)
			result = resultText.getText();
		return result;
	}

	@FindBy(xpath = "//div[@id='drop-down-sort']")
	WebElement dropdown;

	public WebElement findDropdown() {
		WebElement element = null;
		element = ew.awaitElement(dropdown, MAXWAIT);
		return element;
	}

	public String getDropdownText() {
		WebElement element = findDropdown();
		String text = "";
		if(element != null) text = element.getText();
		else text = "element null";
		return text;
	}

}
