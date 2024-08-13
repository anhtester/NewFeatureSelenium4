package com.anhtester.common;

import com.anhtester.helpers.PropertiesHelper;
import com.anhtester.keywords.WebUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    public WebDriver driver;

    @BeforeMethod
    public void createBrowser() {
        driver = browserConfig();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));

        new WebUI(driver);
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }

    public void sleep(double second) {
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public WebDriver browserConfig() {

        String browserName = System.getProperty("browser") != null ? System.getProperty("browser")
                : PropertiesHelper.getValue("browser");

        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (PropertiesHelper.getValue("headless").equals("true")) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("window-size=1800,900");
                }
                driver = new ChromeDriver();
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (PropertiesHelper.getValue("headless").equals("true")) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("window-size=1800,900");
                }
                driver = new EdgeDriver();
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (PropertiesHelper.getValue("headless").equals("true")) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("window-size=1800,900");
                }
                driver = new FirefoxDriver();
                break;
        }

        return driver;
    }
}
