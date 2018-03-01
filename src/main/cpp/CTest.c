/*
 * CTests.c
 *
 *  Created on: 09.12.2017
 *      Author: wladimr
 */


#include <stdlib.h>
#include <stdio.h>
#include "CUnit.h"
#include "TestFunctions.h"


    DECLARE_TEST(modul_a)
    DECLARE_TEST(modul_b)
    DECLARE_TEST(modul_c)
	DECLARE_TEST(modul_d)


    BEG_SUITE(suite)

        ADD_TEST(modul_a),
        ADD_TEST(modul_b),
        ADD_TEST(modul_c),
		ADD_TEST(modul_d)

    END_SUITE(suite)

    RUN_SUITE(suite)
