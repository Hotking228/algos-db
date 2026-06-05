# Матричное умножение цепочек (Matrix Chain Multiplication)

## Intuition
Задача матричного умножения цепочек — это классическая задача оптимизации, в которой нужно найти наиболее эффективный порядок перемножения последовательности матриц. Хотя результат умножения не зависит от порядка (ассоциативность), количество элементарных операций может сильно отличаться. Например, для матриц A(10×100), B(100×5), C(5×50), порядок (A×B)×C требует 10×100×5 + 10×5×50 = 5000 + 2500 = 7500 операций, а A×(B×C) требует 100×5×50 + 10×100×50 = 25000 + 50000 = 75000 операций. Разница в 10 раз! Представьте, что вы хотите перемножить несколько матриц и ищете самый быстрый способ.

## Approach
1. Пусть матрицы имеют размеры p0×p1, p1×p2, ..., p(n-1)×pn
2. Создаем матрицу dp[n][n], где dp[i][j] — минимальное количество операций для перемножения матриц с i по j
3. Длина цепочки l от 2 до n:
    - Для каждого i от 0 до n-l:
        - j = i + l - 1
        - dp[i][j] = ∞
        - Для каждого k от i до j-1:
            - cost = dp[i][k] + dp[k+1][j] + p[i] × p[k+1] × p[j+1]
            - dp[i][j] = min(dp[i][j], cost)
4. Ответ находится в dp[0][n-1]

## Complexity
- Time complexity: **O(n³)**
- Space complexity: **O(n²)**

## Code

```java
import java.util.*;

public class MatrixChainMultiplication {
    
    // Базовое решение с полной матрицей
    public static int minMultiplications(int[] dimensions) {
        int n = dimensions.length - 1; // количество матриц
        int[][] dp = new int[n][n];
        
        // l — длина цепочки
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                               dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // Получение порядка умножения (скобочной структуры)
    public static String getOrder(int[] dimensions) {
        int n = dimensions.length - 1;
        int[][] dp = new int[n][n];
        int[][] split = new int[n][n];
        
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                               dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        split[i][j] = k;
                    }
                }
            }
        }
        
        return buildOrder(split, 0, n - 1);
    }
    
    private static String buildOrder(int[][] split, int i, int j) {
        if (i == j) {
            return "A" + (i + 1);
        }
        return "(" + buildOrder(split, i, split[i][j]) + 
               " × " + buildOrder(split, split[i][j] + 1, j) + ")";
    }
    
    // Оптимизированное решение с одномерным массивом для хранения диагоналей
    public static int minMultiplicationsOptimized(int[] dimensions) {
        int n = dimensions.length - 1;
        int[] dp = new int[n * n];
        
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                int index = i * n + j;
                dp[index] = Integer.MAX_VALUE;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i * n + k] + dp[(k + 1) * n + j] + 
                               dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    dp[index] = Math.min(dp[index], cost);
                }
            }
        }
        
        return dp[n - 1];
    }
    
    // Восстановление всех оптимальных порядков (если их несколько)
    public static List<String> getAllOrders(int[] dimensions) {
        int n = dimensions.length - 1;
        int[][] dp = new int[n][n];
        List<String>[][] orders = new ArrayList[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                orders[i][j] = new ArrayList<>();
            }
        }
        
        for (int i = 0; i < n; i++) {
            orders[i][i].add("A" + (i + 1));
        }
        
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                               dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        orders[i][j].clear();
                        for (String left : orders[i][k]) {
                            for (String right : orders[k + 1][j]) {
                                orders[i][j].add("(" + left + " × " + right + ")");
                            }
                        }
                    } else if (cost == dp[i][j]) {
                        for (String left : orders[i][k]) {
                            for (String right : orders[k + 1][j]) {
                                orders[i][j].add("(" + left + " × " + right + ")");
                            }
                        }
                    }
                }
            }
        }
        
        return orders[0][n - 1];
    }
    
    // Класс для результата с дополнительной информацией
    public static class MatrixChainResult {
        int minOperations;
        String order;
        int[][] dp;
        int[][] split;
        
        MatrixChainResult(int minOperations, String order, int[][] dp, int[][] split) {
            this.minOperations = minOperations;
            this.order = order;
            this.dp = dp;
            this.split = split;
        }
        
        public void printDPTable() {
            System.out.println("Таблица DP:");
            for (int[] row : dp) {
                for (int val : row) {
                    if (val == 0) {
                        System.out.print("  0 ");
                    } else {
                        System.out.printf("%4d ", val);
                    }
                }
                System.out.println();
            }
        }
    }
    
    public static MatrixChainResult minMultiplicationsDetailed(int[] dimensions) {
        int n = dimensions.length - 1;
        int[][] dp = new int[n][n];
        int[][] split = new int[n][n];
        
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                               dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        split[i][j] = k;
                    }
                }
            }
        }
        
        String order = buildOrder(split, 0, n - 1);
        return new MatrixChainResult(dp[0][n - 1], order, dp, split);
    }
    
    // Для цепочек, где матрицы заданы как объекты с размерами
    static class Matrix {
        int rows;
        int cols;
        String name;
        
        Matrix(int rows, int cols, String name) {
            this.rows = rows;
            this.cols = cols;
            this.name = name;
        }
    }
    
    public static int minMultiplicationsMatrices(Matrix[] matrices) {
        int n = matrices.length;
        int[] dimensions = new int[n + 1];
        dimensions[0] = matrices[0].rows;
        for (int i = 0; i < n; i++) {
            dimensions[i + 1] = matrices[i].cols;
        }
        return minMultiplications(dimensions);
    }
}