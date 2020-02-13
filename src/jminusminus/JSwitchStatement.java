package jminusminus;

import java.util.Hashtable;
import java.util.Set;
import java.util.ArrayList;
import static jminusminus.CLConstants.*;

/* AST node for switch statement */

public class JSwitchStatement extends JStatement{
    JExpression condition;
    Hashtable<Integer,JExpression> caseList;
    Hashtable<Integer,ArrayList<JStatement>> statementList;
    int caseNum;
    int max;
    int min;
    
    /**
     * Constructs an AST node for a switch statement given its line number, list of case and statement,
     * the condition and default case
     * 
     * @param line
     *            line in which the switch-statement occurs in the source file.
     * @param  condtion
     * 			  test condtion
     * @param caseList
     *            List of case
     *            
     * @param statementList
     * 			List of case statements
     * 
     * @param caseNum
     * 			number of case
     */
    
    public JSwitchStatement (int line, JExpression condition, Hashtable<Integer,JExpression> caseList,
                                    Hashtable<Integer,ArrayList<JStatement>> statementList, int caseNum)
    {
        super(line);
        this.caseList = caseList;
        this.statementList = statementList;
        this.caseNum = caseNum;
        this.condition = condition;
    }
    /**
     * Analysis involves analyzing the test, checking its type and analyzing the
     * statement. Also, find the largest test condition and the smallest condition.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    
    public JStatement analyze(Context context){
    	max = Integer.MIN_VALUE;
    	min = Integer.MAX_VALUE;
    	int temp;
        condition = condition.analyze(context);
        Set<Integer> list = caseList.keySet();
        for(Integer i: list){
        	JExpression expr = caseList.get(i);
        	expr = caseList.get(i).analyze(context);
        	caseList.put(i, expr);
        	temp = getCaseValue(expr);
        	if(temp > max)
        		max = temp;
        	if(temp < min)
        		min = temp;
        	if(statementList.containsKey(i)){
        		for(JStatement stat: statementList.get(i)){
        			stat = (JStatement) stat.analyze(context);
        		}
        	}
        }
        //analyze default
        if(statementList.containsKey(0)){
    		for(JStatement stat: statementList.get(0)){
    			stat = (JStatement) stat.analyze(context);
    		}
        }
        return this;
    }
    //helper method to get the case number.
    private int getCaseValue(JExpression expr){
    	int res = 0;
    	if(expr.type().equals(Type.INT)){
    		res = ((JLiteralInt) expr).toNum();
    	}
    	else if(expr.type().equals(Type.LONG)){
    		res = (int) ((JLiteralLong) expr).toNum();
    	}
    	else if(expr.type().equals(Type.DOUBLE)){
    		res = (int) ((JLiteralDouble) expr).toNum();
    	}
    	else if(expr.type().equals(Type.CHAR)){
    		res = (int) ((JLiteralChar) expr).toNum();
    	}
    	return res;
    }
    /**
     * Generates code for the switch statement.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    
    public void codegen(CLEmitter output){
    	String defaultLabel = output.createLabel();
    	ArrayList<String> labels = new ArrayList<String>(caseNum);
    	String[] labelsTemp = new String[caseNum];
    	Set<Integer> list = statementList.keySet();
    	String endLabel = output.createLabel();
    	
    	condition.codegen(output);
    	
    	//making labels.
    	String label = "123";
    	int currentStatIndex = 0;
    	for(int i = caseNum; i  > 0; i--){
    		if(statementList.containsKey(i)){
    			currentStatIndex = i;
    			String newLabel = output.createLabel();
    			label = newLabel;
    		}
    		labelsTemp[i - 1] = label;
    	}
    	for(String label1: labelsTemp){
    		labels.add(label1);
    	}
    	output.addTABLESWITCHInstruction(defaultLabel, min, max, labels);
    	
    	//generating code for each statements in cases
    	for(int i = 1; i <= caseNum; i++ ){
    		if(statementList.containsKey(i)){
    			output.addLabel(labels.get(i - 1));
        		for(JStatement stat:statementList.get(i)){
        			stat.codegen(output);
        			//if it's a break 
        			if (stat instanceof JBreakStatement){
        				output.addBranchInstruction(GOTO, endLabel);
        			}
        		}
    		}else
    			continue;
    	}
    	//generating default case if it have.
    	if (statementList.containsKey(0)){
    		output.addLabel(defaultLabel);
    		for(JStatement stat:statementList.get(0))
    			stat.codegen(output);
    	}
    	output.addLabel(endLabel);
    }
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JSwitchStatement line=\"%d\" >\n", line());
        p.indentRight();
        
        p.printf("<TestExpression>\n");
        p.indentRight();
        condition.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        
        p.printf("<SwitchBlockStatementGroup>\n");
        p.indentRight();
        for(int i = 1; i <= caseNum; i++){
            p.printf("<CaseLabel>\n");
            p.indentRight();
            caseList.get(i).writeToStdOut(p);
            p.indentLeft();
            p.printf("</CaseLabel>\n");
            if(statementList.containsKey(i)){
                 for(int j = 0; j < statementList.get(i).size(); j++){
                    p.printf("<Body>\n");
                    p.indentRight();
                    statementList.get(i).get(j).writeToStdOut(p);
                    p.indentLeft();
                    p.printf("</Body>\n");    
                }
            }
        }
        if (statementList.containsKey(0)){
            p.printf("<DefaultLabel/> \n");
            p.indentRight();
            for (int i = 0; i < statementList.get(0).size(); i++){
                p.printf("<Body>\n");
                p.indentRight();
                statementList.get(0).get(i).writeToStdOut(p);
                p.indentLeft();
                p.printf("</Body>\n");   
                }
            p.indentLeft();
        }
        p.indentLeft();
        p.printf("</SwitchBlockStatementGroup>\n");
        p.indentLeft();
        p.printf("</JSwitchStatement>\n");
        
    }
}
