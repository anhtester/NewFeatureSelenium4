package com.anhtester.handles.HandlePopupWindow;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.Set;

public class HandlePopupWindow extends BaseTest {

    @Test
    public void demoNotSwitchToTab() {
        driver.get("https://demoqa.com/browser-windows");
        sleep(2);
        driver.findElement(By.xpath("//button[@id='tabButton']")).click();
        sleep(1);
        //Sau khi chuyển hướng sang Tab mới thì getText cái header
        System.out.println(driver.findElement(By.xpath("//h1[@id='sampleHeading']")).getText());
        sleep(1);
    }

    @Test
    public void demoPopupTypeTab() {
        driver.get("https://demoqa.com/browser-windows");
        sleep(2);
        driver.findElement(By.xpath("//button[@id='tabButton']")).click();
        sleep(1);
        // Lưu lại lớp window đầu tiên - mã ID hơi dài, in ra sẽ thấy :)
        String MainWindow = driver.getWindowHandle();
        System.out.println(MainWindow);

        // Lấy tất cả các mã định danh Tab Window.
        Set<String> windows = driver.getWindowHandles();

        //Set là một Collection để lưu các phần tử giá trị KHÔNG trùng lặp.
        //Cách duyệt từng phần tử không trùng lặp trong Collection (Set) - Java Basic
        for (String window : windows) {
            System.out.println(window);
            if (!MainWindow.equals(window)) {
                //So sánh nếu thằng nào khác thằng Chính (đầu tiên) thì chuyển hướng qua nó mới thao tác được
                //Chuyển hướng driver đến Tab mới (Tab con)
                driver.switchTo().window(window);
                sleep(1);
                System.out.println("Đã chuyển đến Tab Window mới");

                //Một số hàm hỗ trợ
                System.out.println(driver.switchTo().window(window).getTitle());
                System.out.println(driver.switchTo().window(window).getCurrentUrl());

                sleep(1);
                //Sau khi chuyển hướng sang Tab mới thì getText cái header
                System.out.println(driver.findElement(By.xpath("//h1[@id='sampleHeading']")).getText());

                // Tắt cái Tab Window mới.
                sleep(1);
                driver.close();
            }
        }
        // Chuyển hướng về lại tab chính ban đầu (Main Window)
        driver.switchTo().window(MainWindow);
        System.out.println("Đã chuyển về lớp Window chính: " + driver.getCurrentUrl());

        sleep(1);
    }
}
