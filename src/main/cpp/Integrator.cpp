/*
 * integrate.c
 *
 *  Created on: Dec 5, 2017
 *      Author: stefan
 */

#include <math.h>
#include <stdio.h>
#include "Integrator.h"
#include "Convergence.h"

int intCalls = 0;
const int NMAX = 32000;

int getIntCalls(){
	return intCalls;
}

void setIntCalls(int calls){
	intCalls = calls;
}

double integrate(Function& f, double a, double b, double eps) {
	if(b < a) return -1 * integrate(f, b, a, eps);
	int n = 2;
	double eo = -1;
	double v = -1;
	double vo = -1;
	double h = 0;
	double fafb = f(a) + f(b);
	double t4, t2, sum2, sum4;

	do {
		if (v != -1) vo = v;
		intCalls++;
		h = (b - a) / n;
		v = fafb;

		t4 = a+h/2;
		t2 = a+h;
		sum2 = 0;
		sum4 = f(t4);

		for (int j = 1; j < n; sum2 += f(t2), t2 += h, t4 += h, sum4 += f(t4), j++, intCalls+=2);

		v += 2 * sum2 + 4 * sum4;
		v *= (h / 6);

		n *= 2;
	} while (convergence(v, vo, &eo, n, NMAX, eps) != -1);
	return v;
}

