package com.insider.pages;

import com.insider.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class LeverPage extends BasePage {

    @FindBy(css = "[data-qa='btn-apply-bottom'], a[data-qa='show-page-apply']")
    private WebElement applyBtn;

    @FindBy(css = ".posting-headline h1")
    private List<WebElement> postingHeadlineTitles;

    @FindBy(css = ".posting-header h1")
    private List<WebElement> postingHeaderTitles;

    @FindBy(css = "[data-qa='posting-name']")
    private List<WebElement> postingNameElements;

    @FindBy(css = "h1")
    private List<WebElement> h1Elements;

    @FindBy(tagName = "body")
    private WebElement bodyElement;

    public LeverPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isApplyFormDisplayed() {
        return waitUtils.waitForElementVisible(applyBtn);
    }

    public boolean titleIncludes(String expected) {
        String text = getFirstVisibleTitle();
        if (text != null && text.contains(expected)) return true;
        return bodyElement.getText().contains(expected);
    }

    private String getFirstVisibleTitle() {
        if (!postingHeadlineTitles.isEmpty()) return postingHeadlineTitles.get(0).getText().trim();
        if (!postingHeaderTitles.isEmpty()) return postingHeaderTitles.get(0).getText().trim();
        if (!postingNameElements.isEmpty()) return postingNameElements.get(0).getText().trim();
        if (!h1Elements.isEmpty()) return h1Elements.get(0).getText().trim();
        return null;
    }

    public boolean locationIncludes(String expected) {
        return bodyElement.getText().contains(expected);
    }

    public boolean departmentIncludes(String expected) {
        return bodyElement.getText().contains(expected);
    }
}
