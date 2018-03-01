/*
 * Project: WRB
 *
 * Copyright (c) 2008-2013,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for Computer Sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.lab4inf.wrb;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the Wulff RunsBeta-Script language.
 * 
 * @author nwulff
 * @since 16.10.2013
 * @version $Id: SimpleWRBScriptTest.java,v 1.2 2016/10/20 16:01:47 nwulff Exp $
 */
public class SimpleTest {
	final double eps = 1.E-8;
	Script script;

	DecimalFormat df = new DecimalFormat("#,###");
	Double rand1 = Double.valueOf(df.format(ThreadLocalRandom.current().nextDouble(-65535, 65535)));
	Double rand2 = Double.valueOf(df.format(ThreadLocalRandom.current().nextDouble(-65535, 65535)));

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public final void setUp() throws Exception {
		script = getScript();
		assertNotNull("no script implementation", script);
	}

	/**
	 * Get the actual implementation for the script test.
	 * 
	 * @return script implementation
	 */
	protected Script getScript() {
		return new WRBScript();
	}

	@Test
	public final void testAddition() throws Exception {
		assertEquals(rand1 + rand2, script.parse(rand1 + "+" + rand2), eps);
	}

	@Test
	public final void testSubtraction() throws Exception {
		assertEquals(rand1 - rand2, script.parse(rand1 + "-" + rand2), eps);
	}

	@Test
	public final void testMultiplication() throws Exception {
		assertEquals(rand1 * rand2, script.parse(rand1 + "*" + rand2), eps);
	}

	@Test
	public final void testDivide() throws Exception {
		assertEquals(rand1 / rand2, script.parse(rand1 + " / " + rand2), eps);
	}

	@Test
	public final void testPower() throws Exception {
		assertEquals(Math.pow(rand1, rand2), script.parse(rand1 + "^" + rand2), eps);
	}

	@Test
	public final void testSqrt() throws Exception {
		assertEquals(Math.sqrt(rand1), script.parse("sqrt(" + rand1 + ")"), eps);
	}

	@Test
	public final void testSine() throws Exception {
		assertEquals(Math.sin(rand1), script.parse("sin(" + rand1 + ")"), eps);
	}

	@Test
	public final void testCosine() throws Exception {
		assertEquals(Math.cos(rand1), script.parse("cos(" + rand1 + ")"), eps);
	}
	
	@Test
	public final void testNegativeVarInBrackets() throws Exception {
		assertEquals(5+5, script.parse("p=5; 5-(-p)"), eps);
	}

	@Test
	public final void testTangens() throws Exception {
		assertEquals(Math.tan(rand1), script.parse("tan(" + rand1 + ")"), eps);
	}
	
	@Test
	public final void testSemicolon() throws Exception {
		assertEquals(16.5, script.parse("p=12.5+3; p+1"), eps);
	}
	
	@Test
	public final void testPeterfromCyprus() throws Exception {
		script.parse("peter(zypern)=zypern+(4*4^4)");
		assertEquals(rand1 + (4 * Math.pow(4, 4)), script.parse("peter(" + rand1 + ")"), eps);
	}
}

