import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class RunOnGrid {

    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

    @Parameters({"ipAddress", "portNumber"})
    @BeforeMethod
    public void setDriver(String ipAddress, String portNumber) throws MalformedURLException {
        String HUB_URL = "http://192.168.77.1:4444/wd/hub";
        //HUB_URL = "http://localhost:4444/wd/hub";
        String NODE_URL = "http://" + ipAddress + ":" + portNumber + "";

        DesiredCapabilities capabilities = capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setPlatform(Platform.WINDOWS);
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);
        driver.set(new RemoteWebDriver(new URL(String.format(HUB_URL, NODE_URL)), options));
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod
    public void closeBrowser() {
        driver.get().quit();
        driver.remove();
    }

    @Test
    public void testGrid01() {
        getDriver().get("https://anhtester.com");
        Assert.assertEquals(getDriver().getTitle(), "Anh Tester Automation Testing");
    }

    @Test
    public void testGrid02() throws InterruptedException {
        getDriver().get("https://crm.anhtester.com/admin/authentication");
        getDriver().findElement(By.xpath("//input[@id='email']")).sendKeys("admin@example.com");
        getDriver().findElement(By.xpath("//input[@id='password']")).sendKeys("123456");
        getDriver().findElement(By.xpath("//button[normalize-space()='Login']")).click();
        Thread.sleep(3000);
        Assert.assertEquals(getDriver().getTitle(), "Dashboard", "Title not match.");
    }

    @Test
    public void TC2_NewTab() {
        getDriver().get("https://anhtester.com");

        getDriver().switchTo().newWindow(WindowType.TAB);

        getDriver().get("https://google.com");
    }

    @Test
    public void TC3_NewWindow() {
        getDriver().get("https://anhtester.com");

        getDriver().switchTo().newWindow(WindowType.WINDOW);

        getDriver().get("https://google.com");
    }
} 