package lans.hotels.use_cases;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.domain.IDataSource;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public abstract class UseCase {
    IDataSource dataSource;
    protected boolean succeeded;
    protected JSONObject result;
    protected JSONObject responseData;

    protected UseCase(IDataSource dataSource) {
        this.result = new JSONObject();
        this.responseData = new JSONObject();
        this.dataSource = dataSource;
        this.succeeded = false;
    }

    public abstract void doExecute() throws Exception;

    public Void execute(Callable<Void> onSuccess, Callable<Void> onFailure) throws Exception {
        execute();
        if (succeeded) {
            return onSuccess.call();
        } else {
            return onFailure.call();
        }
    }
    public void execute() {
        try {
            doExecute();
            dataSource.commit();
        } catch (DataSourceLayerException dataSourceException) {
            dataSourceException.printStackTrace();
            fail();
            setResponseErrorMessage("Data integrity violation");
            dataSourceException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("use case error");
        }
    }
    protected abstract void constructResult();

    public boolean succeeded() {
        return succeeded;
    }

    protected void fail() {
        this.succeeded = false;
    }

    protected  void succeed() {
        this.succeeded = true;
    }

    public JSONObject getResult() {
        constructResult();
        this.result.put("success", succeeded());
        this.result.put("result", responseData);
        return this.result;
    }

    protected void setResponseErrorMessage(String message) {
        result.put("error", message);
        fail();
    }
}
