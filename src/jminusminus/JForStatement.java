// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for an for-statement.
 */

class JForStatement extends JStatement {
    JStatement initStatement;
    JVariableDeclaration initDecl;
    JExpression test;
    JStatement update;
    JStatement body;
    boolean isInitDecl;
    
    /**
     * Constructs an AST node for a while-statement given its line number, the
     * test expression, and the body.
     * 
     * @param line
     *            line in which the for-statement occurs in the source file.
     * @param test
     *            test expression.
     * @param body
     *            the body.
     *  @param initDecl
     *            the initial value declarator.
     *  @param update
     *  		   update statement
     *  		          
     */

    
    public JForStatement(int line, JStatement initStatement, JExpression test,
                            JStatement update, JStatement body){
        super(line);
        this.initStatement = initStatement;
        this.test = test;
        this.update = update;
        this.body = body;
        isInitDecl = false;
    }
    public JForStatement(int line, JVariableDeclaration initDecl, JExpression test,
                            JStatement update, JStatement body){
        super(line);
        this.initDecl = initDecl;
        this.test = test;
        this.update = update;
        this.body = body;
        isInitDecl = true;
    } 
    
    /**
     * Analyzing the for-statement means analyzing its components and checking
     * that the test is a boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    
    public JStatement analyze(Context context) {
    	initStatement = (JStatement) initStatement.analyze(context);
    	test = test.analyze(context);
    	test.type().mustMatchExpected(line, Type.BOOLEAN);
    	update = (JStatement) update.analyze(context);
    	body = (JStatement) body.analyze(context);
    	return this;
    	}
    
    /**
     * Code generation for an for-statement. 
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    
    public void codegen(CLEmitter output) {
    	initStatement.codegen(output);
    	
    	String cond = output.createLabel();
    	String out = output.createLabel();
    	
    	output.addLabel(cond);
    	test.codegen(output, out, false);
    	body.codegen(output);
    	update.codegen(output);
    	
    	output.addBranchInstruction(GOTO, cond);
    	output.addLabel(out);
    	
    }
    
    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JForStatement line=\"%d\">\n", line());
        
        p.indentRight();
        p.printf("<InitialExpression>>\n");
        p.indentRight();
        if (isInitDecl)
            initDecl.writeToStdOut(p);
        else
            initStatement.writeToStdOut(p);
        p.indentLeft();
        p.printf("</InitialExpression>>\n");
        
        
        p.indentRight();
        p.printf("<TestExpression>\n");
        p.indentRight();
        test.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        
        p.printf("<UpdateExpression>>\n");
        p.indentRight();
        update.writeToStdOut(p);
        p.indentLeft();
        p.printf("</UpdateExpression>>\n");

        p.indentRight();
        body.writeToStdOut(p);
        p.indentLeft();

        p.indentLeft();
        p.printf("</JForStatement>\n");
    }
}
