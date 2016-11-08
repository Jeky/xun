package org.xun.xuntemplate;

import org.xun.xuncore.views.Request;
import org.xun.xuncore.views.Response;

/**
 *
 * @author Jeky
 */
public class RenderContext {

    public RenderContext(Request request, Response response, Object model) {
        this.request = request;
        this.response = response;
        this.model = model;
    }

    public RenderContext(Object model) {
        this(null, null, model);
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public Object getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "RenderContext{" + model.toString() + "}";
    }

    private final Request request;
    private final Response response;
    private final Object model;
}
