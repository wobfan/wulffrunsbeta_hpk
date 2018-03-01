package de.lab4inf.wrb;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.Math;

import de.lab4inf.wrb.WRBParser.*;

public class Visitor extends WRBBaseVisitor<Double> {
	private HashMap<String, Double> memory = new HashMap<>();
	private HashMap<String, Function> functions = new HashMap<>();

	public Visitor() {
		FunctionMap.initMap(this, functions);
	}
	
	@Override
	public Double visitAssignment(AssignmentContext ctx) {
		Integer number = null;
		String id = ctx.ID().getText();
		if (ctx.nmbr() != null)
			number = visit(ctx.nmbr()).intValue();
		Double value = visit(ctx.expr());
		if (number == null)
			memory.put(id, value);
		else
			memory.put(id + number, value);
		return value;
	}

	@Override
	public Double visitFunction(FunctionContext ctx) {
		Double expr = visit(ctx.expr(0));

		if (ctx.nmbr() != null && ctx.ID().getText().equals("log")) {
			Integer base = visit(ctx.nmbr()).intValue();

			return Math.log(expr) / Math.log(base);
		}
		else if (functions.containsKey(ctx.ID().getText())) {
			String name = ctx.ID().getText();
			Function function = functions.get(name);
			double arguments[] = new double[ctx.expr().size()];
			for (int i = 0; i < ctx.expr().size(); i++) {
				arguments[i] = visit(ctx.expr(i));
			}
			return function.eval(arguments);
		} else
			throw new IllegalArgumentException(String.format("%s is no known function", ctx.ID().getText()));
	}
	
	

	@Override
	public Double visitDiffint(DiffintContext ctx) {
		if (ctx.getChild(0).getText().equals("f'")) {
			if (ctx.expr().size() != 1) throw new IllegalArgumentException("too few/many arguments for differentiation: f'(function, value[, error])");
			if (!functions.containsKey(ctx.ID().getText())) throw new IllegalArgumentException(ctx.ID().getText() + " is no known function to differentiate.");
			
			Differentiator diff = new Differentiator();
			return diff.differentiate(functions.get(ctx.ID().getText()), visit(ctx.expr(0)));
		}
		else if (ctx.getChild(0).getText().equals("F")) {
			if (ctx.expr().size() != 2) throw new IllegalArgumentException("too few/many arguments for integration: F(function, start, end[, error])");
			if (!functions.containsKey(ctx.ID().getText())) throw new IllegalArgumentException(ctx.ID().getText() + " is no known function to differentiate.");
			Integrator integr = new Integrator();
			return integr.integrate(functions.get(ctx.ID().getText()), visit(ctx.expr(0)), visit(ctx.expr(1)));
		}
		return 0.0;
	}

	@Override
	public Double visitFunction_def(Function_defContext ctx) {
		String name = ctx.ID(0).getText();
		ArrayList<String> variables = new ArrayList<>();

		for (TerminalNode id : ctx.ID()) {
			variables.add(id.getText());
		}
		variables.remove(0);
		FunctionMap newFunc = new FunctionMap(this, variables, ctx.expr());
		functions.put(name, newFunc);
		return 0.0;
	}

	@Override
	public Double visitPower(PowerContext ctx) {

		Double exp[] = new Double[ctx.powtypes().size() - 1];
		Double base = visit(ctx.expr());
		Double currentExp = visit(ctx.powtypes(ctx.powtypes().size() - 1));

		for (int i = exp.length - 1; i >= 0; i--) {
			Double before = 0.0;
			if (visit(ctx.powtypes(i)) != null) before = visit(ctx.powtypes(i));
			else continue;
			currentExp = Math.pow(before, currentExp);
		}

		return Math.pow(base, currentExp);

	}

	@Override
	public Double visitPowtypes(PowtypesContext ctx) {
		if (ctx.getChildCount() == 1) {
			return visit(ctx.getChild(0));
		}
		else {
			return visit(ctx.getChild(1));
		}
	}

	@Override
	public Double visitCalculation(CalculationContext ctx) {
		Double value = visit(ctx.expr()); // evaluate the expr child
		return value;
	}

	@Override
	public Double visitBrackets(BracketsContext ctx) {
		return visit(ctx.expr());
	}

	@Override
	public Double visitDouble(DoubleContext ctx) {
		return visit(ctx.nmbr());
	}

	@Override
	public Double visitInt(IntContext ctx) {
		return Double.valueOf(ctx.getText());
	}

	@Override
	public Double visitPos_tenspotency(Pos_tenspotencyContext ctx) {
		Double base = Double.valueOf(ctx.getChild(0).getText());
		Double potency = Double.valueOf(ctx.getChild(2).getText());
		return base * (Math.pow(10, potency));
	}

	@Override
	public Double visitNeg_tenspotency(Neg_tenspotencyContext ctx) {
		Double base = Double.valueOf(ctx.getChild(0).getText());
		Double potency = Double.valueOf(ctx.getChild(2).getText());
		return base * (Math.pow(10, (-1) * potency));
	}

	@Override
	public Double visitSub(SubContext ctx) {
		Double value = 0.0;
		if (ctx.getChildCount() == 4)
			value = visit(ctx.expr());
		else
			value = visit(ctx.children.get(1));
		return (-1) * value;
	}

	@Override
	public Double visitId(IdContext ctx) {
		Double value;
		Integer number;
		String id = ctx.ID().getText();
		if (ctx.nmbr() != null) {
			number = visit(ctx.nmbr()).intValue();
			id = id + number;
		}
		if (memory.containsKey(id)) {
			value = memory.get(id);
			return value;
		}
		throw new IllegalArgumentException(String.format("variable %s is unknown", id));
	}

	@Override
	public Double visitMultiplication(MultiplicationContext ctx) {
		Double value;
		Double left = visit(ctx.expr(0));
		Double right = visit(ctx.expr(1));

		if (ctx.op.getText().equals("*"))
			value = left * right;
		else
			value = left / right;
		return value;
	}

	@Override
	public Double visitAddition(AdditionContext ctx) {
		Double value;
		Double left = visit(ctx.expr(0));
		Double right = visit(ctx.expr(1));
		if (ctx.op.getText().equals("+"))
			value = left + right;
		else
			value = left - right;
		return value;
	}

	public Set<String> getVariablesKeySet() {
		return memory.keySet();
	}

	public Set<String> getFunctionsKeySet() {
		return functions.keySet();
	}

	public HashMap<String, Double> getMemory() {
		return memory;
	}

	public Function getFunction(String name) {
		if (functions.containsKey(name)) return functions.get(name);
		throw new IllegalArgumentException(String.format("%s is no valid function.", name));
	}

	public HashMap<String, Function> getFunctionHashmap() {
		return functions;
	}

	public HashMap<String, Double> getVariablesHashmap() {
		return memory;
	}

	public void addVariable(String name, Double value) {
		memory.put(name, value);
	}

	public void addFunction(String name, Function funct) {
		functions.put(name, funct);
	}
}
