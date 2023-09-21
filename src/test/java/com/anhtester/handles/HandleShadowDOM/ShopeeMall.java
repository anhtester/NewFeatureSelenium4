package com.anhtester.handles.HandleShadowDOM;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ShopeeMall extends BaseTest {

    @Test
    public void handleShadowDOM() {
        driver.get("https://shopee.vn/mall");

        WebElement shadowHost = driver.findElement(By.cssSelector("shopee-banner-popup-stateful[spacekey='PC-VN-MALL_POPUP']"));
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        WebElement shadowContent = shadowRoot.findElement(By.cssSelector(".shopee-popup__close-btn"));
        shadowContent.click();
    }
}
