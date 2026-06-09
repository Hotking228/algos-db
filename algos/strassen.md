# Алгоритм Штрассена (Strassen's Matrix Multiplication)

## Intuition
Алгоритм Штрассена — это алгоритм умножения матриц, который работает быстрее стандартного O(n³). Он использует метод "разделяй-и-властвуй" и рекурсивно разбивает матрицы на 4 подматрицы. Вместо 8 умножений подматриц (как в стандартном методе), Штрассен делает 7 умножений, используя специальные комбинации подматриц, что дает асимптотику O(n^2.81). Представьте, что вы умножаете большие матрицы: вместо того чтобы делать все умножения по отдельности, вы сначала комбинируете блоки специальным образом, умножаете рекурсивно, а затем восстанавливаете результат.

## Approach
1. Разбиваем матрицы A и B на 4 подматрицы:
   A = [[A11, A12], [A21, A22]], B = [[B11, B12], [B21, B22]]
2. Вычисляем 7 вспомогательных матриц:
    - M1 = (A11 + A22) × (B11 + B22)
    - M2 = (A21 + A22) × B11
    - M3 = A11 × (B12 - B22)
    - M4 = A22 × (B21 - B11)
    - M5 = (A11 + A12) × B22
    - M6 = (A21 - A11) × (B11 + B12)
    - M7 = (A12 - A22) × (B21 + B22)
3. Результат C = A × B:
    - C11 = M1 + M4 - M5 + M7
    - C12 = M3 + M5
    - C21 = M2 + M4
    - C22 = M1 - M2 + M3 + M6

## Complexity
- Time complexity: **O(n^log₂7) ≈ O(n^2.807)**
- Space complexity: **O(n²)**

## Code

