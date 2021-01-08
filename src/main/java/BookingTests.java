import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookingTests {

    private WebDriver driver;
    LocalDate checkInDate;
    LocalDate checkOutDate;


    @BeforeMethod
    public void Preconditions() {
        System.setProperty("webdriver.chrome.driver", "src/main/java/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.booking.com/searchresults.html");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(5L, TimeUnit.SECONDS);
        new BookingPage(driver).ClickAcceptCookies();
        new BookingPage(driver).SetDestination("Limerick");
        new BookingPage(driver).SetAdults(2);
        new BookingPage(driver).SetRooms(1);
    }

    @AfterMethod
    public void Cleanup() {
        driver.close();
        driver.quit();
    }

    @Test
    public void Star5TheSavoyHotelListed() {
        checkInDate = LocalDate.now().plusMonths(3);
        checkOutDate = checkInDate.plusDays(1);
        new BookingPage(driver).SetDates(checkInDate, checkOutDate);
        new BookingPage(driver).ClickSearch();
        new BookingPage(driver).FilterByStars(5);
        List<String> result = new BookingPage(driver).resultHotels();
        Assert.assertTrue(result.contains("The Savoy Hotel"));
    }

    @Test
    public void Star5GeorgeLimerickHotelNotListed() {
        checkInDate = LocalDate.now().plusMonths(3);
        checkOutDate = checkInDate.plusDays(1);
        new BookingPage(driver).SetDates(checkInDate, checkOutDate);
        new BookingPage(driver).ClickSearch();
        new BookingPage(driver).FilterByStars(5);
        List<String> result = new BookingPage(driver).resultHotels();
        Assert.assertFalse(result.contains("George Limerick Hotel"));
    }

    @Test
    public void SaunaLimerickStrandHotelListed() {
        checkInDate = LocalDate.now().plusDays(3);
        checkOutDate = checkInDate.plusDays(1);
        new BookingPage(driver).SetDates(checkInDate, checkOutDate);
        new BookingPage(driver).ClickSearch();
        new BookingPage(driver).FilterByFunThingsToDo("Sauna");
        List<String> result = new BookingPage(driver).resultHotels();
        Assert.assertTrue(result.contains("Limerick Strand Hotel"));
    }

    @Test
    public void SaunaGeorgeLimerickHotelNotListed() {
        checkInDate = LocalDate.now().plusDays(3);
        checkOutDate = checkInDate.plusDays(1);
        new BookingPage(driver).SetDates(checkInDate, checkOutDate);
        new BookingPage(driver).ClickSearch();
        new BookingPage(driver).FilterByFunThingsToDo("Sauna");
        List<String> result = new BookingPage(driver).resultHotels();
        Assert.assertFalse(result.contains("George Limerick Hotel"));
    }

}


