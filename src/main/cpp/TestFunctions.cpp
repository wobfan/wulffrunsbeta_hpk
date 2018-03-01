#include "Differentiator.h"
#include "Integrator.h"
#include "Function.h"
#include "CUnit.h"
#include "TestFunctions.h"
#include <stdio.h>
#include <stdlib.h>


void print(int size, double x[], double fx[], double fdx[], double fix[], int diffCalls[], int intCalls[]) {
        printf("\n");
        printf("      x       |         f(x)       |        f'(x)       |        F(x)        |   #f'  |   #F   \n");
        printf("--------------+--------------------+--------------------+--------------------+--------+----------\n");

        for (int i = 0; i < size; i++) {
                printf("%+12.8f  |  %+15.8f   |  %+15.8f   |  %+15.8f   |   %3d  |   %4d  \n",
                                x[i], fx[i], fdx[i], fix[i], diffCalls[i], intCalls[i]);
        }
        printf("\n");
}

double sq2(double x) {
        return x * x;
}

double onedivx(double x){
        return 1/x;
}

double squaro(double x){
        return sqrt(x);
}

int modul_aTest(int argc, char** args) {
        int n = 5;
        Function f = Function(sq2, "sq2");
        double xvalues[n];
        double fResults[n], fDifResults[n], fIntResults[n];
        double intExpected[n], difExpected[n];
        int intcalls[n], difcalls[n];

        printf("Testing function \"x²\"");

        int i = 0;
        for (double x = 0; i < n; x += 0.25, i++) {
                intExpected[i] = (x * x * x) / 3;
                difExpected[i] = 2 * x;

                xvalues[i] = x;
                fResults[i] = f(x);

                fDifResults[i] = differentiate(f, x, ERR);
                difcalls[i] = getDifCalls();
                assertEqualsF(difExpected[i], fDifResults[i], fabs(difExpected[i]) * EPS);

                fIntResults[i] = integrate(f, 0, x, ERR);
                intcalls[i] = getIntCalls();
                assertEqualsF(intExpected[i], fIntResults[i], fabs(intExpected[i])*EPS);
        }

        print(n, xvalues, fResults, fDifResults, fIntResults, difcalls, intcalls);

        return 1;
}

int modul_bTest(int argc, char** argv) {
        int n = 11;
        Function f = Function(exp, "exp");
        double xvalues[n];
        double fResults[n], fDifResults[n], fIntResults[n];
        double intExpected[n], difExpected[n];
        int intcalls[n], difcalls[n];

        printf("Testing function \"Exp\"");
        int i = 0;

        for (double x = 0; i < n; x++, i++) {
                intExpected[i] = exp(x)-exp(0);
                difExpected[i] = exp(x);

                xvalues[i] = x;
                fResults[i] = f(x);

                fDifResults[i] = differentiate(f, x, ERR);
                difcalls[i] = getDifCalls();
                //printf("exp: %.12f\n", difExpected[i]);
                //printf("res: %.12f\n", fDifResults[i]);
                assertEqualsF(difExpected[i], fDifResults[i], fabs(difExpected[i]) * EPS);

                fIntResults[i] = integrate(f, 0, x, ERR);
                //printf("exp: %.12f\n", intExpected[i]);
                //printf("res: %.12f\n", fIntResults[i]);
                intcalls[i] = getIntCalls();

                assertEqualsF(intExpected[i], fIntResults[i], fabs(intExpected[i]) * EPS);
        }

        print(n, xvalues, fResults, fDifResults, fIntResults, difcalls, intcalls);

        return 1;
}

