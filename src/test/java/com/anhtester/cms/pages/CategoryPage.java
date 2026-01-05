package com.anhtester.cms.pages;

import com.anhtester.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;

public class CategoryPage {

    // Locators for Menu
    private By menuProducts = By.xpath("//span[normalize-space()='Products']");
    private By menuCategory = By.xpath("//span[normalize-space()='Category']");

    // Locators for Category Page
    private By buttonAddNewCategory = By.xpath("//a[@class='btn btn-primary']");
    private By headerAddCategory = By.xpath("//h5[normalize-space()='Category Information']");
    private By inputName = By.id("name");
    // Parent Category dropdown search input (if needed, but simple add might not
    // need it for root category)
    private By buttonSave = By.xpath("//button[@type='submit']");

    // Locators for Category List
    private By inputSearch = By.id("search");
    private By firstCategoryName = By.xpath("//tbody/tr[1]/td[2]"); // Assuming the list sorts by latest or we search
                                                                    // exact match

    // Methods
    public void openCategoryPage() {
        WebUI.clickElement(menuProducts);
        WebUI.clickElement(menuCategory);
        WebUI.waitForPageLoaded();
    }

    public void addCategory(String categoryName) {
        WebUI.clickElement(buttonAddNewCategory);
        WebUI.waitForPageLoaded();
        WebUI.verifyElementVisible(headerAddCategory);

        WebUI.setText(inputName, categoryName);
        // Add more fields if necessary, currently minimal requirement

        WebUI.clickElement(buttonSave);
        WebUI.waitForPageLoaded();
    }

    public void checkCategoryExists(String categoryName) {
        // Search for the category
        WebUI.setText(inputSearch, categoryName);
        WebUI.getDriver().findElement(inputSearch).sendKeys(Keys.ENTER); // Need to press Enter or wait for auto search
        WebUI.sleep(2); // Wait for search results

        // Assert the first result matches
        String actualName = WebUI.getTextElement(firstCategoryName);
        System.out.println("Found Category: " + actualName);
        Assert.assertEquals(actualName, categoryName, "Category name does not match!");
    }
}
