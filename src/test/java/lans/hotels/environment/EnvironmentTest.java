package lans.hotels.environment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {
    private Map<String, String> mockEnvVars;
    private static String dummyHerokuPostgresURI;

    @BeforeAll
    static void beforeAllTests() {
        dummyHerokuPostgresURI = "postgres://abcdefg:hijklmn@ec3-14-159-582-314.compute-2.amazonaws.com:5432/lmnopqrs";
    }

    @BeforeEach
    void beforeEachTest() {
        mockEnvVars = new HashMap<>();
        // Default env vars - override for specific tests
        mockEnvVars.put(VariableName.STAGE.name(), Stage.DEVELOPMENT.name());
        mockEnvVars.put(VariableName.DATABASE_URL.name(), "jdbc:postgresql://localhost:5432/lans_hotels");
        mockEnvVars.put(VariableName.DB_USERNAME.name(), "postgres");
        mockEnvVars.put(VariableName.DB_PASSWORD.name(), "postgres");
    }

    @Test
    void Environment_without_STAGE_fails() {
        // Arrange
        mockEnvVars.remove(VariableName.STAGE.name());

        // Act
        InvalidEnvironmentException thrown =
                Assertions.assertThrows(InvalidEnvironmentException.class,
                        () -> new Environment(mockEnvVars));

        // Assert
        Assertions.assertEquals("FATAL ERROR: STAGE has not been set!", thrown.getMessage());
    }

    @Test
    void Environment_with_invalid_STAGE_fails() {
        // Arrange
        String invalidStageEnvVar = "invalidStage";
        mockEnvVars.put(VariableName.STAGE.name(), invalidStageEnvVar);

        // Act
        InvalidEnvironmentException thrown =
                assertThrows(InvalidEnvironmentException.class,
                        () -> new Environment(mockEnvVars));

        // Assert
        assertEquals("FATAL ERROR: invalid environment variable STAGE=" + invalidStageEnvVar, thrown.getMessage());
    }

    @Test
    void Env_configured_with_DEVELOPMENT_STAGE_is_in_development() {
        try {
            // Arrange
            mockEnvVars.put(VariableName.STAGE.name(), Stage.DEVELOPMENT.name());

            // Act
            Environment env = new Environment(mockEnvVars);

            // Assert
            Assertions.assertTrue(env.isInDevelopment());
            Assertions.assertFalse(env.isInProduction());
        } catch (InvalidEnvironmentException invalidEnvironmentException) {
            System.out.println(invalidEnvironmentException.getMessage());
            fail();
        }
    }

    @Test
    void Env_configured_with_PRODUCTION_STAGE_is_in_production() {
        try {
            // Arrange
            mockEnvVars.put(VariableName.STAGE.name(), Stage.PRODUCTION.name());
            mockEnvVars.put(VariableName.DATABASE_URL.name(), dummyHerokuPostgresURI);

            // Act
            Environment env = new Environment(mockEnvVars);

            // Assert
            Assertions.assertTrue(env.isInProduction());
            Assertions.assertFalse(env.isInDevelopment());
        } catch (InvalidEnvironmentException invalidEnvironmentException) {
            System.out.println(invalidEnvironmentException.getMessage());
            fail();
        }
    }

    @Test
    void Env_fails_without_db_URL() {
        // Arrange
        mockEnvVars.remove(VariableName.DATABASE_URL.name());

        // Act
        InvalidEnvironmentException thrown =
                assertThrows(InvalidEnvironmentException.class,
                        () -> new Environment(mockEnvVars));

        // Assert
        assertEquals("FATAL ERROR: DATABASE_URL has not been set!", thrown.getMessage());
    }

    @Test
    void Env_fails_without_db_PASSWORD() {
        // Arrange
        mockEnvVars.remove(VariableName.DB_PASSWORD.name());

        // Act
        InvalidEnvironmentException thrown =
                assertThrows(InvalidEnvironmentException.class,
                        () -> new Environment(mockEnvVars));

        // Assert
        assertEquals("FATAL ERROR: DB_PASSWORD has not been set!", thrown.getMessage());
    }

    @Test
    void Env_fails_without_db_USERNAME() {
        // Arrange
        mockEnvVars.remove(VariableName.DB_USERNAME.name());

        // Act
        InvalidEnvironmentException thrown =
                assertThrows(InvalidEnvironmentException.class,
                        () -> new Environment(mockEnvVars));

        // Assert
        assertEquals("FATAL ERROR: DB_USERNAME has not been set!", thrown.getMessage());
    }
}