package de.lab4inf.wrb;

import static java.lang.Math.floor;
import static java.lang.Math.random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OurTest {
	final double eps = 1.E-8;
	Script script;

	private static long[] pow10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 100000000, 10000000000L };
	private static final int DEFAULT_DIGITS = 3;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public final void setUp() throws Exception {
		script = getScript();
		assertNotNull("no script implementation", script);
	}

	protected static double rnd(final int position) {
		if (position < 0 || position >= pow10.length) {
			String msg = String.format("wrong number of digits %d", position);
			throw new IllegalArgumentException(msg);
		}
		final long p10 = pow10[position];
		return floor((random() - 0.5) * (p10 << 1)) / p10;
	}

	protected static double rnd() {
		return rnd(DEFAULT_DIGITS);
	}

	protected Script getScript() {
		return new WRBScript();
	}

	@Test
	public final void testMultipleVars() throws Exception {
		String task = "s(x)=sin(x); f(x,y,z)=x*y^z; f(1,2,3)";
		assertEquals(1 * Math.pow(2, 3), script.parse(task), eps);
	}

	@Test
	public final void testFctInFctInFct() throws Exception {
		String task = "s(x)=sin(x); f(x)=s(x); a(y)=f(y); a(1)";
		assertEquals(Math.sin(1.0), script.parse(task), eps);
	}

	@Test
	public final void testAbs() throws Exception {
		String task = "abs(-1.5352)";
		assertEquals(Math.abs(-1.5352), script.parse(task), eps);
	}

	@Test
	public final void testFloor() throws Exception {
		String task = "floor(2.538569);";
		assertEquals(2, script.parse(task), eps);
	}

	@Test
	public final void testSin() throws Exception {
		String task = "sin(3)+sin(4);";
		assertEquals(Math.sin(3) + Math.sin(4), script.parse(task), eps);
	}

	@Test
	public final void testSomeFct() throws Exception {
		String task = "f(x)= x + x^2; f(2);";
		assertEquals(6.0, script.parse(task), eps);
	}

	@Test
	public final void testPowOrder() throws Exception {
		String task = "f(x)=x^2^3; f(10);";
		assertEquals(Math.pow(10, Math.pow(2, 3)), script.parse(task), eps);
	}

	@Test
	public final void testPowRang() throws Exception {
		String task = "f(x)=x^2+5; f(10)";
		assertEquals(Math.pow(10, 2) + 5, script.parse(task), eps);
	}

	@Test
	public final void testCos() throws Exception {
		String task = "cos(19)/2^10;";
		assertEquals(Math.cos(19) / Math.pow(2, 10), script.parse(task), eps);
	}

	@Test
	public final void testPete() throws Exception {
		String task = "peter(zypern)= zypern^zypern/12; peter(10);";
		assertEquals(Math.pow(10, 10) / 12, script.parse(task), eps);
	}

	@Test
	public void testSignedMinus() throws Exception {
		String task = "19.4 - -767.93";
		assertEquals(787.33, script.parse(task), eps);
	}

	@Test
	public void testTan() throws Exception {
		String task = "tan(123456789)";
		assertEquals(Math.tan(123456789), script.parse(task), eps);
	}

	@Test
	public final void testRndValue() throws Exception {
		Double nmbr1 = rnd();
		Double nmbr2 = rnd();
		String task = "" + nmbr1 + "+" + nmbr2 + ";";
		assertTrue(Math.abs(nmbr1 + nmbr2 - script.parse(task)) / Math.abs(nmbr1 + nmbr2) < eps);
	}
	
	@Test
	public void testBigNmbrs() throws Exception {
		Double rand = rnd();
		String task = rand + "^2";
		Double expected = Math.pow(rand, 2);
		assertTrue(Math.abs((expected - script.parse(task)) / expected) < eps);
	}
	
	@Test
	public void testSmallNmbrs() throws Exception {
		String task = "0.0235*123";
		assertTrue(Math.abs( 0.0235*123- script.parse(task)) / 0.0235*123 < eps);
	}
}
