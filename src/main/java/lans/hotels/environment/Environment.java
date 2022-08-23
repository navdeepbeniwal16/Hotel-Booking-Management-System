package lans.hotels.environment;

import lans.hotels.datasource.DBConfiguration;

import java.net.URISyntaxException;
import java.util.Map;

public class Environment implements EnvironmentI {
    private Map<String, String> envVars;
    private Stage stage;
    private DBConfiguration dbConfiguration;

    public Environment(Map<String, String> envVars) throws InvalidEnvironmentException {
        this.envVars = envVars;
        checkHasAllRequiredEnvironmentVariables();
        setStage();
        setDB();
    }

    private void setStage() throws InvalidEnvironmentException {
        String envVarStage = envVars.get(VariableName.STAGE.name());
        if (envVarStage.equals(Stage.DEVELOPMENT.name())) {
            stage = Stage.DEVELOPMENT;
        } else if (envVarStage.equals(Stage.PRODUCTION.name())) {
            stage = Stage.PRODUCTION;
        } else if (envVarStage.equals(Stage.STAGING.name())) {
            stage = Stage.STAGING;
        } else {
            throw new InvalidEnvironmentException("FATAL ERROR: invalid environment variable STAGE=" + envVarStage);
        }
    }

    private void setDB() throws InvalidEnvironmentException {
        String url = envVars.get(VariableName.DATABASE_URL.name());
        String username = envVars.get(VariableName.DB_USERNAME.name());
        String password = envVars.get(VariableName.DB_PASSWORD.name());
        if (isInDevelopment()) {
            dbConfiguration = new DevelopmentConfiguration(url, username, password);
        } else {
            try {
                dbConfiguration = new HerokuConfiguration(url);
            } catch (URISyntaxException uriSyntaxException){
                throw new InvalidEnvironmentException(uriSyntaxException.getMessage());
            }
        }
    }

    private void checkHasAllRequiredEnvironmentVariables() throws InvalidEnvironmentException {
        for (VariableName envVar: VariableName.values()) {
            checkEnvVar(envVar, "FATAL ERROR: " + envVar.name() + " has not been set!");
        }
    }

    private void checkEnvVar(VariableName envVar, String errorMessage) throws InvalidEnvironmentException {
        if (envVars.get(envVar.name()) == null) throw new InvalidEnvironmentException(errorMessage);
    }

    protected Boolean isInDevelopment() {
        return stage.equals(Stage.DEVELOPMENT);
    }

    protected Boolean isInProduction() { return stage.equals(Stage.PRODUCTION); }

    protected Boolean isInStaging() { return stage.equals(Stage.STAGING); }
    public DBConfiguration getDBConfiguration() {
        return dbConfiguration;
    }
}
