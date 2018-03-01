package de.lab4inf.wrb;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix {

	/*
	 * Multiplicates m1 with m2 serially
	 */
	public static double[][] matSeriell(double[][] m1, double[][] m2) {
		if (m1[0].length != m2.length) throw new IllegalArgumentException("m1 row dimension from m2 column dimension");
		MatrixMultiplication multi = new MatrixMultiplication(m1, transpose(m2));
		multi.run();
		return multi.getResult();
	}

	public static void printMatrix(double[][] m) {
		for (double[] a : m) {
			for (double b : a) {
				System.out.print(b + " ");
			}
			System.out.println();
		}
	}

	/*
	 * Divides m1 into pieces fitting to processor thread count and works with them in Threads
	 */
	public static double[][] matDivideConquer(double[][] m1, double[][] m2) throws InterruptedException {		
		if (m1[0].length != m2.length) throw new IllegalArgumentException("m1 row dimension from m2 column dimension");

		ArrayList<double[][][]> splittedMatrices;
		double[][][] a, b;
		double[][][] tempResult = new double[4][][];
		double[][] result;

		Thread[] threads = new Thread[8];
		MatrixMultiplication[] multi = new MatrixMultiplication[8];
		MatrixAddition[] addi = new MatrixAddition[4];

		splittedMatrices = splitMatrices(m1, m2);
		a = splittedMatrices.get(0);
		b = splittedMatrices.get(1);

		for (int i = 0; i < b.length; i++) b[i] = transpose(b[i]);

		multi[0] = new MatrixMultiplication(a[0], b[0]);
		multi[1] = new MatrixMultiplication(a[1], b[2]);
		multi[2] = new MatrixMultiplication(a[0], b[1]);
		multi[3] = new MatrixMultiplication(a[1], b[3]);
		multi[4] = new MatrixMultiplication(a[2], b[0]);
		multi[5] = new MatrixMultiplication(a[3], b[2]);
		multi[6] = new MatrixMultiplication(a[2], b[1]);
		multi[7] = new MatrixMultiplication(a[3], b[3]);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(multi[i]);
			threads[i].start();
		}
		for (Thread thread : threads) thread.join();

		addi[0] = new MatrixAddition(multi[0].getResult(), multi[1].getResult());
		addi[1] = new MatrixAddition(multi[2].getResult(), multi[3].getResult());
		addi[2] = new MatrixAddition(multi[4].getResult(), multi[5].getResult());
		addi[3] = new MatrixAddition(multi[6].getResult(), multi[7].getResult());

		for (int i = 0; i < 4; i++) {
			threads[i] = new Thread(addi[i]);
			threads[i].start();
		}
		for (Thread thread : threads) thread.join();

		for (int i = 0; i < 4; i++) tempResult[i] = addi[i].getResult();
		result = merge(tempResult);

		return result;
	}

	/*
	 * Merges an array of matrices into one matrix.
	 */
	public static double[][] merge(double[][][] m) {
		int rows = m[0].length + m[2].length;
		int columns = m[0][0].length + m[1][0].length;
		double[][] result = new double[rows][columns];

		double[][] m1 = m[0], m2 = m[1], m3 = m[2], m4 = m[3];

		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {
				result[i][j] = m1[i][j];
			}
		}

		for (int i = 0; i < m2.length; i++) {
			for (int j = 0; (j + m1[0].length) < result[0].length; j++) {
				result[i][j + m1[0].length] = m2[i][j];
			}
		}

		for (int i = 0; (i + m1.length) < result.length; i++) {
			for (int j = 0; j < m3[0].length; j++) {
				result[i + m1.length][j] = m3[i][j];
			}
		}

		for (int i = 0; (i + m2.length) < result.length; i++) {
			for (int j = 0; (j + m3[0].length) < result[0].length; j++) {
				result[i + m2.length][j + m3[0].length] = m4[i][j];
			}
		}

		return result;
	}

	/*
	 * Splits Matrix rows into barely matching workloads to match processor count
	 * TODO transpose in the end all partitions
	 */
	public static ArrayList<double[][][]> splitMatrices(double[][] m1, double[][] m2) {
		ArrayList<double[][][]> splittedMatrices = new ArrayList<>();
		splittedMatrices.add(split(m1));
		splittedMatrices.add(split(m2));

		return splittedMatrices;
	}

	private static double[][][] split(double[][] m) {
		int rowSplit = m.length / 2, colSplit = m[0].length / 2;
		double[][][] splittedMatrices = new double[4][][];

		double[][] m1 = new double[rowSplit][colSplit];
		double[][] m2 = new double[rowSplit][m[0].length - colSplit];
		double[][] m3 = new double[m.length - rowSplit][colSplit];
		double[][] m4 = new double[m.length - rowSplit][m[0].length - colSplit];

		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {
				m1[i][j] = m[i][j];
			}
		}

		for (int i = 0; i < m2.length; i++) {
			for (int j = 0; (j + m1[0].length) < m[0].length; j++) {
				m2[i][j] = m[i][j + m1[0].length];
			}
		}

		for (int i = 0; i < m3.length; i++) {
			for (int j = 0; j < m3[0].length; j++) {
				m3[i][j] = m[i + m1.length][j];
			}
		}

		for (int i = 0; i < m4.length; i++) {
			for (int j = 0; (j + m3[0].length) < m[0].length; j++) {
				m4[i][j] = m[i + m1.length][j + m3[0].length];
			}
		}

		splittedMatrices[0] = m1;
		splittedMatrices[1] = m2;
		splittedMatrices[2] = m3;
		splittedMatrices[3] = m4;

		return splittedMatrices;
	}

	@Deprecated
	public static double[][][] splitMatrix(double[][] m) {		
		int cores = Runtime.getRuntime().availableProcessors();
		int amount = m.length;				// amount of rows in m1
		if (amount < cores) cores = amount; 
		int workload[] = new int[2];
		double[][][] splittedMatrices;

		if((amount % cores) != 0) {
			workload[0] = amount % cores;
			amount -= workload[0];
		}
		workload[1] += amount / cores;

		splittedMatrices = new double[cores][workload[0]][m.length];

		int start = 0;
		int end = workload[0];
		for (int i = 0; i < cores; i++) {
			end = start + workload[1];
			splittedMatrices[i] = Arrays.copyOfRange(m, start, end);
			start = end;
		}

		return splittedMatrices;
	}

	private static double[][] transpose(double[][] m) {
		double[][] transposed = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				transposed[j][i] = m[i][j];
			}
		}
		return transposed;
	}
}

/*
 * Multiplicates two matrices and writes result into matrix.
 */
class MatrixMultiplication implements Runnable {
	private double[][] m1;
	private double[][] m2;
	private double[][] result;

	public MatrixMultiplication(double[][] m1, double[][] m2) {
		this.m1 = m1;
		this.m2 = m2;
		result = new double[m1.length][m2.length];
	}

	public double[][] getResult() { return result; }

	@Override
	public void run() {
		for (int i = 0; i < m1.length; i++) { 
			for (int j = 0; j < m2.length; j++) {
				for (int k = 0; k < m1[0].length; k++) {
					result[i][j] += m1[i][k] * m2[j][k];
				}
			}
		}
	}
}

class MatrixAddition implements Runnable {
	private double[][] m1;
	private double[][] m2;
	private double[][] result;

	public MatrixAddition(double[][] m1, double[][] m2) {
		this.m1 = m1;
		this.m2 = m2;
		result = new double[m1.length][m1[0].length];
	}

	public double[][] getResult() { return result; }

	public void run() {
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {
				result[i][j] = m1[i][j] + m2[i][j];
			}
		}
	}
}


