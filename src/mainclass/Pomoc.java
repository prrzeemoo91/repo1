package mainclass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.UUID;

/**
 * Created by Przemo on 2017-03-06.
 */
public class Pomoc {

    WebDriver driver;
    String id = "";

    public Pomoc(WebDriver driver) {
        this.driver = driver;
        id = UUID.randomUUID().toString();
        System.out.println(id);
        PageFactory.initElements(driver, this);
    }

    public void waitForElementName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
    }

    @FindBy(how = How.NAME, using = "q")
    public WebElement searchBox;

    @FindBy(name = "btnG")
    public WebElement searchButton;
}
