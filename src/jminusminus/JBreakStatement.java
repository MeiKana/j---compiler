package jminusminus;

public class JBreakStatement extends JStatement{
    public JBreakStatement(int line){
        super(line);
    }
    public JStatement analyze(Context context){
        return this;
    }
    public void codegen(CLEmitter output){}
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JBreakStatement line=\"%d\" />\n", line());
     }
}
