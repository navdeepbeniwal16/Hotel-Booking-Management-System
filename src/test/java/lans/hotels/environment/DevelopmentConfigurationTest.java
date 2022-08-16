package lans.hotels.environment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DevelopmentConfigurationTest {
    private static String dummyURL;
    private static String dummyUsername;
    private static String dummyPassword;

    private DevelopmentConfiguration developmentConfiguration;

    @BeforeAll
    static void beforeAllTests() {
        dummyURL = "jdbc:postgresql://localhost:5432/lans_hotels";
        dummyUsername = "fakeDevUser";
        dummyPassword = "fakeDevPassword";
    }

    @BeforeEach
    void beforeEachTest() {
        developmentConfiguration = new DevelopmentConfiguration(dummyURL, dummyUsername, dummyPassword);
    }

    @Test
    void It_has_the_correct_username() {
        // Arrange => beforeEachTest
        Assertions.assertEquals(dummyUsername, developmentConfiguration.getUsername());
    }

    @Test
    void It_has_the_correct_password() {
        // Arrange => beforeEachTest
        Assertions.assertEquals(dummyPassword, developmentConfiguration.getPassword());
    }

    @Test
    void It_has_the_correct_URL() {
        // Arrange => beforeEachTest
        String expectedURL = "jdbc:postgresql://localhost:5432/lans_hotels";
        Assertions.assertEquals(expectedURL, developmentConfiguration.getUrl());
    }
}