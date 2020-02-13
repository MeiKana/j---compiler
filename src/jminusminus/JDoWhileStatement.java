package jminusminus;

// AST tree for do-while statement.

public class JDoWhileStatement extends JStatement{
    JStatement body;
    JExpression condition;
    
    /**
     * Constructs an AST node for a DO-While-statement given its line number, the
     * test expression, and the body.
     * 
     * @param line
     *            line in which the for-statement occurs in the source file.
     * @param test
     *            test expression.
     * @param body
     *            the body.
     *  		          
     */
    
    public JDoWhileStatement(int line, JStatement body, JExpression condition){
        super(line);
        this.body = body;
        this.condition = condition;
    }
    /**
     * Generates code for the do-while loop.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    
    public void codegen(CLEmitter output){
    	String loop = output.createLabel();
    	output.addLabel(loop);
    	body.codegen(output);
    	condition.codegen(output,loop,true);
    }
    /**
     * Analysis involves analyzing the test, checking its type and analyzing the
     * body statement.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    
    public JStatement analyze(Context context){
    	body = (JStatement) body.analyze(context);
        condition = condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
         return this;
        }
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JDoWhileStatemnet line=%d > \n ", line());
        p.indentRight();
        body.writeToStdOut(p);
        
        p.printf("<TestExpression> \n");
        p.indentRight();
        condition.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression> \n");
        p.indentLeft();
        
        p.printf("</JDoWhileStatemnet> \n");
        
    }
}
