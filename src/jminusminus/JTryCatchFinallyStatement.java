package jminusminus;

import java.util.ArrayList;

public class JTryCatchFinallyStatement extends JStatement{
    JBlock tryBlock;
    ArrayList<JBlock> catchBlocks;
    ArrayList<JFormalParameter> catchParas;
    JBlock finallyBlock;
    boolean noCatchs;
    boolean noFinally;
    
    public JTryCatchFinallyStatement(int line,JBlock tryBlock, ArrayList<JBlock> catchBlocks,
                                        ArrayList<JFormalParameter> catchParas,
                                        JBlock finallyBlock){
        super(line);
        noCatchs = false;
        noFinally = false;
        if((catchBlocks.size() == 0) && (catchParas.size() == 0))
            noCatchs = true;
        this.catchBlocks = catchBlocks;
        this.tryBlock = tryBlock;
        this.catchParas = catchParas;
        this.finallyBlock = finallyBlock;
    }
    
    public JTryCatchFinallyStatement(int line,JBlock tryBlock, ArrayList<JBlock> catchBlocks,
                                        ArrayList<JFormalParameter> catchParas){
        super(line);
        noCatchs = false;
        this.catchBlocks = catchBlocks;
        this.tryBlock = tryBlock;
        this.catchParas = catchParas;
        noFinally = true;
    }
    
    public JStatement analyze(Context context) {return this;}
    public void codegen(CLEmitter output) {}
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JTryCatchFinallyStatement  line = \"%d\" >\n",line());
        p.indentRight();
        
        p.printf("<TryBlock> \n");
        p.indentRight();
        tryBlock.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TryBlock> \n");
        
        if(!noCatchs){
            for(int i = 0; i < catchBlocks.size();i++){
                p.printf("<CatchBlock>\n");
                p.indentRight();
                catchParas.get(i).writeToStdOut(p);
                catchBlocks.get(i).writeToStdOut(p);
                p.indentLeft();
            }
        }
        if(!noFinally){
            p.printf("<FinallyBlock>\n");
            p.indentRight();
            finallyBlock.writeToStdOut(p);
            p.indentLeft();
        }
    }
}
