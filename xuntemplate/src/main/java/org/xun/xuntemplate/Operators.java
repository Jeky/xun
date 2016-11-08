package org.xun.xuntemplate;

/**
 *
 * @author Jeky
 */
public class Operators {

    public static Object apply(Object arg0, Object arg1, ASTNode current) {
        if (arg0 instanceof String || arg1 instanceof String) {
            return applyBinaryOp(arg0.toString(), arg1.toString(), current);
        } else if (arg0 instanceof Double || arg1 instanceof Double) {
            return applyBinaryOp(((Number) arg0).doubleValue(), ((Number) arg1).doubleValue(), current);
        } else if (arg0 instanceof Float || arg1 instanceof Float) {
            return applyBinaryOp(((Number) arg0).floatValue(), ((Number) arg1).floatValue(), current);
        } else if (arg0 instanceof Long || arg1 instanceof Long) {
            return applyBinaryOp(((Number) arg0).longValue(), ((Number) arg1).longValue(), current);
        } else if (arg0 instanceof Integer || arg1 instanceof Integer) {
            return applyBinaryOp(((Number) arg0).intValue(), ((Number) arg1).intValue(), current);
        } else if (arg0 instanceof Boolean && arg1 instanceof Boolean) {
            return applyBinaryOp((boolean) arg0, (boolean) arg1, current);
        } else if (arg0 instanceof Comparable && arg1 instanceof Comparable) {
            return applyBinaryOp((Comparable) arg0, (Comparable) arg1, current);
        } else {
            return applyBinaryOp(arg0, arg1, current);
        }
    }

    public static Object apply(Object arg, ASTNode current) {
        if (arg instanceof Boolean && current.getType() == Type.NOT) {
            return !((boolean) arg);
        } else if (arg instanceof Double && current.getType() == Type.NEG) {
            return -((double) arg);
        } else if (arg instanceof Float && current.getType() == Type.NEG) {
            return -((float) arg);
        } else if (arg instanceof Long && current.getType() == Type.NEG) {
            return -((long) arg);
        } else if (arg instanceof Integer && current.getType() == Type.NEG) {
            return -((int) arg);
        } else {
            throw new TemplateParsingException("Cannot apply " + current.getType() + " on "
                + arg + "[" + arg.getClass() + "]", current);
        }
    }

    private static Object applyBinaryOp(double arg0, double arg1, ASTNode current) {
        switch (current.getType()) {
            case ADD:
                return arg0 + arg1;
            case SUB:
                return arg0 - arg1;
            case MUL:
                return arg0 * arg1;
            case DIV:
                return arg0 / arg1;
            case MOD:
                return arg0 % arg1;
            case EQ:
                return arg0 == arg1;
            case NEQ:
                return arg0 != arg1;
            case LESS:
                return arg0 < arg1;
            case GREATER:
                return arg0 > arg1;
            case LE:
                return arg0 <= arg1;
            case GE:
                return arg0 >= arg1;
            default:
                throw new TemplateParsingException("Cannot apply " + current.getType() + " on " + arg0 + " and " + arg1, current);
        }
    }

    private static Object applyBinaryOp(float arg0, float arg1, ASTNode current) {
        switch (current.getType()) {
            case ADD:
                return arg0 + arg1;
            case SUB:
                return arg0 - arg1;
            case MUL:
                return arg0 * arg1;
            case DIV:
                return arg0 / arg1;
            case MOD:
                return arg0 % arg1;
            case EQ:
                return arg0 == arg1;
            case NEQ:
                return arg0 != arg1;
            case LESS:
                return arg0 < arg1;
            case GREATER:
                return arg0 > arg1;
            case LE:
                return arg0 <= arg1;
            case GE:
                return arg0 >= arg1;
            default:
                throw new TemplateParsingException("Cannot apply " + current.getType() + " on " + arg0 + " and " + arg1, current);
        }
    }

    private static Object applyBinaryOp(long arg0, long arg1, ASTNode current) {
        switch (current.getType()) {
            case ADD:
                return arg0 + arg1;
            case SUB:
                return arg0 - arg1;
            case MUL:
                return arg0 * arg1;
            case DIV:
                return arg0 / arg1;
            case MOD:
                return arg0 % arg1;
            case EQ:
                return arg0 == arg1;
            case NEQ:
                return arg0 != arg1;
            case LESS:
                return arg0 < arg1;
            case GREATER:
                return arg0 > arg1;
            case LE:
                return arg0 <= arg1;
            case GE:
                return arg0 >= arg1;
            default:
                throw new TemplateParsingException("Cannot apply " + current.getType() + " on " + arg0 + " and " + arg1, current);
        }
    }

    private static Object applyBinaryOp(int arg0, int arg1, ASTNode current) {
        switch (current.getType()) {
            case ADD:
                return arg0 + arg1;
            case SUB:
                return arg0 - arg1;
            case MUL:
                return arg0 * arg1;
            case DIV:
                return arg0 / arg1;
            case MOD:
                return arg0 % arg1;
            case EQ:
                return arg0 == arg1;
            case NEQ:
                return arg0 != arg1;
            case LESS:
                return arg0 < arg1;
            case GREATER:
                return arg0 > arg1;
            case LE:
                return arg0 <= arg1;
            case GE:
                return arg0 >= arg1;
            default:
                throw new TemplateParsingException("Cannot apply " + current.getType() + " on " + arg0 + " and " + arg1, current);
        }
    }

    private static Object applyBinaryOp(String arg0, String arg1, ASTNode current) {
        switch (current.getType()) {
            case ADD:
                return arg0 + arg1;
            default:
                return applyBinaryOp(arg0, arg1, current);
        }
    }

    private static Object applyBinaryOp(boolean arg0, boolean arg1, ASTNode current) {
        switch (current.getType()) {
            case EQ:
                return arg0 == arg1;
            case NEQ:
                return arg0 != arg1;
            case AND:
                return arg0 && arg1;
            case OR:
                return arg0 || arg1;
            default:
                return applyBinaryOp(arg0, arg1, current);
        }
    }

    private static Object applyBinaryOp(Comparable arg0, Comparable arg1, ASTNode current) {
        switch (current.getType()) {
            case EQ:
                return arg0.equals(arg1);
            case NEQ:
                return !arg0.equals(arg1);
            case LESS:
                return arg0.compareTo(arg1) < 0;
            case GREATER:
                return arg0.compareTo(arg1) > 0;
            case LE:
                return arg0.compareTo(arg1) <= 0;
            case GE:
                return arg0.compareTo(arg1) >= 0;
            default:
                throw new TemplateParsingException("Cannot apply " + current.getType() + " on "
                    + arg0 + "[" + arg0.getClass() + "] and " + arg1 + "[" + arg1.getClass() + "]", current);
        }
    }

    private static Object applyBinaryOp(Object arg0, Object arg1, ASTNode current) {
        switch (current.getType()) {
            case EQ:
                return arg0.equals(arg1);
            case NEQ:
                return !arg0.equals(arg1);
            default:
                throw new TemplateParsingException("Cannot apply " + current.getType() + " on "
                    + arg0 + "[" + arg0.getClass() + "] and " + arg1 + "[" + arg1.getClass() + "]", current);
        }
    }

}
