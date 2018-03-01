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

import static java.lang.Math.floor;
import static java.lang.Math.random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the Wulff RunsBeta-Script language.
 * 
 * @author nwulff
 * @since 16.10.2013
 * @version $Id: SimpleWRBScriptTest.java,v 1.2 2016/10/20 16:01:47 nwulff Exp $
 */
public class RandomTest {
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
	
	/**
	 * Get the actual implementation for the script test.
	 * 
	 * @return script implementation
	 */
	protected Script getScript() {
		return new WRBScript();
	}

	/*
	 * This test creates 15 test evaluations, each with an amount of between 5 and 15 operations
	 * The expected result is calculated by a BeanShell interpreter.
	 * The Generator tests negative and positive Doubles.
	 * Operators used: +, -, *, /, ^ 
	 */
	@Test
	public final void testGenerator() throws Exception {
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	    
		for (int i = 0; i < 15; i++) {
			int count = ThreadLocalRandom.current().nextInt(5,15);
			StringBuilder calc = new StringBuilder();
			for (int j = 0; j <= count; j++) {
				int[] op = { ThreadLocalRandom.current().nextInt(0, 4), ThreadLocalRandom.current().nextInt(0, 4) };
				int dec = ThreadLocalRandom.current().nextInt(0, 6);
				Double left = rnd();
				Double right = rnd();

				String[] operators = {"+", "-", "*", "/", "^"};
				
				if (right < 0 && left < 0) calc.append("(-" + (left*(-1)) + ")" + operators[op[0]] + "(-" + (right*(-1)) + ")" + operators[op[1]]);
				else if (left < 0) calc.append("(-" + (left*(-1)) + ")" + operators[op[0]] + right + operators[op[1]]);
				else if (right < 0) calc.append(left + operators[op[0]] + "(-" + (right*(-1)) + ")" + operators[op[1]]);
				else calc.append(left + operators[op[0]] + right + operators[op[1]]);
			}
			calc.append(1);
			
			String expected = engine.eval(calc.toString()).toString();
			
			System.out.println("Generated: " + calc.toString());
			assertEquals(Double.valueOf(expected), script.parse(calc.toString()), eps);
		}
	}
}

