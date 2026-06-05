# Быстрое возведение в степень (Binary Exponentiation)

## Intuition
Быстрое возведение в степень — это алгоритм для вычисления a^n за O(log n) операций вместо O(n). Идея основана на двоичном представлении показателя степени. Например, для вычисления 3^13 вместо 13 умножений (3×3×3×...), мы представляем 13 как 1101₂ (8+4+1) и вычисляем: 3^13 = 3^8 × 3^4 × 3^1. Это требует всего 3 умножения вместо 13! Представьте, что вы строите степени числа: сначала a, затем a², a⁴, a⁸ и так далее, а затем перемножаете только те, которые соответствуют единицам в двоичной записи показателя.

## Approach
1. Представляем показатель n в двоичной системе
2. Инициализируем result = 1
3. Пока n > 0:
    - Если текущий бит n равен 1: result = result × a
    - a = a × a (возводим в квадрат для следующего бита)
    - n = n >> 1 (сдвигаем вправо)
4. Ключевое свойство: a^(b+c) = a^b × a^c

## Complexity
- Time complexity: **O(log n)** — количество битов в показателе
- Space complexity: **O(1)**

## Code

```java
public class BinaryExponentiation {
    
    // Базовая версия для целых чисел (без модуля)
    public static long pow(long a, long n) {
        long result = 1;
        
        while (n > 0) {
            if ((n & 1) == 1) {  // если текущий бит = 1
                result *= a;
            }
            a *= a;              // возводим a в квадрат
            n >>= 1;             // сдвигаем вправо
        }
        
        return result;
    }
    
    // Версия с модулем (для больших чисел)
    public static long powMod(long a, long n, long mod) {
        long result = 1;
        a %= mod;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = (result * a) % mod;
            }
            a = (a * a) % mod;
            n >>= 1;
        }
        
        return result;
    }
    
    // Рекурсивная версия
    public static long powRecursive(long a, long n) {
        if (n == 0) return 1;
        
        long half = powRecursive(a, n / 2);
        long result = half * half;
        
        if (n % 2 == 1) {
            result *= a;
        }
        
        return result;
    }
    
    // Рекурсивная версия с модулем
    public static long powRecursiveMod(long a, long n, long mod) {
        if (n == 0) return 1 % mod;
        
        long half = powRecursiveMod(a, n / 2, mod);
        long result = (half * half) % mod;
        
        if (n % 2 == 1) {
            result = (result * (a % mod)) % mod;
        }
        
        return result;
    }
    
    // Для больших чисел (BigInteger)
    public static java.math.BigInteger powBig(java.math.BigInteger a, java.math.BigInteger n) {
        java.math.BigInteger result = java.math.BigInteger.ONE;
        
        while (n.compareTo(java.math.BigInteger.ZERO) > 0) {
            if (n.and(java.math.BigInteger.ONE).equals(java.math.BigInteger.ONE)) {
                result = result.multiply(a);
            }
            a = a.multiply(a);
            n = n.shiftRight(1);
        }
        
        return result;
    }
    
    // Для матриц (возведение матрицы в степень)
    public static int[][] matrixPow(int[][] matrix, int n) {
        int size = matrix.length;
        int[][] result = identityMatrix(size);
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = multiplyMatrices(result, matrix);
            }
            matrix = multiplyMatrices(matrix, matrix);
            n >>= 1;
        }
        
        return result;
    }
    
    private static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int[][] result = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        
        return result;
    }
    
    private static int[][] identityMatrix(int size) {
        int[][] identity = new int[size][size];
        for (int i = 0; i < size; i++) {
            identity[i][i] = 1;
        }
        return identity;
    }
    
    // Возведение числа в степень с плавающей точкой
    public static double powDouble(double a, int n) {
        if (n == 0) return 1;
        if (n < 0) {
            a = 1 / a;
            n = -n;
        }
        
        double result = 1;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result *= a;
            }
            a *= a;
            n >>= 1;
        }
        
        return result;
    }
    
    // Вычисление n-го числа Фибоначчи за O(log n)
    public static long fibonacci(int n) {
        if (n <= 1) return n;
        
        long[][] base = {{1, 1}, {1, 0}};
        long[][] result = matrixPowLong(base, n - 1);
        
        return result[0][0];
    }
    
    private static long[][] matrixPowLong(long[][] matrix, int n) {
        int size = matrix.length;
        long[][] result = identityMatrixLong(size);
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = multiplyMatricesLong(result, matrix);
            }
            matrix = multiplyMatricesLong(matrix, matrix);
            n >>= 1;
        }
        
        return result;
    }
    
    private static long[][] multiplyMatricesLong(long[][] a, long[][] b) {
        int n = a.length;
        long[][] result = new long[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        
        return result;
    }
    
    private static long[][] identityMatrixLong(int size) {
        long[][] identity = new long[size][size];
        for (int i = 0; i < size; i++) {
            identity[i][i] = 1;
        }
        return identity;
    }
    
    // Модульное возведение в степень с защитой от переполнения (умножение через сложение)
    public static long powModSafe(long a, long n, long mod) {
        long result = 1;
        a %= mod;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = multiplyMod(result, a, mod);
            }
            a = multiplyMod(a, a, mod);
            n >>= 1;
        }
        
        return result;
    }
    
    private static long multiplyMod(long a, long b, long mod) {
        long result = 0;
        a %= mod;
        
        while (b > 0) {
            if ((b & 1) == 1) {
                result = (result + a) % mod;
            }
            a = (a * 2) % mod;
            b >>= 1;
        }
        
        return result;
    }
}