```java
public class StrassenMultiplication {
    
    // Стандартное умножение матриц (для маленьких матриц)
    public static int[][] standardMultiply(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        
        return C;
    }
    
    // Алгоритм Штрассена
    public static int[][] strassenMultiply(int[][] A, int[][] B) {
        int n = A.length;
        
        // Базовый случай: маленькие матрицы умножаем стандартно
        if (n <= 64) {  // порог можно настроить
            return standardMultiply(A, B);
        }
        
        int newSize = n / 2;
        
        // Разбиение матриц на подматрицы
        int[][] A11 = new int[newSize][newSize];
        int[][] A12 = new int[newSize][newSize];
        int[][] A21 = new int[newSize][newSize];
        int[][] A22 = new int[newSize][newSize];
        
        int[][] B11 = new int[newSize][newSize];
        int[][] B12 = new int[newSize][newSize];
        int[][] B21 = new int[newSize][newSize];
        int[][] B22 = new int[newSize][newSize];
        
        // Заполнение подматриц
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                A11[i][j] = A[i][j];
                A12[i][j] = A[i][j + newSize];
                A21[i][j] = A[i + newSize][j];
                A22[i][j] = A[i + newSize][j + newSize];
                
                B11[i][j] = B[i][j];
                B12[i][j] = B[i][j + newSize];
                B21[i][j] = B[i + newSize][j];
                B22[i][j] = B[i + newSize][j + newSize];
            }
        }
        
        // Вычисление вспомогательных матриц M1...M7
        int[][] M1 = strassenMultiply(add(A11, A22), add(B11, B22));
        int[][] M2 = strassenMultiply(add(A21, A22), B11);
        int[][] M3 = strassenMultiply(A11, sub(B12, B22));
        int[][] M4 = strassenMultiply(A22, sub(B21, B11));
        int[][] M5 = strassenMultiply(add(A11, A12), B22);
        int[][] M6 = strassenMultiply(sub(A21, A11), add(B11, B12));
        int[][] M7 = strassenMultiply(sub(A12, A22), add(B21, B22));
        
        // Вычисление результатов
        int[][] C11 = add(sub(add(M1, M4), M5), M7);
        int[][] C12 = add(M3, M5);
        int[][] C21 = add(M2, M4);
        int[][] C22 = add(sub(add(M1, M3), M2), M6);
        
        // Объединение подматриц
        int[][] C = new int[n][n];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                C[i][j] = C11[i][j];
                C[i][j + newSize] = C12[i][j];
                C[i + newSize][j] = C21[i][j];
                C[i + newSize][j + newSize] = C22[i][j];
            }
        }
        
        return C;
    }
    
    // Сложение матриц
    private static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        
        return result;
    }
    
    // Вычитание матриц
    private static int[][] sub(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        
        return result;
    }
    
    // Версия для double матриц
    public static double[][] strassenMultiplyDouble(double[][] A, double[][] B) {
        int n = A.length;
        
        if (n <= 64) {
            return standardMultiplyDouble(A, B);
        }
        
        int newSize = n / 2;
        
        double[][] A11 = new double[newSize][newSize];
        double[][] A12 = new double[newSize][newSize];
        double[][] A21 = new double[newSize][newSize];
        double[][] A22 = new double[newSize][newSize];
        
        double[][] B11 = new double[newSize][newSize];
        double[][] B12 = new double[newSize][newSize];
        double[][] B21 = new double[newSize][newSize];
        double[][] B22 = new double[newSize][newSize];
        
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                A11[i][j] = A[i][j];
                A12[i][j] = A[i][j + newSize];
                A21[i][j] = A[i + newSize][j];
                A22[i][j] = A[i + newSize][j + newSize];
                
                B11[i][j] = B[i][j];
                B12[i][j] = B[i][j + newSize];
                B21[i][j] = B[i + newSize][j];
                B22[i][j] = B[i + newSize][j + newSize];
            }
        }
        
        double[][] M1 = strassenMultiplyDouble(addDouble(A11, A22), addDouble(B11, B22));
        double[][] M2 = strassenMultiplyDouble(addDouble(A21, A22), B11);
        double[][] M3 = strassenMultiplyDouble(A11, subDouble(B12, B22));
        double[][] M4 = strassenMultiplyDouble(A22, subDouble(B21, B11));
        double[][] M5 = strassenMultiplyDouble(addDouble(A11, A12), B22);
        double[][] M6 = strassenMultiplyDouble(subDouble(A21, A11), addDouble(B11, B12));
        double[][] M7 = strassenMultiplyDouble(subDouble(A12, A22), addDouble(B21, B22));
        
        double[][] C11 = addDouble(subDouble(addDouble(M1, M4), M5), M7);
        double[][] C12 = addDouble(M3, M5);
        double[][] C21 = addDouble(M2, M4);
        double[][] C22 = addDouble(subDouble(addDouble(M1, M3), M2), M6);
        
        double[][] C = new double[n][n];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                C[i][j] = C11[i][j];
                C[i][j + newSize] = C12[i][j];
                C[i + newSize][j] = C21[i][j];
                C[i + newSize][j + newSize] = C22[i][j];
            }
        }
        
        return C;
    }
    
    private static double[][] addDouble(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }
    
    private static double[][] subDouble(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }
    
    private static double[][] standardMultiplyDouble(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
    
    // Дополнение матриц до размера степени двойки
    public static int[][] padToPowerOfTwo(int[][] matrix) {
        int n = matrix.length;
        int newSize = 1;
        while (newSize < n) {
            newSize <<= 1;
        }
        
        if (newSize == n) return matrix;
        
        int[][] padded = new int[newSize][newSize];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, padded[i], 0, n);
        }
        
        return padded;
    }
    
    // Обрезка матрицы до исходного размера
    public static int[][] trim(int[][] matrix, int originalSize) {
        int[][] trimmed = new int[originalSize][originalSize];
        for (int i = 0; i < originalSize; i++) {
            System.arraycopy(matrix[i], 0, trimmed[i], 0, originalSize);
        }
        return trimmed;
    }
    
    // Умножение с автоматическим дополнением
    public static int[][] multiplyWithPadding(int[][] A, int[][] B) {
        int[][] paddedA = padToPowerOfTwo(A);
        int[][] paddedB = padToPowerOfTwo(B);
        int[][] resultPadded = strassenMultiply(paddedA, paddedB);
        return trim(resultPadded, A.length);
    }
}