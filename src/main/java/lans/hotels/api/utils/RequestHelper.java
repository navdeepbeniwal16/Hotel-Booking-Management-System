package lans.hotels.api.utils;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestHelper {
    HttpServletRequest request;
    JSONObject data;
    protected String[] commandPath;
    public RequestHelper(HttpServletRequest request) throws IOException {
        this.request = request;
        parseBody();
        this.commandPath = request.getPathInfo().split("/");
    }

    private void parseBody() throws IOException {
        BufferedReader requestReader = request.getReader();
        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject body = new JSONObject();
        if (lines.length() > 0) {
            body = new JSONObject(lines);
        }
        data = body;
    }

    public JSONObject body() {
        return this.data;
    }

    public JSONObject body(String key) {
        if (data.has(key)) return data.getJSONObject(key);
        return new JSONObject();
    }

    public Object body(String parent, String child) {
        if (body(parent).has(child)) return body(parent).get(child);
        return null;
    }

    public Void checkCommandPath(Integer exactLength) {
        assert commandPath.length == exactLength;
        return null;
    }

    public Void checkCommandPath(Integer minLength, Integer maxLength) {
        assert commandPath.length >= minLength;
        assert commandPath.length <= maxLength;
        return null;
    }

    public String methodAndURI() {
        return request.getMethod() + " " + request.getRequestURI();
    }
}
