package jminusminus;

import static jminusminus.CLConstants.*;

public class JThrowStatement extends JStatement {
	private JExpression exct;
	
	public JThrowStatement(int line,JExpression exct) {
		super(line);
		// TODO Auto-generated constructor stub
		this.exct = exct;
	}

	@Override
	public JAST analyze(Context context) {
		// TODO Auto-generated method stub
		exct = exct.analyze(context);
		return this;
	}

	@Override
	public void codegen(CLEmitter output) {
		// TODO Auto-generated method stub
		exct.codegen(output);
		output.addNoArgInstruction(ATHROW);
	}

	@Override
	public void writeToStdOut(PrettyPrinter p) {
		// TODO Auto-generated method stub
		p.println("<JThrowStatement>");
		p.indentRight();
		exct.writeToStdOut(p);
		p.indentLeft();
		p.println("</JThrowStatement>");
	}

}
