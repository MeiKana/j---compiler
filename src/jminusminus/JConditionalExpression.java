package jminusminus;


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
    public JExpression analyze(Context context)
        {return this;
        }
        
    public void codegen(CLEmitter c){
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
