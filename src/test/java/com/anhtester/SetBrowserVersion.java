package com.anhtester;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SetBrowserVersion {
    private WebDriver driver;

    @BeforeMethod
    public void createDriver() {
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        //System.setProperty("webdriver.chrome.verboseLogging", "true");
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        options.setBrowserVersion("115");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        new WebUI(driver);
    }

    @Test(priority = 1)
    public void testLoginCRM() {
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
