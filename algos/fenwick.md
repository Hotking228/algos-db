# Дерево Фенвика (Fenwick Tree / Binary Indexed Tree - BIT)

## Intuition
Дерево Фенвика — это структура данных для эффективного вычисления префиксных сумм и обновления элементов за O(log n). Она использует тот факт, что любое число можно представить как сумму степеней двойки, и хранит суммы для интервалов, соответствующих младшим единичным битам. Представьте, что у вас есть массив чисел, и вам нужно часто получать сумму первых k элементов и изменять отдельные элементы. Дерево Фенвика решает эту задачу более компактно, чем дерево отрезков (требует O(n) памяти вместо O(4n)), но поддерживает только обратимые операции (сумма, XOR, произведение, но не min/max).

## Approach
1. **Построение**: создаем массив BIT размера n+1 (1-индексация)
2. **Обновление (add)**: для i от index до n: i += i & -i, BIT[i] += delta
3. **Префиксная сумма (sum)**: для i от index до 1: i -= i & -i, result += BIT[i]
4. **Сумма на отрезке [l, r]**: sum(r) - sum(l-1)
5. **Поиск наименьшего индекса с заданной суммой**: бинарный поиск по BIT

## Complexity
- Time complexity (построение): **O(n log n)** или **O(n)** с префиксными суммами
- Time complexity (обновление): **O(log n)**
- Time complexity (запрос суммы): **O(log n)**
- Space complexity: **O(n)**

## Code

```java
public class FenwickTree {
    
    private int[] tree;
    private int n;
    
    public FenwickTree(int size) {
        this.n = size;
        this.tree = new int[n + 1]; // 1-индексация
    }
    
    // Построение из массива за O(n)
    public FenwickTree(int[] arr) {
        this.n = arr.length;
        this.tree = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            tree[i] += arr[i - 1];
            int parent = i + (i & -i);
            if (parent <= n) {
                tree[parent] += tree[i];
            }
        }
    }
    
    // Добавление значения в позицию index (0-индексация)
    public void add(int index, int delta) {
        int i = index + 1; // переход к 1-индексации
        while (i <= n) {
            tree[i] += delta;
            i += i & -i; // переход к следующему элементу
        }
    }
    
    // Получение суммы префикса [0, index]
    public int sum(int index) {
        int i = index + 1;
        int result = 0;
        while (i > 0) {
            result += tree[i];
            i -= i & -i;
        }
        return result;
    }
    
    // Получение суммы на отрезке [l, r]
    public int rangeSum(int l, int r) {
        return sum(r) - (l > 0 ? sum(l - 1) : 0);
    }
    
    // Получение значения в позиции index
    public int get(int index) {
        return rangeSum(index, index);
    }
    
    // Установка значения в позиции index
    public void set(int index, int value) {
        int current = get(index);
        add(index, value - current);
    }
    
    // Двумерное дерево Фенвика
    public static class FenwickTree2D {
        private int[][] tree;
        private int n, m;
        
        public FenwickTree2D(int n, int m) {
            this.n = n;
            this.m = m;
            this.tree = new int[n + 1][m + 1];
        }
        
        public void add(int x, int y, int delta) {
            int i = x + 1;
            while (i <= n) {
                int j = y + 1;
                while (j <= m) {
                    tree[i][j] += delta;
                    j += j & -j;
                }
                i += i & -i;
            }
        }
        
        public int sum(int x, int y) {
            int i = x + 1;
            int result = 0;
            while (i > 0) {
                int j = y + 1;
                while (j > 0) {
                    result += tree[i][j];
                    j -= j & -j;
                }
                i -= i & -i;
            }
            return result;
        }
        
        public int rangeSum(int x1, int y1, int x2, int y2) {
            return sum(x2, y2) - sum(x1 - 1, y2) - sum(x2, y1 - 1) + sum(x1 - 1, y1 - 1);
        }
    }
    
    // Дерево Фенвика для инверсий (подсчет количества элементов меньше текущего)
    public static long countInversions(int[] arr) {
        int maxVal = 0;
        for (int num : arr) maxVal = Math.max(maxVal, num);
        
        FenwickTree bit = new FenwickTree(maxVal + 1);
        long inversions = 0;
        
        for (int i = arr.length - 1; i >= 0; i--) {
            inversions += bit.sum(arr[i] - 1);
            bit.add(arr[i], 1);
        }
        
        return inversions;
    }
    
    // Диапазонные обновления и точечные запросы
    public static class RangeUpdatePointQuery {
        private FenwickTree bit;
        
        public RangeUpdatePointQuery(int size) {
            bit = new FenwickTree(size);
        }
        
        // Добавление delta на отрезок [l, r]
        public void rangeAdd(int l, int r, int delta) {
            bit.add(l, delta);
            if (r + 1 < bit.n) {
                bit.add(r + 1, -delta);
            }
        }
        
        // Получение значения в позиции index
        public int pointQuery(int index) {
            return bit.sum(index);
        }
    }
    
    // Дерево Фенвика для XOR (поддерживает XOR вместо суммы)
    public static class FenwickTreeXOR {
        private int[] tree;
        private int n;
        
        public FenwickTreeXOR(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }
        
        public void xorUpdate(int index, int value) {
            int i = index + 1;
            while (i <= n) {
                tree[i] ^= value;
                i += i & -i;
            }
        }
        
        public int xorQuery(int index) {
            int i = index + 1;
            int result = 0;
            while (i > 0) {
                result ^= tree[i];
                i -= i & -i;
            }
            return result;
        }
        
        public int rangeXor(int l, int r) {
            return xorQuery(r) ^ (l > 0 ? xorQuery(l - 1) : 0);
        }
    }
    
    // Поиск наименьшего индекса с заданной суммой (prefix sum)
    public int findIndexByPrefixSum(int targetSum) {
        int index = 0;
        int bitMask = Integer.highestOneBit(n);
        
        while (bitMask != 0) {
            int nextIndex = index + bitMask;
            if (nextIndex <= n && tree[nextIndex] < targetSum) {
                targetSum -= tree[nextIndex];
                index = nextIndex;
            }
            bitMask >>= 1;
        }
        
        return index; // индекс в 1-индексации
    }
    
    // Мультимножество с поддержкой порядка (Order Statistics Tree)
    public static class OrderStatisticsTree {
        private FenwickTree bit;
        private int maxVal;
        
        public OrderStatisticsTree(int maxVal) {
            this.maxVal = maxVal;
            this.bit = new FenwickTree(maxVal + 1);
        }
        
        public void add(int value, int count) {
            bit.add(value, count);
        }
        
        public int kthSmallest(int k) {
            return bit.findIndexByPrefixSum(k);
        }
        
        public int rank(int value) {
            return bit.sum(value - 1) + 1;
        }
    }
}