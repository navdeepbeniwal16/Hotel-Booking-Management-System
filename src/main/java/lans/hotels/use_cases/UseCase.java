package lans.hotels.use_cases;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.domain.IDataSource;
import org.json.JSONObject;

public abstract class UseCase {
    IDataSource dataSource;
    protected boolean succeeded;
    private JSONObject result;
    protected JSONObject responseData;

    protected UseCase(IDataSource dataSource) {
        result = new JSONObject();
        responseData = new JSONObject();
        this.dataSource = dataSource;
        succeeded = false;
    }

    public abstract void doExecute() throws Exception;
    public void execute() {
        try {
            doExecute();
            dataSource.commit();
        } catch (DataSourceLayerException dataSourceException) {
            dataSourceException.printStackTrace();
            fail();
            setResponseErrorMessage("data source error");
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
        result.put("success", succeeded());
        result.put("result", responseData);
        return this.result;
    }

    protected void setResponseErrorMessage(String message) {
        result.put("error", message);
    }
}
