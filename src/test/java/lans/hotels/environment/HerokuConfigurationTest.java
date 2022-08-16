package lans.hotels.environment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

class HerokuConfigurationTest {
    private static String dummyURI;
    private static String dummyUsername;
    private static String dummyPassword;
    private static String urlPrefix;

    private HerokuConfiguration heroku;

    @BeforeAll
    static void beforeAllTests() {
        dummyURI = "postgres://fakeUser:fakePassword@ec3-14-159-582-314.compute-2.amazonaws.com:5432/lmnopqrs";
        dummyUsername = "fakeUser";
        dummyPassword = "fakePassword";
        urlPrefix = "jdbc:postgresql://";
    }

    @BeforeEach
    void beforeEachTest() {
        try {
            heroku = new HerokuConfiguration(dummyURI);
        } catch (URISyntaxException uriSyntaxException) {
            System.out.println(uriSyntaxException.getMessage());
        }
    }

    @Test
    void It_has_the_correct_username() {
        // Arrange => beforeEachTest
        Assertions.assertEquals(dummyUsername, heroku.getUsername());
    }

    @Test
    void It_has_the_correct_password() {
        // Arrange => beforeEachTest
        Assertions.assertEquals(dummyPassword, heroku.getPassword());
    }

    @Test
    void It_has_the_correct_URL() {
        // Arrange => beforeEachTest
        String expectedURL = urlPrefix + "ec3-14-159-582-314.compute-2.amazonaws.com:5432/lmnopqrs";
        Assertions.assertEquals(expectedURL, heroku.getUrl());
    }
}