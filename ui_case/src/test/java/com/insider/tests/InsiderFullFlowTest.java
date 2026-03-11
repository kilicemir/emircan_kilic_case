package com.insider.tests;

import com.insider.pages.CareersPage;
import com.insider.pages.HomePage;
import com.insider.pages.LeverPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class InsiderFullFlowTest extends BaseTest {

    @Test(description = "Complete career journey: Homepage through Lever apply")
    public void testCompleteCareerJourney() {
        WebDriver driver = getDriver();

        HomePage home = new HomePage(driver);
        home.open();
        assertThat(home.isReady()).as("Homepage loads with main blocks").isTrue();

        CareersPage careers = new CareersPage(driver);
        careers.expandTeamsAndSelectQA();
        careers.setLocation();
        careers.setDepartment();
        assertThat(careers.hasPostings()).as("Filtered job list appears").isTrue();

        assertThat(careers.positionTextIncludes("Quality Assurance")).as("Position shows QA").isTrue();
        assertThat(careers.departmentTextIncludes("Quality Assurance")).as("Department shows QA").isTrue();
        assertThat(careers.locationTextIncludes("Istanbul, Turkiye")).as("Location shows Istanbul").isTrue();

        careers.openJobApplication(0);

        LeverPage lever = new LeverPage(driver);
        assertThat(lever.isApplyFormDisplayed()).as("Lever apply page opens").isTrue();
        assertThat(lever.titleIncludes("Software Quality Assurance Engineer")).as("Job title correct on Lever").isTrue();
        assertThat(lever.locationIncludes("Istanbul, Turkiye")).as("Location correct on Lever").isTrue();
        assertThat(lever.departmentIncludes("Quality Assurance")).as("Department correct on Lever").isTrue();
    }
}
