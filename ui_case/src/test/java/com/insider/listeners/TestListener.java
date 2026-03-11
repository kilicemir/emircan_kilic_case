package com.insider.listeners;

import com.insider.utils.DriverManager;
import com.insider.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestListener implements ITestListener {

    private static final Logger log = Logger.getLogger(TestListener.class.getName());

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String desc = (description != null && !description.isBlank()) ? " - " + description : "";
        log.info("[SUCCESS] Test passed: " + testName + desc);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String desc = (description != null && !description.isBlank()) ? " - " + description : "";
        Throwable throwable = result.getThrowable();
        String errorMsg = throwable != null ? throwable.getMessage() : "Unknown error";
        log.severe("[FAIL] Test failed: " + testName + desc);
        log.severe("[FAIL] Reason: " + errorMsg);
        if (throwable != null) {
            log.log(Level.FINE, "Stack trace", throwable);
        }

        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            ScreenshotUtils.takeScreenshot(driver, testName);
            byte[] screenshot = ScreenshotUtils.getScreenshotBytes(driver);
            if (screenshot != null && screenshot.length > 0) {
                Allure.attachment("Failure Screenshot", new java.io.ByteArrayInputStream(screenshot));
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.warning("[SKIPPED] Test skipped: " + testName);
    }
}
