package com.anhtester.handles.HandleShadowDOM;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HandleMultiShadowRoot extends BaseTest {

    @Test
    public void handleShadowDOM_01() {
        driver.get("http://watir.com/examples/shadow_dom.html");

        WebElement shadowHost = driver.findElement(By.cssSelector("#shadow_host"));
        
        //Get shadow root 1st
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        WebElement shadowContent = shadowRoot.findElement(By.cssSelector("#shadow_content"));

        Assert.assertEquals("some text", shadowContent.getText());
    }

    @Test
    public void handleShadowDOM_02() {
        driver.get("http://watir.com/examples/shadow_dom.html");

        WebElement shadowHost = driver.findElement(By.cssSelector("#shadow_host"));

        //Get shadow root 1st
        SearchContext shadowRootOne = shadowHost.getShadowRoot();
        WebElement shadowContent = shadowRootOne.findElement(By.cssSelector("#shadow_content"));
        Assert.assertEquals("some text", shadowContent.getText());

        //Get shadow root 2nd
        WebElement nestedShadowContent = shadowRootOne.findElement(By.cssSelector("#nested_shadow_host"));
        SearchContext shadowRootTwo = nestedShadowContent.getShadowRoot();
        String nestedText = shadowRootTwo.findElement(By.cssSelector("#nested_shadow_content > div")).getText();

        Assert.assertEquals("nested text", nestedText);
    }

}
