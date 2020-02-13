package jminusminus;

import java.util.ArrayList;
import static jminusminus.CLConstants.*;

public class JInterfaceDeclaration extends JAST implements JTypeDecl {
    String name;
    public Type superType;
    public Type thisType;
    private ArrayList<JMember> classBlock;
    
    public JInterfaceDeclaration(int line, String name, Type superType, 
                         ArrayList<JMember> classBlock)
    {
        super(line);
        this.name = name;
        this.superType = superType;
        this.classBlock = classBlock;
    }
    
    public JAST analyze(Context context){
        return this;
    }
    public String name(){
        return name;
    }
    public Type superType(){
        return superType();
    }
    public Type thisType(){
        return thisType;
    }
    public void declareThisType(Context context){}
    
    public void preAnalyze(Context context){}
    
    public void codegen(CLEmitter outpout){}
    
    public void writeToStdOut(PrettyPrinter p){
        p.printf("<JInterfaceDeclaration line=\"%d\" name=\"%s\""
                + " super=\"%s\">\n", line(), name, superType.toString());
        p.indentRight();
        if (classBlock != null) {
            p.println("<InterfaceBlock>");
            p.indentRight();
            for (JMember member : classBlock) {
                ((JAST) member).writeToStdOut(p);
            }
            p.indentLeft();
            p.println("</InterfaceBlock>");
        }
        p.indentLeft();
        p.println("</JInterfaceDeclaration>");
    }
}
