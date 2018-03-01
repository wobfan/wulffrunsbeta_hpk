/*
 ============================================================================
 Name        : cFunction.c
 Author      :
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include "Function.h"
#include "Differentiator.h"
#include "Integrator.h"
/*
void print(int size, double x[], double fx[],double fdx[],double fix[]){
	printf("\n");
	printf("    x     |     f(x)    |    f'(x)    |  F(x)+c     \n");
	printf("----------+-------------+-------------+-------------\n");
	for (int i = 0; i<size; i++){
		printf("%+.5f  |  %+.5f   |  %+.5f   |  %+.5f   \n", x[i], fx[i], fdx[i], fix[i]);
	}
	printf("\n");
}

double sq2 (double x){
	return x*x;
}

void test_sq2(){
	int n =5;
	Function f = Function(sq2, "sq2");
	double values[] = { 0, 0.25, 0.5, 0.75, 1 };
	double fResults[n];
	double fDifResults[n];
	double fIntResults[n];

	printf("Testing function sq2");

	for (int i=0 ; i < n; i++){
		fResults[i] = f(values[i]);
		fDifResults[i] = differentiate(f,values[i],0.00001);
		fIntResults[i] = integrate(f,0,values[i],0.000001);
	}

	print(n,values,fResults,fDifResults, fIntResults);
}

void test_Exp(){
	int n = 5;
	Function f = Function(exp, "exp");
	double values[] =  { 0, 0.25, 0.5, 0.75, 1 };
	double fResults[n];
	double fDifResults[n];
	double fIntResults[n];

	printf("Testing function sin");
	for (int i=0 ; i < n; i++){
		fResults[i] = f(values[i]);
		fDifResults[i] = differentiate(f,values[i],0.00001);
		fIntResults[i] = integrate(f,0,values[i],0.00001);
	}

	print(n,values,fResults,fDifResults, fIntResults);
}

void test_Sin(){
	int n =7;
	Function f = Function(sin, "sin");
	double values[] = { 0.00000, 0.52360, 1.04720, 1.57080, 2.09440 , 2.61799, 3.14159};
	double fResults[n];
	double fDifResults[n];
	double fIntResults[n];

	printf("Testing function sin");
	for (int i=0 ; i < n; i++){
		fResults[i] = f(values[i]);
		fDifResults[i] = differentiate(f,values[i],0.00001);
		fIntResults[i] = integrate(f,0,values[i],0.00001);
	}

	print(n,values,fResults,fDifResults, fIntResults);
}



int main() {
	test_sq2();
	test_Exp();
	test_Sin();
	return EXIT_SUCCESS;
}*/
