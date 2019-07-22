package management;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import searchResultsMachinery.DictaMachinery;
import searchResultsMachinery.Hit;

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
		int numDictaResults = dm.getNumResults(targetPhrase);
		List<Hit> dictaHits = dm.getListHits(targetPhrase, numDictaResults);
		int size = dictaHits.size();
		for(int i = 0; i < size; i++) {
			System.out.println("\nHit #" + (i + 1));
			System.out.println("Masechta: " + dictaHits.get(i).getMasechta());
			System.out.println("Daf: " + dictaHits.get(i).getDaf());
			System.out.println("Amud: " + dictaHits.get(i).getAmud());
			System.out.println("Text: " + dictaHits.get(i).getText());
			System.out.println("========================================");
		}
		driver.close();
	}

}
