package management;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import searchResultsMachinery.DictaMachinery;
import searchResultsMachinery.Hit;
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
		targetPhrase = "היינו תנא קמא";  // phrase hard-coded for ease of testing
		driver = Setup.setupDriver("Chrome");
		System.out.println("Target phrase is {" + targetPhrase + "}");

		DictaMachinery dm = new DictaMachinery(driver, MAXWAIT);
		dm.goToDictaTalmudSearch(targetPhrase);
		int numDictaResults = dm.getNumResults();
		List<Hit> dictaHits = dm.getListHits(targetPhrase, numDictaResults);
		int dictaSize = dictaHits.size();

		SefariaMachinery sm = new SefariaMachinery(driver, MAXWAIT);
		sm.goToSefaria();
		sm.closeCookieNotification();
		int numSefariaResults = sm.getNumResults(targetPhrase);
		System.out.println("Sefaria # Results: {" + numSefariaResults + "}");

		List<Hit> mainSefariaHits = sm.getListHits(targetPhrase, numSefariaResults);
		int sefariaSize = mainSefariaHits.size();
		
		List<Hit> alternateSefariaHits = sm.getListAlternateHits(targetPhrase, numSefariaResults);
//		for(int i = 0; i < size; i++) {
//			Hit hit = sefariaHits.get(i);
//			System.out.println(
//					"Instance #" + (i + 1) + 
//					"\nMasechta: " + hit.getMasechta() + 
//					"\nDaf: " + hit.getDaf() + 
//					"\nAmud: " + hit.getAmud() +
//					"\nText: "+ hit.getText());
//			System.out.println("****************************************");
//		}
		System.out.println("Dicta Hits: " + dictaSize);
		System.out.println("Sefaria Hits: " + sefariaSize);
		driver.close();
	}

}
