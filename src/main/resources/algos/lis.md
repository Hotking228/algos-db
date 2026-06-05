# Наибольшая возрастающая подпоследовательность (LIS - Longest Increasing Subsequence)

## Intuition
Наибольшая возрастающая подпоследовательность (LIS) — это задача нахождения самой длинной подпоследовательности в массиве, элементы которой строго возрастают. Элементы не обязательно должны быть непрерывными. Например, для массива [10, 9, 2, 5, 3, 7, 101, 18] LIS будет [2, 3, 7, 101] (длина 4). Представьте, что вы анализируете цены акций и хотите найти самую длинную последовательность дней, когда цена росла, чтобы максимизировать прибыль.

## Approach
**Метод 1: Динамическое программирование O(n²)**
- dp[i] — длина LIS, заканчивающейся на элементе i
- Для каждого i, ищем все j < i, где arr[j] < arr[i]
- dp[i] = max(dp[j]) + 1

**Метод 2: Жадный + бинарный поиск O(n log n)**
- Строим массив tails, где tails[k] — наименьший возможный последний элемент возрастающей подпоследовательности длины k+1
- Для каждого числа x, находим позицию для вставки (первый элемент tails >= x)
- Обновляем tails[позиция] = x

## Complexity
- Time complexity (DP): **O(n²)**
- Time complexity (оптимизированный): **O(n log n)**
- Space complexity: **O(n)**

## Code

```java
import java.util.*;

public class LIS {
    
    // Метод 1: Динамическое программирование O(n²)
    public static int lisLengthDP(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        int maxLen = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }
    
    // Метод 2: Бинарный поиск O(n log n)
    public static int lisLengthOptimized(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        
        int[] tails = new int[arr.length];
        int size = 0;
        
        for (int x : arr) {
            int left = 0, right = size;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < x) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = x;
            if (left == size) {
                size++;
            }
        }
        
        return size;
    }
    
    // Восстановление самой подпоследовательности (O(n²) версия)
    public static List<Integer> lisSequence(int[] arr) {
        int n = arr.length;
        if (n == 0) return new ArrayList<>();
        
        int[] dp = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);
        
        int maxLen = 1;
        int maxIndex = 0;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIndex = i;
            }
        }
        
        // Восстановление последовательности
        List<Integer> sequence = new ArrayList<>();
        int current = maxIndex;
        while (current != -1) {
            sequence.add(arr[current]);
            current = prev[current];
        }
        Collections.reverse(sequence);
        
        return sequence;
    }
    
    // Восстановление с помощью бинарного поиска (без prev, просто получаем элементы)
    public static List<Integer> lisSequenceOptimized(int[] arr) {
        int n = arr.length;
        if (n == 0) return new ArrayList<>();
        
        int[] tails = new int[n];
        int[] tailsIndex = new int[n];
        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        
        int size = 0;
        
        for (int i = 0; i < n; i++) {
            int left = 0, right = size;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < arr[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = arr[i];
            tailsIndex[left] = i;
            
            if (left > 0) {
                prev[i] = tailsIndex[left - 1];
            }
            
            if (left == size) {
                size++;
            }
        }
        
        // Восстановление последовательности
        List<Integer> sequence = new ArrayList<>();
        int current = tailsIndex[size - 1];
        while (current != -1) {
            sequence.add(arr[current]);
            current = prev[current];
        }
        Collections.reverse(sequence);
        
        return sequence;
    }
    
    // Количество различных LIS
    public static int countLIS(int[] arr) {
        int n = arr.length;
        int[] length = new int[n];
        int[] count = new int[n];
        Arrays.fill(length, 1);
        Arrays.fill(count, 1);
        
        int maxLen = 1;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    if (length[j] + 1 > length[i]) {
                        length[i] = length[j] + 1;
                        count[i] = count[j];
                    } else if (length[j] + 1 == length[i]) {
                        count[i] += count[j];
                    }
                }
            }
            maxLen = Math.max(maxLen, length[i]);
        }
        
        int totalCount = 0;
        for (int i = 0; i < n; i++) {
            if (length[i] == maxLen) {
                totalCount += count[i];
            }
        }
        
        return totalCount;
    }
    
    // LIS для невозрастающей подпоследовательности (строго)
    public static int longestNonDecreasing(int[] arr) {
        int[] tails = new int[arr.length];
        int size = 0;
        
        for (int x : arr) {
            int left = 0, right = size;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] <= x) {  // <= для невозрастающей
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = x;
            if (left == size) size++;
        }
        
        return size;
    }
    
    // LIS с пользовательским компаратором
    public static <T> int lisCustom(T[] arr, Comparator<T> comparator) {
        if (arr == null || arr.length == 0) return 0;
        
        T[] tails = (T[]) new Object[arr.length];
        int size = 0;
        
        for (T x : arr) {
            int left = 0, right = size;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (comparator.compare(tails[mid], x) < 0) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = x;
            if (left == size) size++;
        }
        
        return size;
    }
    
    // LIS для 2D точек (сначала по x, потом по y)
    public static int lis2D(int[][] points) {
        // Сортируем по x, при равенстве x по y
        Arrays.sort(points, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });
        
        int[] yCoords = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            yCoords[i] = points[i][1];
        }
        
        return lisLengthOptimized(yCoords);
    }
    
    // Min Patches (минимальное количество дополнений до LIS)
    public static int minPatchesForLIS(int[] arr, int targetLength) {
        int[] tails = new int[arr.length];
        int size = 0;
        
        for (int x : arr) {
            int pos = Arrays.binarySearch(tails, 0, size, x);
            if (pos < 0) pos = -(pos + 1);
            tails[pos] = x;
            if (pos == size) size++;
        }
        
        return Math.max(0, targetLength - size);
    }
}