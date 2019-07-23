package searchResultsMachinery;

import java.util.List;

import org.openqa.selenium.WebDriver;

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
		repo.goToSefaria();
		repo.loadTargetPhrase(targetPhrase);
		repo.filterResults();
		return repo.getNumResults();
	}

	private void goToSefariaSearch(String targetPhrase) {
		// TODO Auto-generated method stub
		
	}

	public List<Hit> getListHits(String targetPhrase, int numSefariaResults) {
		// TODO Auto-generated method stub
		return null;
	}
}
