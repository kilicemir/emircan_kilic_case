package com.insider.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WaitUtils(WebDriver driver, long timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public WebDriverWait createWaitWithTimeout(long timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public boolean waitForElementVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement waitForElementClickable(By locator, long timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForQAPositionsToLoad(By positionsLinkLocator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(positionsLinkLocator));
        wait.until(d -> {
            WebElement btn = d.findElement(positionsLinkLocator);
            String text = btn.getText().trim();
            return !text.equals("0 Open Positions") && !text.equals("0 Open Position");
        });
    }

    public void waitForQAPositionsToLoad(WebElement positionsLink) {
        wait.until(ExpectedConditions.visibilityOf(positionsLink));
        wait.until(d -> {
            String text = positionsLink.getText().trim();
            return !text.equals("0 Open Positions") && !text.equals("0 Open Position");
        });
    }

    public void waitForUrlToContain(String substring) {
        wait.until(d -> d.getCurrentUrl().contains(substring));
    }

    public void waitForUrlToChange(String urlBefore) {
        wait.until(d -> !d.getCurrentUrl().equals(urlBefore));
    }

    public void waitForListNonEmpty(List<WebElement> list, long timeoutSeconds) {
        createWaitWithTimeout(timeoutSeconds).until(d -> !list.isEmpty());
    }
}
