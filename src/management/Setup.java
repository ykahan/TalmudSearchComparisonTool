package management;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Setup {
	private final static String FIREFOX_PROPERTY = "webdriver.gecko.driver";
	private final static String FIREFOX_PATH = "C:\\Users\\USER\\Java_Workspace_Yehoshua\\Selenium Dependencies\\geckodriver.exe";
	private final static String CHROME_PROPERTY = "webdriver.chrome.driver";
	private final static String CHROME_PATH = "C:\\Users\\USER\\Java_Workspace_Yehoshua\\Selenium Dependencies\\chromedriver.exe";
	private final static String EDGE_PROPERTY = "webdriver.edge.driver";
	private final static String EDGE_PATH = "C:\\Users\\USER\\Java_Workspace_Yehoshua\\Selenium Dependencies\\MicrosoftWebDriver.exe";
	
	public static WebDriver setupDriver(String browser, int waitTime) {
		WebDriver driver = setupDriver(browser);

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.MILLISECONDS);

		return driver;
	}

	public static WebDriver setupDriver(String browser) {
		WebDriver driver = null;
		switch (browser) {
		case "Chrome":
			driver = getChromeDriver();
			break;
		case "FireFox":
			driver = getFireFoxDriver();
			break;
		case "Edge":
			driver = getEdgeDriver();
			break;
		}

		driver.manage().window().maximize();
		return driver;
	}

	public static WebDriver getEdgeDriver() {
		
		System.setProperty(EDGE_PROPERTY, EDGE_PATH);
		WebDriver webDriver = new EdgeDriver();
		webDriver.get("about:InPrivate");

		setBrowserSizePosition(webDriver);

		return webDriver;
	}

	public static WebDriver getFireFoxDriver() {

		System.setProperty(FIREFOX_PROPERTY, FIREFOX_PATH);

		WebDriver webDriver = new FirefoxDriver();

		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("-private");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("moz:firefoxOptions", options);
		System.out.println("Firefox set to Private mode\n\n");

		setBrowserSizePosition(webDriver);

		return webDriver;
	}

	private static void setBrowserSizePosition(WebDriver webDriver) {
		Dimension targetSize = new Dimension(1200, 800);
		webDriver.manage().window().setSize(targetSize);
		Point targetPosition = new Point(540, 0);
		webDriver.manage().window().setPosition(targetPosition);
	}

	public static WebDriver getChromeDriver() {
		WebDriver driver;
		System.setProperty(CHROME_PROPERTY, CHROME_PATH);

		driver = new ChromeDriver();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
		System.out.println("Chrome set to Incognito mode\n\n");
		setBrowserSizePosition(driver);

		return driver;
	}

}
