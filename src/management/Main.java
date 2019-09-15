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
		targetPhrase = "היינו תנא קמא"; // low frequency phrase, hard-coded for ease of testing
//		targetPhrase = "רבי מאיר"; // high frequency phrase, hard-coded for ease of testing
		driver = Setup.setupDriver("Chrome");
		targetPhrase = "טב למיתב";
		System.out.println("Target phrase is {" + targetPhrase + "}");

		DictaMachinery dm = new DictaMachinery(driver, MAXWAIT);
		dm.goToDictaTalmudSearch(targetPhrase);
		int numDictaResults = dm.getNumResults();
		System.out.println("Dicta results: {" + numDictaResults + "}");

		List<Hit> dictaHits = dm.getListHits(targetPhrase, numDictaResults);
		HitList dictaList = new HitList(dictaHits, "Dicta");
//			dictaList.printString();

		SefariaMachinery sm = new SefariaMachinery(driver, MAXWAIT);
		sm.goToSefaria();
		sm.closeCookieNotification();
		int numSefariaResults = sm.getNumResults(targetPhrase);
		System.out.println("Sefaria # Results (in total): {" + numSefariaResults + "}");

		List<Hit> mainSefariaHits = sm.getListHits(targetPhrase, numSefariaResults);
		HitList mainSefariaList = new HitList(mainSefariaHits, "Main Sefaria");

		List<Hit> alternateSefariaHits = sm.getListAlternateHits(targetPhrase, numSefariaResults);
		HitList alternateSefariaList = new HitList(alternateSefariaHits, "Alt Girsa Sefaria");

		//
//		int sefariaSize = mainSefaria.getSize();
//		int altSize = alternateSefaria.getSize();
//
//		int totalSefariaHits = sefariaSize + altSize;
//		boolean rightNumHits = (totalSefariaHits == numSefariaResults);
//		System.out.println("Total Sefaria Hits equals all Sefaria results: " + rightNumHits);
		Evaluate.compare(dictaList, mainSefariaList);
		Evaluate.compare(dictaList, alternateSefariaList);
		
		dictaList.printString();
		mainSefariaList.printString();
		alternateSefariaList.printString();	
		
		dictaList.printNumFound();
		mainSefariaList.printNumFound();
		alternateSefariaList.printNumFound();
		
		driver.close();
	}

}
