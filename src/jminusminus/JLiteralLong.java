package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for an {@code int} literal.
 */

class JLiteralLong extends JExpression {

    /** String representation of the int. */
    private String text;

    /**
     * Constructs an AST node for an {@code long} literal given its line number 
     * and string representation.
     * 
     * @param line
     *            line in which the literal occurs in the source file.
     * @param text
     *            string representation of the literal.
     */

    public JLiteralLong(int line, String text) {
        super(line);
        this.text = text;
    }

    /**
     * Analyzing an long literal is trivial.
     * 
     * @param context
     *            context in which names are resolved (ignored here).
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        type = Type.LONG;
        return this;
    }
    
    public long toNum(){
    	return Long.parseLong(text);
    }

    /**
     * Generating code for an long literal means generating code to push it onto
     * the stack.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
    	if(text.contains("L") || text.contains("l")){
    		text = text.substring(0, text.length() - 1);
    	}
    	long i = Long.parseLong(text);
    	if(i == 0 ) output.addNoArgInstruction(LCONST_0);
    	else if (i == 1) output.addNoArgInstruction(LCONST_1);
    	else
    		output.addLDCInstruction(i);
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JLiteralLong line=\"%d\" type=\"%s\" " + "value=\"%s\"/>\n",
                line(), ((type == null) ? "" : type.toString()), text);
    }

}
