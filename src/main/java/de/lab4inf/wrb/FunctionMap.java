package de.lab4inf.wrb;

import java.util.ArrayList;
import java.util.HashMap;
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

	public FunctionMap(ArrayList<String> variables, String evaluation) {
		mainVisitor = null;
		this.variables = variables;

		WRBLexer lexer = new WRBLexer(new ANTLRInputStream(evaluation));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		WRBParser parser = new WRBParser(tokens);
		ErrorListener errList = new ErrorListener(); 

		parser.removeErrorListeners();   
		parser.addErrorListener(errList);
		lexer.removeErrorListeners();
		lexer.addErrorListener(errList);	

		tree = parser.prog();
	}

	public static void initMap(Visitor parent, HashMap<String, Function> fncMap) {
		fncMap.put("max", new Function() {
			@Override
			public double eval(double... args) {
				if (args.length == 2) {
					return Math.max(args[0], args[1]);
				}
				double[] values = new double[2];
				values[0] = Math.max(args[0], args[1]);
				values[1] = args[2];
				for (int i = 3; i < args.length; i++) {
					values[0] = Math.max(values[0], values[1]);
					values[1] = args[i];
				}
				return Math.max(values[0], values[1]);
			}
		});

		fncMap.put("min", new Function() {
			@Override
			public double eval(double... args) {
				if (args.length == 2) {
					return Math.min(args[0], args[1]);
				}
				double[] values = new double[2];
				values[0] = Math.min(args[0], args[1]);
				values[1] = args[2];
				for (int i = 3; i < args.length; i++) {
					values[0] = Math.min(values[0], values[1]);
					values[1] = args[i];
				}
				return Math.min(values[0], values[1]);
			}
		});

		fncMap.put("abs", new Function() {
			@Override
			public double eval(double... args) {
				return Math.abs(args[0]);
			}
		});

		fncMap.put("sin", new Function() {
			@Override
			public double eval(double... args) {
				return Math.sin(args[0]);
			}
		});

		fncMap.put("sinh", new Function() {
			@Override
			public double eval(double... args) {
				return Math.sinh(args[0]);
			}
		});

		fncMap.put("cos", new Function() {
			@Override
			public double eval(double... args) {
				return Math.cos(args[0]);
			}
		});

		fncMap.put("cosh", new Function() {
			@Override
			public double eval(double... args) {
				return Math.cosh(args[0]);
			}
		});

		fncMap.put("tan", new Function() {
			@Override
			public double eval(double... args) {
				return Math.tan(args[0]);
			}
		});

		fncMap.put("tanh", new Function() {
			@Override
			public double eval(double... args) {
				return Math.tan(args[0]);
			}
		});

		fncMap.put("asin", new Function() {
			@Override
			public double eval(double... args) {
				return Math.asin(args[0]);
			}
		});

		fncMap.put("acos", new Function() {
			@Override
			public double eval(double... args) {
				return Math.acos(args[0]);
			}
		});

		fncMap.put("atan", new Function() {
			@Override
			public double eval(double... args) {
				return Math.atan(args[0]);
			}
		});

		fncMap.put("pow", new Function() {
			@Override
			public double eval(double... args) {
				return Math.pow(args[0], args[1]);
			}
		});

		fncMap.put("sqrt", new Function() {
			@Override
			public double eval(double... args) {
				return Math.sqrt(args[0]);
			}
		});

		fncMap.put("exp", new Function() {
			@Override
			public double eval(double... args) {
				return Math.exp(args[0]);
			}
		});

		fncMap.put("expm1", new Function() {
			@Override
			public double eval(double... args) {
				return Math.expm1(args[0]);
			}
		});

		fncMap.put("ln", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log(args[0]);
			}
		});

		fncMap.put("logE", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log(args[0]);
			}
		});

		fncMap.put("log", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log10(args[0]);
			}
		});

		fncMap.put("log10", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log10(args[0]);
			}
		});

		fncMap.put("log2", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log10(args[0])/Math.log10(2);
			}
		});

		fncMap.put("lb", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log10(args[0])/Math.log10(2);
			}
		});

		fncMap.put("ld", new Function() {
			@Override
			public double eval(double... args) {
				return Math.log10(args[0])/Math.log10(2);
			}
		});
	}

	@Override
	public double eval(double... args) {
		Visitor visitor = new Visitor();
		if (variables.size() != args.length) throw new IllegalArgumentException("too many or too few arguments");
		if (mainVisitor != null) {
			Map<String, Double> varCopy = mainVisitor.getVariablesHashmap();
			Set<String> varCopyKeys = varCopy.keySet();
			Map<String, Function> funcCopy = mainVisitor.getFunctionHashmap();
			Set<String> funcCopyKeys = funcCopy.keySet();
			for (String key : funcCopyKeys) {
				visitor.addFunction(key, funcCopy.get(key));
			}
			for (String key : varCopyKeys) {
				visitor.addVariable(key, varCopy.get(key));
			}
		}
		for (int i = 0; i < args.length; i++) {
			visitor.addVariable(variables.get(i), args[i]);
		}
		return visitor.visit(tree);
	}
}
