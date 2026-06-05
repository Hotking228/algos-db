# Тернарный поиск (Ternary Search)

## Intuition
Тернарный поиск — это алгоритм для нахождения максимума или минимума унимодальной функции (функции, которая сначала возрастает, а затем убывает, или наоборот). В отличие от бинарного поиска, который делит интервал на две части, тернарный поиск делит его на три части, используя две промежуточные точки. Представьте, что вы ищете вершину горы в тумане: вы проверяете две точки на склоне и определяете, в какой из трех частей находится вершина, затем сужаете область поиска.

## Approach
1. Для поиска максимума унимодальной функции:
2. Вычисляем две точки: m1 = left + (right - left) / 3, m2 = right - (right - left) / 3
3. Сравниваем f(m1) и f(m2):
    - Если f(m1) < f(m2) → максимум находится в [m1, right]
    - Если f(m1) > f(m2) → максимум находится в [left, m2]
    - Если равны → максимум в [m1, m2]
4. Повторяем, пока интервал не станет достаточно маленьким

## Complexity
- Time complexity: **O(log₃ n)** — 3 основания логарифма
- В отличие от бинарного поиска O(log₂ n), тернарный делает больше сравнений на каждом шаге
- Space complexity: **O(1)**

## Code

```java
public class TernarySearch {
    
    // Тернарный поиск максимума для функции f (f сначала возрастает, затем убывает)
    public static double findMaximum(double left, double right, Function f, double epsilon) {
        while (right - left > epsilon) {
            double m1 = left + (right - left) / 3;
            double m2 = right - (right - left) / 3;
            
            double f1 = f.evaluate(m1);
            double f2 = f.evaluate(m2);
            
            if (f1 < f2) {
                left = m1; // максимум справа
            } else if (f1 > f2) {
                right = m2; // максимум слева
            } else {
                left = m1;
                right = m2;
            }
        }
        
        return (left + right) / 2;
    }
    
    // Тернарный поиск минимума для функции f (сначала убывает, затем возрастает)
    public static double findMinimum(double left, double right, Function f, double epsilon) {
        while (right - left > epsilon) {
            double m1 = left + (right - left) / 3;
            double m2 = right - (right - left) / 3;
            
            double f1 = f.evaluate(m1);
            double f2 = f.evaluate(m2);
            
            if (f1 > f2) {
                left = m1; // минимум справа
            } else if (f1 < f2) {
                right = m2; // минимум слева
            } else {
                left = m1;
                right = m2;
            }
        }
        
        return (left + right) / 2;
    }
    
    // Тернарный поиск максимума для целочисленного массива (пик)
    public static int findPeak(int[] arr) {
        if (arr == null || arr.length == 0) return -1;
        if (arr.length == 1) return 0;
        
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            if (left == right) return left;
            
            int m1 = left + (right - left) / 3;
            int m2 = right - (right - left) / 3;
            
            // Для массива нужно учитывать границы
            int val1 = arr[m1];
            int val2 = arr[m2];
            
            if (val1 < val2) {
                // Пик справа
                left = m1 + 1;
            } else if (val1 > val2) {
                // Пик слева
                right = m2 - 1;
            } else {
                // Могут быть равны, сужаем с обеих сторон
                left = m1 + 1;
                right = m2 - 1;
            }
        }
        
        return -1;
    }
    
    // Тернарный поиск для поиска элемента в отсортированном массиве (медленнее бинарного)
    public static int searchSorted(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            if (left == right) {
                return arr[left] == target ? left : -1;
            }
            
            int m1 = left + (right - left) / 3;
            int m2 = right - (right - left) / 3;
            
            if (arr[m1] == target) return m1;
            if (arr[m2] == target) return m2;
            
            if (target < arr[m1]) {
                right = m1 - 1;
            } else if (target > arr[m2]) {
                left = m2 + 1;
            } else {
                left = m1 + 1;
                right = m2 - 1;
            }
        }
        
        return -1;
    }
    
    // Тернарный поиск целочисленного максимума с заданной точностью
    public static int findMaximumInteger(int left, int right, IntegerFunction f) {
        while (right - left > 2) {
            int m1 = left + (right - left) / 3;
            int m2 = right - (right - left) / 3;
            
            int f1 = f.evaluate(m1);
            int f2 = f.evaluate(m2);
            
            if (f1 < f2) {
                left = m1;
            } else if (f1 > f2) {
                right = m2;
            } else {
                left = m1;
                right = m2;
            }
        }
        
        // Находим максимум среди последних нескольких точек
        int maxVal = f.evaluate(left);
        int maxPos = left;
        for (int i = left + 1; i <= right; i++) {
            int val = f.evaluate(i);
            if (val > maxVal) {
                maxVal = val;
                maxPos = i;
            }
        }
        
        return maxPos;
    }
    
    // Тернарный поиск для вещественных функций с подсчетом итераций
    public static SearchResult findMaximumWithStats(double left, double right, Function f, double epsilon) {
        int iterations = 0;
        
        while (right - left > epsilon) {
            double m1 = left + (right - left) / 3;
            double m2 = right - (right - left) / 3;
            
            double f1 = f.evaluate(m1);
            double f2 = f.evaluate(m2);
            
            if (f1 < f2) {
                left = m1;
            } else if (f1 > f2) {
                right = m2;
            } else {
                left = m1;
                right = m2;
            }
            
            iterations++;
        }
        
        double x = (left + right) / 2;
        double value = f.evaluate(x);
        
        return new SearchResult(x, value, iterations);
    }
    
    static class SearchResult {
        double x;
        double value;
        int iterations;
        
        SearchResult(double x, double value, int iterations) {
            this.x = x;
            this.value = value;
            this.iterations = iterations;
        }
    }
    
    // Функциональный интерфейс
    @FunctionalInterface
    public interface Function {
        double evaluate(double x);
    }
    
    @FunctionalInterface
    public interface IntegerFunction {
        int evaluate(int x);
    }
    
    // Пример: квадратичная функция -x² + 4x (максимум в x = 2)
    public static class QuadraticFunction implements Function {
        @Override
        public double evaluate(double x) {
            return -x * x + 4 * x;
        }
    }
    
    // Пример: функция sin(x) + x/10 на интервале [0, 10]
    public static class SinFunction implements Function {
        @Override
        public double evaluate(double x) {
            return Math.sin(x) + x / 10;
        }
    }
}