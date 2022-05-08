package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTest {
	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(String browser) {

		switch (browser) {
		case "chrome": 
			// Create driver
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			driver = new ChromeDriver();
			break;
		case "firefox": 
			// Create driver
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
			driver = new FirefoxDriver();
			break;
		default:
			System.out.println("Do not know how to start " + browser + ", starting chrome instead.");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			driver = new ChromeDriver();
			break;
		}

		// Maximize browser windows
		driver.manage().window().maximize();

		// Use this to delay screen to open page or to delay steps
		sleep(1500);
	}

	@Test(priority = 1, groups = { "positiveLoginTest", "smokeTest}" })
	public void positiveLoginTest() {
		System.out.println("Start loginTest");
		// Open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened");

		// Enter username
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

		// Enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");

		// Click login button
		WebElement loginButton = driver.findElement(By.tagName("button"));
		loginButton.click();

		// Verifications:
		// Redirect to new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual url is not same as expected");

		// Logout button is visible
		WebElement logoutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));

		// Success login message appear
		WebElement successMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = successMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\n Actual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);
	}

	@Parameters({ "username", "password", "expectedErrorMessage" })
	@Test(priority = 2, groups = { "negativeLoginTest", "smokeTest}" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) {
		System.out.println("Start login test");

		// Open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened");

		// Enter username
		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

		// Enter password
		WebElement passwordElement = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);

		// Click Login button
		WebElement loginButton = driver.findElement(By.tagName("button"));
		loginButton.click();

		sleep(1500);

		// Verifications:
		// Stay on login page
		String expectedUrl = "http://the-internet.herokuapp.com/login";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual url is not same as expected");

		// Error message is shown
		WebElement errorMessageElement = driver.findElement(By.id("flash"));
		String actualMessage = errorMessageElement.getText();
		Assert.assertTrue(actualMessage.contains(expectedErrorMessage),
				"Actual message already contain expected message");
	}

	private void sleep(long milisecond) {
		try {
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}
}
