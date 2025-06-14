package by.it.group410972.rak.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        int period = getPisanoPeriod(m);

        int remainderIndex = (int)(n % period);

        return getFibonacciMod(remainderIndex, m);
    }

    // Нахождение периода Пизано для заданного m
    private int getPisanoPeriod(int m) {
        int a = 0, b = 1;
        for (int i = 0; i < m * 6; i++) { // максимум длина периода ≤ 6*m
            int c = (a + b) % m;
            a = b;
            b = c;
            if (a == 0 && b == 1) {
                return i + 1;
            }
        }
        return 0;
    }

    // Вычисление n-го числа Фибоначчи по модулю m
    private int getFibonacciMod(int n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        int a = 0;
        int b = 1;
        for (int i = 2; i <= n; i++) {
            int c = (a + b) % m;
            a = b;
            b = c;
        }
        return b;
    }

}

