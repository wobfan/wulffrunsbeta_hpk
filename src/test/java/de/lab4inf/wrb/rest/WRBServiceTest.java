package de.lab4inf.wrb.rest;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.*;

import de.lab4inf.wrb.Function;
import de.lab4inf.wrb.Script;
import de.lab4inf.wrb.WRBScript;

public class WRBServiceTest extends AbstractWRBServiceTester {

	Script script;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		script = getWRBScript();
	}
	
	@Override
	protected Script getWRBScript() {
		return new WRBScript();
	}
	
	@Test
    public void testIntegrate() throws Exception{
        //Function f = script.getFunction("sin");
        double a = -3; 
        double b = 12;
        double expected = -Math.cos(b)+Math.cos(a);
        double response = Double.valueOf(submitIntegralRequest("sin", String.format("xmin=%s;xmax=%s;",a,b),".6f"));
        assertEquals(response, expected, eps);
    }
    
    @Test
    public void testDifferentiate() throws Exception{
        //Function f = script.getFunction("sin");
        double x = 6;
        double expected = Math.cos(x);
        double response = Double.valueOf(submitDifferentialRequest("sin", "x="+x+";"));
        assertEquals(response, expected, eps);
    }
    
    @Test
    public void testEvaluateFct() throws Exception{    
        double dx = 0.1;
        String FMT = "%.8f";
        StringBuilder expectedBuilder = new StringBuilder();
        expectedBuilder.append("sin: [");
        for(double i = 0.0;i<=1;i+=dx) {
                expectedBuilder.append(String.format(Locale.US, FMT, roundFmt(i, FMT)));
                expectedBuilder.append(",");
                expectedBuilder.append(String.format(Locale.US, FMT, roundFmt(Math.sin(i), FMT)));
                expectedBuilder.append(",");
        }
        expectedBuilder.deleteCharAt(expectedBuilder.length() - 1);
        expectedBuilder.append("]");
        Function f = script.getFunction("sin");
        String response = expectedValue("sin", f,FMT, 0, 1, dx);
        String expected = expectedBuilder.toString();
        for(int i=0;i<response.length();i++) {
                //System.out.println("Response: "+response.charAt(i)+"| Expected: "+expected.charAt(i));
                assertEquals(response.charAt(i), expected.charAt(i));
        }
    }
    
    
    @Test
    public void testExp() throws Exception{
        double x = 7;
        double expected = Math.exp(x);
        double response = Double.valueOf(submitDifferentialRequest("exp", "x="+x+";"));
        assertEquals(response, expected, eps);
    }
    
    @Test
    public void testNotDefinedFunction() throws Exception{
        String ret = submitIntegralRequest("adfish", "xmin=1;xmax=10");
        assertTrue("did not return an error although function was not defined", ret.startsWith("error"));
    }
    
    @Test
    public void testUnknownSymbols() throws Exception{
        String ret = submitIntegralRequest("sin", "~");
        assertTrue("did not return an error", ret.startsWith("error"));
    }
}


