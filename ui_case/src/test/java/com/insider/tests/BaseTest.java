package com.insider.tests;

import com.insider.listeners.TestListener;
import com.insider.utils.ConfigReader;
import com.insider.utils.DriverFactory;
import com.insider.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.nio.file.Paths;

@Listeners(TestListener.class)
public abstract class BaseTest {

    @BeforeSuite
    public void beforeSuite() throws IOException {
        String configPath = Paths.get("src", "test", "resources", "config.properties").toString();
        ConfigReader.load(configPath);
    }

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.get("default.browser", "chrome");
        WebDriver driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
