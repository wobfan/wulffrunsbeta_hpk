package de.lab4inf.wrb.rest;

import static java.lang.String.format;

import java.io.*;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.lab4inf.wrb.*;

@Path(AbstractWRBService.SERVICE)

public class WRBRESTService extends AbstractWRBService {

	static Script wrb = new WRBScript();


	public Function getFct(String fct) {
		return wrb.getFunction(fct);
	}

	public static String format(String format, Object... args) {
		return String.format(Locale.US, format, args);
	}

	private String showForm(String type) {
		String head = "<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head><form action=\"#\" method=\"get\">";
		String int_body = "<body><h1>Integration</h1>Function Name: <input type=\"text\" name=\"fct\"><br>\n" + 
				"  Definition: <input type=\"text\" name=\"def\"><br>\n";
		String dif_body = "<body><h1>Differentiation</h1>Function Name: <input type=\"text\" name=\"fct\"><br>\n" + 
				"  Definition: <input type=\"text\" name=\"def\"><br>\n";
		String parse_body = "<body><h1>Parsing</h1>Definition: <input type=\"text\" name=\"msg\"><br>";
		String eval_body = "<body><h1>Evaluation of Function</h1>Function: <input type=\"text\" name=\"fct\"><br>Definition: <input type=\"text\" name=\"def\"><br>\n";
		String footer = "<input type=\"submit\" value=\"Submit\">\n" + 
				"</form></html>";

		if (type.equals("int")) return head + int_body + footer;
		else if (type.equals("dif")) return head + dif_body + footer;
		else if (type.equals("parse")) return head + parse_body + footer;
		else if (type.equals("eval")) return head + eval_body + footer;
		else return new String();
	}

	//	private String showError()

	@GET
	@Path(FCT_INTEGRAL_PATH)
	@Produces(TEXT_PLAIN)
	@Consumes(TEXT_PLAIN)
	public String doIntegrate(@DefaultValue(DEF_INT) @QueryParam("def") String def_String, @DefaultValue(FCT) @QueryParam("fct") String fct_String) {
		Thread t = Thread.currentThread();
		double eps = 0.0;
		String retValue = new String();
		try {
			wrb.parse(def_String);

			double a = wrb.getVariable("xmin");
			double b = wrb.getVariable("xmax");
			if (def_String.contains("eps=")) eps = wrb.getVariable("eps");

			Integrator integrator = new Integrator();
			if (eps != 0.0) integrator.setEps(eps);
			Date d = new Date();
			retValue = Double.toString(integrator.integrate(getFct(fct_String), a, b));

			log.info(format("GET %s INT fct=%s a=%s b=%s eps=%s Thread: %s Time: %s", FCT_INTEGRAL_PATH, fct_String, a, b, eps, t, d));
			log.info(format("RET %s", retValue));

		} catch(Throwable e) {
			return "error: internal";
		}
		return retValue;
	}

	@GET
	@Path(FCT_INTEGRAL_PATH)
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String doIntegrateHtml(@DefaultValue(DEF_INT) @QueryParam("def") String def_String, @DefaultValue(FCT) @QueryParam("fct") String fct_String) {
		if (def_String.equals(DEF_INT) && fct_String.equals(FCT)) return showForm("int");
		Thread t = Thread.currentThread();
		double eps = 0.0;
		String retValue = new String(); 

		try {
			wrb.parse(def_String);

			double a = wrb.getVariable("xmin");
			double b = wrb.getVariable("xmax");
			if (def_String.contains("eps=")) eps = wrb.getVariable("eps");

			Integrator integrator = new Integrator();
			if (eps != 0) integrator.setEps(eps);
			Date d = new Date();

			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head>");
			sb.append("<body><h1>Integration</h1>");
			sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
			sb.append(format("Thread: %s <br>Time: %s<br>a was: %s<br>b was: %s<br>eps was: %s<br>function: %s<br>", t, d, a, b, eps, fct_String));
			sb.append(format("Result is: %s", Double.toString(integrator.integrate(getFct(fct_String), a, b))));
			sb.append("<br><form><input type=\"button\" value=\"Go back!\" onclick=\"history.back()\">\n</form>");
			sb.append("</body></html>");
			retValue = sb.toString();
		} catch(Throwable e) {
			return "<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head><script type='text/javascript'>alert('Internal Error.');</script></html>";
		}
		return retValue;
	}

