// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * This abstract base class is the AST node for a binary expression. 
 * A binary expression has an operator and two operands: a lhs and a rhs.
 */

abstract class JBinaryExpression extends JExpression {

    /** The binary operator. */
    protected String operator;

    /** The lhs operand. */
    protected JExpression lhs;

    /** The rhs operand. */
    protected JExpression rhs;

    /**
     * Constructs an AST node for a binary expression given its line number, the
     * binary operator, and lhs and rhs operands.
     * 
     * @param line
     *            line in which the binary expression occurs in the source file.
     * @param operator
     *            the binary operator.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    protected JBinaryExpression(int line, String operator, JExpression lhs,
            JExpression rhs) {
        super(line);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JBinaryExpression line=\"%d\" type=\"%s\" "
                + "operator=\"%s\">\n", line(), ((type == null) ? "" : type
                .toString()), Util.escapeSpecialXMLChars(operator));
        p.indentRight();
        p.printf("<Lhs>\n");
        p.indentRight();
        lhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Lhs>\n");
        p.printf("<Rhs>\n");
        p.indentRight();
        rhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Rhs>\n");
        p.indentLeft();
        p.printf("</JBinaryExpression>\n");
    }

}
class JUnsignedLeftShiftOp extends JBinaryExpression{
    public JUnsignedLeftShiftOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "<<<", lhs, rhs);
    }
    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }
        else if(lhs.type == Type.DOUBLE){
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        }else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        }
        return this;
    }
    public void codegen(CLEmitter output) {

    }
}

/**
 * The AST node for a unsigned right shift (>>>) expression.
 */
class JUnsignedRightShiftOp extends JBinaryExpression{
    public JUnsignedRightShiftOp(int line, JExpression lhs, JExpression rhs) {
        super(line, ">>>", lhs, rhs);
    }
    /**
     * Analyzing the >>> operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }
        else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        }
        return this;
    }
    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(IUSHR);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LUSHR);
    	}
    }
}
/**
 * The AST node for a left shift (<<) expression.
 */
class JLeftShiftOp extends JBinaryExpression{
    public JLeftShiftOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "<<", lhs, rhs);
    }
    /**
     * Analyzing the << operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    //Only long and int
    
    public JExpression analyze(Context context) {       
	    lhs = (JExpression) lhs.analyze(context);
	    rhs = (JExpression) rhs.analyze(context);
	    if(lhs.type == Type.LONG){
	        lhs.type().mustMatchExpected(line(), Type.LONG);
	        rhs.type().mustMatchExpected(line(), Type.LONG);
	        type = Type.LONG;
	    }
	    else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
	    	}
	    return this;
	    }
    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(ISHL);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LSHL);
    	}
    }
}
/**
 * The AST node for a right shift (>>) expression.
 */

class JRightShiftOp extends JBinaryExpression{
    public JRightShiftOp(int line, JExpression lhs, JExpression rhs) {
        super(line, ">>", lhs, rhs);
    }
    /**
     * Analyzing the >> operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    //only long and int
    public JExpression analyze(Context context) { 
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        	}
        return this;
        }
    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(ISHR);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LSHR);
    	}    	
    }
}
/**
 * The AST node for a reminder (%) expression.
 */
class JRemOp extends JBinaryExpression{
    public JRemOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "%", lhs, rhs);
    }
    
    /**
     * Analyzing the % operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }
        else if(lhs.type == Type.DOUBLE){
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        }else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        }
        return this;}
    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(IREM);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LREM);
    	}
    	else if (lhs.type == Type.DOUBLE){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(DREM);
    	}
    }
}
/**
 * The AST node for a division (/) expression.
 */

class JDivOp extends JBinaryExpression{
    public JDivOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "/", lhs, rhs);
    }
    public JExpression analyze(Context context) {return this;}
    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(IDIV);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LDIV);
    	}
    	else if (lhs.type == Type.DOUBLE){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(DDIV);
    	}
    }
}


/**
 * The AST node for a plus (+) expression. In j--, as in Java, + is overloaded
 * to denote addition for numbers and concatenation for Strings.
 */

class JPlusOp extends JBinaryExpression {

    /**
     * Constructs an AST node for an addition expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the addition expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JPlusOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "+", lhs, rhs);
    }

    /**
     * Analysis involves first analyzing the operands. If this is a string
     * concatenation, we rewrite the subtree to make that explicit (and analyze
     * that). Otherwise we check the types of the addition operands and compute
     * the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }
        else if(lhs.type == Type.DOUBLE){
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        }
        else if(lhs.type == Type.STRING || rhs.type == Type.STRING){
        	type = type.STRING;
        }
        else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        }
        return this;
    }

    /**
     * Any string concatenation has been rewritten as a 
     * {@link JStringConcatenationOp} (in {@code analyze}), so code generation 
     * here involves simply generating code for loading the operands onto the 
     * stack and then generating the appropriate add instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
    		if(rhs.type == Type.STRING){
    			output.addReferenceInstruction(NEW, "java/lang/StringBuilder");
    			output.addNoArgInstruction(DUP);
    			output.addMemberAccessInstruction(INVOKESPECIAL, 
    					"java/lang/StringBuilder", "<init>", "()V");
    			lhs.codegen(output);
    			output.addMemberAccessInstruction(INVOKEVIRTUAL, 
    					"java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
    			rhs.codegen(output);
    			output.addMemberAccessInstruction(INVOKEVIRTUAL,
    					"java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
    			output.addMemberAccessInstruction(INVOKEVIRTUAL,
    					"java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
    		}
    		else{
		        lhs.codegen(output);
		        rhs.codegen(output);
		        output.addNoArgInstruction(IADD);
    		}
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LADD);
    	}
    	else if (lhs.type == Type.DOUBLE){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(DADD);
    	}
    }

}

/**
 * The AST node for a subtraction (-) expression.
 */

class JSubtractOp extends JBinaryExpression {

    /**
     * Constructs an AST node for a subtraction expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the subtraction expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JSubtractOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "-", lhs, rhs);
    }

    /**
     * Analyzing the - operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }
        else if(lhs.type == Type.DOUBLE){
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        }else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        }
        return this;
    }

    /**
     * Generating code for the - operation involves generating code for the two
     * operands, and then the subtraction instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(ISUB);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LSUB);
    	}
    	else if (lhs.type == Type.DOUBLE){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(DSUB);
    	}
    }

}

/**
 * The AST node for a multiplication (*) expression.
 */

class JMultiplyOp extends JBinaryExpression {

    /**
     * Constructs an AST for a multiplication expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the multiplication expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JMultiplyOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "*", lhs, rhs);
    }

    /**
     * Analyzing the * operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if(lhs.type == Type.LONG){
            lhs.type().mustMatchExpected(line(), Type.LONG);
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
        }
        else if(lhs.type == Type.DOUBLE){
            lhs.type().mustMatchExpected(line(), Type.DOUBLE);
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
        }else{
	        lhs.type().mustMatchExpected(line(), Type.INT);
	        rhs.type().mustMatchExpected(line(), Type.INT);
	        type = Type.INT;
        }
        return this;
    }

    /**
     * Generating code for the * operation involves generating code for the two
     * operands, and then the multiplication instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
    	if(lhs.type == Type.INT){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(IMUL);
    	}else if(lhs.type == Type.LONG){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(LMUL);
    	}
    	else if (lhs.type == Type.DOUBLE){
	        lhs.codegen(output);
	        rhs.codegen(output);
	        output.addNoArgInstruction(DMUL);
    	}
    }

}
