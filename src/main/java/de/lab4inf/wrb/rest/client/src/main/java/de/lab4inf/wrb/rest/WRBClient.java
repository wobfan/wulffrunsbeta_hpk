package de.lab4inf.wrb.rest;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WRBClient extends AbstractWRBClient{

	WRBClient client;

	public WRBClient(String hostUrl) {
		super(hostUrl);
	}
	
	public static String format(String format, Object... args) {
		return String.format(Locale.US, format, args);
	}

	public Double getIntegral(String fct, Double xmin, Double xmax, Double eps) throws Exception {
		String str = new String();
		if (eps != 0.0) str = format("integrate?fct=%s&def=xmin=%f;xmax=%f;eps=%f", fct, xmin, xmax, eps);
		else str = format("integrate?fct=%s&def=xmin=%f;xmax=%f", fct, xmin, xmax);
		return Double.valueOf(wrbRequest(str));
	}
	
	public Double getIntegral(String fct, Double xmin, Double xmax, Double eps, String format) throws Exception {
		String str = new String();
		if (eps != 0.0) str = format("integrate?fct=%s&def=xmin=%f;xmax=%f;eps=%f&fmt=%s", fct, xmin, xmax, eps, format);
		else str = format("integrate?fct=%s&def=xmin=%f;xmax=%f&fmt=%s", fct, xmin, xmax, format);
		return Double.valueOf(wrbRequest(str));
	}

	public Double getDiff(String fct, Double x, Double eps) throws Exception {
		String str = new String();
		if (eps != 0.0) str = format("differentiate?fct=%s&def=x=%f;eps=%f", fct, x, eps);
		else str = format("differentiate?fct=%s&def=x=%f", fct, x);
		return Double.valueOf(wrbRequest(str));
	}
	
	public Double getDiff(String fct, Double x, Double eps, String format) throws Exception {
		String str = new String();
		if (eps != 0.0) str = format("differentiate?fct=%s&def=x=%f;eps=%f&fmt=%s", fct, x, eps, format);
		else str = format("differentiate?fct=%s&def=x=%f&fmt=%s", fct, x, format);
		return Double.valueOf(wrbRequest(str));
	}

	public List<String> getFunctions() throws Exception {
		List<String> fcts = new ArrayList<String>();
		String ret = wrbRequest("fctList");
		for (String partition : ret.split(",")) {
			fcts.add(partition);
		}
		return fcts;
	}

	public Double parse(String str) throws Exception {
		str = format("parse?msg=%s",str);
		return Double.valueOf(wrbRequest(str));
	}

	public List<Double> getEval(String fct, double xmin, double xmax, double n) throws Exception {
		String str = format("evaluate?fct=%s&def=xmin=%f;xmax=%f;dx=%f", fct, xmin, xmax, n);
		List<Double> listvalues = new ArrayList<>();
		String res = wrbRequest(str);
				
		for (String retval : res.split(",")) {
			listvalues.add(Double.valueOf(retval));
		}

		return listvalues;
	}

	public String wrbRequest(String msg) throws Exception {
		//msg = URLEncoder.encode(msg, "UTF-8");
		if (msg.startsWith("error")) {
			throw new IllegalArgumentException("server returned an internal error");
		}
		String path = format("WRBService/%s", msg);
		System.out.println("path: "+path+"\n");
		return submitRequest(path);
	}
}