	@GET
	@Path(FCT_DIFFERENTIAL_PATH)
	@Produces(TEXT_PLAIN)
	@Consumes(TEXT_PLAIN)
	public String doDifferentiate(@DefaultValue(DEF_DIF) @QueryParam("def") String def_String, @DefaultValue(FCT) @QueryParam("fct") String fct_String) {
		Thread t = Thread.currentThread();
		double eps = 0.0;
		String retValue = new String();
		try {
			wrb.parse(def_String);

			double x = wrb.getVariable("x");
			if (def_String.contains("eps=")) eps = wrb.getVariable("eps");

			Differentiator differentiator = new Differentiator();
			if (eps != 0.0) differentiator.setError(eps);
			Date d = new Date();
			retValue = Double.toString(differentiator.differentiate(getFct(fct_String), x));

			log.info(format("GET %s DIFF fct=%s x=%s eps=%s Thread: %s Time: %s", FCT_DIFFERENTIAL_PATH, fct_String, x, eps, t, d));
			log.info(format("RET %s", retValue));
		} catch(Throwable e) {
			return "error: internal";
		}
		return retValue;
	}

	@GET
	@Path(FCT_DIFFERENTIAL_PATH)
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String doDifferentiateHtml(@DefaultValue(DEF_DIF) @QueryParam("def") String def_String, @DefaultValue(FCT) @QueryParam("fct") String fct_String, @DefaultValue(FMT) @QueryParam("fmt") String format) {
		if (def_String.equals(DEF_DIF) && fct_String.equals(FCT) && format.equals(FMT)) return showForm("dif");
		Thread t = Thread.currentThread();
		double eps = 0.0;
		String retValue = new String();
		try {
			wrb.parse(def_String);

			double x = wrb.getVariable("x");
			if (def_String.contains("eps=")) eps = wrb.getVariable("eps");

			Differentiator differentiator = new Differentiator();
			if (eps != 0) differentiator.setError(eps);
			Date d = new Date();

			Double result = differentiator.differentiate(getFct(fct_String), x);

			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head>");
			sb.append("<body><h1>Differentiation</h1>");
			sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
			sb.append(format("Thread: %s <br>Time: %s<br>x was: %s<br>eps was: %s<br>function: %s<br>", t, d, x, eps, fct_String));
			sb.append(format("Result is: %" + format, result));
			sb.append("<br><form><input type=\"button\" value=\"Go back!\" onclick=\"history.back()\">\n</form>");
			sb.append("</body></html>");
			retValue = sb.toString();
		} catch(Throwable e) {
			return "<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head><script type='text/javascript'>alert('Internal Error.');</script></html>";
		}
		return retValue;
	}

	@GET
	@Path("parse")
	@Produces(TEXT_PLAIN)
	@Consumes(TEXT_PLAIN)
	public String doDefine(@DefaultValue("0") @QueryParam("msg") String def_String, @DefaultValue(FMT) @QueryParam("fmt") String format) {
		Thread t = Thread.currentThread();
		Date d = new Date();
		Double retValue = 0.0;
		try {
			retValue = wrb.parse(def_String);

			log.info(format("GET %s PARSE WRBScript parsed a string: \"%s\" WRBScript returned: \"%" + format + "\" Thread: %s Time: %s", HELLO_PATH, def_String, retValue, t, d));
			log.info(format("RET %" + format, retValue));
		} catch(Throwable e) {
			return "error: internal";
		}
		return retValue.toString();
	}

	@GET
	@Path("parse")
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String doDefineHtml(@DefaultValue("0") @QueryParam("msg") String def_String, @DefaultValue(FMT) @QueryParam("fmt") String format) {
		if (def_String.equals("0") && format.equals(FMT)) return showForm("parse");
		Thread t = Thread.currentThread();

		Date d = new Date();
		String retValue = new String();
		try {

			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head>");
			sb.append("<body><h1>Parsing</h1>");
			sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
			sb.append(format("Thread: %s <br>Time: %s<br>WRBScript parsed a string: \"%s\"<br>", t, d, def_String));
			sb.append(format("Script returned: %" + format, wrb.parse(def_String)));
			sb.append("<br><form><input type=\"button\" value=\"Go back!\" onclick=\"history.back()\">\n</form>");
			sb.append("</body></html>");
			retValue = sb.toString();
		} catch(Throwable e) {
			return "<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head><script type='text/javascript'>alert('Internal Error.');</script></html>";
		}
		return retValue;
	}

	@GET
	@Path("fctList")
	@Produces(TEXT_PLAIN)
	@Consumes(TEXT_PLAIN)
	public String doFunctionList() {
		Thread t = Thread.currentThread();
		Date d = new Date();
		String fct[];
		try {
			fct = new String[wrb.getFunctionNames().size()];
			wrb.getFunctionNames().toArray(fct);

			log.info(format("GET %s LIST Thread: %s Time: %s", HELLO_PATH, t, d));
			log.info("RET");
		} catch(Throwable e) {
			return "error: internal";
		}
		return String.join(",", fct);
	}

