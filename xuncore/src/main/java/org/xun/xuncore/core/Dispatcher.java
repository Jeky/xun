package org.xun.xuncore.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xun.xuncore.views.Request;
import org.xun.xuncore.views.Response;
import org.xun.xuncore.views.converters.ParameterConverter;
import org.xun.xuncore.reflect.BeanMethod;
import org.xun.xuncore.reflect.LocalVariables;

/**
 *
 * @author Jeky
 */
@WebFilter(filterName = "Dispatcher", urlPatterns = {"/*"})
public class Dispatcher implements Filter {

    public Dispatcher() {
        logger = Logger.getLogger(Dispatcher.class.getName());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest originalRequest, ServletResponse originalResponse, FilterChain chain)
        throws IOException,
               ServletException {
        HttpServletRequest request = (HttpServletRequest) originalRequest;
        HttpServletResponse response = (HttpServletResponse) originalResponse;

        String uri = request.getRequestURI();
        if (uri.endsWith(".jsp") || uri.endsWith(".html")) {
            System.out.println("handling static resouces:" + uri);
            chain.doFilter(originalRequest, originalResponse);
            return;
        }

        if (uri.charAt(0) == '/') {
            uri = uri.substring(1);
        }

        Map<Pattern, List<BeanMethod>> views = (Map<Pattern, List<BeanMethod>>) DefaultSettings.getSettings().getSettingValue("VIEWS");
        for (Map.Entry<Pattern, List<BeanMethod>> e : views.entrySet()) {
            Matcher urlMatcher = e.getKey().matcher(uri);
            if (urlMatcher.matches()) {
                // fetch url parameters
                Set<String> urlArgNames = new HashSet<>();
                try {
                    Method m = Pattern.class.getDeclaredMethod("namedGroups");
                    m.setAccessible(true);
                    Map<String, Integer> groups = (Map<String, Integer>) m.invoke(e.getKey());
                    urlArgNames = groups.keySet();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                Map<String, String[]> parameters = new HashMap<>(request.getParameterMap());
                for (String urlArg : urlArgNames) {
                    parameters.put(urlArg, new String[]{urlMatcher.group(urlArg)});
                }

                LocalVariables vars = new LocalVariables();
                Request req = new Request(request, parameters);
                Response res = new Response(request, response);
                vars.putVariable("request", req);
                vars.putVariable("response", res);

                try {
                    for (BeanMethod m : e.getValue()) {
                        if (m.matchArgTypes(Request.class, Response.class)) { // try to find func(req, res)
                            m.invoke(vars, req, res);
                            return;
                        } else if (m.matchArgNames(parameters.keySet())) { // try invoke plain bean
                            // convert request parameters to method arguments
                            Object[] args = new Object[parameters.size()];
                            int i = 0;
                            for (String argName : m.getArgNames()) {
                                Class argType = m.getArgType(argName);
                                Map<Class, ParameterConverter> converters = (Map<Class, ParameterConverter>) DefaultSettings.getSettings().getSettingValue("PARAMETER_CONVERTER");
                                ParameterConverter converter = converters.get(argType);
                                if (converter == null) {
                                    throw new ServletException("Cannot find converter for " + argType);
                                }
                                Object argValue = converter.convert(parameters.get(argName));
                                args[i] = argValue;
                                i++;
                            }

                            m.invoke(vars, args);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    // internal error should be shown in debug mode
                    logger.log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
            }
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void destroy() {
    }

    private Logger logger;
}
