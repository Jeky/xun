package org.xun.xuntemplate.filters;

import java.util.*;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;
import org.xun.xuntemplate.TemplateRenderingException;

/**
 *
 * @author Jeky
 */
public class Last implements TemplateFilter {

    @Override
    public Object process(RenderContext context, ASTNode current, Object target, Object[] args) {
        try {
            if (target instanceof LinkedList) {
                LinkedList list = (LinkedList) target;
                return list.getLast();
            } else if (target instanceof List) {
                List list = (List) target;
                return list.get(list.size() - 1);
            } else if (target.getClass().isArray()) {
                Object[] arr = (Object[]) target;
                return arr[arr.length - 1];
            } else {
                throw new TemplateRenderingException("Target " + target + " is not a list or array.", current);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new TemplateRenderingException("Cannot get last from an empty list", e, current);
        }
    }

}
