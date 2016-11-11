package org.xun.xuncore.views;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jeky
 */
public class Response {

    public Response(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void renderString(String html) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(html);
        } catch (IOException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, "Error when rendering html", ex);
        }
    }

    public void renderJson(Object o) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(o);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(json);
        } catch (IOException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, "Error when rendering json", ex);
        }
    }

    private final HttpServletRequest request;
    private final HttpServletResponse response;

}
