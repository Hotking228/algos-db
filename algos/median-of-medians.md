# Алгоритм поиска k-й порядковой статистики (Медиана медиан)

## Intuition
Алгоритм "медиана медиан" (Median of Medians) — это детерминированный алгоритм выбора k-го наименьшего элемента, который гарантирует линейное время работы O(n) в худшем случае, в отличие от QuickSelect с O(n²) в худшем. Идея заключается в том, чтобы выбрать хороший опорный элемент (pivot), который гарантированно отсекает достаточное количество элементов. Для этого массив разбивается на группы по 5 элементов, в каждой группе находится медиана, а затем медиана этих медиан становится опорным элементом. Это обеспечивает сбалансированное разбиение.

## Approach
1. Разбиваем массив на группы по 5 элементов (последняя группа может быть меньше)
2. Находим медиану каждой группы (сортировкой группы, т.к. она маленькая)
3. Рекурсивно находим медиану медиан (medOfMed) — медиану всех групповых медиан
4. Используем medOfMed как опорный элемент для разбиения массива
5. В зависимости от позиции опорного элемента:
    - Если позиция == k — нашли ответ
    - Если позиция < k — ищем в правой части
    - Если позиция > k — ищем в левой части

## Complexity
- Time complexity: **O(n)** в худшем случае
- Time complexity (лучшая): **O(n)**
- Time complexity (средняя): **O(n)**
- Space complexity: **O(n)** (из-за рекурсии и вспомогательных массивов)

## Code

```java
import java.util.Arrays;

public class MedianOfMedians {
    
    // Нахождение k-го наименьшего элемента (0-индексация)
    public static int quickSelect(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return quickSelect(arr, 0, arr.length - 1, k);
    }
    
    private static int quickSelect(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }
        
        // Находим медиану медиан в качестве опорного элемента
        int pivotIndex = selectPivotIndex(arr, left, right);
        
        // Разбиваем массив вокруг опорного элемента
        int partitionIndex = partition(arr, left, right, pivotIndex);
        
        if (k == partitionIndex) {
            return arr[k];
        } else if (k < partitionIndex) {
            return quickSelect(arr, left, partitionIndex - 1, k);
        } else {
            return quickSelect(arr, partitionIndex + 1, right, k);
        }
    }
    
    // Выбор опорного индекса с использованием медианы медиан
    private static int selectPivotIndex(int[] arr, int left, int right) {
        int n = right - left + 1;
        
        // Базовый случай: если массив маленький, просто возвращаем медиану
        if (n <= 5) {
            // Копируем подмассив и сортируем
            int[] temp = new int[n];
            System.arraycopy(arr, left, temp, 0, n);
            Arrays.sort(temp);
            // Возвращаем индекс медианы в оригинальном массиве
            int medianValue = temp[n / 2];
            for (int i = left; i <= right; i++) {
                if (arr[i] == medianValue) {
                    return i;
                }
            }
        }
        
        // Разбиваем на группы по 5 и находим медиану каждой группы
        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];
        
        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            medians[i] = medianOfSmallArray(arr, groupLeft, groupRight);
        }
        
        // Находим медиану медиан рекурсивно
        int medianOfMedians = quickSelect(medians, medians.length / 2);
        
        // Находим индекс медианы медиан в оригинальном массиве
        for (int i = left; i <= right; i++) {
            if (arr[i] == medianOfMedians) {
                return i;
            }
        }
        
        return left;
    }
    
    // Нахождение медианы в маленьком подмассиве
    private static int medianOfSmallArray(int[] arr, int left, int right) {
        int size = right - left + 1;
        int[] temp = new int[size];
        System.arraycopy(arr, left, temp, 0, size);
        Arrays.sort(temp);
        return temp[size / 2];
    }
    
    // Разбиение массива вокруг опорного элемента
    private static int partition(int[] arr, int left, int right, int pivotIndex) {
        int pivotValue = arr[pivotIndex];
        // Перемещаем опорный элемент в конец
        swap(arr, pivotIndex, right);
        
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }
        
        // Перемещаем опорный элемент на его место
        swap(arr, storeIndex, right);
        return storeIndex;
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    // Альтернативная реализация, возвращающая значение k-го наименьшего элемента
    public static int findKthSmallest(int[] arr, int k) {
        // Создаем копию, чтобы не изменять оригинальный массив
        int[] copy = arr.clone();
        return quickSelect(copy, k);
    }
    
    // Нахождение медианы массива
    public static double findMedian(int[] arr) {
        int n = arr.length;
        if (n % 2 == 1) {
            return findKthSmallest(arr, n / 2);
        } else {
            int left = findKthSmallest(arr, n / 2 - 1);
            int right = findKthSmallest(arr, n / 2);
            return (left + right) / 2.0;
        }
    }
    
    // Версия для работы с дубликатами
    public static int quickSelectWithDuplicates(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return quickSelectWithDuplicates(arr, 0, arr.length - 1, k);
    }
    
    private static int quickSelectWithDuplicates(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }
        
        int pivotValue = selectPivotValue(arr, left, right);
        
        // 3-стороннее разбиение: < pivot | = pivot | > pivot
        int lt = left;      // последний индекс < pivot
        int gt = right;     // первый индекс > pivot
        int i = left;
        
        while (i <= gt) {
            if (arr[i] < pivotValue) {
                swap(arr, i, lt);
                lt++;
                i++;
            } else if (arr[i] > pivotValue) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        
        // Теперь: [left, lt-1] < pivot, [lt, gt] = pivot, [gt+1, right] > pivot
        if (k < lt) {
            return quickSelectWithDuplicates(arr, left, lt - 1, k);
        } else if (k <= gt) {
            return pivotValue;
        } else {
            return quickSelectWithDuplicates(arr, gt + 1, right, k);
        }
    }
    
    private static int selectPivotValue(int[] arr, int left, int right) {
        int n = right - left + 1;
        
        if (n <= 5) {
            int[] temp = new int[n];
            System.arraycopy(arr, left, temp, 0, n);
            Arrays.sort(temp);
            return temp[n / 2];
        }
        
        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];
        
        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            medians[i] = medianOfSmallArray(arr, groupLeft, groupRight);
        }
        
        return selectPivotValue(medians, 0, medians.length - 1);
    }
}