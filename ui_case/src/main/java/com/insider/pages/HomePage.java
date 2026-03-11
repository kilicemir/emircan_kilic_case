package com.insider.pages;

import com.insider.base.BasePage;
import com.insider.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class HomePage extends BasePage {

    @FindBy(id = "navigation")
    private WebElement navigation;

    @FindBy(css = "section.homepage-hero")
    private WebElement heroSection;

    @FindBy(id = "footer")
    private WebElement footer;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        String url = ConfigReader.get("insider.homepage.url");
        driver.get(url);
    }

    public boolean isReady() {
        return isPageOpened() && areAllMainBlocksLoaded();
    }

    public boolean isPageOpened() {
        String homeUrl = ConfigReader.get("insider.homepage.url");
        if (homeUrl == null) return false;
        return navigation.isDisplayed() && Objects.equals(driver.getCurrentUrl(), homeUrl);
    }

    public boolean areAllMainBlocksLoaded() {
        return waitUtils.waitForElementVisible(navigation)
                && waitUtils.waitForElementVisible(heroSection)
                && waitUtils.waitForElementVisible(footer);
    }
}
