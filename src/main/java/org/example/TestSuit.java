package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
public class TestSuit {
    protected static WebDriver driver;
    public static void clickOnElement(By by){

        driver.findElement(by).click();
    }
    public static void typeText(By by, String text){

        driver.findElement(by).sendKeys(text);
    }
    public static String getTextFromElement(By by){

        return driver.findElement(by).getText();
    }

    public static long timeStamp(){
        Timestamp timestamp =  new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
    public  static boolean elementIsDisplayed(By by){
      return driver.findElement(by).isDisplayed();
    }
    public static void manageTimeouts(int seconds){
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }
    static String expectedRegistrationCompleteMsg = "Thanks for registration";
    static String expectedCommunityPoleErrorMessage = "Only registered users can vote.";

    @BeforeMethod
    public static void openBrowser(){
        driver = new ChromeDriver();
        driver.get("https://demo.nopcommerce.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    @AfterMethod
    public static void closeBrowser(){

      // driver.close();
    }
    @Test
    public static void verifyUserShouldBeRegisterSuccessfully(){

        //click on register button
        clickOnElement(By.className("ico-register"));

        //type firstname
        typeText(By.id("FirstName" ),"TestFirstName");
        //driver.findElement(By.id("FirstName")).sendKeys("TestFirstName");

        //type lastname
        typeText(By.id("LastName"),"TestLastName");
        //driver.findElement(By.id("LastName")).sendKeys("TestLastName");

        //type email address
        typeText(By.name("Email"),"testemail+"+timeStamp()+"@gmail.com");
        //driver.findElement(By.name("Email")).sendKeys("testemail+"+timestamp.getTime()+"@gmail.com");

        //type password
        typeText(By.id("Password"),"test1234");
        // driver.findElement(By.id("Password")).sendKeys("test1234");

        //type confirm password
        typeText(By.id("ConfirmPassword"),"test1234");
        //driver.findElement(By.id("ConfirmPassword")).sendKeys("test1234");

        //click on register submit button
        clickOnElement(By.id("register-button"));
        //driver.findElement(By.id("register-button")).click();

        //gettext from webelement
        String actualMessage = getTextFromElement(By.xpath("//div[@class='result']"));
        //String actualMessage = driver.findElement(By.xpath("//div[@class='result']")).getText();

        System.out.println("My message:"+actualMessage);
        Assert.assertEquals(actualMessage,expectedRegistrationCompleteMsg,"registration is not completed.");

    }
    @Test
    public static void verifyUserShouldNotVoteWithoutRegistration(){

        //select Good radio button
        clickOnElement(By.xpath("//input[@id='pollanswers-2']"));

        //click on vote button
        clickOnElement(By.id("vote-poll-1"));

        //waiting for element to be displayed
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='block-poll-vote-error-1']")));

        //user should see error message
        String actualErrorMessage = getTextFromElement(By.xpath("//*[@id='block-poll-vote-error-1']"));
        Assert.assertEquals(actualErrorMessage,expectedCommunityPoleErrorMessage);
    }
    @Test
    public static void verifyUserCanVoteSuccessfullyWithRegistration() {

        //click on login button
        clickOnElement(By.className("ico-login"));

        //enter email address
        typeText(By.xpath("//input[@id='Email'] "), "abc123@gmail.com");

        //enter password
        typeText(By.xpath("//input[@id='Password']"), "abc123");

        //click on login button
        clickOnElement(By.xpath("//button[@class='button-1 login-button']"));

        //select Good radio button
        clickOnElement(By.xpath("//input[@id='pollanswers-2']"));

        //click on vote button
        clickOnElement(By.id("vote-poll-1"));

        //user should see pole results
        Assert.assertTrue(elementIsDisplayed(By.xpath("//ul[@class='poll-results'] ")));
        //user should not see error message
        Assert.assertFalse(elementIsDisplayed(By.xpath("//*[@id='block-poll-vote-error-1']")));

    }
    @Test
    public static void verifyRegisteredUserShouldAbleToReferAProductToAFriendSuccessfully(){

        //click on log in button
        clickOnElement(By.className("ico-login"));

        //enter email address
        typeText(By.xpath("//input[@id='Email'] "),"abc123@gmail.com");

        //enter password
        typeText(By.xpath("//input[@id='Password']"),"abc123");

        //click login button
        clickOnElement(By.xpath("//button[@class='button-1 login-button']"));

        //add to cart Apple MacBook Pro 13 inch
        clickOnElement(By.xpath("(//button[@class='button-2 product-box-add-to-cart-button'])[2]"));

        //click on email a friend button
        clickOnElement(By.xpath("//button[@class='button-2 email-a-friend-button']"));

        //enter friend's email address
        typeText(By.xpath("//input[@class='friend-email']"),"hdwhefiuw@gmail.com");

        //enter personal message
        typeText(By.id("PersonalMessage"),"Price of Apple McBook.");

        //click on send email button
        clickOnElement(By.name("send-email"));

        //verify message " your message has been sent."
        String actualMessage = getTextFromElement(By.xpath("//div[@class='result']"));
        String expectedMessage = "Your message has been sent.";
        Assert.assertEquals(actualMessage,expectedMessage);

    }
    @Test
    public static void verifyUserShouldNotAbleToReferAProductToFriendWithoutRegistration(){

        //add to cart Apple MacBook Pro 13 inch
        clickOnElement(By.xpath("(//button[@class='button-2 product-box-add-to-cart-button'])[2]"));

        //click on email a friend button
        clickOnElement(By.xpath("//button[@class='button-2 email-a-friend-button']"));

        //enter friend's email address
        typeText(By.xpath("//input[@class='friend-email']"),"hdwhefiuw@gmail.com");

        //enter your email address
        typeText(By.xpath("//input[@class='your-email']"),"mih3@gmail.com");

        //enter personal message
        typeText(By.id("PersonalMessage"),"Price of Apple McBook.");

        //click on send email button
        clickOnElement(By.name("send-email"));

        //verify error message "Only registered customers can use email a friend feature"
        String actualErrorMessage = getTextFromElement(By.xpath("//*[@class='message-error validation-summary-errors']"));
        String expectedErrorMessage = "Only registered customers can use email a friend feature";
        Assert.assertEquals(actualErrorMessage,expectedErrorMessage);
    }
    @Test
    public static void verifyUserShouldAbleToCompareTwoProducts() {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));

        wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("(//button[@title='Add to compare list'])[3]"))));
        //click add to compare list button for HTC One M8 Android L 5.0 Lollipop
        clickOnElement(By.xpath("(//button[@title='Add to compare list'])[3]"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@title='Add to compare list'])[2]")));
        //click add to compare list button for Apple MacBook Pro 13-inch
        clickOnElement(By.xpath("(//button[@title='Add to compare list'])[2]"));
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//a[contains(@href,'/compareproducts')])[1]")));

        //click on The product has been added to your product comparison
        clickOnElement(By.xpath("(//a[contains(@href,'/compareproducts')])[1]"));

       //print out HTC One M8 Android L 5.0 Lollipop
        String productName1 = getTextFromElement(By.xpath("//a[contains(text(),'HTC One M8 Android L 5.0 Lollipop')]"));
        System.out.println("Product1 name =  " + productName1);

        //print out Apple MacBook Pro 13-inch
        String productName2 = getTextFromElement(By.xpath("//a[contains(text(),'Apple MacBook Pro 13-inch')]"));
        System.out.println("Product2 name =  " + productName2);

        //verifying both products are in compare list
        Assert.assertTrue(elementIsDisplayed(By.xpath("//a[contains(text(),'HTC One M8 Android L 5.0 Lollipop')]")));
        Assert.assertTrue(elementIsDisplayed(By.xpath("//a[contains(text(),'Apple MacBook Pro 13-inch')]")));

        //click on clear list button
        clickOnElement(By.xpath("//a[@class='clear-list']"));

        //print out you have no items to compare
        String expectedMessage = "You have no items to compare.";
        String actualMessage = getTextFromElement(By.xpath("//div[@class='no-data']"));
        System.out.println("My message:"+actualMessage);
        Assert.assertEquals(actualMessage,expectedMessage);
    }
    @Test
    public static void verifyUserShouldAddProductToShoppingCart(){

        //Click on electronics
        clickOnElement(By.xpath("//div/a[@title='Show products in category Electronics']"));

        //select Nikon camera and photos
        clickOnElement(By.xpath("//div/a[@title='Show products in category Camera & photo']"));

        //add camera to cart
        clickOnElement(By.xpath("(//*[text()='Add to cart'])[1]"));
        manageTimeouts(20);

        //again add camera to cart
        clickOnElement(By.xpath("//*[@id='add-to-cart-button-14']"));
        manageTimeouts(20);

        //open shopping cart
        clickOnElement(By.xpath("(//a[contains(@href,'/cart')])[1]"));

        //verify product in cart
        Assert.assertTrue(elementIsDisplayed(By.xpath("(//a[contains(@href,'/nikon-d5500-dslr-black')])[4]")));
    }

}
