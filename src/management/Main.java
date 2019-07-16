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
		dm.getResults(targetPhrase);
		driver.close();
//		dictaList = DictaMachinery.getResults(targetPhrase);
//		int listSize = dictaList.size();
//		for(int i = 0; i < listSize; i++) {
//			Hit hit = dictaList.get(i);
//			System.out.println("Hit #" + (i + 1) + "\n");
//			System.out.println(hit.toString());
//		}
	}

}
