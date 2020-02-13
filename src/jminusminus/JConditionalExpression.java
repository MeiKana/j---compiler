package jminusminus;
import static jminusminus.CLConstants.GOTO;

class JConditionalExpression extends JExpression{
    JExpression test;
    JExpression trueExp;
    JExpression falseExp;
    Type type;
    
    public JConditionalExpression(int line, JExpression exp, JExpression trueExp, JExpression falseExp){
        super(line);
        test = exp;
        this.trueExp = trueExp;
        this.falseExp = falseExp;        
    }
    
    /**
     * Analyzing the condition-expression means analyzing its components and checking
     * that the test is a boolean. Also set the type() to boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context){
    		
    		test = (JExpression) test.analyze(context);
    		test.type().mustMatchExpected(line, Type.BOOLEAN);
    		trueExp = (JExpression) trueExp.analyze(context);
    		falseExp = (JExpression) falseExp.analyze(context);
    		this.type = trueExp.type();
    		return this;
        }
    
    public Type type(){
    	return type;
    }
    /**
     * Code generation for an condition-expression. We generate code to branch over the
     * consequent if !test; the consequent is followed by an unconditonal branch
     * over (any) alternate.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

        
    public void codegen(CLEmitter output){
        String falseLabel = output.createLabel();
        String endLabel = output.createLabel();
        test.codegen(output, falseLabel, false);
        trueExp.codegen(output);
        output.addBranchInstruction(GOTO, endLabel);
        output.addLabel(falseLabel);
        falseExp.codegen(output);
        output.addLabel(endLabel);
    }
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JConditionalExpression line=%d type=\"%s\" operator=\"?\"> \n ", line(), ((type == null) ? "" : type.toString()));
        p.indentRight();
        p.printf("<TestExpression>\n");
        p.indentRight();
        test.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        p.indentLeft();
        p.indentRight();
        p.printf("<TrueClause>\n");
        p.indentRight();
        trueExp.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TrueClause>\n");
        p.printf("<FalseClause>\n");
        p.indentRight();
        falseExp.writeToStdOut(p);
        p.indentLeft();
        p.printf("</FalseClause>\n");
        p.indentLeft();
        p.printf("</JConditionalExpression>\n");
    }
}
