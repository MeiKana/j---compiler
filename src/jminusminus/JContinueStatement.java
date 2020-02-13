package jminusminus;

public class JContinueStatement extends JStatement{
    public JContinueStatement(int line){
        super(line);
    }
    public JStatement analyze(Context context){
        return this;
    }
    public void codegen(CLEmitter output){}
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JCoutinueStatement line=\"%d\" />\n", line());
     }
}
