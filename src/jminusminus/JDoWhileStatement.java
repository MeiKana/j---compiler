package jminusminus;

public class JDoWhileStatement extends JStatement{
    JStatement body;
    JExpression condition;
    
    public JDoWhileStatement(int line, JStatement body, JExpression condition){
        super(line);
        this.body = body;
        this.condition = condition;
    }
    public void codegen(CLEmitter c){}
    public JStatement analyze(Context context)
        {return this;}
    
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
