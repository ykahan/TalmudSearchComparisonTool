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
import searchResultsMachinery.SharedMachinery;

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
	List<WebElement> mainTextsList;

	@FindBy(xpath = "//div[@class = 'similar-trigger-box']")
	List<WebElement> girsaNosefetList;

	@FindBy(xpath = "//div[@class='result-title']")
	List<WebElement> mainLocationDataList;

	@FindBy(xpath = "//body")
	WebElement body;

	@FindBy(xpath = "//span[@class = 'int-he button small white']")
	WebElement closeCookieNotificationButton;

	@FindBy(xpath = "//div[@class='similar-results']//span[@class = 'int-he']")
	List<WebElement> alternateLocationDataList;

	@FindBy(xpath = "//div[@class='similar-results']")
	List<WebElement> alternateGirsaBlocks;

	@FindBy(xpath = "//div[@class='similar-results']//span[@class='int-he']")
	List<WebElement> alternateGirsaLocationDataList;

	@FindBy(xpath = "//div[@class='similar-results']//div[@class='contentText snippet he']")
	List<WebElement> alternateGirsaTextList;

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

	private List<WebElement> getAlternateTextBlocks() throws InterruptedException {
		List<WebElement> alternateTextBlocksList = ew.awaitList(alternateGirsaBlocks, MAXWAIT);
		return alternateTextBlocksList;
	}

	private void openAlternateTexts(List<WebElement> otherVersions, int size) {
		for (int current = 0; current < size; current++) {
			otherVersions.get(current).click();

			String waitingMessage = "Opened alternate girsaos in Sefaria: " + (current + 1) + "/" + size + "\n";
			SharedMachinery.waiting(waitingMessage);
		}
	}

	public List<Hit> getMainListHits(String targetPhrase, int numSefariaResults) throws InterruptedException {
		// only the first 50 elements of the list load until the user gets to
		// the bottom of the page
		makeListFinishLoading(numSefariaResults);

		List<WebElement> blocksFound = getResultsList();
		List<WebElement> localLocationDataList = ew.awaitList(mainLocationDataList, MAXWAIT);
		List<WebElement> localTextsList = ew.awaitList(mainTextsList, MAXWAIT);

		int numBlocksFound = blocksFound.size();

		List<Hit> output = extractHitData(localLocationDataList, localTextsList, numBlocksFound, targetPhrase);
		return output;
	}

	public List<Hit> getListAlternateHits(String targetPhrase, int numSefariaResults) throws InterruptedException {
		List<WebElement> alternateTexts = ew.awaitList(girsaNosefetList, MAXWAIT);
		int size = alternateTexts.size();
		openAlternateTexts(alternateTexts, size);
		List<WebElement> alternateGirsaLocationDataList = getAlternateGirsaLocationData();
		List<WebElement> alternateGirsaTextList = getAlternateGirsaTexts();
		int numAltGirsaos = alternateGirsaLocationDataList.size();

		List<Hit> output = extractHitData(alternateGirsaLocationDataList, alternateGirsaTextList, numAltGirsaos, targetPhrase);

		return output;
	}

	private List<WebElement> getAlternateGirsaTexts() throws InterruptedException {
		List<WebElement> altGirsaTextList = ew.awaitList(alternateGirsaTextList, MAXWAIT);
		return altGirsaTextList;
	}

	private List<WebElement> getAlternateGirsaLocationData() throws InterruptedException {
		List<WebElement> altGirsaLocationDataList = ew.awaitList(alternateGirsaLocationDataList, MAXWAIT);
		return altGirsaLocationDataList;
	}

	private List<Hit> extractHitData(List<WebElement> localLocationDataList, List<WebElement> localTextsList,
			int numBlocksFound, String targetPhrase) throws InterruptedException {
		System.out.println("Beginning extraction of Sefaria Hit data.");
		List<Hit> output = new ArrayList<Hit>();
//		System.out.println("Extracting Sefaria Data...");
		for (int current = 0; current < numBlocksFound; current++) {
			if (notYerushalmi(localLocationDataList, current)) {
				String[] locationDataArray = getlocationDataArray(localLocationDataList, current);

				String masechta = locationDataArray[0];
				String daf = locationDataArray[1];
				String amud = locationDataArray[2];
				String text = getInstanceText(localTextsList, current);

				Hit hit = new Hit(masechta, daf, amud, text, targetPhrase);

				output.add(hit);
				String waitingMessage = "Extracted Sefaria Hit Data " + (current + 1) + "/" + numBlocksFound + "\n";
				SharedMachinery.waiting(waitingMessage);
			}
		}
		return output;
	}

	private boolean notYerushalmi(List<WebElement> locationDataList, int current) {
		String text = locationDataList.get(current).getText();
//		return false;
		if (text.contains("תלמוד ירושלמי"))
			return false;
		return true;
	}

	private String getInstanceText(List<WebElement> givenTextsList, int current) throws InterruptedException {
		List<WebElement> texts = ew.awaitList(givenTextsList, MAXWAIT);
		String text = texts.get(current).getText();
		return text;
	}

	private void makeListFinishLoading(int numSefariaResults) throws InterruptedException {
		int timesToScrollDown = (numSefariaResults / 50);
		for (int i = 0; i < timesToScrollDown; i++) {
			scrollToBottom();
			String waitingMessage = "Sefaria Scrolling Down " + (i + 1) + "/" + timesToScrollDown + "\n";
			SharedMachinery.waiting(waitingMessage);
			Thread.sleep(1000);

		}
	}

	private String[] getlocationDataArray(List<WebElement> localLocationDataList, int current) {
		String[] locationDataArray = localLocationDataList.get(current).getText().split(" ");
//		System.out.println("locationDataArray established");
		String masechta = "";
		String daf = "";
		String amud = "";

		masechta = locationDataArray[0];
		boolean doubleName = (masechta.contentEquals("בבא") || masechta.contentEquals("מועד")
				|| masechta.contentEquals("עבודה") || masechta.contentEquals("ראש"));
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
//		int amudLength = amud.length();
//		amud = discardAfter(amud, ':');
		amud = amudArray[0];

		String[] output = { masechta, daf, amud };
//		System.out.println("output array established");
		return output;
	}

	private String discardAfter(String givenString, char targetCharacter) {
		int charLocation = getCharLocation(givenString, targetCharacter);
		if (charLocation > -1) {
			givenString = givenString.substring(0, charLocation);
		}
		return givenString;
	}

	private int getCharLocation(String givenString, char targetCharacter) {
		int length = givenString.length();
		for (int current = 0; current < length; current++) {
			char character = givenString.charAt(current);
			if (character == targetCharacter)
				return current;
		}
		return -1;
	}

	private void scrollToTop() {
		String scrollUp = Keys.chord(Keys.HOME);
		body.sendKeys(scrollUp);

	}

	private void scrollToBottom() {
		String scrollDown = Keys.chord(Keys.END);
		body.sendKeys(scrollDown);
	}

	public void closeCookieNotification() {
		WebElement element = ew.awaitElement(closeCookieNotificationButton, MAXWAIT);
		element.click();
	}
}
