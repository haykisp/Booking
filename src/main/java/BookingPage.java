import com.google.common.collect.Lists;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingPage {

    private WebDriver driver;

    public BookingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "onetrust-accept-btn-handler")
    public WebElement buttonAcceptCookies;

    @FindBy(id = "ss")
    public WebElement inputDestination;

    @FindBy(xpath = "//div[@data-placeholder=\"Check-in Date\"]")
    public WebElement calendarCheckInDate;

    @FindBy(xpath = "//div[@data-placeholder=\"Check-out Date\"]")
    public WebElement calendarCheckOutDate;

    @FindBy(id = "group_adults")
    public WebElement selectAdults;

    @FindBy(id = "group_children")
    public WebElement selectChildren;

    @FindBy(id = "no_rooms")
    public WebElement selectRooms;

    @FindBy(xpath = "//button[@class=\"sb-searchbox__button \"]")
    public WebElement buttonSearch;

    @FindBy(xpath = "//div[@data-bui-ref=\"calendar-next\"]")
    public WebElement buttonCalendarNextPage;

    public WebElement checkBoxHotelStars(Integer stars) {
        return driver.findElement(By.xpath("//div[@id=\"filter_class\"]//a[@data-id=\"class-" + stars + "\"]"));

    }

    public WebElement checkBoxFunThingsToDo(String activity) {
        return driver.findElement(By.xpath("//span[contains(text(),\"" + activity + "\")]/ancestor::a[@data-name=\"popular_activities\"]"));
    }

    public WebElement itemInDestinationList(String destination) {
        return driver.findElement(By.xpath("//li//span[text()='" + destination + "']"));
    }

    public WebElement calendarDate(String date) {
        return driver.findElement(By.xpath("//td[@data-date=\"" + date + "\"]"));
    }

    public WebElement itemInAdultsList(Integer adults) {
        return driver.findElement(By.xpath("//select[@id=\"group_adults\"]/option[@value=\"" + adults + "\"]"));
    }

    public WebElement itemInChildrenList(Integer children) {
        return driver.findElement(By.xpath("//select[@id=\"group_children\"]/option[@value=\"" + children + "\"]"));
    }

    public WebElement itemInRoomsList(Integer rooms) {
        return driver.findElement(By.xpath("//select[@id=\"no_rooms\"]/option[@value=\"" + rooms + "\"]"));
    }

    public WebElement filtersShowAll(String filtersGroupId) {
        return driver.findElement(By.xpath("//div[@id=\"" + filtersGroupId + "\"]//button[contains(text(),\"Show all\")]"));
    }

    public List<String> resultHotels() {
        new WebDriverWait(driver, 10).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript(("return (document.readyState == 'complete' && jQuery.active == 0)")).toString().equals("true"));

        List<WebElement> results = driver.findElements(By.xpath("//span[contains(@class,\"sr-hotel__name\")]"));
        List<String> hotels = Lists.newArrayList();
        for (int i = 0; i < results.size(); i++) {
            hotels.add(results.get(i).getText());
        }
        return hotels;
    }


    public void ClickAcceptCookies() {
        if (buttonAcceptCookies.isDisplayed()) {
            buttonAcceptCookies.click();
        }
    }

    public void ClickCalendarNextPage() {
        buttonCalendarNextPage.click();
    }


    public void SetDestination(String destination) {
        inputDestination.sendKeys(destination);
        itemInDestinationList(destination).click();
    }

    public void SetDates(LocalDate checkIn, LocalDate checkOut) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (checkIn.compareTo(LocalDate.now()) < 0) {
            Assert.fail(">>>>>>>>>> Check-in date is past date !!! ");
        }
        if (checkOut.compareTo(checkIn) < 0) {
            Assert.fail(">>>>>>>>>> Check-out date is before check-in date !!! ");
        }

        long diff = ChronoUnit.MONTHS.between(YearMonth.from(LocalDate.now()), YearMonth.from(checkIn));

        calendarCheckInDate.click();
        for (int i = 0; i < diff; i++) {
            ClickCalendarNextPage();
        }
        calendarDate(dtf.format(checkIn)).click();

        diff = ChronoUnit.MONTHS.between(YearMonth.from(checkIn), YearMonth.from(checkOut));
        calendarCheckOutDate.click();
        for (int i = 0; i < diff; i++) {
            ClickCalendarNextPage();
        }
        calendarDate(dtf.format(checkOut)).click();
    }

    public void SetCheckOutDate(LocalDate date) {
        calendarCheckOutDate.click();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        calendarDate(dtf.format(date)).click();
    }

    public void SetAdults(Integer adults) {
        selectAdults.click();
        itemInAdultsList(adults);
    }

    public void SetChildren(Integer children) {
        selectChildren.click();
        itemInChildrenList(children);
    }

    public void SetRooms(Integer rooms) {
        selectRooms.click();
        itemInRoomsList(rooms);
    }

    public void ClickSearch() {
        buttonSearch.click();
    }

    public void FilterByStars(Integer stars) {
        try {
            checkBoxHotelStars(stars).click();
        } catch (NoSuchElementException e) {
            Assert.fail(">>>>>>>>>> " + stars + " stars filter is absent !!!!");
        }
    }

    public void FilterByFunThingsToDo(String activity) {
        try {
            filtersShowAll("filter_popular_activities").click();
        } catch (NoSuchElementException e) {
        }
        try {
            checkBoxFunThingsToDo(activity).click();
        } catch (NoSuchElementException e) {
            Assert.fail(">>>>>>>>>> " + activity + " activity filter is absent !!!!");
        }
    }

}
