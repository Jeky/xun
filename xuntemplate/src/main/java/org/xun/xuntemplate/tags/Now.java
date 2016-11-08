package org.xun.xuntemplate.tags;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class Now extends SimpleTemplateTag {

    public Now() {
        super("now");
    }

    @Override
    public EvalFunc createTagFunc() {
        return FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        // poll tag name ast node
        ASTNode tag = nodes.pollFirst();
        if (nodes.isEmpty()) {
            LinkedList<ASTNode> dummy = new LinkedList<>();
            dummy.add(new ASTNode("\"\"", tag.getLineNum(), tag.getColNum()));
            return FACTORYS.get(Type.STRING).build(dummy);
        }

        if (!isStr(nodes.peekFirst())) {
            throw new TemplateParsingException("Tag Now should be given a string argument", tag);
        }
        ASTNode arg = FACTORYS.get(Type.STRING).build(nodes);
        return arg;
    }

    private static final EvalFunc FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            String fmt = current.getChildren().get(0).eval(context).toString();
            LocalDateTime now = LocalDateTime.now();
            if (fmt.isEmpty()) {
                return now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm:ss"));
            } else {
                return now.format(DateTimeFormatter.ofPattern(fmt));
            }
        }
    };
}
