import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class AddCookies {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://anhtester.com/");

        // Adds the cookie into current browser context
        driver.manage().addCookie(new Cookie("ci_session", "9a44c54bb8ee51ea46571ab6309b591519320740"));
        Thread.sleep(2000);
        driver.navigate().refresh();
        Thread.sleep(5000);

        driver.quit();
    }
}
