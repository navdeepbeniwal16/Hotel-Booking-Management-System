package lans.hotels.api.DTOs;

import jdk.jshell.spi.ExecutionControl;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractDTO<T> {
    private JSONObject json = new JSONObject();
    private T object = null;

    protected T buildObject() throws JSONException, ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented yet: " + this.getClass());
    }

    protected JSONObject buildJson() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented yet: " + this.getClass());
    }

    public AbstractDTO(JSONObject json) {
        this.json = json;
        try {
            this.object = buildObject();
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
    }

    public AbstractDTO(T domainObject) {
        this.object = domainObject;
        try {
            this.json = buildJson();
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
    }

    public JSONObject json() {
        return json;
    }

    public T object() {
        return object;
    }
}
