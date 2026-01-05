package com.anhtester.cms;

import com.anhtester.cms.pages.CategoryPage;
import com.anhtester.cms.pages.LoginPage;
import com.anhtester.common.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddCategory extends BaseTest {

    // DataProvider to supply category names
    @DataProvider(name = "categoryData")
    public Object[][] getCategoryData() {
        return new Object[][] {
                { "Mobile Phones " + System.currentTimeMillis() }, // Dynamic name to avoid duplicates
                { "Laptops " + System.currentTimeMillis() }
        };
    }

    @Test(dataProvider = "categoryData")
    public void testAddNewCategory(String categoryName) {
        // 1. Init Pages
        LoginPage loginPage = new LoginPage();
        CategoryPage categoryPage = new CategoryPage();

        // 2. Login
        // Note: Using the credentials provided in the request
        loginPage.login("admin@example.com", "123456");

        // 3. Navigate to Category Page
        categoryPage.openCategoryPage();

        // 4. Add New Category
        categoryPage.addCategory(categoryName);

        // 5. Verify Category Exists
        categoryPage.checkCategoryExists(categoryName);
    }
}
