package management;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import searchResultsMachinery.DictaMachinery;
import searchResultsMachinery.Evaluate;
import searchResultsMachinery.Hit;
import searchResultsMachinery.HitList;
import searchResultsMachinery.SefariaMachinery;

public class Main {
	final static int MAXWAIT = 10; // maximum seconds to wait for element to appear
	static WebDriver driver;
	static String targetPhrase;

	public static void main(String[] args) throws InterruptedException {
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Target phrase:");
//		targetPhrase = scanner.nextLine();
		targetPhrase = Phrases.tanyaKevasei;
		driver = Setup.setupDriver("Chrome");
		System.out.println("Target phrase is {" + targetPhrase + "}");

		DictaMachinery dm = new DictaMachinery(driver, MAXWAIT);
		dm.goToDictaTalmudSearch(targetPhrase);
		int numDictaResults = dm.getNumResults();
		System.out.println("Dicta results: {" + numDictaResults + "}");

		List<Hit> dictaHits = dm.getListHits(targetPhrase, numDictaResults);
		HitList dictaList = new HitList(dictaHits, "Dicta");
		HitList dictaList2 = dictaList.duplicateList("Dicta");

		SefariaMachinery sm = new SefariaMachinery(driver, MAXWAIT);
		sm.goToSefaria();
		sm.closeCookieNotification();
		int numSefariaResults = sm.getNumResults(targetPhrase);
		System.out.println("Sefaria # Results (in total): {" + numSefariaResults + "}");

		List<Hit> mainSefariaHits = sm.getListHits(targetPhrase, numSefariaResults);
		HitList mainSefariaList = new HitList(mainSefariaHits, "Main Sefaria");

		List<Hit> alternateSefariaHits = sm.getListAlternateHits(targetPhrase, numSefariaResults);
		HitList alternateSefariaList = new HitList(alternateSefariaHits, "Alt Girsa Sefaria");
		
		dictaList.printString();
		mainSefariaList.printString();
		alternateSefariaList.printString();	
		
		Evaluate.printComparison(dictaList, mainSefariaList);
		Evaluate.printComparison(dictaList2, alternateSefariaList);
		
		driver.close();
	}

}
