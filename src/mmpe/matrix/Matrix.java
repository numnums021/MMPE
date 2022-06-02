package mmpe.matrix;

import java.util.ArrayList;
import java.util.Map;


public class Matrix {

    private final String name;
    private final double[][] matrix;

    public Matrix(String name, int columns, int rows) {
        this.name = name;
        this.matrix = new double[columns][rows];
    }

    public void fillingCriteriaMatrix(){
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

    public void fillingCodingFactorsMatrix() {
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

    public void fillingNaturalFactorsMatrix(Map<String, ArrayList<Double>> initialMap, double[][] matrixCodingFactors){

        int columns = matrix.length;
        int rows = matrix[0].length;
        for (int i = 0; i < columns; i++) {
            double xAvg = initialMap.get("X").get(i);
            double xMin = initialMap.get("xMin").get(i);
            for (int j = 0; j< rows; j++) {
                double elementMatrixCodedFactors = matrixCodingFactors[i][j];
                matrix[i][j] = (xAvg - xMin) * elementMatrixCodedFactors  + xAvg;
            }
        }
    }

    public void fillingExtendedMatrix(double[][] factorCodingMatrix, double[][] yExpMatrix) {

        int colFC = factorCodingMatrix.length; // 3
        int rowFC = factorCodingMatrix[0].length; // 15

        // перенос данных из кодированной матрицы в расширенную
        for (int i = 1; i < colFC + 1; i++) {
            for (int j = 0; j < rowFC; j++) {
                matrix[i][j] = factorCodingMatrix[i - 1][j];
            }
        }

        int rows = matrix[0].length;
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
            double a = 0.73;
            matrix[colFC + 5][i] = (matrix[1][i] * matrix[1][i]) - a;
            // x2^2-a
            matrix[colFC + 6][i] = (matrix[2][i] * matrix[2][i]) - a;
            // x3^2-a
            matrix[colFC + 7][i] = (matrix[3][i] * matrix[3][i]) - a;
        }

        // заполнение с 11 по 20 столбец
        int columns = matrix.length;
        int tmp = 0;
        for (int i = columns / 2; i < columns; i++) { // 22
            for (int j = 0; j < rows; j++) { // 15
                matrix[i][j] = yExpMatrix[0][j] * matrix[tmp][j];
            }
            tmp++;
        }
    }

    public void fillingSumSquaresMatrix(double[][] expandedMatrix) {
        for (int i = 0; i < 11; i++){
            matrix[i][0] = getSumSquaresColumn(i, expandedMatrix);
        }

        int tmp = 0;
        for (int i = 11; i < matrix.length; i++){
            matrix[i][0] = getSumColumn(i, expandedMatrix) / matrix[tmp][0];
            tmp++;
        }
    }

    public void fillingRegressionMatrix(double[][] expandedMatrix, double[][] sumSquaresMatrix) {
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

    public void fillingCAOMatrix(double[][] yExpMatrix, double[][] regressionMatrix) {
        int rows = matrix[0].length; // 15
        for (int row = 0; row < rows; row++)
            matrix[0][row] =
                    Math.abs((yExpMatrix[0][row] - regressionMatrix[0][row]) / regressionMatrix[0][row]);
    }

    public double getSumSquaresColumn(int column, double[][] expandedMatrix) {
        double sumExpandedMatrix = 0.0;
        for (int row = 0; row < expandedMatrix[0].length; row++){
            sumExpandedMatrix += expandedMatrix[column][row] * expandedMatrix[column][row];
        }
        return sumExpandedMatrix;
    }

    public double getSumColumn(int column, double[][] expandedMatrix) {
        double sum = 0.0;
        for (int row = 0; row < expandedMatrix[0].length; row++){
            sum += expandedMatrix[column][row];
        }
        return sum;
    }

    public void printMatrix(){
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Печать матрицы '" + name +"'");

        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.printf("%10.2f", matrix[j][i]);
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    public double[][] getMatrix() {
        return matrix;
    }

}
