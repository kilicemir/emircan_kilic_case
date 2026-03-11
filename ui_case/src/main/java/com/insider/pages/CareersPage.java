package com.insider.pages;

import com.insider.base.BasePage;
import com.insider.utils.ConfigReader;
import com.insider.utils.ElementUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CareersPage extends BasePage {

    @FindBy(xpath = "//a[@id='wt-cli-accept-all-btn']")
    private WebElement cookieAcceptBtn;

    @FindBy(id = "open-roles")
    private WebElement openRolesSection;

    @FindBy(css = "a.inso-btn.see-more")
    private WebElement seeAllTeamsButton;

    @FindBy(xpath = "//div[@class='insiderone-icon-cards-grid-item elementor-repeater-item-56ca501']//a[@class='insiderone-icon-cards-grid-item-btn'][contains(normalize-space(), 'Open Position')]")
    private WebElement qaPositionsLink;

    private final JobListingsPage jobListingsPage;

    public CareersPage(WebDriver driver) {
        super(driver);
        this.jobListingsPage = new JobListingsPage(driver);
        PageFactory.initElements(driver, this);
    }

    public void expandTeamsAndSelectQA() {
        openCareersPage();
        clickSeeAllTeams();
        waitForPositionsToLoad();
        clickQualityAssurancePositions();
    }

    private void openCareersPage() {
        String url = ConfigReader.get("insider.careers.page.url");
        driver.get(url);
        waitUtils.waitForElementVisible(openRolesSection);
    }

    private void clickSeeAllTeams() {
        dismissCookieBannerIfPresent();
        waitUtils.waitForElementVisible(seeAllTeamsButton);
        ElementUtils.scrollAndClick(driver, seeAllTeamsButton);
    }

    private void waitForPositionsToLoad() {
        waitUtils.waitForQAPositionsToLoad(qaPositionsLink);
    }

    private void clickQualityAssurancePositions() {
        ElementUtils.scrollAndClick(driver, qaPositionsLink);
    }

    private void dismissCookieBannerIfPresent() {
        waitUtils.createWaitWithTimeout(3).until(ExpectedConditions.elementToBeClickable(cookieAcceptBtn));
        cookieAcceptBtn.click();
    }

    public void setLocation() {
        jobListingsPage.selectLocationIstanbulTurkiye();
    }

    public void setDepartment() {
        jobListingsPage.selectTeamQualityAssurance();
    }

    public boolean hasPostings() {
        jobListingsPage.waitForPostingsToLoad();
        return jobListingsPage.doesPositionContain("Quality Assurance");
    }

    public boolean positionTextIncludes(String expected) {
        return jobListingsPage.doesPositionContain(expected);
    }

    public boolean departmentTextIncludes(String expected) {
        return jobListingsPage.doesDepartmentContain(expected);
    }

    public boolean locationTextIncludes(String expected) {
        return jobListingsPage.doesLocationContain(expected);
    }

    public void openJobApplication(int index) {
        jobListingsPage.clickApplyButtonByIndex(index);
    }
}
