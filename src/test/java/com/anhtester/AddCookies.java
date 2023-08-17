package com.anhtester;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.annotations.Test;

public class AddCookies extends BaseTest {

    private static String cookiesCurrent;

    @Test(priority = 1)
    public void testGetCookies() {
        driver.get("https://rise.fairsketch.com/signin");
        driver.findElement(By.xpath("//button[normalize-space()='Sign in']")).click();
        sleep(5);
        //Get cookies current by name
        cookiesCurrent = driver.manage().getCookieNamed("ci_session").getValue();
        System.out.println("Current Cookies: " + cookiesCurrent);
    }

    @Test(priority = 2)
    public void testAddCookies() {
        driver.get("https://rise.fairsketch.com/signin");
        // Add the cookie into current browser context (cookiesCurrent)
        System.out.println("Get Current Cookies: " + cookiesCurrent);
        driver.manage().addCookie(new Cookie("ci_session", cookiesCurrent));
        sleep(3);
        driver.navigate().refresh();
        sleep(5);
    }
}
