/*
 * de_lab4inf_wrb_Differentiator.cpp
 *
 *  Created on: Dec 8, 2017
 *      Author: stefan
 */

#include "de_lab4inf_wrb_Differentiator.h"
#include "Differentiator.h"
#include "JavaFunction.h"

JNIEXPORT jdouble JNICALL Java_de_lab4inf_wrb_Differentiator_differentiate
  (JNIEnv * env, jobject obj, jobject fct, jdouble x, jdouble err) {

	double r = 0;
	try {
		JavaFunction f = JavaFunction(env, fct);
		r = differentiate(f, x, err);
	} catch(const char* error) {
		jclass exception = env->FindClass("java/lang/ArithmeticException");
		env->ThrowNew(exception, error);
	}

	return r;
}
