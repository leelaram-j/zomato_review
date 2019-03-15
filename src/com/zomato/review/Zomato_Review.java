package com.zomato.review;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.txt.read.Txt_Read;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Zomato_Review
{
    static WebDriver driver;

    public static String tempEmailIdFetch(WebDriver driver)
    {

        driver.get("https://getnada.com");
        String emailId = driver.findElement(By.xpath("//h1[@class='the_mail']/span[2]")).getText();
        return emailId;
    }

    public static void zomatoRegister(WebDriver driver,String fullName)
    {
        driver.manage().deleteAllCookies();
        driver.get("https://www.zomato.com/chennai");
        driver.findElement(By.id("signup-link")).click();
        driver.findElement(By.xpath("//a[@id='signup-email']")).click();
        String parentWindow = driver.getWindowHandle();
        driver.findElement(By.id("sd-fullname")).sendKeys(Keys.CONTROL + "t");
        String mailId=tempEmailIdFetch(driver);
        driver.switchTo().window(parentWindow);
        driver.findElement(By.id("sd-fullname")).sendKeys(mailId);
        driver.findElement(By.id("sd-email")).sendKeys(fullName);
        driver.findElement(By.id("sd-newsletter")).click();
        driver.findElement(By.id("sd-submit")).click();
        String otp=otpRetrieve(driver,parentWindow);
        driver.findElement(By.id("verification-code-value")).sendKeys(otp);
        driver.findElement(By.xpath("//div[@class='signup-verif-form']/a/span")).click();
    }

    public static String otpRetrieve(WebDriver driver,String parentWindow)
    {
        for(String childWindow:driver.getWindowHandles())
        {
            if (!childWindow.equals(parentWindow))
            {
                driver.switchTo().window(childWindow);
                break;
            }
        }
        //Email delivery timer adjuster
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//div[@class='subject ']")));
        driver.findElement(By.xpath("//div[@class='subject ']")).click();
        String otp=driver.findElement(By.xpath("//div[@class='coupon-box']/p[2]")).getText();
        driver.close();
        driver.switchTo().window(parentWindow);
        return otp;
    }

    public static void shopLocate(WebDriver driver)
    {
        driver.get("https://www.zomato.com/chennai/citruse-purasavakkam");
    }

    public static void writeReview(WebDriver driver,String rating,String review)
    {
        driver.findElement(By.xpath("//div[@id='quick_review_initial']/button")).click();
        driver.findElement(By.xpath("//div[@id='review-form']/div/a["+rating+"]")).click();
        driver.findElement(By.id("review-form-textarea-id")).sendKeys(review);
        driver.findElement(By.xpath("//div[@class='col-l-7']/a")).click();

    }

    public static void main(String [] args)
    {
        System.setProperty("webdriver.chrome.driver","//Users//lee//Downloads//chromedriver");
        String inFilename="//Users//lee//Documents//sample.txt";
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        String fullName;
        String review;
        String rating;
        try
        {
            String[] lines = Txt_Read.txtRead(inFilename);
            for (String line : lines)
            {
                String[] arrOfStr = line.split("|");
                fullName = arrOfStr[0];
                rating = arrOfStr[1];
                review = arrOfStr[2];
                zomatoRegister(driver,fullName);
                shopLocate(driver);
                writeReview(driver,rating,review);
            }
        }
        catch (ElementNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        driver.quit();

    }
}
