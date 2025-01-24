package com.anhtester.keywords;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class WebUI {

    private static WebDriver driver;

    public WebUI(WebDriver _driver) {
        driver = _driver;
    }

    public static void sleep(double second) {
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pressDownKey(int totalPress) {
        for (int i = 1; i <= totalPress; i++) {
            new Actions(driver)
                    .scrollByAmount(0, 500)
                    .perform();
            sleep(1);
        }
    }

    public static Boolean checkElementExist(By by) {
        List<WebElement> listElement = driver.findElements(by);

        if (listElement.size() > 0) {
            System.out.println("Element existing: " + true);
            return true;
        } else {
            System.out.println("Element existing: " + false);
            return false;
        }
    }

    public static void waitForElementToBeClickable(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Throwable error) {
            System.out.println("Timeout waiting for the element to be clickable. " + by.toString());
            Assert.fail("Timeout waiting for the element to be clickable. " + by.toString());
        }
    }

    public static void waitForElementVisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofMillis(500));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            System.out.println("Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
        }
    }

    public static boolean verifyElementVisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofMillis(500));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (NoSuchElementException error) {
            error.printStackTrace();
            System.out.println("Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
        }
        return false;
    }

    public static void waitForPageLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");

        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");
        if (!jsReady) {
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                error.printStackTrace();
                Assert.fail("Timeout waiting for page load (Javascript)");
            }
        }
    }

    public static void clickElement(By by) {
        waitForElementToBeClickable(by);
        driver.findElement(by).click();
    }

    public static void setText(By by, String text) {
        waitForElementVisible(by);
        driver.findElement(by).sendKeys(text);
    }

    public static void clickElementStale(By by) {
        waitForPageLoaded();
        for (int i = 0; i < 5; i++) {
            try {
                driver.findElement(by).click();
                break; // Exit loop if successful
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(1000); // Wait and try again
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } catch (StaleElementReferenceException e) {
                try {
                    Thread.sleep(1000); // Wait and try again
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (ElementClickInterceptedException e) {
                try {
                    Thread.sleep(1000); // Wait and try again
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public static void clickElementStale(By by, int second) {
        waitForPageLoaded();
        for (int i = 0; i < second; i++) {
            try {
                driver.findElement(by).click();
                break; // Exit loop if successful
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(1000); // Wait and try again
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } catch (StaleElementReferenceException e) {
                try {
                    Thread.sleep(1000); // Wait and try again
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (ElementClickInterceptedException e) {
                try {
                    Thread.sleep(1000); // Wait and try again
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public static void setTextElementStale(By by, String text) {
        waitForPageLoaded();
        waitForElementVisible(by);
        for (int i = 0; i < 5; i++) {
            try {
                driver.findElement(by).sendKeys(text);
                break; // Exit loop if successful
            } catch (StaleElementReferenceException e) {
                try {
                    Thread.sleep(500); // Wait and try again
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
