# Сортировка Шелла (Shell Sort)

## Intuition
Сортировка Шелла — это улучшенная версия сортировки вставками, которая позволяет обменивать элементы, находящиеся далеко друг от друга. Идея в том, чтобы сначала отсортировать элементы, отстоящие на большом расстоянии (интервале), а затем уменьшать интервал, пока он не станет равен 1. Представьте, что вы сортируете колоду карт, сначала упорядочивая каждую 5-ю карту, затем каждую 3-ю, и наконец, соседние. Это позволяет быстро "протащить" маленькие элементы в начало, избегая множества мелких перестановок.

## Approach
1. Выбираем последовательность интервалов (gap): например, n/2, n/4, ..., 1
2. Для каждого интервала:
    - Выполняем сортировку вставками для элементов, отстоящих на этот интервал
    - Фактически сортируем отдельные подмассивы: элементы на позициях 0, gap, 2gap, ...;
      потом 1, 1+gap, 1+2gap, ... и так далее
3. Уменьшаем интервал и повторяем
4. На последнем шаге (gap=1) выполняется обычная сортировка вставками,
   но массив уже почти отсортирован, поэтому она работает быстро

## Complexity
- Time complexity (худшая): **O(n²)** при неудачном выборе интервалов
- Time complexity (средняя): **O(n^(3/2))** для последовательности Кнута
- Time complexity (лучшая): **O(n log n)**
- Space complexity: **O(1)** — сортировка на месте

## Code

```java
public class ShellSort {
    
    // Сортировка Шелла с последовательностью интервалов: n/2, n/4, ..., 1
    public static void sort(int[] arr) {
        int n = arr.length;
        
        // Начинаем с большого интервала и уменьшаем
        for (int gap = n / 2; gap > 0; gap /= 2) {
            
            // Сортировка вставками для текущего интервала
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                
                // Сдвигаем элементы, отстоящие на gap
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                
                arr[j] = temp;
            }
        }
    }
    
    // Сортировка Шелла с последовательностью Кнута: 1, 4, 13, 40, ...
    public static void sortKnuth(int[] arr) {
        int n = arr.length;
        
        // Вычисляем начальный интервал по формуле Кнута: h = 3*h + 1
        int gap = 1;
        while (gap < n / 3) {
            gap = 3 * gap + 1;
        }
        
        // Уменьшаем интервал
        while (gap > 0) {
            
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                
                arr[j] = temp;
            }
            
            gap /= 3;
        }
    }
    
    // Сортировка Шелла с последовательностью Хиббарда: 1, 3, 7, 15, ...
    public static void sortHibbard(int[] arr) {
        int n = arr.length;
        
        // Находим максимальный интервал: 2^k - 1 < n
        int gap = 1;
        while (gap < n) {
            gap = 2 * gap + 1;
        }
        
        // Уменьшаем интервал
        while (gap > 0) {
            
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                
                arr[j] = temp;
            }
            
            gap = (gap - 1) / 2;
        }
    }
    
    // Сортировка с пользовательской последовательностью интервалов
    public static void sortWithCustomGaps(int[] arr, int[] gaps) {
        for (int gap : gaps) {
            if (gap >= arr.length) continue;
            
            for (int i = gap; i < arr.length; i++) {
                int temp = arr[i];
                int j = i;
                
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                
                arr[j] = temp;
            }
        }
    }
}