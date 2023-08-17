package com.anhtester.handles.HandleDragAndDrop;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.InputEvent;

public class HandleDragAndDrop extends BaseTest {

    public boolean dragAndDropHTML5(WebElement fromElement, WebElement toElement) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(0, 0);

            int X1 = fromElement.getLocation().getX() + (fromElement.getSize().getWidth() / 2);
            int Y1 = fromElement.getLocation().getY() + (fromElement.getSize().getHeight() / 2);
            System.out.println(X1 + " , " + Y1);

            int X2 = toElement.getLocation().getX() + (toElement.getSize().getWidth() / 2);
            int Y2 = toElement.getLocation().getY() + (toElement.getSize().getHeight() / 2);
            System.out.println(X2 + " , " + Y2);

            //Chổ này lấy toạ độ hiện tại cộng thêm 120px là phần header của browser (1920x1080 current window)
            //Header: chrome is being controlled by automated test software
            sleep(1);
            robot.mouseMove(X1, Y1 + 190); //Tuỳ độ phân giải của màn hình
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            sleep(1);
            robot.mouseMove(X2, Y2 + 150);
            sleep(1);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Test
    public void demoDragAndDropHTML5() {
        driver.get("https://ui.vision/demo/webtest/dragdrop/");
        sleep(2);

        // Bắt element cần kéo
        WebElement From = driver.findElement(By.xpath("//a[@id='one']"));
        // Bắt element cần thả đến
        WebElement To = driver.findElement(By.xpath("//div[@id='bin']"));

        sleep(1);
        //Gọi lại hàm dragAndDropHTML5 đã xây dựng riêng
        dragAndDropHTML5(From, To);

        sleep(2);
    }

    @Test
    public void demoDragAndDropWithActionClass() {
        driver.get("http://www.dhtmlgoodies.com/scripts/drag-drop-custom/demo-drag-drop-3.html");
        sleep(1);

        // Bắt element cần kéo
        WebElement From = driver.findElement(By.xpath("//div[@id='box6']"));
        // Bắt element cần thả đến
        WebElement To = driver.findElement(By.xpath("//div[@id='countries']//div[1]"));

        sleep(1);
        Actions action = new Actions(driver);
        // Kéo và thả
        action.dragAndDrop(From, To).build().perform();

        sleep(2);
    }
}
