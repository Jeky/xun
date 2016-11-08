package org.xun.xuntemplate.ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;
import org.xun.xuntemplate.classhandlers.TemplateFilterHandler;

/**
 *
 * @author Jeky
 */
public class FilterNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode filter = nodes.pollFirst();
        filter.setType(Type.FILTER);
        filter.setEvalFunc(FILTER_EVAL_FUNC);

        if (!nodes.isEmpty()) {
            ASTNode filterParamStart = nodes.pollFirst();
            if (filterParamStart.getType() != Type.FILTER_PARAMETER_START) {
                throw new TemplateParsingException("Filter expression error", filterParamStart);
            }

            while (!nodes.isEmpty()) {
                if (nodes.size() >= 3 && nodes.get(1).getType() == Type.METHOD_START) {
                    if (isInt(nodes.peekFirst())) {
                        filter.addChild(buildByNodes(nodes, Type.FLOAT, 3));
                    } else {
                        int i = 0;
                        int maxRightMethod = 1;
                        for (ASTNode n : nodes) {
                            if (n.getType() == Type.METHOD_START) {
                                maxRightMethod = i;
                            }
                            i++;
                        }
                        filter.addChild(buildByNodes(nodes, Type.VAR_EXP, maxRightMethod + 2));
                    }
                } else if (isInt(nodes.peekFirst())) {
                    filter.addChild(FACTORYS.get(Type.INT).build(nodes));
                } else if (isStr(nodes.peekFirst())) {
                    filter.addChild(FACTORYS.get(Type.STRING).build(nodes));
                } else {
                    throw new TemplateParsingException("Filter expression error", filter);
                }
            }
        }

        return filter;
    }

    private static final EvalFunc FILTER_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            String filterName = current.getValue();
            List<ASTNode> children = current.getChildren();
            Object[] args = new Object[children.size() - 1];
            Object target = null;
            int i = 0;
            for (ASTNode n : children) {
                if (i == 0) {
                    target = n.eval(context);
                } else {
                    args[i - 1] = n.eval(context);
                }
                i++;
            }
            Map<String, TemplateFilter> filters = (Map<String, TemplateFilter>) DefaultSettings.getSettings().getSettingValue(TemplateFilterHandler.TEMPLATE_FILTERS);
            TemplateFilter filter = filters.get(filterName);
            return filter.process(context, current, target, args);
        }
    };
}
