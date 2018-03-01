package de.lab4inf.wrb;

/**
 * Simple real valued function interface. Since JDK1.8
 * it can be implemented via a lambda expression. 
 * 
 * @author nwulff
 * @since 22.10.2013
 * @version $Id: Function.java,v 1.6 2017/10/05 11:23:36 nwulff Exp $
 */
@FunctionalInterface
public interface Function {
    /**
     * Function evaluation mapping tuple (x1,...,xn) to y = f(x1,...,xn).
     * 
     * @param args
     *            the double array tuple x1,...,xn
     * @return y = f(x1,...,xn)
     */
    double eval(final double... args);
}