	@GET
	@Path("fctList")
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String doFunctionListHtml() {
		Thread t = Thread.currentThread();

		Date d = new Date();
		String retValue = new String();
		try {

			String[] fct = new String[wrb.getFunctionNames().size()];
			wrb.getFunctionNames().toArray(fct);

			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head>");
			sb.append("<body><h1>Function List</h1>");
			sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
			sb.append(format("Thread: %s <br>Time: %s<br><br>", t, d));
			sb.append("<ul style=\"list-style-type:disc\">");
			sb.append("<li>" + String.join("</li><li>", fct) + "</li>");
			sb.append("</ul>");
			sb.append("<br><form><input type=\"button\" value=\"Go back!\" onclick=\"history.back()\">\n</form></body></html>");
			retValue = sb.toString();
		} catch(Throwable e) {
			return "<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head><script type='text/javascript'>alert('Internal Error.');</script></html>";
		}
		return retValue;
	}

	@GET
	@Path("evaluate")
	@Produces(TEXT_PLAIN)
	@Consumes(TEXT_PLAIN)
	public String doInterval(@DefaultValue(DEF) @QueryParam("def") String def_String, @DefaultValue(FCT) @QueryParam("fct") String fct_String,
			@DefaultValue(FMT) @QueryParam("fmt") String format) {
		Thread t = Thread.currentThread();
		Date d = new Date();
		StringBuilder retValue = new StringBuilder();
		try {
			wrb.parse(def_String);
			double xmax = wrb.getVariable("xmax");
			double xmin = wrb.getVariable("xmin");
			double delta = wrb.getVariable("dx");

			int steps = (int) ((xmax - xmin) / delta);
			double stepsize = delta;
			double[] fx = new double[steps + 1];
			double[] x = new double[steps + 1];
			for (int i = 0; i <= steps; i++) {
				double v = xmin + (i * stepsize);
				fx[i] = wrb.parse(format("%s(%f)", fct_String, v));
				x[i] = v;
			}

			retValue = new StringBuilder();
			for (int i = 0; i < steps - 1; i++) {
				retValue.append(format("%" + format + ",", fx[i]));
			}
			retValue.append(format("%" + format, fx[steps - 1]));

			log.info(format("GET %s EVAL WRBScript parsed a string: \"%s\" WRBScript returned: \"%s\" Thread: %s Time: %s", HELLO_PATH, def_String, retValue, t, d));
			log.info(format("RET %s", retValue));

		} catch(Throwable e) {
			return "error: internal";
		}
		return retValue.toString();
	}

	@GET
	@Path("evaluate")
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String doIntervalHtml(@DefaultValue(DEF) @QueryParam("def") String def_String, @DefaultValue(FCT) @QueryParam("fct") String fct_String,
			@DefaultValue(FMT) @QueryParam("fmt") String format) {
		if (def_String.equals(DEF) && fct_String.equals(FCT) && format.equals(FMT)) return showForm("eval");
		Thread t = Thread.currentThread();
		Date d = new Date();
		String retValue = new String();
		try {
			wrb.parse(def_String);
			double xmax = wrb.getVariable("xmax");
			double xmin = wrb.getVariable("xmin");
			double delta = wrb.getVariable("dx");

			int steps = (int) ((xmax - xmin) / delta);
			double stepsize = delta;
			double[] fx = new double[steps + 1];
			double[] x = new double[steps + 1];
			for (int i = 0; i <= steps; i++) {
				double v = xmin + (i * stepsize);
				fx[i] = wrb.parse(format("%s(%f)", fct_String, v));
				x[i] = v;
			}

			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head>");
			sb.append("<body><h1>Evaluation of Function</h1>");
			sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
			sb.append(format("Thread: %s <br>Time: %s<br>WRBScript parsed a string: \"%s\"<br>", t, d, def_String));
			sb.append(format("Script returned: %s", wrb.parse(def_String)));
			sb.append(format("<br><br>Function values from %f to %f:<br><br>", xmin, xmax));
			sb.append("<table>");
			sb.append("<tr><th>x</th><th>f(x)</th></tr>");
			for (int i = 0; i < fx.length; i++) {
				sb.append(format("<tr><td>%" + format + "</td><td>%" + format + "</td></tr>", x[i], fx[i]));
			}
			sb.append("</table>");
			sb.append("<br><form><input type=\"button\" value=\"Go back!\" onclick=\"history.back()\">\n</form>");
			sb.append("</body></html>");
			retValue = sb.toString();
		} catch(Throwable e) {
			return "<!DOCTYPE html><html><head><title>WRB RESTful Service</title></head><script type='text/javascript'>alert('Internal Error.');</script></html>";
		}
		return retValue;
	}

}
