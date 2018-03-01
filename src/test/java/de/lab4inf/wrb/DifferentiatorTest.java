package de.lab4inf.wrb;

import org.junit.*;
import static org.junit.Assert.*;
import java.math.*;

public class DifferentiatorTest {
	
	private final double EPS = 1.E-6;

	Differentiator diff;

	private Function getFunction(String name) {
		if (name.equals("sin")) 
			return new Function() {
			@Override
			public double eval(double... args) {
				return Math.sin(args[0]);
			}
		};
		else if (name.equals("tan"))
			return new Function() {
			@Override
			public double eval(double... args) {
				return Math.tan(args[0]);
			}
		};
		else if (name.equals("cos"))
			return new Function() {
			@Override
			public double eval(double... args) {
				return Math.cos(args[0]);
			}
		};
		else if (name.equals("square"))
			return new Function() {
			@Override
			public double eval(double... args) {
				return Math.pow(args[0], 2);
			}
		};
		else if (name.equals("sqrt"))
			return new Function() {
			@Override
			public double eval(double... args) {
				return Math.sqrt(args[0]);
			}
		};
		else if (name.equals("log"))
			return new Function() {
			@Override
			public double eval(double... args) {
				return Math.log(args[0]);
			}
		};
		return null;
	}


	@Before
	public final void setUp() throws Exception {
		diff = new Differentiator();
	}

	@Test
	public final void diffSineTest() {
		double expected = -0.939918;
		Function f = getFunction("sin");
		double result = diff.differentiate(f, 3.49);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void diffSine2PiTest() {
		double expected = 1;
		Function f = getFunction("sin");
		double result = diff.differentiate(f, 2 * Math.PI);
		assertEquals(expected, result, EPS);
	}

	@Test
	public final void diffCosTest() {
		double expected = 0.876916;
		double result = diff.differentiate(getFunction("cos"), 4.211);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void diffTanTest() {
		double expected = 1.0;
		double result = diff.differentiate(getFunction("tan"), 2 * Math.PI);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void diffLogTest() {
		double expected = 0.4;
		double result = diff.differentiate(getFunction("log"), 2.5);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void diffSquareTest() {
		double expected = 43030;
		double result = diff.differentiate(getFunction("square"), 21515);
		assertEquals(expected, result, EPS);
	}
}
