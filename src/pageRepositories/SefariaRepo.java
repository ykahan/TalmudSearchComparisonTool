package pageRepositories;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import management.ExplicitlyWait;
import searchResultsMachinery.Hit;

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

	@FindBy(xpath = "//div[@class='type-button-title']/span[@class='int-he']")
	List<WebElement> resultsReports;

	@FindBy(xpath = "//div[@class='result text_result']")
	List<WebElement> resultsList;
	
	@FindBy(xpath = "//div[@class='contentText snippet he']")
	List<WebElement> textsList;

//	@FindBy(xpath = "//span[@class='similar-title int-he']")
	@FindBy (xpath = "//div[@class = 'similar-trigger-box']")
	List<WebElement> girsaNosefetList;

	@FindBy(xpath = "//div[@class='result-title']")
	List<WebElement> locationDataList;
	
	@FindBy (xpath = "//body")
	WebElement body;

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
		Thread.sleep(500);
		List<WebElement> localResultsReports = ew.awaitList(resultsReports, MAXWAIT);
		String totalResultsMessage = localResultsReports.get(0).getText();
		totalResultsMessage = totalResultsMessage.replaceAll("[^\\d]", "");
		return Integer.parseInt(totalResultsMessage);
	}

	public List<WebElement> getResultsList() throws InterruptedException {
		List<WebElement> localResultsList = ew.awaitList(resultsList, MAXWAIT);
		return localResultsList;
	}
	
	public List<Hit> getListAlternateHits(String targetPhrase, int numSefariaResults) throws InterruptedException {
		List<WebElement> otherVersions = ew.awaitList(girsaNosefetList, MAXWAIT);
		int size = otherVersions.size();
		for(int current = 0; current < size; current++) {
			otherVersions.get(current).click();
		}
		Thread.sleep(20000);
		return null;
	}

	public List<Hit> getListHits(String targetPhrase, int numSefariaResults) throws InterruptedException {
		List<Hit> output = new ArrayList<Hit>();
		// only the first 50 elements of the list load until the user gets to
		// the bottom of the page
		makeListFinishLoading(numSefariaResults);
		
		List<WebElement> blocksFound = getResultsList();
		List<WebElement> localLocationDataList = ew.awaitList(locationDataList, MAXWAIT);

		int numBlocksFound = blocksFound.size();
		int numLocationData = localLocationDataList.size();
		
		for (int current = 0; current < numBlocksFound; current++) {

			String[] locationDataArray = getlocationDataArray(localLocationDataList, current);
			String masechta = locationDataArray[0];
			String daf = locationDataArray[1];
			String amud = locationDataArray[2];
			String text = getInstanceText(current);
			
			Hit hit = new Hit(masechta, daf, amud, text);
			output.add(hit);
		}
		return output;
	}
	
	private String getInstanceText(int current) throws InterruptedException {
		List<WebElement> texts = ew.awaitList(textsList, MAXWAIT);
		String text = texts.get(current).getText();
		return text;
	}


	private void makeListFinishLoading(int numSefariaResults) throws InterruptedException {
		int timesToScrollDown = (numSefariaResults / 50);
		for(int i = 0; i < timesToScrollDown; i++) {
			scrollToBottom();
			Thread.sleep(500);
		}
	}

	private String[] getlocationDataArray(List<WebElement> localLocationDataList, int current) {
		String[] locationDataArray = localLocationDataList.get(current).getText().split(" ");
		String masechta = "";
		String daf = "";
		String amud = "";
		
		masechta = locationDataArray[0];
		boolean doubleName = (masechta.contentEquals("בבא") || masechta.contentEquals("מועד")
				|| masechta.contentEquals("עבודה"));
		if (doubleName) {
			masechta += " " + locationDataArray[1];
			daf = locationDataArray[2];
			amud = locationDataArray[3];
		} else {
			daf = locationDataArray[1];
			amud = locationDataArray[2];
		}
		daf = daf.replaceAll("״", "");
		daf = daf.replaceAll("׳", "");

		String[] amudArray = amud.split(":");
		amud = amudArray[0];
		
		String[] output = {masechta, daf, amud};
		return output;
	}

	private void scrollToTop() {
		String scrollUp = Keys.chord(Keys.HOME);
		body.sendKeys(scrollUp);
		
	}

	private void scrollToBottom() {
		String scrollDown = Keys.chord(Keys.END);
		body.sendKeys(scrollDown);		
	}
}