int modul_cTest(int argc, char** argv) {
        int n = 9;
        Function f = Function(sin, "sin");
        double xvalues[n];
        double fResults[n], fDifResults[n], fIntResults[n];
        double intExpected[n], difExpected[n];
        int intcalls[n], difcalls[n];

        printf("Testing function \"Sin\"");
        int i = 0;
        for (double x = 0; i < n; x += (M_PI / 8), i++) {
                intExpected[i] = -cos(x)+cos(0);
                difExpected[i] = cos(x);

                xvalues[i] = x;
                fResults[i] = f(x);

                fDifResults[i] = differentiate(f, x, ERR);
                difcalls[i] = getDifCalls();
                assertEqualsF(difExpected[i], fDifResults[i], EPS);

                fIntResults[i] = integrate(f, 0, x, ERR);
                intcalls[i] = getIntCalls();
                assertEqualsF(intExpected[i], fIntResults[i], EPS);
        }

        print(n, xvalues, fResults, fDifResults, fIntResults, difcalls, intcalls);

        return 1;
}

int modul_dTest(int argc, char** argv) {
        int n = 5;
        Function f = Function(tan, "tan");
        double xvalues[n];
        double fResults[n], fDifResults[n], fIntResults[n];
        double intExpected[n], difExpected[n];
        int intcalls[n], difcalls[n];

        printf("Testing function \"Tan\"");

        int i = 0;
        for (double x = 0; i < n; x += (M_PI / 9), i++) {
                intExpected[i] = (-1)*log(fabs(cos(x)))+log(fabs(cos(0)));
                difExpected[i] = 1/(cos(x)*cos(x));

                xvalues[i] = x;
                fResults[i] = f(x);

                fDifResults[i] = differentiate(f, x, ERR);
                difcalls[i] = getDifCalls();
                assertEqualsF(difExpected[i], fDifResults[i], fabs(difExpected[i]) * EPS);

                fIntResults[i] = integrate(f, 0, x, ERR);
                intcalls[i] = getIntCalls();
                assertEqualsF(intExpected[i], fIntResults[i], fabs(intExpected[i])*EPS);
        }

        print(n, xvalues, fResults, fDifResults, fIntResults, difcalls, intcalls);

        return 1;
}

int modul_eTest(int argc, char** argv) {
        int n = 3;
        Function f = Function(onedivx, "onedivx");
        double xvalues[n];
        double fResults[n], fDifResults[n], fIntResults[n];
        double intExpected[n], difExpected[n];
        int intcalls[n], difcalls[n];

        printf("Testing function \"OneDivX\"");

        int i = 0;
        for (double x = 1; i < n; x += n, i++) {
                intExpected[i] = log(fabs(x));
                difExpected[i] = -(1/(x*x));

                xvalues[i] = x;
                fResults[i] = f(x);

                fDifResults[i] = differentiate(f, x, ERR);
                difcalls[i] = getDifCalls();
                assertEqualsF(difExpected[i], fDifResults[i], fabs(difExpected[i]) * EPS);

                fIntResults[i] = integrate(f, 1, x, ERR);
                intcalls[i] = getIntCalls();
                assertEqualsF(intExpected[i], fIntResults[i], fabs(intExpected[i])*EPS);
        }

        print(n, xvalues, fResults, fDifResults, fIntResults, difcalls, intcalls);

        return 1;
}

int modul_fTest(int argc, char** argv) {
        int n = 4;
        Function f = Function(squaro, "sqaro");
        double xvalues[n];
        double fResults[n], fDifResults[n], fIntResults[n];
        double intExpected[n], difExpected[n];
        int intcalls[n], difcalls[n];

        printf("Testing function \"Squareroot\"");

        int i = 0;
        for (double x = 1; i < n; x *= n, i++) {
                intExpected[i] = (2*pow(x, (3./2.)))/3 - (2*pow(1., (3./2.)))/3;
                difExpected[i] = 1/(2*sqrt(x));

                xvalues[i] = x;
                fResults[i] = f(x);

                fDifResults[i] = differentiate(f, x, ERR);
                difcalls[i] = getDifCalls();
                assertEqualsF(difExpected[i], fDifResults[i], fabs(difExpected[i]) * EPS);

                fIntResults[i] = integrate(f, 1, x, ERR);
                intcalls[i] = getIntCalls();
                assertEqualsF(intExpected[i], fIntResults[i], fabs(intExpected[i])*EPS);
        }

        print(n, xvalues, fResults, fDifResults, fIntResults, difcalls, intcalls);

        return 1;
}
