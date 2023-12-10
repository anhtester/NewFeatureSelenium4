package com.anhtester.handles.HandleDropdown;

import com.anhtester.WebUI;
import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class CRM_Dropdown_TaskPage extends BaseTest {

    //Test case auto click chọn dropdown kiểu khác
    @Test
    public void testSelectDropdown() {
        driver.get("https://crm.anhtester.com/admin/authentication");
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//span[normalize-space()='Tasks']")).click();
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//a[normalize-space()='New Task']")).click();
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);

        //Dropdown "Priority"
        driver.findElement(By.xpath("//label[@for='priority']/following-sibling::div")).click();
        WebUI.sleep(1);
        driver.findElement(By.xpath("//span[normalize-space()='High']")).click();
        WebUI.sleep(2);

        //Dropdown "Repeat every"
        driver.findElement(By.xpath("//label[normalize-space()='Repeat every']/following-sibling::div")).click();
        WebUI.sleep(1);
        driver.findElement(By.xpath("//span[normalize-space()='3 Months']")).click();
        WebUI.sleep(2);

        //Dropdown "Related To"
        driver.findElement(By.xpath("//label[normalize-space()='Related To']/following-sibling::div")).click();
        WebUI.sleep(1);
        driver.findElement(By.xpath("//span[@class='text'][normalize-space()='Estimate']")).click();
        WebUI.sleep(2);
    }
}
