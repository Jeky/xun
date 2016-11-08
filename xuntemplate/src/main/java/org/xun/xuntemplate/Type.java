package org.xun.xuntemplate;

/**
 *
 * @author Jeky
 */
public enum Type {
    UNKNOWN, ROOT,
    // template symbols
    /**
     * {{
     */
    EXP_START,
    /**
     * }}
     */
    EXP_END,
    /**
     * {%
     */
    TAG_START,
    /**
     * %}
     */
    TAG_END,
    /**
     * {#
     */
    COMMENT_START,
    /**
     * #}
     */
    COMMENT_END,
    /**
     * |
     */
    FILTER_START,
    /**
     * .
     */
    METHOD_START,
    /**
     * :
     */
    FILTER_PARAMETER_START,
    // expression
    EXP, VAR, VAR_EXP, METHOD, FIELD, BOOLEAN_EXP,
    // filter
    FILTER, FILTER_PARAMTER,
    // tag
    TAG, TAG_PARAMETER, TAG_NAME,
    // plain
    HTML, COMMENT,
    // types
    INT, FLOAT, STRING,
    // operators
    /**
     * ==
     */
    EQ,
    /**
     * !=
     */
    NEQ,
    /**
     * <
     */
    LESS,
    /**
     * >
     */
    GREATER,
    /**
     * <=
     */
    LE,
    /**
     * >=
     */
    GE,
    /**
     * !
     */
    NOT,
    /**
     * &&
     */
    AND,
    /**
     * ||
     */
    OR,
    /**
     * (
     */
    LB,
    /**
     * )
     */
    RB,
    /**
     * +
     */
    ADD,
    /**
     * -
     */
    SUB,
    /**
     * *
     */
    MUL,
    /**
     * /
     */
    DIV, 
    /**
     * %
     */
    MOD,
    /**
     * -
     */
    NEG;
}
