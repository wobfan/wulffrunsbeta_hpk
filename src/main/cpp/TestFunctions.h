/*
 * TestFunctions.h
 *
 *  Created on: 11.12.2017
 *      Author: wladimr
 */

#ifndef TESTFUNCTIONS_H_
#define TESTFUNCTIONS_H_

#define ERR 1E-8
#define EPS 1E-8

#include "Function.h"
#include "Differentiator.h"
#include "Integrator.h"
#include "CUnit.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int modul_aTest(int argc, char** args);
int modul_bTest(int argc, char** args);
int modul_cTest(int argc, char** args);
int modul_dTest(int argc, char** args);

#endif /* TESTFUNCTIONS_H_ */
