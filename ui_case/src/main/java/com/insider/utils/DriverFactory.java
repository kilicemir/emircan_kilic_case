package com.insider.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        io.github.bonigarcia.wdm.WebDriverManager wdm;
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                wdm = io.github.bonigarcia.wdm.WebDriverManager.chromedriver();
                wdm.setup();
                driver = new ChromeDriver(new ChromeOptions());
                break;
            case "firefox":
                wdm = io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver();
                wdm.setup();
                driver = new FirefoxDriver(new FirefoxOptions());
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        return driver;
    }
}
