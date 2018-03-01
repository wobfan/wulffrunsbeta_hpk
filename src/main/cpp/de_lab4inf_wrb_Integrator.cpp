 	/*
 * de_lab4inf_wrb_Integrator.cpp
 *
 *  Created on: Dec 8, 2017
 *      Author: stefan
 */

#include <jni.h>
#include "de_lab4inf_wrb_Integrator.h"
#include "JavaFunction.h"
#include "Integrator.h"

JNIEXPORT jdouble JNICALL Java_de_lab4inf_wrb_Integrator_integrate
(JNIEnv *env, jobject obj, jobject fct, jdouble x, jdouble y, jdouble err) {
	double r = 0;
	try {
		JavaFunction f = JavaFunction(env, fct);
		r = integrate(f, x, y, err);
	} catch(const char* error) {
		jclass exception = env->FindClass("java/lang/ArithmeticException");
		env->ThrowNew(exception, error);
	}

	return r;
}
