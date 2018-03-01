package de.lab4inf.wrb;

public class Differentiator {
	
	static {
		System.load(System.getProperty("user.dir") + "/src/main/cpp/Debug/WRB.so");
	}
	
	double err = 1.E-8;
		
	public void setError(double err) {
		this.err = err;
	}
	
	public double differentiate(final Function fct, final double x) {
		return differentiate(fct, x, this.err);
	}
	
	public native double differentiate(final Function fct, final double x, final double err);
	
}
