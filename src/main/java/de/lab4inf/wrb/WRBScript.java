package de.lab4inf.wrb;


import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class WRBScript implements Script {
	private WRBLexer lexer;
	private CommonTokenStream tokens;
	private WRBParser parser;
	private ParseTree tree;
	ErrorListener errList = new ErrorListener(); 
	private Visitor visitor = new Visitor();

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String [] args) throws Exception {
		WRBScript worker = new WRBScript();
		System.out.println("Enter evaluations (exit by an empty newline, enter 0 to escape): ");
		while (true) {
			StringBuilder eval = new StringBuilder();
			String lastInput = new String();
			while(true) {
				String line = scanner.nextLine();
				lastInput = line;
				if (line.length() > 0) eval.append(line);
				else break;
			}
			//ANTLRInputStream input = new ANTLRInputStream(scanner.nextLine());	// read from STDIN, EOF = Ctrl+D in Linux

			//ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("/home/stefan/prak.demo"));

			if (lastInput.equals("0")) break;
			
			System.out.println(worker.parse(eval.toString()));

			//print tree in LISP style for debugging
			//System.out.println(worker.tree.toStringTree(worker.parser)); 

			//ParseTreeWalker walker = new ParseTreeWalker(); 
			//walker.walk(new Main(), tree);
		}
	}

	@Override
	public double parse(String definition) {
		lexer = new WRBLexer(new ANTLRInputStream(definition));
		return visitAndReturnResult();
	}

	@Override
	public double parse(InputStream defStream) throws IOException {
		lexer = new WRBLexer(new ANTLRInputStream(defStream));
		return visitAndReturnResult();
	}

	private double visitAndReturnResult() {
		tokens = new CommonTokenStream(lexer);
		parser = new WRBParser(tokens);
		attachErrorListener();
		tree = parser.prog();
		return visitor.visit(tree);
	}

	private void attachErrorListener() {
		parser.removeErrorListeners();   
		parser.addErrorListener(errList);
		lexer.removeErrorListeners();
		lexer.addErrorListener(errList);
	}

	@Override
	public Set<String> getFunctionNames() {
		return visitor.getFunctionsKeySet();
	}

	@Override
	public Set<String> getVariableNames() {
		return visitor.getVariablesKeySet();	
	}

	@Override 
	public void setFunction(String name, Function fct) {
		visitor.addFunction(name, fct); 
	}

	@Override
	public Function getFunction(String name) {
		return visitor.getFunction(name);
	}

	@Override
	public double getVariable(String name) {
		return parse(name);
	}

	@Override
	public void setVariable(String key, double value) {
		parse(key + "=" + value);
	}

	@Override
	public Script concat(Script that) {
		Script newScript = new WRBScript();
		for (String varName : this.getVariableNames()) {
            double var = this.getVariable(varName);
            newScript.setVariable(varName, var);
        }
		for (String varName : that.getVariableNames()) {
            double var = that.getVariable(varName);
            newScript.setVariable(varName, var);
        }

		for (String fctName : this.getFunctionNames()) {
            Function var = this.getFunction(fctName);
            newScript.setFunction(fctName, var);
        }
		for (String fctName : that.getFunctionNames()) {
            Function var = that.getFunction(fctName);
            newScript.setFunction(fctName, var);
        }
        
        return newScript;
	};

}
