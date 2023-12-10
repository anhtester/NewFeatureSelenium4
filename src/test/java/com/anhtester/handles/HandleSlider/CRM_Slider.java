package com.anhtester.handles.HandleSlider;

import com.anhtester.WebUI;
import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class CRM_Slider extends BaseTest {

    //Test case auto click chọn thanh trượt theo phần trăm
    @Test
    public void testSelectSliderByPercent() {
        driver.get("https://crm.anhtester.com/admin/authentication");
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//span[normalize-space()='Projects']")).click();
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//a[normalize-space()='New Project']")).click();
        WebUI.waitForPageLoaded();

        driver.findElement(By.xpath("//input[@id='progress_from_tasks']")).click();
        WebUI.sleep(1);

        WebElement inputSliderBefore = driver.findElement(By.xpath("//input[@name='progress']"));
        System.out.println("Attribute Slider trước khi set: " + inputSliderBefore.getAttribute("value"));

        //Dùng JS để set giá trị cho thuộc tính style="left: 20%;"
        //Để hiển thị trên giao diện tại 20%
        WebElement elementSlider = driver.findElement(By.xpath("//span[@class='ui-slider-handle ui-corner-all ui-state-default']"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'left: 20%;')", elementSlider);

        //Set giá trị value cho ô input thì khi submit mới lưu giá trị đúng
        WebElement inputSlider = driver.findElement(By.xpath("//input[@name='progress']"));
        js.executeScript("arguments[0].setAttribute('value', '20')", inputSlider);
        WebUI.sleep(1);
        WebElement inputSliderAfter = driver.findElement(By.xpath("//input[@name='progress']"));
        System.out.println("Attribute Slider sau khi set: " + inputSlider.getAttribute("value"));

    }
}
