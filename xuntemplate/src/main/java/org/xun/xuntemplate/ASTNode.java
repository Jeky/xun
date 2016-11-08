package org.xun.xuntemplate;

import java.util.*;

/**
 *
 * @author Jeky
 */
public class ASTNode {

    public ASTNode(Type type, String value, int lineNum, int colNum) {
        this.type = type;
        this.value = value;
        this.lineNum = lineNum;
        this.colNum = colNum;
        this.children = new ArrayList<>();
    }

    public ASTNode(String value, int lineNum, int colNum) {
        this(Type.UNKNOWN, value, lineNum, colNum);
    }

    public ASTNode(Type type) {
        this(type, "", 0, 0);
    }

    public ASTNode(Type type, String value) {
        this(type, value, 0, 0);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setEvalFunc(EvalFunc evalFunc) {
        this.evalFunc = evalFunc;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void visit(ASTNodeVisitor visitor) {
        Queue<ASTNode> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            ASTNode n = queue.poll();
            visitor.visit(n);
            for (ASTNode child : n.getChildren()) {
                queue.add(child);
            }
        }
    }

    public ASTNode addChild(ASTNode node) {
        children.add(node);
        return this;
    }

    public EvalFunc getEvalFunc() {
        return evalFunc;
    }

    public Object eval(RenderContext context) {
        return evalFunc.eval(context, this);
    }

    public void printTree() {
        printTree(this, 0);
    }

    private void printTree(ASTNode n, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(n);
        for (ASTNode c : n.getChildren()) {
            printTree(c, level + 1);
        }
    }

    public static interface ASTNodeVisitor {

        void visit(ASTNode n);
    }

    @Override
    public String toString() {
        return value + "[" + type + "](" + lineNum + "," + colNum + ")";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.lineNum;
        hash = 37 * hash + this.colNum;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ASTNode other = (ASTNode) obj;
        if (this.lineNum != other.lineNum) {
            return false;
        }
        if (this.colNum != other.colNum) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    private int lineNum;
    private int colNum;
    private Type type;
    private String value;
    private EvalFunc evalFunc;
    private List<ASTNode> children;
}
