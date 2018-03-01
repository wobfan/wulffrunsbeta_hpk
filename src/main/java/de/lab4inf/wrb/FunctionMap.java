package de.lab4inf.wrb;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class FunctionMap implements Function {

	private ArrayList<String> variables;
	private ParseTree tree;
	
	private Visitor mainVisitor;
	
	public FunctionMap(Visitor mainVisitor, ArrayList<String> variables, ParseTree tree) {
		this.mainVisitor = mainVisitor;
		this.variables = variables;
		this.tree = tree;
		
		
	}

	@Override
	public double eval(double... args) {
		Visitor visitor = new Visitor();
		if (variables.size() != args.length) throw new IllegalArgumentException("too many or too few arguments");
		Map<String, Double> varCopy = mainVisitor.getVariablesHashmap();
		Set<String> varCopyKeys = varCopy.keySet();
		Map<String, FunctionMap> funcCopy = mainVisitor.getFunctionHashmap();
		Set<String> funcCopyKeys = funcCopy.keySet();
		for (String key : funcCopyKeys) {
			visitor.addFunction(key, funcCopy.get(key));
		}
		for (String key : varCopyKeys) {
			visitor.addVariable(key, varCopy.get(key));
		}
		for (int i = 0; i < args.length; i++) {
			visitor.addVariable(variables.get(i), args[i]);
		}
		return visitor.visit(tree);
	}
}
