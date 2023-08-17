package com.anhtester;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class RunOnGrid {

    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

    @Parameters({"ipAddress", "portNumber"})
    @BeforeMethod
    public void setDriver(String ipAddress, String portNumber) throws MalformedURLException {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        String HUB_URL = "http://192.168.77.1:4444/wd/hub";
        //HUB_URL = "http://localhost:4444/wd/hub";
        String NODE_URL = "http://" + ipAddress + ":" + portNumber + "";

        DesiredCapabilities capabilities = capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setPlatform(Platform.WINDOWS);
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);
        driver.set(new RemoteWebDriver(new URL(String.format(HUB_URL, NODE_URL)), options));
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        new WebUI(driver.get());
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public static void sleep(double second) {
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void closeBrowser() {
        driver.get().quit();
        driver.remove();
    }

    @Test
    public void testGrid01() {
        getDriver().get("https://google.com");
        getDriver().findElement(By.xpath("//textarea[@name='q']")).sendKeys("học auto test", Keys.ENTER);
        getDriver().findElement(By.xpath("//h3[contains(text(),'Lộ trình học để trở thành Automation Tester')]")).click();
        sleep(5);
        Assert.assertEquals(getDriver().getTitle(), "Lộ trình học để trở thành Automation Tester | Anh Tester");
    }

    @Test
    public void testGrid02() throws InterruptedException {
        getDriver().get("https://crm.anhtester.com/admin/authentication");
        getDriver().findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        getDriver().findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        getDriver().findElement(By.xpath("//button[normalize-space()='Login']")).click();
        Thread.sleep(3000);
        Assert.assertEquals(getDriver().getTitle(), "Dashboard", "Title not match.");
    }

    @Test
    public void testSearchKeywordOnGoogle01() {
        getDriver().get("https://google.com");
        WebUI.waitForPageLoaded();
        getDriver().findElement(By.xpath("//textarea[@name='q']")).sendKeys("học auto test", Keys.ENTER);
        getDriver().findElement(By.xpath("//h3[contains(text(),'Lộ trình học để trở thành Automation Tester')]")).click();
        WebUI.waitForPageLoaded();
        sleep(3);
        WebUI.pressDownKey(5);
        Assert.assertEquals(getDriver().getTitle(), "Lộ trình học để trở thành Automation Tester | Anh Tester");
    }

    @Test
    public void testSearchKeywordOnGoogle02() {
        getDriver().get("https://google.com");
        WebUI.waitForPageLoaded();
        getDriver().findElement(By.xpath("//textarea[@name='q']")).sendKeys("selenium là gì", Keys.ENTER);
        WebUI.waitForPageLoaded();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", getDriver().findElement(By.xpath("//textarea[@name='q']")));
        getDriver().findElement(By.xpath("//a[contains(@href,'https://anhtester.com')]//h3")).click();
        WebUI.waitForPageLoaded();
        sleep(3);
        WebUI.pressDownKey(5);
        Assert.assertEquals(getDriver().getTitle(), "Giới thiệu về Selenium | Anh Tester");
    }
} 