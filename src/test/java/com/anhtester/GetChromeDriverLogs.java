package com.anhtester;

import com.anhtester.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class GetChromeDriverLogs {

    private WebDriver driver;

    @BeforeMethod
    public void createDriver() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");

        // System.setProperty("webdriver.edge.logfile", "edgedriverlogs.log");
        // System.setProperty("webdriver.edge.verboseLogging", "true");
        // System.setProperty("webdriver.firefox.logfile", "firefoxdriverlogs.log");
        // System.setProperty("webdriver.firefox.verboseLogging", "true");

        System.setProperty("webdriver.chrome.logfile", "chromedriverlogs.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        ChromeOptions options = new ChromeOptions();
        options.setBrowserVersion("117"); //Hiện tại 116 mà mình cố tình chaạy 117 xem logs như nào =))

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        new WebUI(driver);
    }

    @Test(priority = 1)
    public void testGetChromeDriverLogs() {
        driver.get("https://crm.anhtester.com/admin/authentication");
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        driver.findElement(By.xpath("//span[normalize-space()='Projects']")).click();
    }

    @AfterMethod
    public void closeDriver() {
        driver.quit();
    }
}