package mmpe;

import mmpe.matrix.Matrix;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Map<String, ArrayList<Double>> initialDataMap = enterInputStream();

        Matrix criteria = new Matrix("Столбец значений Yexp", 1, 15);
        criteria.fillingCriteriaMatrix();
        criteria.printMatrix();

        Matrix factorCoding = new Matrix("Матрица кодирования фактора", 3, 15);
        factorCoding.fillingCodingFactorsMatrix();
        factorCoding.printMatrix();

        Matrix factorNatural = new Matrix("Матрица натуральных значений", 3, 15);
        factorNatural.fillingNaturalFactorsMatrix(initialDataMap, factorCoding.getMatrix());
        factorNatural.printMatrix();

        Matrix extended = new Matrix("Расширенная матрица", 22, 15);
        extended.fillingExtendedMatrix(factorCoding.getMatrix(), criteria.getMatrix());
        extended.printMatrix();

        Matrix sumSquares = new Matrix("Сумма квадратов", 22, 1);
        sumSquares.fillingSumSquaresMatrix(extended.getMatrix());
        sumSquares.printMatrix();

        Matrix regression = new Matrix("Регрессионная матрица Yregr", 1,15);
        regression.fillingRegressionMatrix(extended.getMatrix(), sumSquares.getMatrix());
        regression.printMatrix();

        Matrix cao = new Matrix("COA", 1, 15);
        cao.fillingCAOMatrix(criteria.getMatrix(), regression.getMatrix());
        cao.printMatrix();
    }

    // 5,5     4,5    9,5
    static Map<String, ArrayList<Double>> enterInputStream(){
        Map<String, ArrayList<Double>> initialDataMap = new HashMap<>();

        initialDataMap.put("X", new ArrayList<>());
        initialDataMap.put("xMin", new ArrayList<>(Arrays.asList(5.0, 4.0, 8.0)));
        initialDataMap.put("xMax", new ArrayList<>(Arrays.asList(6.0, 5.0, 11.0)));

        System.out.println("Введите x1, x2 и x3 соответственно");
        for (int i = 0; i < 3; i++) {
            System.out.print("x" + (i + 1) +" = ");
            try {
            double value = new Scanner(System.in).nextDouble();
            if (value > initialDataMap.get("xMin").get(i) && value < initialDataMap.get("xMax").get(i))
                initialDataMap.get("X").add(value);
            else {
                System.out.println("Ошибка ввода данных. Введённые данные не соответствуют заданному диапазону!");
                i--;
            }
            }
        catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода данных. Введённые данные не являются числом с плавающей точкой!");
                i--;
            }
        }

        return initialDataMap;
    }
}
