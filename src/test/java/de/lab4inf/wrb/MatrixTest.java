package de.lab4inf.wrb;

import static java.lang.Math.floor;
import static java.lang.Math.random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

public class MatrixTest {
	final double eps = 1.E-8;
	Script script;

	private static long[] pow10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 100000000, 10000000000L };
	private static final int DEFAULT_DIGITS = 3;

	protected static double rnd(final int position) {
		if (position < 0 || position >= pow10.length) {
			String msg = String.format("wrong number of digits %d", position);
			throw new IllegalArgumentException(msg);
		}
		final long p10 = pow10[position];
		return floor((random() - 0.5) * (p10 << 1)) / p10;
	}

	protected static double rnd() {
		return rnd(DEFAULT_DIGITS);
	}

	private double[][] randomMatrix(int x, int y) {
		double[][] matrix = new double[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				matrix[i][j] = rnd();
			}
		}
		return matrix;
	}
	
	private double inSec(long value) {
		return (double) value / 1000000000;
	}

	public boolean compareMatrices(double[][] serial, double[][] parallel) {
		for (int i = 0; i < serial.length; i++) {
			for (int j = 0; j < serial[0].length; j++) {
//				if (serial[i][j] != parallel[i][j]) return false;
//				assertTrue(String.format("matrices differ at %dx%d", i, j), Math.abs((serial[i][j] - parallel[i][j]) / serial[i][j]) < eps);
				assertEquals(String.format("matrices differ at %dx%d", i, j), serial[i][j], parallel[i][j], eps);
			}
		}
		return true;
	}

	@Test
	public final void randomDimensionRandomMatrix() throws InterruptedException {
		int dimension = ThreadLocalRandom.current().nextInt(1, 512);
		int difference = ThreadLocalRandom.current().nextInt(1, 128);
		double[][] m1 = randomMatrix(dimension, dimension + difference);
		double[][] m2 = randomMatrix(dimension + difference, dimension);

		double[][] serial = Matrix.matSeriell(m1, m2);
		double[][] parallel = Matrix.matDivideConquer(m1, m2);

		assertTrue("The serial matrix differs from the parallel one!", compareMatrices(serial, parallel));
	}

	@Test
	public final void calculateTestMatrices() throws InterruptedException {
		double[][] m1 = {	{14.1, 15, 2, 1},
				{1, 9, 3, 23},
				{11, 9, 2, 5}		};
		double[][] m2 = {	{2, 13.1, 1, 1, 4},
				{1, 3, 6, 21, 0},
				{6, 35, 1, 0, 12.1},
				{2, 12, 0, 2, 5}	};

		double[][] expectedResult = {	{57.2, 311.71, 106.1, 331.1, 85.6},
				{75.0, 421.1, 58.0, 236.0, 155.3},
				{53.0, 301.1, 67.0, 210.0, 93.2}};
		
		double[][] serialResult = Matrix.matSeriell(m1, m2);
		double[][] parallelResult = Matrix.matDivideConquer(m1, m2);
		
		assertTrue("The serial calculated Matrix differs from the expected Matrix!", compareMatrices(serialResult, expectedResult));
		assertTrue("The parallel calculated Matrix differs from the expected Matrix!", compareMatrices(parallelResult, expectedResult));
	}
	
	@Test
	public final void calcMatr1() throws InterruptedException {
		double[][] m1 = {	{14.1, 15, 2, 1},
				{1, 9, 3, 23},
				{11, 9, 2, 5}		};
		double[][] m2 = {	{2, 13.1, 1, 1, 4},
				{1, 3, 6, 21, 0},
				{6, 35, 1, 0, 12.1},
				{2, 12, 0, 2, 5}	};

		double[][] expectedResult = {	{57.2, 311.71, 106.1, 331.1, 85.6},
				{75.0, 421.1, 58.0, 236.0, 155.3},
				{53.0, 301.1, 67.0, 210.0, 93.2}};
		
		double[][] serialResult = Matrix.matSeriell(m1, m2);
		double[][] parallelResult = Matrix.matDivideConquer(m1, m2);
		
		assertTrue("The serial calculated Matrix differs from the expected Matrix!", compareMatrices(serialResult, expectedResult));
		assertTrue("The parallel calculated Matrix differs from the expected Matrix!", compareMatrices(parallelResult, expectedResult));
	}
	
	@Test
	public final void calcMatr2() throws InterruptedException {
		double[][] m1 = {{5.9, 456, 3556},
				{57, 29, 45},
				{6, 9, 14},
				{ 78, 8, 2}};
		double[][] m2 = {	{12.3, 12, 76, 67, 94},
				{14, 41, 78, 2.2, 98},
				{67, 454, 9976, 56, 1.1},
		};

		double[][] expectedResult = {{244708.57, 1633190.8, 35510672.4, 200534.5, 49154.2 },
				{4122.1, 22303.0, 455514.0, 6402.8, 8249.5 },
				{1137.8, 6797.0, 140822.0, 1205.8, 1461.4 },
				{1205.4, 2172.0, 26504.0, 5355.6, 8118.2 }};
		
		double[][] serialResult = Matrix.matSeriell(m1, m2);
		double[][] parallelResult = Matrix.matDivideConquer(m1, m2);
		
		assertTrue("The serial calculated Matrix differs from the expected Matrix!", compareMatrices(serialResult, expectedResult));
		assertTrue("The parallel calculated Matrix differs from the expected Matrix!", compareMatrices(parallelResult, expectedResult));
	}
	
	@Test
	public final void calcMatr3() throws InterruptedException {
		double[][] m1 = {	{12.9, 448 , 9411.5},
				{57.44, 294, 1494},
				{64, 945, 14},
				{7158, 7, 244} };
		double[][] m2 = {	{2.3, 5, 14, 25, 8},
				{16, 4, 54, 2, 55},
				{67.8, 65, 7, 65, 1.1},
		};

		double[][] expectedResult = {{645297.37, 613604.0, 90253.1, 612966.0, 35095.85 },
				{106129.312, 98573.2, 27138.16, 99134.0, 18272.92 },
				{16216.4, 5010.0, 52024.0, 4400.0, 52502.4 },
				{33118.6, 51678.0, 102298.0, 194824.0, 57917.4 }};
		
		double[][] serialResult = Matrix.matSeriell(m1, m2);
		double[][] parallelResult = Matrix.matDivideConquer(m1, m2);
		
		assertTrue("The serial calculated Matrix differs from the expected Matrix!", compareMatrices(serialResult, expectedResult));
		assertTrue("The parallel calculated Matrix differs from the expected Matrix!", compareMatrices(parallelResult, expectedResult));
	}

	@Test
	public final void timingMatrixMultiplication() throws InterruptedException {
		Integer dim[] = { 64, 128, 256, 512, 768, 1024, 1536, 2048 };
		Integer rep[] = { 100, 50, 25, 12, 5, 2, 1, 1 };
		long fullTime = System.nanoTime();

		System.out.println("repititions\t| dimension\t| serial\t| parallel\t| speedup");
		System.out.println("\t\t+\t\t+\t\t+\t\t+");
		for (int i = 0; i < dim.length; i++) {			
			double[][] m1 = randomMatrix(dim[i], dim[i]+1);
			double[][] m2 = randomMatrix(dim[i]+1, dim[i]);
			double[][] serial = new double[dim[i]][dim[i]];
			double[][] parallel = new double[dim[i]][dim[i]];

			long startTime = System.nanoTime();
			System.out.printf("%d\t\t| %d x %d\t| ", rep[i], dim[i], dim[i]+1);
			for (int k = 0; k < rep[i]; k++) 
				serial = Matrix.matSeriell(m1, m2);
			long switchTime = System.nanoTime();
			long serialTime = (switchTime - startTime) / rep[i];
			System.out.printf("%.4f\t| ", inSec(serialTime));
			for (int k = 0; k < rep[i]; k++) 
				parallel = Matrix.matDivideConquer(m1, m2);
			long parallelTime = (System.nanoTime() - switchTime) / rep[i];
			double speedup = (double) serialTime / parallelTime;

			System.out.printf("%.4f\t| %.1f\n", inSec(parallelTime), speedup);
			assertTrue("Serial matrix differs from parallel matrix.", compareMatrices(serial, parallel));
		}

		System.out.printf("\n -> Needed %d nanoseconds / %.1f seconds to pass the whole test.\n", System.nanoTime() - fullTime, inSec(System.nanoTime() - fullTime));
	}

}

