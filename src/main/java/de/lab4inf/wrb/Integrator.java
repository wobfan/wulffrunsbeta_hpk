package de.lab4inf.wrb;

public class Integrator {

	static {
		System.load(System.getProperty("user.dir") + "/src/main/cpp/Debug/WRB.so");
	}
	
	double err = 1.E-8;
	
	public void setEps(double err) {
		this.err = err;
	}
	
	public double integrate(final Function fct, final double a, final double x) {
		return integrate(fct, a, x, this.err);
	}
	
	public native double integrate(final Function fct, final double a, final double x, final double err);
	
}
