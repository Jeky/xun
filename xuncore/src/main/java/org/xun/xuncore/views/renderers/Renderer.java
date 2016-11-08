package org.xun.xuncore.views.renderers;

import org.xun.xuncore.views.Request;
import org.xun.xuncore.views.Response;
import org.xun.xuncore.reflect.Decorator;
import org.xun.xuncore.reflect.LocalVariables;


/**
 *
 * @author Jeky
 */
public abstract class Renderer implements Decorator<Object> {

    @Override
    public final void pre(LocalVariables vars, Object... args) {
    }

    @Override
    public final Object post(LocalVariables vars, Object resultValue) {
        this.request = vars.getVariable("request", Request.class);
        this.response = vars.getVariable("response", Response.class);
        
        render(resultValue);

        return resultValue;
    }

    public abstract void render(Object result);
    protected Request request;
    protected Response response;

}
