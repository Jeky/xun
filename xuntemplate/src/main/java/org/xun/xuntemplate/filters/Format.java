package org.xun.xuntemplate.filters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateRenderingException;

/**
 *
 * @author Jeky
 */
public class Format extends StringArgumentTemplateFilter {

    public Format() {
        super("Format");
    }

    @Override
    protected Object process(RenderContext context, ASTNode current, Object target, String arg) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(arg);
        LocalDateTime ldt = null;
        if (target instanceof java.util.Date) {
            Instant instant = Instant.ofEpochMilli(((java.util.Date) target).getTime());
            ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } else if (target instanceof java.sql.Date) {
            Instant instant = Instant.ofEpochMilli(((java.sql.Date) target).getTime());
            ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } else if (target instanceof LocalDateTime) {
            ldt = (LocalDateTime) target;
        } else {
            throw new TemplateRenderingException("Cannot convert " + target + " to LocalDateTime", current);
        }
        return dtf.format(ldt);
    }

}
