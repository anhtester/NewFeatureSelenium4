package com.anhtester.handles.HandleWaitAngular2;

import com.anhtester.common.BaseTest;
import com.anhtester.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class DemoWaitAngular2 {

    public WebDriver driver;

    @BeforeMethod
    public void createBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        new WebUI(driver);
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }

    @Test
    public void testWaitAngular2() {
        driver.get("https://upfleet.com.vn/bus");

        WebUI.setText(By.xpath("//input[@id='username']"), "test.fleetwork1@gmail.com");
        WebUI.setText(By.xpath("//input[@id='password']"), "123456");
        WebUI.clickElement(By.xpath("//button[@id='login-button']"));

        WebUI.clickElementStale(By.xpath("//div[normalize-space()='Testprod']"));
        WebUI.clickElementStale(By.xpath("(//span[normalize-space()='Danh sách'])[1]"));

        WebUI.setTextElementStale(By.xpath("//input[@placeholder='Tìm kiếm ']"), "dfdsfsd");

        WebUI.sleep(1);
        WebUI.waitForElementVisible(By.xpath("//td[1]//span[contains(.,'dfdsfsd')]"));
    }
}
