package jminusminus;

import java.util.ArrayList;
import static jminusminus.CLConstants.*;

//AST node of try-catch statement.

public class JTryCatchFinallyStatement extends JStatement{
    JBlock tryBlock;
    ArrayList<JBlock> catchBlocks;
    ArrayList<JFormalParameter> catchParas;
    JBlock finallyBlock;
    boolean noCatchs;
    boolean noFinally;
    
    /**
     * Constructs an AST node for a try-catch-statement given its line number, the
     * try block, catch blocks and finnally block.
     * 
     * @param line
     *            line in which the try-statement occurs in the source file.
     * @param tryBlock
     *            try block
     * @param catchBlocks
     *            List of catch blocks
     *            
     * @param catchParas
     * 			List of catch parameters 
     * 
     * @param finallyBlock
     * 			block of finally 
     */
    
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
    /**
     * Analysis involves analyzing the test, checking its type and analyzing the
     * try catch and finally statement. And analyze the  JFormalParameter here.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    
    public JStatement analyze(Context context) {
    	tryBlock = tryBlock.analyze(context);
    	if(!noCatchs)
	    	for(int i = 0; i < catchBlocks.size(); i++){
	    		int offset = ((LocalContext) context).nextOffset(catchParas.get(i).type());
	            LocalVariableDefn defn = new LocalVariableDefn(catchParas.get(i).type().resolve(
	                    context), offset);
	            defn.initialize();
	            context.addEntry(catchParas.get(i).line(), catchParas.get(i).name(), defn);
	    		JBlock catchBlock = catchBlocks.get(i).analyze(context);
	    		catchBlocks.set(i, catchBlock);
	    	}
    	if(!noFinally)
    		finallyBlock = finallyBlock.analyze(context);
    	
    	return this;
    	}
    
    /**
     * Generates code for the Exception Handler
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    
    public void codegen(CLEmitter output) {
    	String endLabel = output.createLabel();
    	String tryStartLabel = output.createLabel();
    	String tryEndLabel = output.createLabel();
    	
    	output.addLabel(tryStartLabel);
    	tryBlock.codegen(output);
    	output.addLabel(tryEndLabel);
    	if(!noFinally)
    		finallyBlock.codegen(output);
    	output.addBranchInstruction(GOTO, endLabel);
    	if(noCatchs){
    		for(int i = 0; i < catchBlocks.size(); i++){
    			String catchTargetLabel = output.createLabel();
    			output.addLabel(catchTargetLabel);
    			output.addExceptionHandler(tryStartLabel, tryEndLabel, catchTargetLabel, "java/lang/Exception");
    			
    			catchBlocks.get(i).codegen(output);
    			catchParas.get(i).codegen(output);
    			if(!noFinally)
    				finallyBlock.codegen(output);
    			output.addBranchInstruction(GOTO, endLabel);
    		}
    	}
    	output.addLabel(endLabel);
    	
    }
    
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
