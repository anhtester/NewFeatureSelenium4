package com.anhtester;

import com.anhtester.common.BaseTest;
import com.anhtester.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class CloseAdsGoogle extends BaseTest {
    @Test
    public void testCloseAdsGoogle() {
        try {
            driver.get("https://demo.guru99.com/V4/");
            WebUI.waitForPageLoaded();
            Thread.sleep(1000);
            driver.findElement(By.name("uid")).sendKeys("mngr546738");
            driver.findElement(By.name("password")).sendKeys("jemEtYr");
            driver.findElement(By.name("btnLogin")).click();
            WebUI.waitForElementVisible(By.linkText("New Customer"));
            Thread.sleep(2000);
            driver.findElement(By.linkText("New Customer")).click();

            WebUI.waitForElementVisible(By.id("google_ads_iframe_/24132379/INTERSTITIAL_DemoGuru99_0"));

            //Get element in frame by ID
            WebElement frame1 = driver.findElement(By.id("google_ads_iframe_/24132379/INTERSTITIAL_DemoGuru99_0"));
            //Switch to frame with element
            driver.switchTo().frame(frame1);
            //Check button X or Close displays
            List<WebElement> checkButtonX = driver.findElements(By.xpath("//div[@id='dismiss-button']"));
            System.out.println("checkButtonX: " + checkButtonX.size());
            if (checkButtonX.size() > 0) {
                driver.findElement(By.xpath("//div[@id='dismiss-button']")).click();
            } else {
                WebElement frame2 = driver.findElement(By.id("ad_iframe"));
                driver.switchTo().frame(frame2);
                List<WebElement> checkButtonClose = driver.findElements(By.xpath("//div[@id='dismiss-button']//span[normalize-space()='Close']"));
                System.out.println("checkButtonClose: " + checkButtonClose.size());
                if (checkButtonClose.size() > 0) {
                    driver.findElement(By.xpath("//div[@id='dismiss-button']//span[normalize-space()='Close']")).click();
                } else {
                    driver.findElement(By.xpath("//div[@id='dismiss-button']")).click();
                }
            }

            driver.switchTo().defaultContent();
            WebUI.verifyElementVisible(By.xpath("//p[@class='heading3']"));
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
