package searchResultsMachinery;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import management.ExplicitlyWait;
import pageRepositories.DictaRepo;

public class DictaMachinery {
	static DictaRepo repo;
	static WebDriver driver;
	int MAXWAIT;

	public DictaMachinery(WebDriver driver, int MAXWAIT) {
		this.driver = driver;
		this.MAXWAIT = MAXWAIT;
		this.repo = new DictaRepo(driver, MAXWAIT);
	}

	public static void getResults(String targetPhrase) throws InterruptedException {
		goToTalmudSearch(targetPhrase);
		waitForSkip();
		String hitsFound = repo.getNumHitsFound();
		System.out.println(hitsFound);
	}

	private static void waitForSkip() throws InterruptedException {
		int waitTime = 10000; // time to wait in milliseconds to allow siteskip
		Thread.sleep(waitTime);
		
	}

	private static void goToTalmudSearch(String targetPhrase) {
		String url = getUrl(targetPhrase);
		driver.get(url);
	}

	private static String getUrl(String targetPhrase) {
		String[] words = targetPhrase.split(" ");
		int numWords = words.length;
		StringBuilder sb = new StringBuilder();
		sb.append("https://talmud-plus--cranky-banach-377068.netlify.com/result?text=");
		for(int i = 0; i < numWords; i++) {
			sb.append(words[i]);
			if(i < (numWords - 1)) sb.append("%20");
		}
		String url = sb.toString();
		return url;
	}
}
