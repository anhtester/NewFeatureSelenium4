package com.anhtester.handles.HandleClearText;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class OrangeHRM extends BaseTest {

    @Test
    public void updateUser() throws InterruptedException {
//        WebDriver driver;
//        driver = new ChromeDriver();
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://opensource-demo.orangehrmlive.com/");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//span[text()='Admin']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[normalize-space()='Username']/parent::div/following-sibling::div/input")).sendKeys("admin");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//i[@class='oxd-icon bi-pencil-fill']/parent::button")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[normalize-space()='Username']/parent::div/following-sibling::div/input")).click(); //Edit button
        Thread.sleep(1000);

        //driver.findElement(By.xpath("//label[normalize-space()='Username']/parent::div/following-sibling::div/input")).clear(); //Input Username

        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).keyDown("a").keyUp(Keys.CONTROL).keyUp("a").build().perform();
        actions.keyDown(Keys.DELETE).build().perform();

        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[normalize-space()='Username']/parent::div/following-sibling::div/input")).sendKeys("Linhnmbt");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();
        Thread.sleep(1000);
    }
}
