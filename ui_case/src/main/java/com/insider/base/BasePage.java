package com.insider.base;

import com.insider.utils.ConfigReader;
import com.insider.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected WebDriver driver;
    protected WaitUtils waitUtils;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver, ConfigReader.getInt("default.timeout", 30));
    }
}
