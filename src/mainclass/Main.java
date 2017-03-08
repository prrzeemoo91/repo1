package mainclass;

import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Przemo on 2017-02-28.
 */
public class Main {

    WebDriver driver;
    Pomoc tester;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Przemo\\Documents\\geckodriver-v0.14.0-win32\\geckodriver.exe");

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        WebDriver driver = new FirefoxDriver(capabilities);

        driver.get("http://google.pl");
        driver.manage().window().maximize();

        tester = new Pomoc(driver);
    }

    @Test
    public void metoda() throws InterruptedException {
        tester.waitForElementName("q");
        tester.searchBox.sendKeys("selenium");
        tester.searchButton.click();
        Thread.sleep(4000);
        driver.quit();
    }
}
