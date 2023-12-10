package com.anhtester.handles.HandleTinyMCE;

import com.anhtester.WebUI;
import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class CRM_TinyCME extends BaseTest {

    //Test case auto set text trong Tiny MCE
    @Test
    public void testSetTextOnTinyMCE(){
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

        //Tiny MCE nằm trong iframe nên cần switch vào frame
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='description_ifr']")));
        WebUI.sleep(1);
        //Nó chỉ có thẻ body/p
        driver.findElement(By.xpath("//body[@id='tinymce']/p")).click();
        WebUI.sleep(1);
        driver.findElement(By.xpath("//body[@id='tinymce']/p")).sendKeys("Anh Tester");
        WebUI.sleep(2);

        //Thoát khỏi frame
        driver.switchTo().parentFrame();
        driver.findElement(By.xpath("//input[@id='send_created_email']")).click();
        WebUI.sleep(2);
    }

}
