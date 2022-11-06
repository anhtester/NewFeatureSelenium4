import com.google.common.util.concurrent.Uninterruptibles;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.devtools.v107.network.model.Headers;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class NewFeatureSelenium4 {

    private WebDriver driver;

    @BeforeMethod
    public void createDriver() {
        //System.setProperty("webdriver.chrome.driver","D:\\chromedriver.exe");
        //WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void TC1_ScreenshotElement() {
        driver.get("https://anhtester.com");

        WebElement logo = driver.findElement(By.xpath("//a[@class='logo']"));

        File file = logo.getScreenshotAs(OutputType.FILE);
        File destFile = new File("logo.png");

        try {
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TC2_NewTab() {
        driver.get("https://anhtester.com");

        driver.switchTo().newWindow(WindowType.TAB);

        driver.get("https://google.com");
    }

    @Test
    public void TC3_NewWindow() {
        driver.get("https://anhtester.com");

        driver.switchTo().newWindow(WindowType.WINDOW);

        driver.get("https://google.com");
    }

    @Test
    public void TC4_ObjectLocation() {
        driver.get("https://anhtester.com");

        WebElement element = driver.findElement(By.xpath("//a[@id='btn-login']"));

        System.out.println("Height is: " + element.getRect().getDimension().getHeight());
        System.out.println("Width is: " + element.getRect().getDimension().getWidth());
        System.out.println("X location: " + element.getRect().getX());
        System.out.println("Y location: " + element.getRect().getY());
    }

    @Test
    public void TC5_RelativeLocators() throws InterruptedException {
        driver.get("https://rise.fairsketch.com/signin");

        WebElement password = driver.findElement(By.id("password"));
        WebElement email = driver.findElement(RelativeLocator.with(By.tagName("input")).above(password));

        email.clear();
        email.sendKeys("anhtester@mailinator.com");

        Thread.sleep(2000);

        driver.get("https://anhtester.com");

        WebElement APITesting = driver.findElement(By.xpath("//h3[normalize-space()='API Testing']"));
        WebElement WebsiteTesting = driver.findElement(RelativeLocator.with(By.tagName("h3")).toLeftOf(APITesting));
        System.out.println(WebsiteTesting.getText());
    }

    @Test
    public void TC6_Authentication() {
        // Authentication username & password
        String url = "https://the-internet.herokuapp.com/basic_auth";
        String username = "admin";
        String password = "admin";

        // Get the devtools from the running driver and create a session
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Enable the Network domain of devtools
        devTools.send(Network.enable(java.util.Optional.of(100000), java.util.Optional.of(100000), java.util.Optional.of(100000)));
        String auth = username + ":" + password;

        // Encoding the username and password using Base64 (java.util)
        String encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());

        // Pass the network header -> Authorization : Basic <encoded String>
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodeToString);
        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));

        // Load the application url
        driver.get(url);
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));

        String successFullyLoggedInText = driver.findElement(By.xpath("//p")).getText();
        System.out.println(successFullyLoggedInText);
    }

    @AfterMethod
    public void closeDriver() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.quit();
    }

}
