package com.anhtester;

import com.anhtester.common.BaseTest;
import com.anhtester.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.annotations.Test;

public class AddCookies extends BaseTest {

    private static String cookiesCurrent;

    @Test(priority = 1)
    public void testGetCookies() {
        driver.get("https://rise.anhtester.com/signin");
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        driver.findElement(By.xpath("//button[normalize-space()='Sign in']")).click();
        WebUI.waitForPageLoaded();
        //Get cookies current by name
        cookiesCurrent = driver.manage().getCookieNamed("ci_session").getValue();
        System.out.println("Current Cookies: " + cookiesCurrent);
    }

    @Test(priority = 2)
    public void testAddCookies() {
        driver.get("https://rise.anhtester.com/signin");
        WebUI.waitForPageLoaded();
        // Add the cookie into current browser context (cookiesCurrent)
        System.out.println("Get Current Cookies: " + cookiesCurrent);
        driver.manage().addCookie(new Cookie("ci_session", cookiesCurrent));
        sleep(1);
        driver.navigate().refresh();
        WebUI.waitForPageLoaded();
        sleep(3);
    }
}
