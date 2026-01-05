package com.anhtester.cms.pages;

import com.anhtester.keywords.WebUI;
import org.openqa.selenium.By;

public class LoginPage {
    // URL
    private String URL = "https://cms.anhtester.com/login";

    // Locators
    private By headerPage = By.xpath("//h1[normalize-space()='Welcome to Active eCommerce CMS']");
    private By inputEmail = By.id("email");
    private By inputPassword = By.id("password");
    private By buttonLogin = By.xpath("//button[normalize-space()='Login']");

    // Methods
    public void login(String email, String password) {
        WebUI.openURL(URL);
        WebUI.waitForPageLoaded();
        WebUI.setText(inputEmail, email);
        WebUI.setText(inputPassword, password);
        WebUI.clickElement(buttonLogin);
        WebUI.waitForPageLoaded();
    }
}
