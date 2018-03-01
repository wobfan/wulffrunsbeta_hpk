/*
 * differentiate.c
 *
 *  Created on: Dec 5, 2017
 *      Author: stefan
 */

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "Differentiator.h"
#include "Convergence.h"

#define MACH_EPS 2.2E-16
#define H0 1.E-3
#define nextH(); { eo=-1; h/=2; h12/=2; dyo=dy; dfho=dfh; dfo=df; }

int difCalls = 0;
const int NMAX = 30;

int getDifCalls(){
	return difCalls;
}

void setDifCalls(int calls){
	difCalls = calls;
}

double delta(Function& f, double h, double x){
	difCalls+=2;
	return f(x+h)-f(x-h);
}

double fh(Function& f, double h, double x){
	double t1 = 8 * delta(f, h, x);
	double t2 = delta(f, 2 * h, x);
	double dn = 12 * h;
	return (t1 - t2) / dn;
}

double differentiate(Function& f, double x, double eps) {
	double eo = -1;
	double h = H0;
	int n = 2;
	double dy, dyo = -1, df = 0, dfo;
	double dfh, dfho, h12;

	dfh = delta(f, h, x);

	dy = dfh / (2 * h);

	h12 = h * 12;

	do {
		nextH();
		dfh = delta(f, h, x);
		df = (8 * dfh - dfho) / h12;
		dy = (16 * df - dfo) / 15;
	} while (convergence(dy, dyo, &eo, ++n, NMAX, eps) != -1);

	return dy;
}

