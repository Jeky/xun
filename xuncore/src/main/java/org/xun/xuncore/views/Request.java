package org.xun.xuncore.views;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jeky
 */
public class Request {

    public Request(HttpServletRequest request, Map<String, String[]> parameters) {
        this.request = request;
        this.parameters = parameters;
    }

    public int getInt(String key) {
        return Integer.parseInt(parameters.get(key)[0]);
    }

    public int getIntOrDefault(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public HttpSession getSession() {
        return request.getSession(true);
    }

    private final HttpServletRequest request;
    private final Map<String, String[]> parameters;
}
