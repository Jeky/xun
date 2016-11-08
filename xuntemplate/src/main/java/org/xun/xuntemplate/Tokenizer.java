package org.xun.xuntemplate;

import java.util.*;

/**
 *
 * @author Jeky
 */
public class Tokenizer {

    public Tokenizer(String templateStr) {
        this.templateStr = templateStr;
        this.index = 0;
        this.lineNum = 0;
        this.colNum = 0;
        this.nodes = new LinkedList<>();
        this.builder = new StringBuilder();
    }

    public LinkedList<ASTNode> tokenize() {
        boolean isStr = false;
        boolean inTemplateSymbolBlock = false;

        while (hasNext()) {
            if (isStr) {
                if (top() == '\"') {
                    isStr = !isStr;
                }
                builder.append(pop());
            } else if (scanSymbolMap(KEYWORDS)) {
                if (TEMPLATE_BLOCK_START.contains(nodes.peekLast().getType())) {
                    inTemplateSymbolBlock = true;
                } else if (TEMPLATE_BLOCK_END.contains(nodes.peekLast().getType())) {
                    inTemplateSymbolBlock = false;
                } else if (!inTemplateSymbolBlock) {
                    nodes.peekLast().setType(Type.UNKNOWN);
                }
            } else if (!inTemplateSymbolBlock) {
                builder.append(pop());
            } else if (EMPTY_CHARS.contains(top()) && !isStr) {
                if (builder.length() > 0) {
                    nodes.add(new ASTNode(builder.toString(), nodeLineNum, nodeColNum));
                    builder.setLength(0);
                }
                pop();
                nodeLineNum = lineNum;
                nodeColNum = colNum;
            } else {
                if (top() == '\"') {
                    isStr = !isStr;
                }
                builder.append(pop());
            }
        }

        if (builder.length() > 0) {
            nodes.add(new ASTNode(builder.toString(), nodeLineNum, nodeColNum));
        }
        return nodes;
    }

    private boolean scanSymbolMap(Map<String, Type> map) {
        for (Map.Entry<String, Type> e : map.entrySet()) {
            String s = e.getKey();
            Type t = e.getValue();
            if (topEquals(s)) {
                if (builder.length() > 0) {
                    nodes.add(new ASTNode(builder.toString(), nodeLineNum, nodeColNum));
                    builder.setLength(0);
                }
                nodes.add(new ASTNode(t, s, lineNum, colNum));
                pop(s.length());
                nodeLineNum = lineNum;
                nodeColNum = colNum;
                return true;
            }
        }
        return false;
    }

    public boolean hasNext() {
        return index < templateStr.length();
    }

    public char top() {
        return templateStr.charAt(index);
    }

    public char pop() {
        char c = templateStr.charAt(index);
        index++;
        colNum++;
        if (c == '\n') {
            lineNum++;
            colNum = 0;
        }
        return c;
    }

    public void pop(int count) {
        for (int i = 0; i < count; i++) {
            pop();
        }
    }

    public boolean topEquals(String symbol) {
        if (symbol.length() + index > templateStr.length()) {
            return false;
        }
        for (int i = 0; i < symbol.length(); i++) {
            if (symbol.charAt(i) != templateStr.charAt(index + i)) {
                return false;
            }
        }
        return true;
    }

    private static final Set<Character> EMPTY_CHARS = new HashSet<>();
    private static final Set<Type> TEMPLATE_BLOCK_START = new HashSet<>();
    private static final Set<Type> TEMPLATE_BLOCK_END = new HashSet<>();
    private static final Map<String, Type> KEYWORDS = new LinkedHashMap<>();

    static {
        for (char c : " \t\n\f\r".toCharArray()) {
            EMPTY_CHARS.add(c);
        }
        EMPTY_CHARS.add((char) 0x0B);
        
        KEYWORDS.put("==", Type.EQ);
        KEYWORDS.put("!=", Type.NEQ);
        KEYWORDS.put("<", Type.LESS);
        KEYWORDS.put(">", Type.GREATER);
        KEYWORDS.put("<=", Type.LE);
        KEYWORDS.put(">=", Type.GE);
        KEYWORDS.put("!", Type.NOT);
        KEYWORDS.put("&&", Type.AND);
        KEYWORDS.put("||", Type.OR);
        KEYWORDS.put("{{", Type.EXP_START);
        KEYWORDS.put("}}", Type.EXP_END);
        KEYWORDS.put("{%", Type.TAG_START);
        KEYWORDS.put("%}", Type.TAG_END);
        KEYWORDS.put("{#", Type.COMMENT_START);
        KEYWORDS.put("#}", Type.COMMENT_END);
        KEYWORDS.put("|", Type.FILTER_START);
        KEYWORDS.put(".", Type.METHOD_START);
        KEYWORDS.put(":", Type.FILTER_PARAMETER_START);
        KEYWORDS.put("(", Type.LB);
        KEYWORDS.put(")", Type.RB);
        KEYWORDS.put("+", Type.ADD);
        KEYWORDS.put("-", Type.SUB);
        KEYWORDS.put("*", Type.MUL);
        KEYWORDS.put("/", Type.DIV);
        KEYWORDS.put("%", Type.MOD);

        TEMPLATE_BLOCK_START.add(Type.EXP_START);
        TEMPLATE_BLOCK_START.add(Type.TAG_START);
        TEMPLATE_BLOCK_START.add(Type.COMMENT_START);

        TEMPLATE_BLOCK_END.add(Type.EXP_END);
        TEMPLATE_BLOCK_END.add(Type.TAG_END);
        TEMPLATE_BLOCK_END.add(Type.COMMENT_END);
    }

    private final String templateStr;
    private int index;
    private int lineNum;
    private int colNum;
    private int nodeLineNum;
    private int nodeColNum;
    private LinkedList<ASTNode> nodes;
    private StringBuilder builder;
}
