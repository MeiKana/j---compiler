package jminusminus;

import java.util.Hashtable;
import java.util.ArrayList;
public class JSwitchStatement extends JStatement{
    JExpression condition;
    Hashtable<Integer,JExpression> caseList;
    Hashtable<Integer,ArrayList<JStatement>> statementList;
    int caseNum;
    
    public JSwitchStatement (int line, JExpression condition, Hashtable<Integer,JExpression> caseList,
                                    Hashtable<Integer,ArrayList<JStatement>> statementList, int caseNum)
    {
        super(line);
        this.caseList = caseList;
        this.statementList = statementList;
        this.caseNum = caseNum;
        this.condition = condition;
    }
    
    public JStatement analyze(Context context){
        return this;
    }
    
    public void codegen(CLEmitter output){}
    
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
