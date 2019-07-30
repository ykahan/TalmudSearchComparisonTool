package searchResultsMachinery;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pageRepositories.SefariaRepo;

public class SefariaMachinery {
	static SefariaRepo repo;
	static WebDriver driver;
	int MAXWAIT;

	public SefariaMachinery(WebDriver driver, int MAXWAIT) {
		SefariaMachinery.driver = driver;
		this.MAXWAIT = MAXWAIT;
		SefariaMachinery.repo = new SefariaRepo(driver, MAXWAIT);
	}

	public int getNumResults(String targetPhrase) throws InterruptedException {
		repo.loadTargetPhrase(targetPhrase);
		int unfilteredResults = repo.getNumResults();
		if (unfilteredResults > 0) repo.filterResults();
		return repo.getNumResults();
	}
	
	public void goToSefaria() {
		repo.goToSefaria();
	}

	public List<Hit> getListHits(String targetPhrase, int numSefariaResults) throws InterruptedException {
		return repo.getMainListHits(targetPhrase, numSefariaResults);
	}

	public List<Hit> getListAlternateHits(String targetPhrase, int numSefariaResults) throws InterruptedException {
		return repo.getListAlternateHits(targetPhrase, numSefariaResults);
	}

	public void closeCookieNotification() {
		repo.closeCookieNotification();		
	}
}
