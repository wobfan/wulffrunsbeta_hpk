/*
 * Convergence.cpp
 *
 *  Created on: Dec 19, 2017
 *      Author: stefan
 */

#include "Convergence.h"

#define DEBUG false

double convergence(double v, double vo, double *eo, int n, const int NMAX, double eps) {
	double e = -1;
	double avg = v;

	if (vo != -1) {
		if (fabs(v) < 1) avg = 1;
		else avg = fabs((vo + v) / 2);
		e = fabs((v - vo) / avg);
	}

	if (DEBUG) {
		printf("n:%6d, v: %.10f, v2: %.10f, e: %.10f, eo: %.10f, eps: %1.10f\n", n, v, vo, e, *eo, eps);
	}


	if (*eo != -1 && e != -1 && *eo < e) {
		if (DEBUG) printf("throwing exception as error is increasing...\n\n");
		throw "no convergence, error is increasing";
	}

	if (n > NMAX) {
		if (DEBUG) printf("throwing exception: too many calls...\n\n");
		throw "no convergence, too many calls";
	}

	if (e != -1 && e < eps) {
		return -1;
	}

	if (vo != -1) *eo = fabs((v - vo) / avg);
	return -2;
}
