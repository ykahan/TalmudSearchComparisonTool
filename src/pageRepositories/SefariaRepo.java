package pageRepositories;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import management.ExplicitlyWait;

public class SefariaRepo {
	WebDriver driver;
	final int MAXWAIT;
	ExplicitlyWait ew;
	String sefariaUrl = "https://www.sefaria.org.il/texts";

	public SefariaRepo(WebDriver driver, int MAXWAIT) {
		this.driver = driver;
		this.ew = new ExplicitlyWait(driver);
		this.MAXWAIT = MAXWAIT;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@id='searchInput']")
	WebElement searchInput;

	@FindBy(xpath = "//span[@class='readerNavMenuSearchButton']")
	WebElement searchButton;

	@FindBy(xpath = "//span[contains(text(), \"סינון\")]")
	WebElement filterButton;

	@FindBy(xpath = "//label[@id='label-for-Talmud']")
	WebElement talmudFilter;

	@FindBy(xpath = "//div[@id='readerAppWrap']")
	WebElement background;
	
	@FindBy (xpath = "//div[@class='type-button-title']/span[@class='int-he']")
	List<WebElement> resultsReports;

	public void goToSefaria() {
		driver.get(sefariaUrl);
	}

	public void loadTargetPhrase(String targetPhrase) {
		WebElement searchBox = ew.awaitElement(searchInput, MAXWAIT);
		searchBox.sendKeys(targetPhrase);
		clickSearchButton();
	}

	private void clickSearchButton() {
		WebElement searchBttn = ew.awaitElement(searchButton, MAXWAIT);
		searchBttn.click();
	}

	public void filterResults() {
		WebElement filterBttn = ew.awaitElement(filterButton, MAXWAIT);
		filterBttn.click();
		getTalmudResults();
		closeFilterModal();
	}

	private void closeFilterModal() {
		filterButton.click();
	}

	private void getTalmudResults() {
		WebElement talmudFltr = ew.awaitElement(talmudFilter, MAXWAIT);
		talmudFltr.click();
	}

	public int getNumResults() throws InterruptedException {
		List<WebElement> localResultsReports = ew.awaitList(resultsReports, MAXWAIT);
		String totalResultsMessage = localResultsReports.get(0).getText();
		totalResultsMessage = totalResultsMessage.replaceAll("[^\\d]", "");
		return Integer.parseInt(totalResultsMessage);
	}
}
