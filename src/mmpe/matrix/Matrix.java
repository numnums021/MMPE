package mmpe.matrix;

import java.util.ArrayList;
import java.util.Map;


public class Matrix {

    final double A = 0.73;

    private String name;
    private double matrix[][];
    private int columns;
    private int rows;

    public Matrix(String name, int i, int j) {
        this.name = name;
        matrix = new double[i][j];
        columns = i;
        rows = j;
    }

    public void fillingMatrixValues(){
        matrix[0][0] = 9.54;
        matrix[0][1] = 12.15;
        matrix[0][2] = 13.24;
        matrix[0][3] = 12.88;
        matrix[0][4] = 11.6;
        matrix[0][5] = 15.28;
        matrix[0][6] = 17;
        matrix[0][7] = 13.58;
        matrix[0][8] = 13.4;
        matrix[0][9] = 9.71;
        matrix[0][10] = 11.9;
        matrix[0][11] = 14.03;
        matrix[0][12] = 10.2;
        matrix[0][13] = 7.97;
        matrix[0][14] = 8.58;
    }

    public void fillingMatrixCodingFactors() {
        matrix[0][0] = -1;      matrix[1][0] = -1;       matrix[2][0] = -1;
        matrix[0][1] = 1;       matrix[1][1] = -1;       matrix[2][1] = -1;
        matrix[0][2] = -1;      matrix[1][2] = 1;        matrix[2][2] = -1;
        matrix[0][3] = 1;       matrix[1][3] = 1;        matrix[2][3] = -1;
        matrix[0][4] = -1;      matrix[1][4] = -1;       matrix[2][4] = 1;
        matrix[0][5] = 1;       matrix[1][5] = -1;       matrix[2][5] = 1;
        matrix[0][6] = -1;      matrix[1][6] = 1;        matrix[2][6] = 1;
        matrix[0][7] = 1;       matrix[1][7] = 1;        matrix[2][7] = 1;
        matrix[0][8] = -1.215;  matrix[1][8] = 0;        matrix[2][8] = 0;
        matrix[0][9] = 1.215;   matrix[1][9] = 0;        matrix[2][9] = 0;
        matrix[0][10] = 0;      matrix[1][10] = -1.215;  matrix[2][10] = 0;
        matrix[0][11] = 0;      matrix[1][11] = 1.215;   matrix[2][11] = 0;
        matrix[0][12] = 0;      matrix[1][12] = 0;       matrix[2][12] = -1.215;
        matrix[0][13] = 0;      matrix[1][13] = 0;       matrix[2][13] = 1.215;
        matrix[0][14] = 0;      matrix[1][14] = 0;       matrix[2][14] = 0;
    }

    public void fillNaturalFactors(Map<String, ArrayList<Double>> initialMap, Matrix matrixCodedFactors){
        double [][] matrixCodingFactors = matrixCodedFactors.getMatrix();
        for (int i = 0; i < columns; i++) {
            double xAvg = initialMap.get("X").get(i);
            double xMin = initialMap.get("xMin").get(i);
            for (int j = 0; j< rows; j++) {
                double elementMatrixCodedFactors = matrixCodingFactors[i][j];
                matrix[i][j] = (xAvg - xMin) * elementMatrixCodedFactors  + xAvg;
            }
        }
    }

    public void fillExpMatrix(double[][] factorCodingMatrix, double[][] yExpMatrix) {

        int colFC = factorCodingMatrix.length; // 3
        int rowFC = factorCodingMatrix[0].length; // 15

        // перенос данных из кодированной матрицы в расширенную
        for (int i = 1; i < colFC + 1; i++) {
            for (int j = 0; j < rowFC; j++) {
                matrix[i][j] = factorCodingMatrix[i - 1][j];
            }
        }

        // заполнение нулевого единичного столбца и столбцы с 4 по 10
        for (int i = 0; i < rows; i++) {
            // 0 столбец
            matrix[0][i] = 1.0;
            // x1x2
            matrix[colFC + 1][i] = matrix[1][i] * matrix[2][i];
            //x1x3
            matrix[colFC + 2][i] = matrix[1][i] * matrix[3][i];
            //x2x3
            matrix[colFC + 3][i] = matrix[2][i] * matrix[3][i];
            //x1x2x3
            matrix[colFC + 4][i] = matrix[1][i] * matrix[2][i] * matrix[3][i];
            // x1^2-a
            matrix[colFC + 5][i] = (matrix[1][i] * matrix[1][i]) - A;
            // x2^2-a
            matrix[colFC + 6][i] = (matrix[2][i] * matrix[2][i]) - A;
            // x3^2-a
            matrix[colFC + 7][i] = (matrix[3][i] * matrix[3][i]) - A;
        }

        // заполнение с 11 по 20 столбец
        int tmp = 0;
        for (int i = 11; i < columns; i++) { // 22
            for (int j = 0; j < rows; j++) { // 15
                matrix[i][j] = yExpMatrix[0][j] * matrix[tmp][j];
            }
            tmp++;
        }
    }

    public void fillSumSquaresMatrix(double[][] expandedMatrix) {
        for (int i = 0; i < 11; i++){
            matrix[i][0] = getSumSquresColumns(i, expandedMatrix);
        }

        int tmp = 0;
        for (int i = 11; i < columns; i++){
            matrix[i][0] = getSumSquresColumns(i, expandedMatrix) / matrix[tmp][0];
            tmp++;
        }
    }

    public double getSumSquresColumns(int column, double[][] expandedMatrix) {
        double sumExpandedMatrix = 0.0;
        for (int row = 0; row < expandedMatrix[0].length; row++){
            sumExpandedMatrix += expandedMatrix[column][row] * expandedMatrix[column][row];
        }
        return sumExpandedMatrix;
    }

    public void fillRegrMatrix(double[][] expandedMatrix, double[][] sumSquaresMatrix) {
        for (int row = 0; row < expandedMatrix[0].length; row++) {
            int tmp = 11;
            double sum = 0;
            for (int col = 0; col < expandedMatrix.length / 2; col++) {
                sum += expandedMatrix[col][row] * sumSquaresMatrix[tmp][0];
                if (tmp == 21) {
                    tmp = 11;
                } else {
                    tmp++;
                }
            }
            matrix[0][row] = sum;
        }
    }

    public void fillCoaMatrix(double[][] yExpMatrix, double[][] regressionMatrix) {
        for (int row = 0; row < rows; row++){
            matrix[0][row] = Math.abs((yExpMatrix[0][row] - regressionMatrix[0][row]) / regressionMatrix[0][row]);
        }
    }

    public void printMatrix(){
        System.out.println("Печать матрицы '" + name +"'");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%10.2f", matrix[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
