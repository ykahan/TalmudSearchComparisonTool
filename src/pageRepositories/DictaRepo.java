package pageRepositories;

import org.openqa.selenium.By;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.remote.RemoteWebElement;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.PageFactory;
//import management.ExplicitlyWait;
//import java.util.ArrayList;
//import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import management.ExplicitlyWait;
import searchResultsMachinery.Hit;
import searchResultsMachinery.SharedMachinery;

public class DictaRepo {
	WebDriver driver;
	final int MAXWAIT;
	ExplicitlyWait ew;

	public DictaRepo(WebDriver driver, int MAXWAIT) {
		this.driver = driver;
		this.ew = new ExplicitlyWait(driver);
		this.MAXWAIT = MAXWAIT;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@id='search_box']")
	WebElement searchBox;

	@FindBy(xpath = "//button[@id='search_button']")
	WebElement searchBoxButton;

	@FindBy(xpath = "//div[@id='verses-text']//p[@class='f gray top-number-of-results']")
	WebElement resultText;

	@FindBy(xpath = "//div[@id='drop-down-sort']")
	WebElement dropdown;

	@FindBy(xpath = "//a[@data-target='#detail']/span")
	List<WebElement> summaryList;

	@FindBy(xpath = "//ul[@id='pagination']//button")
	List<WebElement> navigationButtons;

	@FindBy(xpath = "//button[@class='pagination__navigation']")
	List<WebElement> pageNavigationButtons;

	@FindBy(xpath = "//div[@class='result-li-top-section']")
	List<WebElement> instancesList;

	@FindBy(xpath = "//div[@class='sentence-holder']")
	List<WebElement> talmudTextList;

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

	public String getNumHitsFound() {
		String result = "number of hits not found";
		WebElement element = ew.awaitElement(resultText, MAXWAIT);
		if (resultText != null)
			result = resultText.getText();
		return result;
	}

	public WebElement findDropdown() {
		WebElement element = null;
		element = ew.awaitElement(dropdown, MAXWAIT);
		return element;
	}

	public String getDropdownText() {
		WebElement element = findDropdown();
		String text = "";
		if (element != null)
			text = element.getText();
		else
			text = "element null";
		return text;
	}

	public List<Hit> getHitsList(String targetPhrase, int numHits) throws InterruptedException {
		int numDictaPages = getNumDictaPages(numHits);
		List<Hit> dictaHitsList = new ArrayList<Hit>();
		String[] nameDafAmudText = new String[4];
		
		System.out.println("Getting Dicta Hits...");
		for (int page = 0; page < numDictaPages; page++) {
			List<WebElement> summaryListLocal = ew.awaitList(summaryList, MAXWAIT);

			int numFoundInstances = summaryListLocal.size();

			for (int instance = 0; instance < numFoundInstances; instance++) {
				// getting name and location data
				nameDafAmudText = getNameDafAmud(summaryListLocal, instance);
				// getting text
				nameDafAmudText[3] = getText(instance);

				String name = nameDafAmudText[0];
				// source material gives "Terumah" for "Temurah"
				if (name.contentEquals("תרומה"))
					name = "תמורה";
				String daf = nameDafAmudText[1];
				String amud = nameDafAmudText[2];
				String text = nameDafAmudText[3];

				Hit hit = new Hit(name, daf, amud, text);
				dictaHitsList.add(hit);
				
				SharedMachinery.waiting(instance);

			}
			if((page + 1) < numDictaPages) clickNextPageButton();
		}
		return dictaHitsList;
	}

	private String getText(int instance) throws InterruptedException {
		List<WebElement> instancesListLocal = ew.awaitList(instancesList, MAXWAIT);
		List<WebElement> textBlocks = instancesListLocal.get(instance)
				.findElements(By.xpath(".//div[@class='sentence-holder']"));
		textBlocks = ew.awaitList(textBlocks, MAXWAIT);
		int textBlocksSize = textBlocks.size();
		String output = "";
		for (int i = 0; i < textBlocksSize; i++) {
			output += textBlocks.get(i).getText();
			output += " ";
		}

		return output;
	}

	private String[] getNameDafAmud(List<WebElement> summaryListLocal, int instance) {
		String[] output = new String[4];

		String masechta = "";
		String daf = "";
		String amud = "";
		String location = "";
		String[] instanceTextArray = summaryListLocal.get(instance).getText().split(" ");
		int instanceTextArrayLength = instanceTextArray.length;
		masechta = instanceTextArray[6];
		// special case of masechtos with two-word names
		if (masechta.equals("בבא") || masechta.contentEquals("מועד") || masechta.contentEquals("ראש")
				|| masechta.contentEquals("עבודה"))
			masechta += " " + instanceTextArray[7];

		location = instanceTextArray[instanceTextArrayLength - 1];
		String[] dafAndAmud = location.split(",");
		daf = dafAndAmud[0].trim();
		amud = dafAndAmud[1].trim();
		output[0] = masechta;
		output[1] = daf;
		output[2] = amud;
		output[3] = "";
		return output;
	}

	private int targetPhraseFound(String[] targetPhraseArray, String[] unprocessedTextArray) {
		int targetPhraseLength = targetPhraseArray.length;
		int unprocessedTextLength = unprocessedTextArray.length;
		int lastUnprocessedWordToCheck = unprocessedTextLength - 1 - targetPhraseLength;

		for (int unprocessedWord = 0; unprocessedWord < lastUnprocessedWordToCheck; unprocessedWord++) {
			String[] textToCheck = new String[targetPhraseLength];
			for (int wordToCheck = 0; wordToCheck < targetPhraseLength; wordToCheck++) {
				textToCheck[wordToCheck] = unprocessedTextArray[unprocessedWord + wordToCheck];
				if (textToCheck.equals(targetPhraseArray))
					return unprocessedWord;
			}
		}
		return -1;
	}

	private void clickNextPageButton() throws InterruptedException {
		List<WebElement> elements = ew.awaitList(pageNavigationButtons, MAXWAIT);
		if (!elements.isEmpty()) {
			int size = elements.size();
			int last = size - 1;
			WebElement element = elements.get(last);
			element.click();
		}

	}

	private int getNumDictaPages(int numHits) {
		int numPages = numHits / 10;
		int hitsLeftOver = numHits % 10;
		if (hitsLeftOver > 0)
			numPages++;
		System.out.println("Dicta pages: {" + numPages + "}");
		return numPages;
	}

}
