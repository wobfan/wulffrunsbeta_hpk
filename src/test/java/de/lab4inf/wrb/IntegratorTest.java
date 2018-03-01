package de.lab4inf.wrb;

import org.junit.*;
import static org.junit.Assert.*;
import java.math.*;

public class IntegratorTest {
	
	private final double EPS = 1.E-5;

	Integrator integr;

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
		integr = new Integrator();
	}

	@Test
	public final void intSineTest() {
		double expected = -1.90009;
		Function f = getFunction("sin");
		double result = integr.integrate(f, 3.49, 6);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void intSine2PiTest() {
		double expected = -0.16093;
		Function f = getFunction("sin");
		double result = integr.integrate(f, Math.PI, 10);
		assertEquals(expected, result, EPS);
	}

	@Test
	public final void intCosTest() {
		double expected = 0.332895;
		double result = integr.integrate(getFunction("cos"), 4.211, 10);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void intTanTest() {
		double expected = 0.344366;
		double result = integr.integrate(getFunction("tan"), 2 * Math.PI, 5.5);
		assertEquals( expected, result, EPS);
	}
	
	@Test
	public final void intLogTest() {
		double expected = 19.4487;
		double result = integr.integrate(getFunction("log"), 2.5, 4 * Math.PI);
		assertEquals(expected, result, EPS);
	}
	
	@Test
	public final void intSquareTest() {
		double expected = 5166.666666667;
		double result = integr.integrate(getFunction("square"), 5, 25);
		assertEquals( expected, result,EPS);
	}
}
