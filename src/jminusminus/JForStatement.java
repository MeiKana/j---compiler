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
    
    public JStatement analyze(Context context) {return this;}
    
    public void codegen(CLEmitter output) {}
    
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
