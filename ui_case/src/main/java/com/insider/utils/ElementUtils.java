package com.insider.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public final class ElementUtils {

    private ElementUtils() {
    }

    public static void scrollAndClick(WebDriver driver, WebElement element) {
        new Actions(driver).moveToElement(element).perform();
        element.click();
    }
}
