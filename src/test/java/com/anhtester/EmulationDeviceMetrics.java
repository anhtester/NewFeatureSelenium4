package com.anhtester;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.anhtester.WebUI.sleep;

public class EmulationDeviceMetrics {
    private WebDriver driver;

    @BeforeMethod
    public void createDriver() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 400);
        deviceMetrics.put("height", 700);
        deviceMetrics.put("pixelRatio", 3.0);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");

        Map<String, Object> clientHints = new HashMap<>();
        clientHints.put("platform", "Android");
        clientHints.put("mobile", true);
        mobileEmulation.put("clientHints", clientHints);

        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        driver = new ChromeDriver(options);

        Dimension screenSize = driver.manage().window().getSize();
        System.out.println("Window Size / Dimensions : " + screenSize);
        System.out.println("Width  : " + screenSize.getWidth());
        System.out.println("Height : " + screenSize.getHeight());

        //driver.manage().window().setPosition(new Point((size.getWidth() / 2), 0));

        Dimension newDimension = new Dimension(300, screenSize.getHeight());
        driver.manage().window().setSize(newDimension);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        new WebUI(driver);
    }

    @Test(priority = 1)
    public void testLoginCRM() {
        driver.get("https://crm.anhtester.com/admin/authentication");
        sleep(1);
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        sleep(1);
        driver.findElement(By.xpath("//i[@class='fa fa-align-left']")).click();
        sleep(1);
        driver.findElement(By.xpath("//span[normalize-space()='Projects']")).click();

    }

    @AfterMethod
    public void closeDriver() {
        driver.quit();
    }
}