package com.insider.pages;

import com.insider.base.BasePage;
import com.insider.utils.ElementUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.Locale;

public class JobListingsPage extends BasePage {

    @FindBy(css = "[aria-label^='Filter by Location:']")
    private WebElement locationFilterBtn;

    @FindBy(css = "[aria-label*='Filter by Team']")
    private WebElement teamFilterBtn;

    @FindBy(className = "postings-wrapper")
    private WebElement postingsWrapper;

    @FindBy(css = ".postings-wrapper .posting, .postings-wrapper .posting-category-title")
    private List<WebElement> postingItems;

    @FindBy(css = ".postings-wrapper .posting span.sort-by-location.location")
    private List<WebElement> postingLocations;

    @FindBy(css = ".postings-wrapper [data-qa='posting-name']")
    private List<WebElement> postingNames;

    @FindBy(css = ".postings-wrapper .posting-category-title,.postings-wrapper .large-category-label")
    private List<WebElement> departmentElements;

    @FindBy(css = ".postings-wrapper [data-qa='btn-apply'] a, .posting .posting-apply a")
    private List<WebElement> applyButtons;

    @FindBy(xpath = "//a[normalize-space()='Istanbul, Turkiye']")
    private WebElement istanbulLink;

    @FindBy(css = "div[aria-label='Filter by Team: Quality Assurance'] a[class='category-link selected']")
    private WebElement qualityAssuranceLink;

    @FindBy(xpath = "//div[@aria-label='Filter by Location: All']//div[@class='filter-popup']")
    private WebElement locationPopup;

    @FindBy(xpath = "//div[@aria-label='Filter by Team: Quality Assurance']//div[@class='filter-popup']")
    private WebElement teamPopup;

    private static final int FILTER_RESULTS_TIMEOUT_SECONDS = 5;

    public JobListingsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void selectLocationIstanbulTurkiye() {
        locationFilterBtn.click();
        waitUtils.createWaitWithTimeout(FILTER_RESULTS_TIMEOUT_SECONDS)
                .until(ExpectedConditions.visibilityOf(locationPopup));
        waitUtils.createWaitWithTimeout(FILTER_RESULTS_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(istanbulLink));
        ElementUtils.scrollAndClick(driver, istanbulLink);
        waitUtils.waitForUrlToContain("Istanbul");
        waitUtils.waitForElementVisible(postingsWrapper);
    }

    public void selectTeamQualityAssurance() {
        teamFilterBtn.click();
        waitUtils.createWaitWithTimeout(FILTER_RESULTS_TIMEOUT_SECONDS)
                .until(ExpectedConditions.visibilityOf(teamPopup));
        waitUtils.createWaitWithTimeout(FILTER_RESULTS_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(qualityAssuranceLink));
        ElementUtils.scrollAndClick(driver, qualityAssuranceLink);
        waitForPostingsVisible();
    }

    public void waitForPostingsToLoad() {
        waitUtils.waitForElementVisible(postingsWrapper);
        waitForPostingsContent();
    }

    private void waitForPostingsVisible() {
        waitUtils.createWaitWithTimeout(JobListingsPage.FILTER_RESULTS_TIMEOUT_SECONDS)
                .until(ExpectedConditions.visibilityOf(postingsWrapper));
    }

    private void waitForPostingsContent() {
        waitUtils.waitForListNonEmpty(postingItems, FILTER_RESULTS_TIMEOUT_SECONDS);
    }

    public void waitForLocationElementsToLoad() {
        waitUtils.waitForListNonEmpty(postingLocations, FILTER_RESULTS_TIMEOUT_SECONDS);
    }

    public boolean doesPositionContain(String expected) {
        if (postingNames.isEmpty()) return false;
        return postingNames.stream().allMatch(el -> el.getText().contains(expected));
    }

    public boolean doesDepartmentContain(String expected) {
        for (WebElement el : departmentElements) {
            if (el.getText().trim().contains(expected)) return true;
        }
        return postingsWrapper.getText().contains(expected);
    }

    public void clickApplyButtonByIndex(int index) {
        String urlBeforeClick = driver.getCurrentUrl();
        if (index < applyButtons.size()) {
            ElementUtils.scrollAndClick(driver, applyButtons.get(index));
            waitUtils.waitForUrlToChange(urlBeforeClick);
        }
    }

    public boolean doesLocationContain(String expected) {
        waitForLocationElementsToLoad();
        List<String> actualLocations = postingLocations.stream().map(el -> el.getText().trim()).toList();
        String expectedLower = expected.toLowerCase(Locale.ENGLISH);
        boolean found = actualLocations.stream()
                .anyMatch(text -> text.toLowerCase(Locale.ENGLISH).contains(expectedLower));
        if (!found) {
            throw new AssertionError("Expected location to contain '" + expected + "', but found: " + actualLocations);
        }
        return true;
    }
}
