package com.demoqa.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.demoqa.utils.Language;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class JUnitTest {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://www.zalando.de/";
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
    }
    @BeforeEach
    void beforeEachTest() {
        open(Configuration.baseUrl);
    }

    @DisplayName("Check That On Modal Window Language Options Both Are Available")
    @EnumSource(Language.class)
    @ParameterizedTest
    void checkThatLanguageModalWindowHasBothOptions(Language language) {
        $("[class=z-navicat-header_navToolLabel]").click();
        $$("[role=group]").find(text(language.name())).shouldBe(visible);
    }

    @DisplayName("Check That Search by Key Words Works")
    @ValueSource(strings = {"Hoodie", "jacket"})
    @ParameterizedTest
    void simplySearchTest(String searchData) {
        $("[name=q]").setValue(searchData).pressEnter();
        $$("[data-zalon-partner-target=true]").first().shouldHave(text(searchData));
    }

    private static Stream<Arguments> navBarHeaderButtonsTextForDiffLanguages() {
        return Stream.of(
                Arguments.of(Language.Deutsch, List.of("DamenHerrenKinder")),
                Arguments.of(Language.English, List.of("WomenMenKids")
                )
        );
    }
    @MethodSource()
    @ParameterizedTest(name = "Check That On Header Nav Bar Button Texts Are Changing For Diff Language: {0}")
    void navBarHeaderButtonsTextForDiffLanguages(Language language, List<String> buttonsTexts) {
        $("[class=z-navicat-header_navToolLabel]").click();
        $$("[class=z-navicat-header_modalLS_row1]").find(text(language.name())).click();
        $("[class=z-navicat-header_modalLS_actions]")
                .$("button[class = 'z-navicat-header_buttonRoot z-navicat-header_buttonBase z-navicat-header_buttonPrimary z-navicat-header_buttonMedium']").click(); // withText("_buttonPrimary")).click();
        $$(".z-navicat-header_genderList").filter(visible).shouldHave(CollectionCondition.texts(buttonsTexts));

    }

    @AfterEach
    void afterEachTest() {
        closeWindow();
    }
}
