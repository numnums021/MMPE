package mmpe;

import mmpe.matrix.Matrix;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Map<String, ArrayList<Double>> initialDataMap = new HashMap<>();

        initialDataMap.put("X", enterInputStream()); // new ArrayList<>(Arrays.asList(5.5, 4.5, 9.5))
        initialDataMap.put("xMin", new ArrayList<>(Arrays.asList(5.0, 4.0, 8.0)));
        initialDataMap.put("xMax", new ArrayList<>(Arrays.asList(6.0, 5.0, 11.0)));

        Matrix valueColumn = new Matrix("Столбец значений Yexp", 1, 15);
        valueColumn.fillingMatrixValues();
        valueColumn.printMatrix();

        Matrix factorCoding = new Matrix("Матрица кодирования фактора", 3, 15);
        factorCoding.fillingMatrixCodingFactors();
        factorCoding.printMatrix();

        Matrix naturalMatrix = new Matrix("Матрица натуральных значений", 3, 15);
        naturalMatrix.fillNaturalFactors(initialDataMap, factorCoding);
        naturalMatrix.printMatrix();

        Matrix expandedMatrix = new Matrix("Расширенная матрица", 22, 15);
        expandedMatrix.fillExpMatrix(factorCoding.getMatrix(), valueColumn.getMatrix());
        expandedMatrix.printMatrix();

        Matrix sumSquaresMatrix = new Matrix("Сумма квадратов", 22, 1);
        sumSquaresMatrix.fillSumSquaresMatrix(expandedMatrix.getMatrix());
        sumSquaresMatrix.printMatrix();

        Matrix regressionMatrix = new Matrix("Регрессионная матрица Yregr", 1,15);
        regressionMatrix.fillRegrMatrix(expandedMatrix.getMatrix(), sumSquaresMatrix.getMatrix());
        regressionMatrix.printMatrix();

        Matrix COAMatrix = new Matrix("COA", 1, 15);
        COAMatrix.fillCoaMatrix(valueColumn.getMatrix(), regressionMatrix.getMatrix());
        COAMatrix.printMatrix();
    }

    // 5,5     4,5    9,5
    static ArrayList<Double> enterInputStream(){
        Scanner in = new Scanner(System.in);
        ArrayList<Double> X = new ArrayList<>();
        System.out.println("Введите x1, x2 и x3 соответственно");

        System.out.print("x1 = ");
        X.add(in.nextDouble());
        System.out.print("x2 = ");
        X.add(in.nextDouble());
        System.out.print("x3 = ");
        X.add(in.nextDouble());

        return X;
    }
}
