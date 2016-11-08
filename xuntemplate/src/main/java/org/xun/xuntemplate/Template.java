package org.xun.xuntemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;
import org.xun.xuntemplate.ast.ExtendsRootNodeFactory;
import org.xun.xuntemplate.ast.RootNodeFactory;

/**
 *
 * @author Jeky
 */
public class Template {

    public Template(String template) {
        parse(template);
    }

    public Template(File templateFile) {
        try {
            Stream<String> lines = Files.lines(templateFile.toPath());
            parse(lines.reduce("", (a, b) -> a + b));
        } catch (IOException ex) {
            throw new TemplateParsingException("Cannot find template file: " + templateFile.getAbsolutePath());
        }
    }

    public String render(RenderContext context) {
        return root.eval(context).toString();
    }

    public void parse(String templateStr) {
        Tokenizer tokenizer = new Tokenizer(templateStr);
        LinkedList<ASTNode> nodes = tokenizer.tokenize();

        String target = getTemplateExtendsTarget(nodes);
        if (target != null) {
            root = new Template(new File(target)).root;
            new ExtendsRootNodeFactory(root).build(nodes);
        } else {
            root = new RootNodeFactory().build(nodes);
        }
    }

    private String getTemplateExtendsTarget(LinkedList<ASTNode> nodes) {
        Iterator<ASTNode> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            ASTNode tagStart = iterator.next();
            if (tagStart.getType() == Type.TAG_START) {
                if (!iterator.hasNext()) {
                    throw new TemplateParsingException("Tag expression is empty", tagStart);
                }
                ASTNode extendsNode = iterator.next();
                if (extendsNode.getValue().equals("extends")) {
                    if (!iterator.hasNext()) {
                        throw new TemplateParsingException("The filename in extends tag is empty", extendsNode);
                    }
                    ASTNode targetNode = iterator.next();
                    String filename = targetNode.getValue();
                    if (filename.length() < 2) {
                        throw new TemplateParsingException("The string argument is error in extends tag.", targetNode);
                    }

                    return filename.substring(1, filename.length() - 1);
                }
            }
        }

        return null;
    }
    public ASTNode root;
}
