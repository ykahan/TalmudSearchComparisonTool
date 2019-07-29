package management;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import searchResultsMachinery.DictaMachinery;
import searchResultsMachinery.Hit;
import searchResultsMachinery.HitList;
import searchResultsMachinery.SefariaMachinery;

public class Main {
	final static int MAXWAIT = 10; // maximum seconds to wait for element to appear
	static WebDriver driver;
	static List<Hit> dictaList = new ArrayList();
	List<Hit> sefariaList = new ArrayList();
	static String targetPhrase;

	public static void main(String[] args) throws InterruptedException {
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Target phrase:");
//		targetPhrase = scanner.nextLine();
		targetPhrase = "היינו תנא קמא"; // phrase hard-coded for ease of testing
		driver = Setup.setupDriver("Chrome");
		System.out.println("Target phrase is {" + targetPhrase + "}");

		DictaMachinery dm = new DictaMachinery(driver, MAXWAIT);
		dm.goToDictaTalmudSearch(targetPhrase);
		int numDictaResults = dm.getNumResults();
		List<Hit> dictaHits = dm.getListHits(targetPhrase, numDictaResults);
		HitList dictaList = new HitList(dictaHits, "Dicta");
		
		SefariaMachinery sm = new SefariaMachinery(driver, MAXWAIT);
		sm.goToSefaria();
		sm.closeCookieNotification();
		int numSefariaResults = sm.getNumResults(targetPhrase);
		System.out.println("Sefaria # Results: {" + numSefariaResults + "}");

		List<Hit> mainSefariaHits = sm.getListHits(targetPhrase, numSefariaResults);
		HitList mainSefaria = new HitList(mainSefariaHits, "Main Sefaria");

		List<Hit> alternateSefariaHits = sm.getListAlternateHits(targetPhrase, numSefariaResults);
		HitList alternateSefaria = new HitList(alternateSefariaHits, "Alt Girsa Sefaria");

		dictaList.printString();
		mainSefaria.printString();
		alternateSefaria.printString();
		int sefariaSize = mainSefaria.getSize();
		int altSize = alternateSefaria.getSize();

		int totalSefariaHits = sefariaSize + altSize;
		boolean rightNumHits = (totalSefariaHits == numSefariaResults);
		System.out.println("Total Hits equals all results: " + rightNumHits);

		driver.close();
	}

}
