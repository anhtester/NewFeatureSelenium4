package com.anhtester.handles.HandleHTML5Message;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DemoHandleHTML5Message extends BaseTest {
    @Test
    public void testGetHTML5Message() {
        driver.get("https://cms.anhtester.com/login");

        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        sleep(1);

        WebElement inputEmail = driver.findElement(By.xpath("//input[@id='email']"));

        //Get HTML5 Message on input
        String validationMessage = inputEmail.getAttribute("validationMessage");
        System.out.println(validationMessage);

        Assert.assertEquals(validationMessage, "Please fill out this field.", "The validation message of Email field not match.");
    }

    @Test
    public void testCheckHTML5HasRequiredMessage() {
        driver.get("https://cms.anhtester.com/login");

        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        sleep(1);

        WebElement inputEmail = driver.findElement(By.xpath("//input[@id='email']"));

        //Check HTML5 Has Required Message
        System.out.println("Required: " + ((JavascriptExecutor) driver).executeScript("return arguments[0].required;", inputEmail));
        Assert.assertTrue((Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].required;", inputEmail), "Email not required field.");
    }

    @Test
    public void testCheckHTML5MessageWithValueValid() {
        driver.get("https://cms.anhtester.com/login");

        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        sleep(1);

        WebElement inputEmail = driver.findElement(By.xpath("//input[@id='email']"));

        inputEmail.sendKeys("admin@example.com");
        sleep(2);

        //Check HTML5 Message With Value Valid - True
        System.out.println("Check Valid: " + ((JavascriptExecutor) driver).executeScript("return arguments[0].validity.valid;", inputEmail));
        Assert.assertTrue((Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].validity.valid;", inputEmail), "Email value not valid.");

        inputEmail.clear();
        inputEmail.sendKeys("admin");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        sleep(2);

        //Check HTML5 Message With Value Valid - False
        System.out.println("Check Valid: " + ((JavascriptExecutor) driver).executeScript("return arguments[0].validity.valid;", inputEmail));
        Assert.assertTrue((Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].validity.valid;", inputEmail), "Email value not valid.");
    }
}
