# Sparse Table

## Intuition
Sparse Table — это структура данных для ответа на запросы о минимуме/максимуме/НОД на отрезке за O(1) после предобработки за O(n log n). В отличие от дерева отрезков, она не поддерживает обновления, но обеспечивает константное время запроса. Идея основана на том, что любой отрезок можно покрыть двумя пересекающимися отрезками, длины которых являются степенями двойки. Представьте, что вы предвычисляете ответы для всех отрезков длиной 1, 2, 4, 8, … и затем комбинируете два таких отрезка для получения ответа на произвольный запрос.

## Approach
1. **Предподсчет**: создаем таблицу st[k][i] — результат для отрезка [i, i + 2^k - 1]
2. st[0][i] = arr[i] (длина 1)
3. st[k][i] = combine(st[k-1][i], st[k-1][i + 2^(k-1)])
4. **Запрос** для отрезка [l, r]:
    - k = floor(log2(r - l + 1))
    - result = combine(st[k][l], st[k][r - 2^k + 1])

## Complexity
- Time complexity (предподсчет): **O(n log n)**
- Time complexity (запрос): **O(1)**
- Space complexity: **O(n log n)**

## Code

```java
public class SparseTable {
    
    private int[][] st;
    private int[] log;
    private int n;
    private Operation operation;
    
    public enum Operation {
        MIN, MAX, GCD, SUM, AND, OR
    }
    
    public SparseTable(int[] arr, Operation operation) {
        this.n = arr.length;
        this.operation = operation;
        this.log = new int[n + 1];
        
        // Предвычисление логарифмов
        log[1] = 0;
        for (int i = 2; i <= n; i++) {
            log[i] = log[i / 2] + 1;
        }
        
        int k = log[n] + 1;
        st = new int[k][n];
        
        // Инициализация
        for (int i = 0; i < n; i++) {
            st[0][i] = arr[i];
        }
        
        // Построение
        for (int j = 1; j < k; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                st[j][i] = combine(st[j - 1][i], st[j - 1][i + (1 << (j - 1))]);
            }
        }
    }
    
    private int combine(int a, int b) {
        switch (operation) {
            case MIN: return Math.min(a, b);
            case MAX: return Math.max(a, b);
            case GCD: return gcd(a, b);
            case SUM: return a + b;
            case AND: return a & b;
            case OR: return a | b;
            default: return Math.min(a, b);
        }
    }
    
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }
    
    // Запрос на отрезке [l, r] (0-индексация)
    public int query(int l, int r) {
        int j = log[r - l + 1];
        int left = st[j][l];
        int right = st[j][r - (1 << j) + 1];
        
        if (operation == Operation.SUM) {
            // Для суммы нужна другая техника (префиксные суммы)
            return prefixSum(r) - (l > 0 ? prefixSum(l - 1) : 0);
        }
        
        return combine(left, right);
    }
    
    // Для суммы используем префиксный массив
    private int[] prefixSums;
    
    public SparseTable forSum(int[] arr) {
        this.operation = Operation.SUM;
        this.prefixSums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + arr[i];
        }
        return this;
    }
    
    private int prefixSum(int index) {
        return prefixSums[index + 1];
    }
    
    // Sparse Table для 2D (квадратная матрица)
    public static class SparseTable2D {
        private int[][][][] st;
        private int[][] log;
        private int n, m;
        private Operation operation;
        
        public SparseTable2D(int[][] matrix, Operation operation) {
            this.n = matrix.length;
            this.m = matrix[0].length;
            this.operation = operation;
            
            // Предвычисление логарифмов
            log = new int[Math.max(n, m) + 1];
            log[1] = 0;
            for (int i = 2; i <= Math.max(n, m); i++) {
                log[i] = log[i / 2] + 1;
            }
            
            int k1 = log[n] + 1;
            int k2 = log[m] + 1;
            st = new int[k1][k2][n][m];
            
            // Инициализация
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    st[0][0][i][j] = matrix[i][j];
                }
            }
            
            // Построение по строкам
            for (int i = 0; i < n; i++) {
                for (int k = 1; k < k2; k++) {
                    for (int j = 0; j + (1 << k) <= m; j++) {
                        st[0][k][i][j] = combine(st[0][k - 1][i][j], 
                                                  st[0][k - 1][i][j + (1 << (k - 1))]);
                    }
                }
            }
            
            // Построение по столбцам
            for (int k1i = 1; k1i < k1; k1i++) {
                for (int k2i = 0; k2i < k2; k2i++) {
                    for (int i = 0; i + (1 << k1i) <= n; i++) {
                        for (int j = 0; j + (1 << k2i) <= m; j++) {
                            st[k1i][k2i][i][j] = combine(st[k1i - 1][k2i][i][j],
                                                          st[k1i - 1][k2i][i + (1 << (k1i - 1))][j]);
                        }
                    }
                }
            }
        }
        
        private int combine(int a, int b) {
            switch (operation) {
                case MIN: return Math.min(a, b);
                case MAX: return Math.max(a, b);
                default: return Math.min(a, b);
            }
        }
        
        // Запрос на прямоугольнике [x1, x2] × [y1, y2]
        public int query(int x1, int y1, int x2, int y2) {
            int kx = log[x2 - x1 + 1];
            int ky = log[y2 - y1 + 1];
            
            int a = st[kx][ky][x1][y1];
            int b = st[kx][ky][x1][y2 - (1 << ky) + 1];
            int c = st[kx][ky][x2 - (1 << kx) + 1][y1];
            int d = st[kx][ky][x2 - (1 << kx) + 1][y2 - (1 << ky) + 1];
            
            return combine(combine(a, b), combine(c, d));
        }
    }
    
    // Sparse Table для НОД (особенность: НОД не идемпотентен, но работает из-за свойства)
    // Для идемпотентных операций (min, max, gcd) можно использовать пересечение отрезков
    
    // Range Minimum Query с помощью Sparse Table
    public static class RMQ {
        private SparseTable st;
        
        public RMQ(int[] arr) {
            st = new SparseTable(arr, Operation.MIN);
        }
        
        public int query(int l, int r) {
            return st.query(l, r);
        }
    }
    
    // Range Maximum Query
    public static class RMaxQ {
        private SparseTable st;
        
        public RMaxQ(int[] arr) {
            st = new SparseTable(arr, Operation.MAX);
        }
        
        public int query(int l, int r) {
            return st.query(l, r);
        }
    }
    
    // Range GCD Query
    public static class RGCDQ {
        private SparseTable st;
        
        public RGCDQ(int[] arr) {
            st = new SparseTable(arr, Operation.GCD);
        }
        
        public int query(int l, int r) {
            return st.query(l, r);
        }
    }
}