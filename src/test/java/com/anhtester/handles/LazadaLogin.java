package com.anhtester.handles;

import com.anhtester.keywords.WebUI;
import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class LazadaLogin extends BaseTest {
    @Test
    public void loginLazada() {
        driver.get("https://lazada.vn");
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//a[normalize-space()='login']")).click();
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//label[normalize-space()='Phone Number or Email']/following-sibling::input")).sendKeys("0398800615");
        driver.findElement(By.xpath("//label[normalize-space()='Password']/following-sibling::input")).sendKeys("123456789");

        WebUI.sleep(3);

        WebElement slider = driver.findElement(By.xpath("//div[@class='mod-login']//span[@class='nc_iconfont btn_slide']"));

        Actions actions = new Actions(driver);
        actions.moveToElement(slider).clickAndHold().pause(1).moveByOffset(300, 0).pause(1).release().perform();

        WebUI.sleep(10);
    }
}
