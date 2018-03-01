package de.lab4inf.wrb.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;

public class ClientTest {

	WRBClient client;
	double eps = 1E-8;

	//Dezimalzahlen formatieren
	DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
	DecimalFormat df, dfsmall;

	@Before
	public void setUp() throws Exception {
		String hostUrl = "http://localhost:8080/";
		System.out.println("host: " + hostUrl);

		client = new WRBClient(hostUrl);
		setUpDecimal();
	}

	/**
	 * set decimal format
	 */
	public void setUpDecimal() {
		dfs.setDecimalSeparator('.');
		df =  new DecimalFormat("#.#####", dfs);
		dfsmall =  new DecimalFormat("#.##", dfs);
	}

	/**
	 * @return random number
	 */
	public double rnd() {
		double rand = Double.valueOf(df.format(ThreadLocalRandom.current().nextDouble(1, 20)));
		return rand;
	}

	/**
	 * @return small random number
	 */
	public double rndsmall() {
		double rand = Double.valueOf(df.format(ThreadLocalRandom.current().nextDouble(0, 10)));
		return rand;
	}

	@Test
	public void testIntegrate() throws Exception {
		double max, min = 0, exp;
		double ret;

		for(int i = 0; i < 5; i++) {
			max = rnd();
			ret = client.getIntegral("sin", min, max, 0.0);

			exp = -Math.cos(max) + Math.cos(min);
			System.out.println(max);
			assertEquals(exp, ret, eps);
		}
	}


	@Test
	public void testDifferentiate() throws Exception {
		double x;
		double ret;

		for(int i = 0; i < 5; i++) {
			x = rnd();
			ret = client.getDiff("sin", x, 0.0, ".5f");
			assertEquals(Math.cos(x), ret, eps);
		}
	}

	@Test
	public void testParse() throws Exception {
		double x;
		String req;
		double ret;

		for(int i = 0; i < 5; i++) {
			x = rnd();
			req = "x="+x+";f(x)="+x+"**2;f("+x+");";
			ret = client.parse(req);

			assertEquals(Math.pow(x,2), ret, eps);
		}
	}

	@Test
	public void testShowFunction() throws Exception {

		String msgtan = "tan";
		String msgcos = "cos";
		//		String msgloge = "loge";
		//		String msglb = "lb";
		//		String msgeul = "eul";
		List<String> res = client.getFunctions();

		assertTrue(msgtan, res.contains(msgtan));
		assertTrue(msgcos, res.contains(msgcos));
		//		assertTrue(msgloge, res.contains(msgloge));
		//		assertTrue(msglb, res.contains(msglb));
		//		assertTrue(msgeul, res.contains(msgeul));
	}

	@Test
	public void testEvaluate() throws Exception {

		double a = 0;
		double b, n, i;

		i = a;
		b = rnd();
		n = b/5;
		List<Double> expvalues = new ArrayList<>();
		expvalues = client.getEval("sin", a, b, n);

		for (double value : expvalues) {
			assertEquals(Math.sin(i), value, eps);
			i+=n;
		}
	}

	@Test
	public void testEvaluateSimple() throws Exception {

		double a = 0;
		double b, n, i;

		i = a;
		b = 5;
		n = 1;
		List<Double> expvalues = new ArrayList<>();
		expvalues = client.getEval("sin", a, b, n);

		for (double value : expvalues) {
			assertEquals(Math.sin(i), value, eps);
			i+=n;
		}
	}

	@Test
	public void testHMcontainsOwnFct() throws Exception {

		String fctname = "myfct";
		String task = fctname+"(x)=(x**2)*3*x;";
		client.parse(task);

		List<String> stringres = client.getFunctions();

		assertTrue(fctname, stringres.contains(fctname));
	}

	@Test (expected = IOException.class)
	public void testWrongPath() throws Exception {

		String req = "fctna";
		client.wrbRequest(req);
	}

	@Test
	public void testParseEval() throws Exception {

		String task = "f(x)=sin(x)*16*tan(x);";
		client.parse(task);

		double a = 0;
		double b, n, i;

		i = a;
		b = rnd();
		n = b/5;
		List<Double> expvalues = new ArrayList<>();

		expvalues = client.getEval("f", a, b, n);

		for (double value : expvalues) {
			assertEquals((16 * Math.tan(i) * Math.sin(i)), value, eps);
			i+=n;
		}
	}

	@Test
	public void testParseIntegr() throws Exception {

		String fctname = "f";
		String task = fctname+"(x)=(sin(x)*16*tan(x))%2B(8);";
		client.parse(task);

		double a = 0;
		double b, n, i;

		i = a;
		b = rnd();
		n= b/5;
		List<Double> expvalues = new ArrayList<>();
		expvalues = client.getEval(fctname, a, b, n);


		for (double value : expvalues) {
			assertEquals(8+(16 * Math.tan(i) * Math.sin(i)), value, eps);
			i+=n;
		}
	}

	@Test
	public void testParsDiff() throws Exception {

		String fctname = "f";
		String task = fctname+"(x)=(cos(x)*12*tan(x))-8;";
		client.parse(task);

		double a = 0;
		double b, n, i;

		i = a;
		b = rnd();
		n = b/5;
		List<Double> expvalues = new ArrayList<>();

		expvalues = client.getEval(fctname, a, b, n);


		for (double value : expvalues) {
			assertEquals((12 * Math.cos(i) * Math.tan(i))-8, value, eps);
			i+=n;
		}
	}
	
	@Test
	public void testNotDefinedFunction() throws Exception {
		String ret = client.wrbRequest("integrate?fct=afbsdifu");
		assertTrue("server did not return an error", ret.startsWith("error"));
	}
	
	@Test
	public void testSymbolsInDefinition() throws Exception {
		String ret = client.wrbRequest("integrate?fct=sin&def=ö");
		assertTrue("server did not return an error", ret.startsWith("error"));
	}
}





























