package com.anhtester;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class HandleChatboxMessenger extends BaseTest {
    @Test
    public void handleChatboxMessenger() {
        driver.navigate().to("https://anhtester.com/contact");
        sleep(10);
        System.out.println("iframe total: " + driver.findElements(By.tagName("iframe")).size());
        //----Switch to content of Messenger--------
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@data-testid='dialog_iframe']")));
        //Get title
        System.out.println(driver.findElement(By.xpath("//strong")).getText());
        //Get description
        System.out.println(driver.findElement(By.xpath("(((//strong/parent::div)/parent::div)/following-sibling::div)[2]")).getText());

        //----Switch to icon of Messenger---------
        //1. Switch to Parent WindowHandle
        driver.switchTo().parentFrame();
        //2. Switch to iframe icon of Messenger
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@data-testid='bubble_iframe']")));
        //Nhấn icon để ẩn messenger chat đi
        driver.findElement(By.tagName("svg")).click();
        sleep(2);
    }
}
